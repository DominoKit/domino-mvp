package com.progressoft.brix.domino.apt.client.processors.module.client.initialtasks;

import com.progressoft.brix.domino.api.client.InitialTaskRegistry;
import com.progressoft.brix.domino.api.client.request.RequestRegistry;
import com.progressoft.brix.domino.apt.commons.FullClassName;
import com.progressoft.brix.domino.apt.commons.JavaSourceBuilder;
import com.progressoft.brix.domino.apt.commons.MethodBuilder;
import com.progressoft.brix.domino.apt.commons.ModifierBuilder;

import java.util.Set;

public class InitialTasksWriter {

    private final JavaSourceBuilder sourceBuilder;

    public InitialTasksWriter(JavaSourceBuilder sourceBuilder) {
        this.sourceBuilder = sourceBuilder;
    }

    public void write(Set<String> initialTasks){

        if(!initialTasks.isEmpty()){
            sourceBuilder.imports(InitialTaskRegistry.class.getCanonicalName());

            MethodBuilder methodBuilder = this.sourceBuilder.method("registerInitialTasks");
            methodBuilder.annotate("@Override")
                    .withModifier(new ModifierBuilder().asPublic())
                    .returnsVoid()
                    .takes("InitialTaskRegistry", "registry");
            initialTasks.stream()
                    .forEach(e-> registerInitialTask(e, methodBuilder));
            methodBuilder.end();
        }
    }

    private void registerInitialTask(String initialTask, MethodBuilder methodBuilder) {
        sourceBuilder.imports(initialTask);
        methodBuilder.line("registry.registerInitialTask(new "+new FullClassName(initialTask).asSimpleName()+"());");
    }

}
