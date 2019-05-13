package org.dominokit.domino.apt.commons;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.Generated;
import javax.annotation.processing.Processor;
import javax.lang.model.element.Modifier;

public class DominoTypeBuilder {

    public static TypeSpec.Builder classBuilder(String name, Class<? extends Processor> processorClass) {
        return TypeSpec.classBuilder(name)
                .addAnnotation(generatedAnnotation(processorClass))
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("This is generated class, please don't modify\n");
    }

    public static TypeSpec.Builder interfaceBuilder(String name, Class<? extends Processor> processorClass) {
        return TypeSpec.interfaceBuilder(name)
                .addAnnotation(generatedAnnotation(processorClass))
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("This is generated class, please don't modify\n");
    }

    public static TypeSpec.Builder enumBuilder(String name, Class<? extends Processor> processorClass) {
        return TypeSpec.enumBuilder(name)
                .addAnnotation(generatedAnnotation(processorClass))
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("This is generated class, please don't modify\n");
    }

    private static AnnotationSpec generatedAnnotation(Class<? extends Processor> processorClass) {
        return AnnotationSpec.builder(Generated.class)
                .addMember("value", "\"" + processorClass.getCanonicalName() + "\"")
                .build();
    }
}
