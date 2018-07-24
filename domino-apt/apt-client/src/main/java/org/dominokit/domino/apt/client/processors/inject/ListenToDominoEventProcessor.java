package org.dominokit.domino.apt.client.processors.inject;

import com.google.auto.common.MoreElements;
import com.google.auto.service.AutoService;
import org.dominokit.domino.api.client.annotations.ListenTo;
import org.dominokit.domino.apt.commons.BaseProcessor;
import org.dominokit.domino.apt.commons.FullClassName;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@AutoService(Processor.class)
public class ListenToDominoEventProcessor extends BaseProcessor {


    private final Set<String> supportedAnnotations = new HashSet<>();

    public ListenToDominoEventProcessor() {
        supportedAnnotations.add(ListenTo.class.getCanonicalName());
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
        roundEnv.getElementsAnnotatedWith(ListenTo.class)
                .forEach(this::generateListener);
        return false;
    }

    private void generateListener(Element e) {
        String dominoEvent = getDominoEvent(e);
        String presenter = asTypeElement((DeclaredType) e.getEnclosingElement().asType()).getQualifiedName().toString();
        FullClassName presenterFulLClassName = new FullClassName(presenter);
        FullClassName dominoEventFullClassName = new FullClassName(dominoEvent);
        String generatedClassName = presenterFulLClassName.asSimpleName() + "ListenerFor" + dominoEventFullClassName.asSimpleName();
        String targetPackage = presenterFulLClassName.asPackage().replace("presenters", "listeners");
        try (Writer sourceWriter = obtainSourceWriter(targetPackage, generatedClassName)) {
            sourceWriter
                    .write(new DominoEventListenerSourceWriter(newProcessorElement(e), presenter, dominoEvent, targetPackage, generatedClassName).write());
        } catch (IOException ex) {
            messager.printMessage(Diagnostic.Kind.ERROR, "could not generate class");
        }
    }

    private String getDominoEvent(Element e) {
        AnnotationMirror
                providerAnnotation = MoreElements.getAnnotationMirror(e, ListenTo.class).get();
        DeclaredType providerInterface = this.getProviderInterface(providerAnnotation);
        TypeElement typeElement = asTypeElement(providerInterface);
        return typeElement.getQualifiedName().toString();

    }

    private TypeElement asTypeElement(DeclaredType p) {
        return (TypeElement) p.asElement();
    }

    private DeclaredType getProviderInterface(AnnotationMirror providerAnnotation) {
        Map valueIndex = providerAnnotation.getElementValues();
        AnnotationValue value = (AnnotationValue) valueIndex.values().iterator().next();
        return (DeclaredType) value.getValue();
    }
}
