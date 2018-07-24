package org.dominokit.domino.api.client.annotations;

import org.dominokit.domino.api.shared.extension.DominoEvent;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ListenTo {
    @NotNull
    Class<? extends DominoEvent> event();
}
