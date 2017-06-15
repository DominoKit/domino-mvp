package com.progressoft.brix.domino.apt.client.processors.module.client.initialtasks;

import com.progressoft.brix.domino.api.client.InitializeTask;
import com.progressoft.brix.domino.api.client.annotations.InitialTask;
import com.progressoft.brix.domino.apt.commons.BaseProcessor;

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
        roundEnv.getElementsAnnotatedWith(InitialTask.class).stream()
                .map(elementFactory::make)
                .filter(e -> e.validateElementKind(ElementKind.CLASS))
                .filter(i -> i.isImplementsInterface(InitializeTask.class))
                .collect(Collectors.toSet())
                .forEach(i -> initialTasks.add(i.fullQualifiedNoneGenericName()));
    }
}