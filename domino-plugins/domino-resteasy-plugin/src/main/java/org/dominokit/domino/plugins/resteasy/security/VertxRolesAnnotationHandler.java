package org.dominokit.domino.plugins.resteasy.security;

import io.reactivex.Single;
import io.vertx.reactivex.ext.auth.User;
import org.dominokit.domino.api.server.security.AuthorizationException;

import javax.annotation.security.RolesAllowed;
import java.lang.annotation.Annotation;

public class VertxRolesAnnotationHandler extends AuthorizingAnnotationHandler {

	@Override
	public Single<Boolean> assertAuthorized(Annotation authzSpec) {
		if(authzSpec instanceof RolesAllowed){
			User user = getUser();
			if(user == null)
				return Single.error(new AuthorizationException("User required"));
			Single<Boolean> ret = Single.just(true);
			for(String perm : ((RolesAllowed) authzSpec).value()){
				ret = user.rxIsAuthorized(perm).zipWith(ret, (a, b) -> a && b);
			}
			return ret;
		}
		return Single.just(true);
	}
}
