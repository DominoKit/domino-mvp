package org.dominokit.domino.api.client.mvp;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;

public class Store<T> implements IsStore<T> {

    private T data;
    private List<Consumer<T>> consumers = new ArrayList<>();

    public Store() {
    }

    public Store(T data) {
        this.data = data;
    }

    public void updateData(T data) {
        this.data = data;
        consumers.forEach(consumer -> consumer.accept(this.data));
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public RegistrationHandler consumeData(Consumer<T> consumer) {
        consumers.add(consumer);
        if (nonNull(data)) {
            consumer.accept(data);
        }
        return () -> consumers.remove(consumer);
    }

    public void reset() {
        consumers.clear();
        this.data = null;
    }

}
