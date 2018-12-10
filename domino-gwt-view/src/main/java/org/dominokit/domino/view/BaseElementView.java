package org.dominokit.domino.view;

import elemental2.dom.DomGlobal;
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

            DomGlobal.console.info("Register attach handler for view : " + this.getClass().getName());
            ElementUtil.onAttach(root, mutationRecord -> {
                SlotsEntries slotsEntries = getSlots();
                if (nonNull(slotsEntries)) {
                    slotsEntries.getSlots().forEach(SlotRegistry::registerSlot);
                }
                if (nonNull(revealHandler)) {
                    revealHandler.onRevealed();
                }
            });


            ElementUtil.onDetach(root, mutationRecord -> {
                DomGlobal.console.info("Register dettach handler for view : " + this.getClass().getName());
                SlotsEntries slotsEntries = getSlots();
                if (nonNull(slotsEntries)) {
                    slotsEntries.getSlots().forEach((key, slot) -> SlotRegistry.removeSlot(key));
                }
                if (nonNull(removeHandler)) {
                    removeHandler.onRemoved();
                    clear();
                }
            });

        }
    }

}
