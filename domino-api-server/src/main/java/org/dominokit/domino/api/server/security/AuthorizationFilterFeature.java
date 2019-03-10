package org.dominokit.domino.api.server.security;

import com.google.auto.service.AutoService;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Provider
@AutoService(Providers.class)
public class AuthorizationFilterFeature implements DynamicFeature {

    private static List<Class<? extends Annotation>> filterAnnotations = Collections.unmodifiableList(Arrays.asList(
            RequiresPermissions.class,
            RequiresUser.class,
            RolesAllowed.class));

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {

        List<Annotation> authzSpecs = new ArrayList<>();
        for (Class<? extends Annotation> annotationClass : filterAnnotations) {
            Annotation classAuthzSpec = resourceInfo.getResourceClass().getAnnotation(annotationClass);
            Annotation methodAuthzSpec = resourceInfo.getResourceMethod().getAnnotation(annotationClass);

            if (classAuthzSpec != null) authzSpecs.add(classAuthzSpec);
            if (methodAuthzSpec != null) authzSpecs.add(methodAuthzSpec);
            
            if(resourceInfo.getResourceClass().isAnnotationPresent(NoAuthFilter.class)
            		|| resourceInfo.getResourceMethod().isAnnotationPresent(NoAuthFilter.class))
            	return;
        }

        if (!authzSpecs.isEmpty()) {
            context.register(new AuthorizationFilter(authzSpecs), Priorities.AUTHORIZATION);
        }
    }

}
