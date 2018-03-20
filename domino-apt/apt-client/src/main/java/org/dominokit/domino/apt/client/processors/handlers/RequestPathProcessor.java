package org.dominokit.domino.apt.client.processors.handlers;

import com.google.auto.service.AutoService;
import org.dominokit.domino.api.client.annotations.Path;
import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.apt.commons.BaseProcessor;
import org.dominokit.domino.apt.commons.FullClassName;
import org.dominokit.domino.apt.commons.ProcessorElement;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


@AutoService(Processor.class)
public class RequestPathProcessor extends BaseProcessor {

    private static final Logger LOGGER = Logger.getLogger(RequestPathProcessor.class.getName());
    public static final int REQUEST_INDEX = 1;
    public static final int RESPONSE_INDEX = 2;

    private final Set<String> supportedAnnotations = new HashSet<>();

    public RequestPathProcessor() {
        supportedAnnotations.add(Path.class.getCanonicalName());
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
        roundEnv.getElementsAnnotatedWith(Path.class).stream().filter(e -> ElementKind.CLASS.equals(e.getKind())).filter(this::extendsServerRequest)
                .forEach(this::generateRequestRestfulSender);
        return false;
    }

    private boolean extendsServerRequest(Element e) {
        FullClassName fullClassName=new FullClassName(((TypeElement)e).getSuperclass().toString());
        if(!ServerRequest.class.getCanonicalName().equals(fullClassName.asImport())){
            messager.printMessage(Diagnostic.Kind.ERROR, "Class does not extends RequestBean", e);
        }
        return true;
    }

    private void generateRequestRestfulSender(Element element) {
        FullClassName fullSuperClassName=new FullClassName(((TypeElement)element).getSuperclass().toString());
        List<String> allSuperClassTypes=fullSuperClassName.allImports();
        FullClassName fullRequestClassName=new FullClassName(allSuperClassTypes.get(REQUEST_INDEX));
        FullClassName fullResponseClassName=new FullClassName(allSuperClassTypes.get(RESPONSE_INDEX));
        ProcessorElement processorElement=newProcessorElement(element);
        try (Writer sourceWriter = obtainSourceWriter(
                processorElement.elementPackage(),processorElement.simpleName()+"Sender")) {
            sourceWriter
                    .write(new RequestSenderSourceWriter(processorElement, fullRequestClassName, fullResponseClassName).write());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not generate classes : ", e);
            messager.printMessage(Diagnostic.Kind.ERROR, "could not generate class");
        }

    }
}
