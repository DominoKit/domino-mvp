package org.dominokit.domino.api.shared.compile;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.TYPE, ElementType.METHOD,
        ElementType.CONSTRUCTOR, ElementType.FIELD })
@Documented
public @interface GwtIncompatible {
    /**
     * An attribute that can be used to explain why the code is incompatible.
     * A GwtIncompatible annotation can have any number of attributes; attributes
     * are for documentation purposes and are ignored by the GWT compiler.
     */
    String value() default "";
}
