package org.dominokit.domino.apt.client.processors.module.client.initialtasks;

import org.dominokit.domino.api.client.ClientStartupTask;
import org.dominokit.domino.api.client.annotations.StartupTask;
import org.dominokit.domino.apt.commons.BaseProcessor;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import java.util.Set;
import java.util.stream.Collectors;

public class InitialTasksCollector {

    private final BaseProcessor.ElementFactory elementFactory;
    private final Set<String> initialTasks;

    public InitialTasksCollector(BaseProcessor.ElementFactory elementFactory, Set<String> initialTasks) {
        this.elementFactory = elementFactory;
        this.initialTasks = initialTasks;
    }

    public void collectInitialTasks(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(StartupTask.class).stream()
                .map(elementFactory::make)
                .filter(e -> e.validateElementKind(ElementKind.CLASS))
                .filter(i -> i.isImplementsInterface(ClientStartupTask.class))
                .collect(Collectors.toSet())
                .forEach(i -> initialTasks.add(i.fullQualifiedNoneGenericName()));
    }
}