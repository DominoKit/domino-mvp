package org.dominokit.domino.test.api.client;

import java.util.function.Consumer;

public class FakeElement {

    private final Consumer<Boolean> attachConsumer;

    public FakeElement(Consumer<Boolean> attachConsumer) {
        this.attachConsumer = attachConsumer;
    }

    public void append(){
        attachConsumer.accept(true);
    }

    public void remove(){
        attachConsumer.accept(false);
    }
}
