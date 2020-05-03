package org.dominokit.domino.api.client.mvp.slots;

import org.dominokit.domino.api.client.mvp.view.HasContent;

public interface SlotsManager {
    void registerSlot(String key, IsSlot slot);
    void removeSlot(String key);
    void revealView(String slotKey, HasContent view, HasContent.CreateHandler createHandler);
}
