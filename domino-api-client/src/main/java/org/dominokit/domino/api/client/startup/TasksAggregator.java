package org.dominokit.domino.api.client.startup;

import org.dominokit.domino.api.shared.extension.ContextAggregator;

import java.util.List;

import static java.util.Objects.nonNull;

public class TasksAggregator extends ContextAggregator.ContextWait<Void> implements Comparable<TasksAggregator> {
    private ContextAggregator contextAggregator;
    private List<AsyncClientStartupTask> tasks;
    private TasksAggregator nextAggregator;
    private Integer order;

    public TasksAggregator(int order, List<AsyncClientStartupTask> tasks) {
        this.order = order;
        this.tasks = tasks;
        this.contextAggregator = ContextAggregator.waitFor(tasks)
                .onReady(() -> {
                    complete(null);
                    if (nonNull(nextAggregator)) {
                        nextAggregator.execute();
                    }
                });
    }

    public TasksAggregator setNextAggregator(TasksAggregator nextAggregator) {
        this.nextAggregator = nextAggregator;
        return this;
    }

    public void execute() {
        tasks.forEach(ClientStartupTask::execute);
    }

    @Override
    public int compareTo(TasksAggregator o) {
        return this.order.compareTo(o.order);
    }
}
