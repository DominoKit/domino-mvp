package org.dominokit.domino.api.client.mvp.slots;

import org.dominokit.domino.api.client.mvp.presenter.ViewBaseClientPresenter;

public class InvalidSlotException extends RuntimeException {
    public InvalidSlotException(Class<? extends ViewBaseClientPresenter> presenter,  String slotKey) {
        super("Slot not found ["+slotKey+"], required by : "+presenter.getCanonicalName());
    }
}
