package org.dominokit.domino.apt.client.processors.module.client.initialtasks;

import org.dominokit.domino.api.client.InitialTaskRegistry;
import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

public class RegisterInitialTasksMethodWriter extends AbstractRegisterMethodWriter<InitialTaskEntry, String> {

    public RegisterInitialTasksMethodWriter(TypeSpec.Builder clientModuleTypeBuilder) {
        super(clientModuleTypeBuilder);
    }

    @Override
    protected String methodName() {
        return "registerInitialTasks";
    }

    @Override
    protected Class<?> registryClass() {
        return InitialTaskRegistry.class;
    }

    @Override
    protected void registerItem(InitialTaskEntry entry, MethodSpec.Builder methodBuilder) {
        methodBuilder.addStatement("registry.registerInitialTask(new $T())", ClassName.bestGuess(entry.initalTask));
    }

    @Override
    protected InitialTaskEntry parseEntry(String item) {
        return new InitialTaskEntry(item);
    }
}
