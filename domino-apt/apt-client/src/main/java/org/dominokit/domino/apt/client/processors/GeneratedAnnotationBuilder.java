package org.dominokit.domino.apt.client.processors;

import com.squareup.javapoet.AnnotationSpec;

import javax.annotation.Generated;
import javax.annotation.processing.Processor;

public class GeneratedAnnotationBuilder {

    public static AnnotationSpec build(Class<? extends Processor> processorClass) {
        return AnnotationSpec.builder(Generated.class)
                .addMember("value", "\"" + processorClass.getCanonicalName() + "\"")
                .build();
    }
}
