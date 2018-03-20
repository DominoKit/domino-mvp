package org.dominokit.domino.api.client.annotations;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Path {
    @NotNull
    String value();
    @NotNull
    String serviceRoot() default "";
    @NotNull
    String method() default "POST";
}
