package com.progressoft.brix.domino.apt.client.processors.module.client.views;

import com.google.auto.common.MoreElements;
import com.progressoft.brix.domino.api.client.annotations.UiView;
import com.progressoft.brix.domino.api.client.mvp.view.View;
import com.progressoft.brix.domino.apt.commons.BaseProcessor;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Types;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ViewsCollector {

    private final BaseProcessor.ElementFactory elementFactory;
    private final Set<String> views;

    public ViewsCollector(BaseProcessor.ElementFactory elementFactory, Set<String> views) {
        this.elementFactory = elementFactory;
        this.views = views;
    }

    public void collectViews(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(UiView.class).stream()
                .map(elementFactory::make)
                .filter(e -> e.validateElementKind(ElementKind.CLASS))
                .filter(this::isView)
                .collect(Collectors.toSet())
                .forEach(v -> views.add(v.fullQualifiedNoneGenericName() + ":" +
                        getViewPresenter(v.asTypeElement())));
    }

    private boolean isView(ProcessorElement element) {
        return element.isImplementsGenericInterface(View.class);
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