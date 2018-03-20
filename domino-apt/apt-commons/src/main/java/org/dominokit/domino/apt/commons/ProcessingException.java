package org.dominokit.domino.apt.commons;

import javax.lang.model.element.Element;

public class ProcessingException extends RuntimeException {

    private final transient Element element;

    public ProcessingException(Element element, String msg, Object... args) {
        super(String.format(msg, args));
        this.element = element;
    }

    public Element getElement() {
        return element;
    }
}
