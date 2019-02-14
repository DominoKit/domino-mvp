package org.dominokit.domino.apt.server;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.dominokit.domino.api.server.config.ServerModule;
import org.dominokit.domino.api.server.config.ServerModuleConfiguration;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;
import org.dominokit.domino.apt.commons.JavaSourceWriter;
import org.dominokit.domino.apt.commons.ProcessorElement;
import org.dominokit.domino.apt.server.handlers.ResourceRegisterMethodWriter;

import java.io.IOException;
import java.util.List;

public class ServerModuleSourceWriter extends JavaSourceWriter {

    private final List<ProcessorElement> resources;

    public ServerModuleSourceWriter(ProcessorElement serverModuleElement,
                                    List<ProcessorElement> resources) {
        super(serverModuleElement);
        this.resources = resources;
    }

    @Override
    public String write() throws IOException {
        AnnotationSpec autoServiceAnnotation = AnnotationSpec.builder(AutoService.class)
                .addMember("value", "$T.class", ServerModuleConfiguration.class).build();
        TypeSpec.Builder serverModuleTypeBuilder = DominoTypeBuilder.build(processorElement.getAnnotation(ServerModule.class).name() +
                "ServerModule", ServerModuleAnnotationProcessor.class)
                .addAnnotation(autoServiceAnnotation)
                .addSuperinterface(ServerModuleConfiguration.class);

        writeRegisterMethods(serverModuleTypeBuilder);

        StringBuilder asString = new StringBuilder();
        JavaFile.builder(processorElement.elementPackage(), serverModuleTypeBuilder.build()).skipJavaLangImports(true)
                .build().writeTo(asString);
        return asString.toString();
    }

    private void writeRegisterMethods(TypeSpec.Builder serverModuleTypeBuilder) {
        new ResourceRegisterMethodWriter(serverModuleTypeBuilder).write(resources);
    }
}
