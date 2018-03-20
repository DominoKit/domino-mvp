package org.dominokit.domino.apt.client.processors.module.client.presenters;

import com.google.auto.service.AutoService;
import org.dominokit.domino.api.client.annotations.Presenter;
import org.dominokit.domino.api.client.mvp.presenter.Presentable;
import org.dominokit.domino.apt.commons.BaseProcessor;
import org.dominokit.domino.apt.commons.ProcessorElement;

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
        final String presenterName = processorElement.simpleName();
        final String className = presenterName + "Command";
        try (Writer sourceWriter = obtainSourceWriter(
                targetPackage, className)) {
            sourceWriter
                    .write(new PresenterCommandSourceWriter(processorElement, targetPackage, className, presenterName).write());
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, "could not generate class", presenterElement);
        }
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
