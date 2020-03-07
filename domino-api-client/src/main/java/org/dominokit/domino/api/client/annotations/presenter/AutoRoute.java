package org.dominokit.domino.api.client.annotations.presenter;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoRoute {


    String token() default "";


    boolean routeOnce() default false;


    boolean reRouteActivated() default false;
}
