package org.dominokit.domino.apt.client.processors.module.client.presenters.model;

import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

public class EventsGroup {

    private final List<TypeMirror> classList = new ArrayList<>();

    public EventsGroup(List<TypeMirror> classList) {
        this.classList.addAll(classList);
    }

    public List<TypeMirror> getClassList() {
        return classList;
    }
}
