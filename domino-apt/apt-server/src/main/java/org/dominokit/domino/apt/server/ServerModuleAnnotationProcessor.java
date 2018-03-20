package org.dominokit.domino.apt.server;

import com.google.auto.service.AutoService;
import org.dominokit.domino.api.server.config.ServerModule;
import org.dominokit.domino.api.server.handler.Handler;
import org.dominokit.domino.api.server.handler.RequestHandler;
import org.dominokit.domino.api.server.interceptor.GlobalInterceptor;
import org.dominokit.domino.api.server.interceptor.GlobalRequestInterceptor;
import org.dominokit.domino.api.server.interceptor.Interceptor;
import org.dominokit.domino.api.server.interceptor.RequestInterceptor;
import org.dominokit.domino.apt.commons.BaseProcessor;
import org.dominokit.domino.apt.commons.ProcessorElement;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@AutoService(Processor.class)
public class ServerModuleAnnotationProcessor extends BaseProcessor {

    private static final Logger LOGGER = Logger.getLogger(ServerModuleAnnotationProcessor.class.getName());

    private final Set<String> supportedAnnotations = new HashSet<>();

    public ServerModuleAnnotationProcessor() {
        supportedAnnotations.add(Handler.class.getCanonicalName());
        supportedAnnotations.add(ServerModule.class.getCanonicalName());
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
        if (roundEnv.getElementsAnnotatedWith(ServerModule.class).size() == 1) {
            ProcessorElement module = getModule(roundEnv.getElementsAnnotatedWith(ServerModule.class).stream()
                    .findFirst().orElseThrow(IllegalArgumentException::new));

            generateServerModule(module, getHandlers(roundEnv), getInterceptors(roundEnv), getGlobalInterceptors(roundEnv));
        } else if (roundEnv.getElementsAnnotatedWith(ServerModule.class).size() > 1) {
            this.messager.printMessage(Diagnostic.Kind.ERROR, "Only one ServerModule is allowed per project.!");
        }
        return true;
    }

    private ProcessorElement getModule(Element element) {
        return newProcessorElement(element);
    }

    private List<ProcessorElement> getHandlers(RoundEnvironment roundEnv) {
        return roundEnv.getElementsAnnotatedWith(Handler.class).stream()
                .filter(e -> implementsHandler(newProcessorElement(e))).map(this::newProcessorElement).collect(Collectors.toList());
    }

    private List<ProcessorElement> getInterceptors(RoundEnvironment roundEnv) {
        return roundEnv.getElementsAnnotatedWith(Interceptor.class).stream()
                .filter(e -> implementsInterceptor(newProcessorElement(e))).map(this::newProcessorElement).collect(Collectors.toList());
    }

    private List<ProcessorElement> getGlobalInterceptors(RoundEnvironment roundEnv) {
        return roundEnv.getElementsAnnotatedWith(GlobalInterceptor.class).stream()
                .filter(e -> implementsGlobalInterceptor(newProcessorElement(e))).map(this::newProcessorElement).collect(Collectors.toList());
    }

    private boolean implementsHandler(ProcessorElement e) {
        if (e.isImplementsGenericInterface(RequestHandler.class))
            return true;
        this.messager.printMessage(Diagnostic.Kind.ERROR, "Classes annotated as Handlers must implement RequestHandler interface.!", e.asTypeElement());
        return false;
    }

    private boolean implementsInterceptor(ProcessorElement e) {
        if (e.isImplementsGenericInterface(RequestInterceptor.class))
            return true;
        this.messager.printMessage(Diagnostic.Kind.ERROR, "Classes annotated as Interceptors must implement RequestInterceptor interface.!", e.asTypeElement());
        return false;
    }

    private boolean implementsGlobalInterceptor(ProcessorElement e) {
        if (e.isImplementsGenericInterface(GlobalRequestInterceptor.class))
            return true;
        this.messager.printMessage(Diagnostic.Kind.ERROR, "Classes annotated as Global Interceptors must implement GlobalRequestInterceptor interface.!", e.asTypeElement());
        return false;
    }


    private void generateServerModule(ProcessorElement processorElement, List<ProcessorElement> handlers,
                                      List<ProcessorElement> interceptors, List<ProcessorElement> globalInterceptors) {
        try (Writer sourceWriter = obtainSourceWriter(processorElement.elementPackage(), processorElement.getAnnotation(ServerModule.class).name() + "ServerModule")) {
            sourceWriter.write(new ServerModuleSourceWriter(processorElement, handlers, interceptors, globalInterceptors).write());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not generate classes : ", e);
            messager.printMessage(Diagnostic.Kind.ERROR, "could not generate class");
        }
    }

}
