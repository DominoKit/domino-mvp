package org.dominokit.domino.api.client.mvp.slots;

import java.util.HashMap;
import java.util.Map;

public class SlotsEntries {

    private final Map<String, IsSlot> slots = new HashMap<>();

    public static SlotsEntries create(){
        return new SlotsEntries();
    }

    public SlotsEntries add(String key, IsSlot slot){
        slots.put(key, slot);
        return this;
    }

    public Map<String, IsSlot> getSlots(){
        return new HashMap<>(slots);
    }

}
