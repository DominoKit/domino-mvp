package com.progressoft.brix.domino.apt.client.processors.module.client.requests;

import com.progressoft.brix.domino.api.client.annotations.Request;
import com.progressoft.brix.domino.api.client.request.ClientRequest;
import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.apt.commons.BaseProcessor;
import com.progressoft.brix.domino.apt.commons.FullClassName;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestsCollector {

    private final Messager messager;
    private final Types typeUtils;
    private final BaseProcessor.ElementFactory elementFactory;
    private final Set<String> requests;

    public RequestsCollector(Messager messager, Types typeUtils,
                             BaseProcessor.ElementFactory elementFactory, Set<String> requests) {
        this.messager = messager;
        this.typeUtils = typeUtils;
        this.elementFactory = elementFactory;
        this.requests = requests;
    }

    public void collectRequests(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(Request.class).stream()
                .map(elementFactory::make)
                .filter(e -> e.validateElementKind(ElementKind.CLASS))
                .collect(Collectors.toSet())
                .forEach(this::addRequest);
    }

    private boolean addRequest(ProcessorElement r) {
        if(!isRequestClass(r)){
            messager.printMessage(Diagnostic.Kind.ERROR, "Not extending base request class", r.getElement());
            throw new NotExtendBaseRequestClassException();
        }
        return requests.add(r.fullQualifiedNoneGenericName() + ":" +
                getRequestPresenter(r));
    }

    private boolean isRequestClass(ProcessorElement element) {
        String superType = elementFactory.make(typeUtils.asElement(element.asTypeElement().getSuperclass()))
                .fullQualifiedNoneGenericName();
        return superType.equals(ClientServerRequest.class.getCanonicalName()) ||
                superType.equals(ClientRequest.class.getCanonicalName());
    }

    private String getRequestPresenter(ProcessorElement element) {
        return new FullClassName(typeUtils.capture(element.asTypeElement().getSuperclass()).toString()).allImports()
                .get(1);
    }

    public class NotExtendBaseRequestClassException extends RuntimeException {
    }
}