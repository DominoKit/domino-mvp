package org.dominokit.domino.apt.server;

import com.google.auto.service.AutoService;
import org.dominokit.domino.api.server.config.ServerModule;
import org.dominokit.domino.api.server.config.ServerModuleConfiguration;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;
import org.dominokit.domino.apt.commons.JavaSourceWriter;
import org.dominokit.domino.apt.commons.ProcessorElement;
import org.dominokit.domino.apt.server.endpoints.EndpointsRegisterMethodWriter;
import org.dominokit.domino.apt.server.handlers.HandlersRegisterMethodWriter;
import org.dominokit.domino.apt.server.interceptors.InterceptorsRegisterMethodWriter;
import org.dominokit.domino.apt.server.interceptors.global.GlobalInterceptorsRegisterMethodWriter;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.dominokit.domino.apt.server.endpoints.EndpointsRegisterMethodWriter;
import org.dominokit.domino.apt.server.interceptors.InterceptorsRegisterMethodWriter;
import org.dominokit.domino.apt.server.interceptors.global.GlobalInterceptorsRegisterMethodWriter;

import java.io.IOException;
import java.util.List;

public class ServerModuleSourceWriter extends JavaSourceWriter {

    private final List<ProcessorElement> handlers;
    private final List<ProcessorElement> interceptors;
    private final List<ProcessorElement> globalInterceptors;

    public ServerModuleSourceWriter(ProcessorElement serverModuleElement,
                                    List<ProcessorElement> handlers,
                                    List<ProcessorElement> interceptors,
                                    List<ProcessorElement> globalInterceptors) {
        super(serverModuleElement);
        this.handlers = handlers;
        this.interceptors = interceptors;
        this.globalInterceptors = globalInterceptors;
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
        new HandlersRegisterMethodWriter(serverModuleTypeBuilder).write(handlers);
        new EndpointsRegisterMethodWriter(serverModuleTypeBuilder).write(handlers);
        new InterceptorsRegisterMethodWriter(serverModuleTypeBuilder).write(interceptors);
        new GlobalInterceptorsRegisterMethodWriter(serverModuleTypeBuilder).write(globalInterceptors);
    }
}
