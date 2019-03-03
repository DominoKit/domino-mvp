package org.dominokit.domino.apt.server.handlers;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.dominokit.domino.api.server.resource.ResourceRegistry;
import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;
import org.dominokit.domino.apt.commons.ProcessorElement;

public class ResourceRegisterMethodWriter extends AbstractRegisterMethodWriter<ResourceEntry, ProcessorElement> {

    public ResourceRegisterMethodWriter(TypeSpec.Builder serverModuleTypeBuilder) {
        super(serverModuleTypeBuilder);
    }

    @Override
    protected String methodName() {
        return "registerResources";
    }

    @Override
    protected Class<?> registryClass() {
        return ResourceRegistry.class;
    }

    @Override
    protected void registerItem(ResourceEntry entry, MethodSpec.Builder methodBuilder) {
        methodBuilder.addStatement("registry.registerResource($L.class)", TypeName.get(entry.resource.getElement().asType()));
    }

    @Override
    protected ResourceEntry parseEntry(ProcessorElement handler) {
        return new ResourceEntry(handler);
    }
}
