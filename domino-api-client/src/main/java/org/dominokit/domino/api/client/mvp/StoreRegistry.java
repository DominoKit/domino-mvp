package org.dominokit.domino.api.client;

import org.dominokit.domino.api.client.mvp.IsStore;
import org.dominokit.domino.api.client.mvp.RegistrationHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Objects.isNull;

public class StoreRegistry {
    public static final StoreRegistry INSTANCE = new StoreRegistry();

    private Map<String, IsStore<?>> stores = new HashMap<>();
    private Map<String, List<Consumer<IsStore<?>>>> consumers = new HashMap<>();

    private StoreRegistry(){

    }

    public RegistrationHandler registerStore(String key, IsStore<?> store){
        stores.put(key, store);
        addConsumer(key, store);

        return () -> {
            stores.remove(key);
            consumers.remove(key);
        };
    }

    private void addConsumer(String key, IsStore<?> store) {
        if(isNull(consumers.get(key))){
            consumers.put(key, new ArrayList<>());
        }else{
            consumers.get(key).forEach(consumer-> consumer.accept(store));
        }
    }

    public <T> IsStore<T> getStore(String key){
        return (IsStore<T>) stores.get(key);
    }
    public <T> RegistrationHandler consumeStore(String key, Consumer<IsStore<T>> consumer){
        addConsumer(key, (IsStore<?>) consumer);

        if(stores.containsKey(key)){
            consumer.accept((IsStore<T>) stores.get(key));
        }
        return () -> consumers.get(key).remove(consumer);
    }
}
