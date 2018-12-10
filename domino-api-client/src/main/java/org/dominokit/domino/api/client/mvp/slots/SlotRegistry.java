package org.dominokit.domino.api.client.mvp.slots;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SlotRegistry {

    private static final Map<String, Deque<Slot>> SLOT_QUEUE = new HashMap<>();

    public static void registerSlot(String key, Slot slot) {
        if (!SLOT_QUEUE.containsKey(key.toLowerCase())) {
            SLOT_QUEUE.put(key.toLowerCase(), new LinkedList<>());
        }

        SLOT_QUEUE.get(key.toLowerCase()).push(slot);
    }

    public static void removeSlot(String key) {
        if (SLOT_QUEUE.containsKey(key.toLowerCase())) {
            SLOT_QUEUE.get(key.toLowerCase()).pop();
        }
    }

    public static Slot get(String key){
        return SLOT_QUEUE.get(key.toLowerCase()).peek();
    }

}
