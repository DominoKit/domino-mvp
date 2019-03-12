package org.dominokit.domino.api.server.security;

import io.reactivex.Single;
import io.vertx.reactivex.ext.auth.User;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import java.lang.annotation.Annotation;

public abstract class AuthorizingAnnotationHandler {

	protected User getUser() {
		RoutingContext ctx = ResteasyProviderFactory.getContextData(RoutingContext.class);
		return ctx.user();
	}

	public abstract Single<Boolean> assertAuthorized(Annotation authzSpec);

}
