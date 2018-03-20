package org.dominokit.domino.apt.client.processors.module.client.views;

import com.google.auto.common.MoreElements;
import org.dominokit.domino.api.client.annotations.UiView;
import org.dominokit.domino.api.client.mvp.view.View;
import org.dominokit.domino.apt.commons.BaseProcessor;
import org.dominokit.domino.apt.commons.ProcessorElement;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic;
import java.util.Map;
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
        return views.add(v.fullQualifiedNoneGenericName() + ":" +
                getViewPresenter(v.asTypeElement()));
    }

    private void isView(ProcessorElement element) {
        if(element.isImplementsGenericInterface(View.class))
            messager.printMessage(Diagnostic.Kind.WARNING, "Class is annotated as View while it is not implementing view interface.!");

    }

    private Element getViewPresenter(Element e) {
        AnnotationMirror annotationMirror = MoreElements.getAnnotationMirror(e, UiView.class).get();
        return getProviderInterface(annotationMirror);
    }

    private Element getProviderInterface(AnnotationMirror providerAnnotation) {
        Map<? extends ExecutableElement, ? extends AnnotationValue> valueIndex =
                providerAnnotation.getElementValues();
        AnnotationValue value = valueIndex.values().iterator().next();
        return ((DeclaredType) value.getValue()).asElement();
    }
}