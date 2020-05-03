package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.mvp.slots.IsSlot;
import org.dominokit.domino.api.client.mvp.slots.SlotsManager;
import org.dominokit.domino.api.client.mvp.view.HasContent;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;

public class TestSlotsManager implements SlotsManager {
    public static final Logger LOGGER = Logger.getLogger(TestSlotsManager.class.getName());

    private static final Map<String, Deque<IsSlot>> SLOT_QUEUE = new HashMap<>();

    public void registerSlot(String key, IsSlot slot) {
        LOGGER.info(" >> REGISTERING SLOT ["+key+"]");
        if (!SLOT_QUEUE.containsKey(key.toLowerCase())) {
            SLOT_QUEUE.put(key.toLowerCase(), new LinkedList<>());
        }

        SLOT_QUEUE.get(key.toLowerCase()).push(slot);
    }

    public void removeSlot(String key) {
        LOGGER.info(" << REMOVING SLOT ["+key+"]");
        if (SLOT_QUEUE.containsKey(key.toLowerCase())) {
            IsSlot popedOut = SLOT_QUEUE.get(key.toLowerCase()).pop();
            popedOut.cleanUp();
        }
    }

    public static IsSlot get(String key){
        return SLOT_QUEUE.get(key.toLowerCase()).peek();
    }

    @Override
    public void revealView(String slotKey, HasContent view, HasContent.CreateHandler createHandler) {
        get(slotKey).updateContent(view.getContent());
    }
}
