package org.dominokit.domino.view.slots;

import elemental2.dom.CustomEvent;
import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.dominokit.domino.api.client.mvp.slots.IsSlot;

public abstract class ElementSlot implements IsSlot<HTMLElement>, EventListener {

    @Override
    public void handleEvent(Event evt) {
        CustomEvent slotEvent = Js.uncheckedCast(evt);
        HTMLElement view = Js.uncheckedCast(slotEvent.detail);
        updateContent(view);
    }
}
