package org.dominokit.domino.api.client.mvp.presenter;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.mvp.slots.*;
import org.dominokit.domino.api.client.mvp.view.*;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Objects.nonNull;

public class ViewBaseClientPresenter<V extends View> extends BaseClientPresenter {

    public static final Logger LOGGER = Logger.getLogger(ViewBaseClientPresenter.class.getName());

    protected V view;
    private Supplier<V> viewSupplier;

    @Override
    protected void initialize() {
        view = viewSupplier.get();
        if (view instanceof DominoView) {
            ((DominoView) view).setRevealHandler(getViewRevealHandler());
            ((DominoView) view).setRemoveHandler(getViewRemoveHandler());
        }
        if (this instanceof UiHandlers && view instanceof HasUiHandlers) {
            ((HasUiHandlers) view).setUiHandlers((UiHandlers) this);
        }
        super.initialize();
    }

    public String revealSlot() {
        return null;
    }

    public void revealInSlot(String key) {
        try{
            if (view instanceof HasContent) {
                onBeforeReveal();
               ClientApp.make().slotsManager().revealView(key, (HasContent) view, this::registerSlots);
            } else {
                throw new RevealViewWithNoContentException(view.getClass().getCanonicalName());
            }
        }catch (InvalidSlotException e){
            LOGGER.log(Level.SEVERE, "Slot ["+key+"] not found, required by presenter : ["+this.getClass().getCanonicalName() +"]");
        }
    }

    public void reveal() {
        if (nonNull(revealSlot()) && !revealSlot().trim().isEmpty()) {
            revealInSlot(revealSlot());
        }
    }

    protected void onBeforeReveal() {

    }

    private DominoView.RevealedHandler getViewRevealHandler() {
        return () -> {
            RevealedHandler revealHandler = getRevealHandler();
            if (nonNull(revealHandler)) {
                revealHandler.onRevealed();
            }
            activate();
        };
    }

    private void registerSlots() {
        SlotsEntries slotsEntries = getSlots();
        if (nonNull(slotsEntries)) {
            slotsEntries.getSlots().forEach((key, slot) -> {
                LOGGER.info("Presenter ["+this.getClass().getCanonicalName()+"] is registering slot ["+key+"]");
                ClientApp.make().slotsManager().registerSlot(key, slot);
            });
        }
    }

    protected SlotsEntries getSlots() {
        return SlotsEntries.create();
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
            SlotsEntries slotsEntries = getSlots();
            if (nonNull(slotsEntries)) {
                slotsEntries.getSlots().forEach((key, slot) -> {
                    LOGGER.info("Presenter ["+this.getClass().getCanonicalName()+"] is removing slot ["+key+"]");
                    ClientApp.make().slotsManager().removeSlot(key);
                });
            }
            RemovedHandler removeHandler = getRemoveHandler();
            if (nonNull(removeHandler)) {
                removeHandler.onRemoved();
            }
            deActivate();
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
