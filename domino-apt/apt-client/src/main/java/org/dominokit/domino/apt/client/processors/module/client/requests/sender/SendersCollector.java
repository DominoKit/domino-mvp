package org.dominokit.domino.apt.client.processors.module.client.requests.sender;

import com.google.auto.common.MoreElements;
import org.dominokit.domino.api.client.annotations.RequestSender;
import org.dominokit.domino.api.client.request.RequestRestSender;
import org.dominokit.domino.apt.commons.BaseProcessor;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SendersCollector {

    private final BaseProcessor.ElementFactory elementFactory;
    private final Set<String> senders;

    public SendersCollector(BaseProcessor.ElementFactory elementFactory, Set<String> senders) {
        this.elementFactory = elementFactory;
        this.senders = senders;
    }

    public void collectSenders(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(RequestSender.class).stream()
                .map(elementFactory::make)
                .filter(e -> e.validateElementKind(ElementKind.CLASS))

                .filter(e -> e.isImplementsGenericInterface(RequestRestSender.class))
                .collect(Collectors.toSet())
                .forEach(
                        s -> senders.add(s.fullQualifiedNoneGenericName() + ":" +
                                getSenderRequest(s.asTypeElement())+":"+s.getAnnotation(RequestSender.class).customServiceRoot()));
    }

    private Element getSenderRequest(Element e) {
        AnnotationMirror annotationMirror = MoreElements.getAnnotationMirror(e, RequestSender.class).get();
        return getProviderInterface(annotationMirror);
    }

    private Element getProviderInterface(AnnotationMirror providerAnnotation) {
        Map<? extends ExecutableElement, ? extends AnnotationValue> valueIndex =
                providerAnnotation.getElementValues();

        AnnotationValue value = valueIndex.values().iterator().next();
        return ((DeclaredType) value.getValue()).asElement();
    }
}