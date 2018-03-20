package org.dominokit.domino.apt.commons;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.Generated;
import javax.annotation.processing.Processor;
import javax.lang.model.element.Modifier;

public class DominoTypeBuilder {

    public static TypeSpec.Builder build(String name, Class<? extends Processor> processorClass) {
        return TypeSpec.classBuilder(name)
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
