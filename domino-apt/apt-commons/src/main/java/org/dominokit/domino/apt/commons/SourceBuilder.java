package org.dominokit.domino.apt.commons;

import com.squareup.javapoet.TypeSpec;

import java.util.List;

public interface SourceBuilder {

    List<TypeSpec.Builder> asTypeBuilder();
}
