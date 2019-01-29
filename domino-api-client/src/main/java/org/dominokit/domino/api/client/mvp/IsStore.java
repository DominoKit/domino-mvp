package org.dominokit.domino.api.client.mvp;

import java.util.function.Consumer;

public interface IsStore<T> {
    T getData();
    RegistrationHandler consumeData(Consumer<T> consumer);
}
