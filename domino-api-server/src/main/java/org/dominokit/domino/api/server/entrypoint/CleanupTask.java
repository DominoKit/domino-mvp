package org.dominokit.domino.api.server.entrypoint;

import java.util.function.Consumer;

import static java.util.Objects.nonNull;

public class CleanupTask {

    private final Consumer<CleanupTask> operation;
    private CleanupTask next;
    private Consumer<CleanupTask> completeConsumer = cleanupTask -> {};
    private final VertxContext vertxContext;

    public CleanupTask(Consumer<CleanupTask> operation, VertxContext vertxContext) {
        this.operation = operation;
        this.vertxContext = vertxContext;
    }

    public void setNext(CleanupTask next) {
        this.next = next;
    }

    public void setCompleteConsumer(Consumer<CleanupTask> completeConsumer) {
        this.completeConsumer = completeConsumer;
    }

    public Consumer<CleanupTask> getCleanUpService() {
        return operation;
    }

    public VertxContext getVertxContext() {
        return vertxContext;
    }

    public void run(){
        operation.accept(this);
    }

    public void next(){
        if(nonNull(next)){
            next.run();
        }else{
            completeConsumer.accept(this);
        }
    }
}
