package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.apt.commons.ProcessorElement;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class RegistrationHelper {

    private ProcessorElement processorElement;
    private Map<String, List<Element>> elements;

    public RegistrationHelper(Map<String, List<Element>> elements, ProcessorElement processorElement) {
        this.processorElement = processorElement;
        this.elements=elements;
    }

    boolean isThereItems(String annotation) {
        return elementsAsStream(annotation).count() > 0;
    }

    Stream<Element> elementsAsStream(String annotation) {
        return elements.get(annotation).stream().filter(e -> e.getKind() == ElementKind.CLASS);
    }

    public boolean isImplementsInterface(Element e, Class<?> interfaceClass) {
        return processorElement.make(e).isImplementsInterface(interfaceClass);
    }

    public boolean isImplementsGenericInterface(Element e, Class<?> interfaceClass) {
        return processorElement.make(e).isImplementsGenericInterface(interfaceClass);
    }

    List<Element> elementsAnnotatedWith(String annotation) {
        return elements.get(annotation);
    }

    public boolean isSubtypeOfGenericClass(Element e, Class<?> superType) {
        return processorElement.make(e).isSubtypeOfGenericClass(superType);
    }
}
