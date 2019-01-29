package org.dominokit.domino.view;

import elemental2.dom.HTMLElement;
import org.dominokit.domino.api.client.mvp.slots.SlotRegistry;
import org.dominokit.domino.api.client.mvp.slots.SlotsEntries;
import org.dominokit.domino.api.client.mvp.view.BaseDominoView;
import org.dominokit.domino.ui.utils.ElementUtil;

import static java.util.Objects.nonNull;

public abstract class BaseElementView<T extends HTMLElement> extends BaseDominoView<T> {
    @Override
    protected void initRoot(T root) {
        if (nonNull(root)) {
            ElementUtil.onAttach(root, mutationRecord -> {
                SlotsEntries slotsEntries = getSlots();
                if (nonNull(slotsEntries)) {
                    slotsEntries.getSlots().forEach(SlotRegistry::registerSlot);
                }
                revealHandler.onRevealed();
            });

            ElementUtil.onDetach(root, mutationRecord -> {
                SlotsEntries slotsEntries = getSlots();
                if (nonNull(slotsEntries)) {
                    slotsEntries.getSlots().forEach((key, slot) -> SlotRegistry.removeSlot(key));
                }

                removeHandler.onRemoved();
                clear();
            });
        }
    }

}
