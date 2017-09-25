package com.progressoft.brix.domino.apt.client.processors.module.client;

import com.google.auto.service.AutoService;
import com.google.common.annotations.GwtIncompatible;
import com.progressoft.brix.domino.api.client.ConfigurationProvider;
import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import com.progressoft.brix.domino.api.client.annotations.ClientModule;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.IOException;

public class ConfigurationProviderSourceWriter {

    private final ProcessorElement element;

    public ConfigurationProviderSourceWriter(ProcessorElement element) {
        this.element = element;
    }

    public String write() throws IOException {
        MethodSpec getMethod = MethodSpec.methodBuilder("get")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(ModuleConfiguration.class)
                .addStatement("return new " + element.getAnnotation(ClientModule.class).name() + "ModuleConfiguration()")
                .build();
        AnnotationSpec autoService = AnnotationSpec.builder(AutoService.class).addMember("value", "ConfigurationProvider.class").build();
        AnnotationSpec gwtIncompatible = AnnotationSpec.builder(GwtIncompatible.class).addMember("value", "\"Unused in GWT compilation\"").build();

        TypeSpec configurationProvider = TypeSpec.classBuilder(element.getAnnotation(ClientModule.class).name() + "ModuleConfiguration_Provider")
                .addAnnotation(gwtIncompatible)
                .addAnnotation(autoService)
                .addSuperinterface(ConfigurationProvider.class)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(getMethod)
                .build();

        JavaFile javaFile = JavaFile.builder(element.elementPackage(), configurationProvider)
                .build();

        StringBuilder classAsString = new StringBuilder();
        javaFile.writeTo(classAsString);
        return classAsString.toString();
    }
}
