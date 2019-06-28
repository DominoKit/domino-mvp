package org.dominokit.domino.api.client.mvp.presenter;

import org.dominokit.domino.api.client.mvp.slots.*;
import org.dominokit.domino.api.client.mvp.view.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

import static java.util.Objects.nonNull;

public class ViewBaseClientPresenter<V extends View> extends BaseClientPresenter {

    public static final Logger LOGGER = LoggerFactory.getLogger(ViewBaseClientPresenter.class);

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
        if (this instanceof UiHandlers && view instanceof HasUiHandlers) {
            ((HasUiHandlers) view).setUiHandlers((UiHandlers) this);
        }
        super.initialize();
    }

    public String revealSlot() {
        return null;
    }

    public void revealInSlot(String key) {
        IsSlot slot = SlotRegistry.get(key);
        if (nonNull(slot)) {
            revealInSlot(slot);
        } else {
            throw new InvalidSlotException(this.getClass(), key);
        }
    }

    public void reveal() {
        if (nonNull(revealSlot()) && !revealSlot().trim().isEmpty()) {
            revealInSlot(revealSlot());
        }
    }

    public void revealInSlot(IsSlot slot) {
        if (view instanceof HasContent) {
            onBeforeReveal();
            slot.updateContent(view, this::registerSlots);
        } else {
            throw new RevealViewWithNoContentException(view.getClass().getCanonicalName());
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
                SlotRegistry.registerSlot(key, slot);
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
                slotsEntries.getSlots().forEach((key, slot) -> SlotRegistry.removeSlot(key));
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
