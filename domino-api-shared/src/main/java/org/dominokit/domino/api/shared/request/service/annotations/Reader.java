package org.dominokit.domino.api.shared.request.service.annotations;

import org.dominokit.domino.api.shared.request.ResponseReader;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Reader {
    @NotNull
    Class<? extends ResponseReader> value();
}
