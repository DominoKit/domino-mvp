package org.dominokit.domino.api.client.annotations;

import org.dominokit.domino.api.shared.extension.ActivationEvent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface EventsGroup {
    Class<? extends ActivationEvent>[] value() default {};
}
