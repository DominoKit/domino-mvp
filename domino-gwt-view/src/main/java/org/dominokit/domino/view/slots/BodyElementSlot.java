package org.dominokit.domino.view.slots;

import elemental2.dom.HTMLBodyElement;
import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.dominokit.domino.api.client.mvp.slots.IsSlot;
import org.dominokit.domino.api.client.mvp.view.ContentView;
import org.dominokit.domino.ui.utils.DominoElement;

import static java.util.Objects.nonNull;

public class BodyElementIsSlot implements IsSlot<ContentView> {

    private DominoElement<HTMLBodyElement> body = DominoElement.body();

    private static final BodyElementIsSlot INSTANCE = new BodyElementIsSlot();

    private ContentView currentView;

    public static BodyElementIsSlot create(){
        return INSTANCE;
    }

    private BodyElementIsSlot(){}

    @Override
    public void updateContent(ContentView view) {
        if(nonNull(currentView)){
            HTMLElement contentElement = Js.uncheckedCast(currentView.getContent().get());
            DominoElement.of(contentElement)
                    .remove();
        }
        currentView = view;
        body.appendChild(Js.<HTMLElement>uncheckedCast(view.getContent().get()));
    }
}
