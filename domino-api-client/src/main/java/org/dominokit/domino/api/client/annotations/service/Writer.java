package org.dominokit.domino.api.client.annotations.service;

import org.dominokit.domino.api.client.request.RequestWriter;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Writer {
    @NotNull
    Class<? extends RequestWriter> value();
}
