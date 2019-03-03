package org.dominokit.domino.apt.client.processors.module.client.views;

import org.dominokit.domino.api.client.annotations.UiView;
import org.dominokit.domino.api.client.mvp.view.View;
import org.dominokit.domino.apt.commons.BaseProcessor;
import org.dominokit.domino.apt.commons.ProcessorElement;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.tools.Diagnostic;
import java.util.Set;
import java.util.stream.Collectors;

public class ViewsCollector {

    private final BaseProcessor.ElementFactory elementFactory;
    private final Set<String> views;
    private final Messager messager;

    public ViewsCollector(Messager messager, BaseProcessor.ElementFactory elementFactory, Set<String> views) {
        this.messager = messager;
        this.elementFactory = elementFactory;
        this.views = views;
    }

    public void collectViews(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(UiView.class).stream()
                .map(elementFactory::make)
                .filter(e -> e.validateElementKind(ElementKind.CLASS))
                .collect(Collectors.toSet())
                .forEach(this::addView);
    }

    private boolean addView(ProcessorElement v) {
        isView(v);
        return views.add(v.asTypeElement().getQualifiedName().toString());
    }

    private void isView(ProcessorElement element) {
        if(element.isImplementsGenericInterface(View.class))
            messager.printMessage(Diagnostic.Kind.WARNING, "Class is annotated as View while it is not implementing view interface.!");

    }
}