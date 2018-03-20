package org.dominokit.domino.apt.server;

import com.google.auto.service.AutoService;
import org.dominokit.domino.api.server.handler.Handler;
import org.dominokit.domino.api.server.handler.RequestHandler;
import org.dominokit.domino.apt.commons.BaseProcessor;
import org.dominokit.domino.apt.commons.ProcessorElement;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@AutoService(Processor.class)
public class EndpointsProcessor extends BaseProcessor {

    private static final Logger LOGGER = Logger.getLogger(EndpointsProcessor.class.getName());

    private final Set<String> supportedAnnotations = new HashSet<>();

    public EndpointsProcessor() {
        supportedAnnotations.add(Handler.class.getCanonicalName());
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return supportedAnnotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        getHandlers(roundEnv).forEach(e -> {
            try {
                generateEndpoint(e);
            } catch (Exception ex) {
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                messager.printMessage(Diagnostic.Kind.NOTE, "could not generate source. " + sw.toString());
            }
        });

        return false;
    }

    private void generateEndpoint(ProcessorElement element) {
        try (Writer sourceWriter = obtainSourceWriter(element.elementPackage(),
                element.simpleName() + "EndpointHandler")) {
            sourceWriter.write(new EndpointHandlerSourceWriter(element).write());
        } catch (IOException e) {

            LOGGER.log(Level.SEVERE, "Could not generate classes : ", e);
            messager.printMessage(Diagnostic.Kind.ERROR, "could not generate class");
        }
    }

    private List<ProcessorElement> getHandlers(RoundEnvironment roundEnv) {
        return roundEnv.getElementsAnnotatedWith(Handler.class).stream()
                .filter(e -> implementsHandler(newProcessorElement(e))).map(this::newProcessorElement)
                .collect(Collectors.toList());
    }

    private boolean implementsHandler(ProcessorElement e) {
        if (e.isImplementsGenericInterface(RequestHandler.class))
            return true;
        this.messager.printMessage(Diagnostic.Kind.ERROR,
                "Classes annotated as Handlers must implement RequestHandler interface.!", e.asTypeElement());
        return false;
    }
}
