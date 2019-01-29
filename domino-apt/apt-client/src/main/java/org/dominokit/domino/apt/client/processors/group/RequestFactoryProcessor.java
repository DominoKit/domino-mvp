package org.dominokit.domino.apt.client.processors.group;

import com.google.auto.service.AutoService;
import org.dominokit.domino.api.client.annotations.RequestFactory;
import org.dominokit.domino.apt.client.processors.module.client.presenters.PresenterProcessingStep;
import org.dominokit.domino.apt.commons.BaseProcessor;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@AutoService(Processor.class)
public class RequestFactoryProcessor extends BaseProcessor {

    private final Set<String> supportedAnnotations = new HashSet<>();

    public RequestFactoryProcessor() {
        supportedAnnotations.add(RequestFactory.class.getCanonicalName());
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

        new RequestFactoryProcessingStep.Builder()
                .setProcessingEnv(processingEnv)
                .build()
                .process(roundEnv.getElementsAnnotatedWith(RequestFactory.class)
                        .stream().filter(e -> ElementKind.INTERFACE.equals(e.getKind()))
                        .collect(Collectors.toSet()));
        return false;
    }
}
