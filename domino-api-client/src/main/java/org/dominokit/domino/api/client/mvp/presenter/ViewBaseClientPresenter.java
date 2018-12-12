package org.dominokit.domino.api.client.mvp.presenter;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.mvp.slots.InvalidSlotException;
import org.dominokit.domino.api.client.mvp.slots.RevealViewWithNoContentException;
import org.dominokit.domino.api.client.mvp.slots.Slot;
import org.dominokit.domino.api.client.mvp.slots.SlotRegistry;
import org.dominokit.domino.api.client.mvp.view.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class ViewBaseClientPresenter<V extends View> extends BaseClientPresenter {

    protected V view;

    @Override
    protected void initialize() {
        view = loadView();
        if (view instanceof DominoView) {
            ((DominoView) view).setRevealHandler(onRevealed());
            ((DominoView) view).setRemoveHandler(onRemoved());
        }
        if(this instanceof UiHandlers && view instanceof HasUiHandlers){
            ((HasUiHandlers) view).setUiHandlers((UiHandlers) this);
        }
        initView(ViewBaseClientPresenter.this.view);
        super.initialize();

        if(nonNull(autoRevealSlot()) && !autoRevealSlot().trim().isEmpty()){
            revealInSlot(autoRevealSlot());
        }

    }

    protected void initView(V view) {
        // Default empty implementation
    }

    protected String autoRevealSlot(){
        return null;
    }

    public void revealInSlot(String key){
        Slot slot = SlotRegistry.get(key);
        if(nonNull(slot)) {
            revealInSlot(slot);
        }else{
            throw new InvalidSlotException(key);
        }
    }

    public void revealInSlot(Slot slot){
        if(view instanceof HasContent) {
            slot.updateContent(((HasContent) view).getContent());
        }else{
            throw new RevealViewWithNoContentException(view.getClass().getCanonicalName());
        }
    }

    public DominoView.RevealedHandler onRevealed() {
        return null;
    }

    public DominoView.RemovedHandler onRemoved() {
        return null;
    }

    private V loadView() {
        return (V) ClientApp.make().getViewsRepository().getView(getName());
    }

}
