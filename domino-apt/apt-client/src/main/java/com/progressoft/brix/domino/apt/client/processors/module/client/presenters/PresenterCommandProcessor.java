package com.progressoft.brix.domino.apt.client.processors.module.client.presenters;

import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.client.annotations.Presenter;
import com.progressoft.brix.domino.api.client.mvp.presenter.Presentable;
import com.progressoft.brix.domino.apt.commons.BaseProcessor;
import com.progressoft.brix.domino.apt.commons.FullClassName;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AutoService(Processor.class)
public class PresenterCommandProcessor extends BaseProcessor{

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(Presenter.class).stream().filter(this::hasCommand)
                .forEach(this::generateCommand);
        return false;
    }

    private void generateCommand(Element presenterElement) {
        final ProcessorElement processorElement = newProcessorElement(presenterElement);
        final String targetPackage = processorElement.elementPackage();
        final String presentableInterface = getPresentableInterface(processorElement);
        final String simpleInterfaceName = new FullClassName(presentableInterface).asSimpleName();
        final String className = simpleInterfaceName + "Command";
        try (Writer sourceWriter = obtainSourceWriter(
                targetPackage, className)) {
            sourceWriter
                    .write(new PresenterCommandSourceWriter(processorElement, targetPackage, className, simpleInterfaceName).write());
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, "could not generate class", presenterElement);
        }
    }

    private String getPresentableInterface(ProcessorElement element) {
        TypeMirror typeMirror = element.asTypeElement().getInterfaces().stream().filter(this::isPresentableInterface)
                .collect(Collectors.toSet()).stream().findFirst().orElseThrow(IllegalArgumentException::new);

        return typeMirror.toString();
    }

    private boolean isPresentableInterface(TypeMirror implementedInterface) {
        return typeUtils.isAssignable(implementedInterface,
                elementUtils.getTypeElement(Presentable.class.getCanonicalName()).asType());
    }

    private boolean hasCommand(Element presenterElement) {
        return presenterElement.getAnnotation(Presenter.class).hasCommand();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add(Presenter.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
