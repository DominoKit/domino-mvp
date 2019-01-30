package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.mvp.slots.SlotRegistry;
import org.dominokit.domino.api.client.mvp.slots.SlotsEntries;
import org.dominokit.domino.api.client.mvp.view.BaseDominoView;

import static java.util.Objects.nonNull;

public abstract class FakeView extends BaseDominoView<FakeElement> {

    @Override
    protected void initRoot(FakeElement root) {

    }

    @Override
    public FakeElement createRoot() {
        return new FakeElement(attached -> {
            if(attached){
                SlotsEntries slotsEntries = getSlots();
                if (nonNull(slotsEntries)) {
                    slotsEntries.getSlots().forEach(SlotRegistry::registerSlot);
                }
                revealHandler.onRevealed();
            }else{
                SlotsEntries slotsEntries = getSlots();
                if (nonNull(slotsEntries)) {
                    slotsEntries.getSlots().forEach((key, slot) -> SlotRegistry.removeSlot(key));
                }
                removeHandler.onRemoved();
            }
        });
    }
}
