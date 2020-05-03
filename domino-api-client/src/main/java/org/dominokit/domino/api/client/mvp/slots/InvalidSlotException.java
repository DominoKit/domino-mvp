package org.dominokit.domino.api.client.mvp.slots;

public class InvalidSlotException extends RuntimeException {
    public InvalidSlotException(String slotKey) {
        super("Slot not found ["+slotKey+"]");
    }
}
