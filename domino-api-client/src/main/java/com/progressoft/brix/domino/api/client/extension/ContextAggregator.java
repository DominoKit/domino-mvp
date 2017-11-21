package com.progressoft.brix.domino.api.client.extension;

import com.progressoft.brix.domino.api.shared.extension.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Objects.nonNull;

public class ContextAggregator {

    private static final Logger LOGGER= LoggerFactory.getLogger(ContextAggregator.class);
    private Set<ContextWait> contextsSet=new LinkedHashSet<>();
    private final ReadyHandler handler;

    private ContextAggregator(Set<ContextWait> contextSet,
                              ReadyHandler handler) {
        this.contextsSet=contextSet;
        this.handler = handler;
        this.contextsSet.forEach(c-> c.onReady(() -> {
            this.contextsSet.remove(c);
            if(this.contextsSet.isEmpty())
                handler.onReady();
        }));
    }

    public static CanWaitForContext waitFor(ContextWait context){
        return ContextAggregatorBuilder.waitForContext(context);
    }

    public interface CanWaitForContext{
        CanWaitForContext and(ContextWait context);
        ContextAggregator onReady(ReadyHandler handler);
    }

    @FunctionalInterface
    public interface ReadyHandler{
        void onReady();
    }

    private static class ContextAggregatorBuilder implements CanWaitForContext {

        private Set<ContextWait> contextSet=new LinkedHashSet<>();

        private ContextAggregatorBuilder(ContextWait context) {
            this.contextSet.add(context);
        }

        public static CanWaitForContext waitForContext(ContextWait context){
            return new ContextAggregatorBuilder(context);
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

    public static class ContextWait<T extends Context>{
        private ReadyHandler readyHandler;
        private T context;

        public static <T extends Context> ContextWait<T> create(){
            return new ContextWait<>();
        }

        void onReady(ReadyHandler handler){
            this.readyHandler=handler;
        }

        public void receiveContext(T context){
            this.context=context;
            if(nonNull(readyHandler))
                readyHandler.onReady();
        }

        public T get(){
            return context;
        }
    }
}
