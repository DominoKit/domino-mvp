package org.dominokit.domino.api.server.security;

import io.reactivex.Single;
import io.vertx.reactivex.ext.auth.User;

import java.lang.annotation.Annotation;

public class VertxUserAnnotationHandler extends AuthorizingAnnotationHandler {

	@Override
	public Single<Boolean> assertAuthorized(Annotation authzSpec) {
		if(authzSpec instanceof RequiresUser){
			User user = getUser();
			if(user == null)
				return Single.error(new AuthenticationException("User required"));
		}
		return Single.just(true);
	}
}
