package org.dominokit.domino.api.client.extension;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class ContextAggregator {

    private Set<ContextWait> contextsSet = new LinkedHashSet<>();

    private ContextAggregator(Set<ContextWait> contextSet,
                              ReadyHandler handler) {
        this.contextsSet = contextSet;
        this.contextsSet.forEach(c -> c.onReady(() -> {
            this.contextsSet.remove(c);
            if (this.contextsSet.isEmpty())
                handler.onReady();
        }));
    }

    public static CanWaitForContext waitFor(ContextWait context) {
        return ContextAggregatorBuilder.waitForContext(context);
    }

    public static CanWaitForContext waitFor(Collection<? extends ContextWait> contexts) {
        return ContextAggregatorBuilder.waitForContext(contexts);
    }

    public interface CanWaitForContext {
        <T extends ContextWait> CanWaitForContext and(T context);

        ContextAggregator onReady(ReadyHandler handler);
    }

    @FunctionalInterface
    public interface ReadyHandler {
        void onReady();
    }

    private static class ContextAggregatorBuilder implements CanWaitForContext {

        private Set<ContextWait> contextSet = new LinkedHashSet<>();

        private ContextAggregatorBuilder(ContextWait context) {
            this.contextSet.add(context);
        }

        private ContextAggregatorBuilder(Collection<? extends ContextWait> contexts) {
            this.contextSet.addAll(contexts);
        }

        public static <T extends ContextWait> CanWaitForContext waitForContext(T context) {
            return new ContextAggregatorBuilder(context);
        }

        public static CanWaitForContext waitForContext(Collection<? extends ContextWait> contexts) {
            return new ContextAggregatorBuilder(contexts);
        }

        @Override
        public CanWaitForContext and(ContextWait context) {
            this.contextSet.add(context);
            return this;
        }

        @Override
        public ContextAggregator onReady(ReadyHandler handler) {
            return new ContextAggregator(contextSet, handler);
        }
    }

    public static class ContextWait<T> {
        private Set<ReadyHandler> readyHandlers = new LinkedHashSet<>();
        private T result;

        public static <T> ContextWait<T> create() {
            return new ContextWait<>();
        }

        void onReady(ReadyHandler handler) {
            this.readyHandlers.add(handler);
        }

        public void complete(T result) {
            this.result = result;
            readyHandlers.forEach(ReadyHandler::onReady);
        }

        public T get() {
            return result;
        }
    }
}
