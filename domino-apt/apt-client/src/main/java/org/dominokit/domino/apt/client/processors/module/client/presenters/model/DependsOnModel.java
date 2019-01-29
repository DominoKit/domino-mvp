package org.dominokit.domino.apt.client.processors.module.client.presenters.model;

import java.util.ArrayList;
import java.util.List;

public class DependsOnModel {

    private List<EventsGroup> eventsGroups = new ArrayList<>();

    public DependsOnModel() {

    }

    public void addEventGroup(EventsGroup eventsGroup) {
        this.eventsGroups.add(eventsGroup);
    }

    public List<EventsGroup> getEventsGroups() {
        return eventsGroups;
    }
}
