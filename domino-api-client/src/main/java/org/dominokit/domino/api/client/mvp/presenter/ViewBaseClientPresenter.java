package org.dominokit.domino.api.client.mvp.presenter;

import org.dominokit.domino.api.client.mvp.slots.InvalidSlotException;
import org.dominokit.domino.api.client.mvp.slots.RevealViewWithNoContentException;
import org.dominokit.domino.api.client.mvp.slots.Slot;
import org.dominokit.domino.api.client.mvp.slots.SlotRegistry;
import org.dominokit.domino.api.client.mvp.view.*;
import org.dominokit.domino.api.shared.extension.ActivationEventContext;

import java.util.function.Supplier;

import static java.util.Objects.nonNull;

public class ViewBaseClientPresenter<V extends View> extends BaseClientPresenter {

    public static final String DOCUMENT_BODY = "document-body";

    protected V view;
    private Supplier<V> viewSupplier;

    @Override
    protected void initialize() {
        view = viewSupplier.get();
        if (view instanceof DominoView) {
            ((DominoView) view).setRevealHandler(getViewRevealHandler());
            ((DominoView) view).setRemoveHandler(getViewRemoveHandler());
        }
        if(this instanceof UiHandlers && view instanceof HasUiHandlers){
            ((HasUiHandlers) view).setUiHandlers((UiHandlers) this);
        }
        super.initialize();
    }

    public String revealSlot(){
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

    public void reveal(){
        if(nonNull(revealSlot()) && !revealSlot().trim().isEmpty()){
            revealInSlot(revealSlot());
        }
    }

    public void revealInSlot(Slot slot){
        if(view instanceof HasContent) {
            onBeforeReveal();
            slot.updateContent(((HasContent) view).getContent());
        }else{
            throw new RevealViewWithNoContentException(view.getClass().getCanonicalName());
        }
    }

    protected void onBeforeReveal(){

    }

    private DominoView.RevealedHandler getViewRevealHandler() {
        return () -> {
            RevealedHandler revealHandler = getRevealHandler();
            if(nonNull(revealHandler)){
                revealHandler.onRevealed();
            }
            activated = true;
            activate();
        };
    }

    @Override
    protected void fireActivationEvent(boolean stats) {
    }

    @Override
    protected final boolean isAutoActivate() {
        return false;
    }

    private DominoView.RemovedHandler getViewRemoveHandler() {
        return () -> {
            deActivate();
            RemovedHandler removeHandler = getRemoveHandler();
            if(nonNull(removeHandler)){
                removeHandler.onRemoved();
            }
            activated =false;
            fireActivationEvent(false);
        };
    }

    protected RevealedHandler getRevealHandler() {
        return null;
    }

    protected RemovedHandler getRemoveHandler() {
        return null;
    }

    public void setViewSupplier(Supplier<V> viewSupplier) {
        this.viewSupplier = viewSupplier;
    }

    @FunctionalInterface
    public interface RevealedHandler {
        void onRevealed();
    }

    @FunctionalInterface
    public interface RemovedHandler {
        void onRemoved();
    }
}
