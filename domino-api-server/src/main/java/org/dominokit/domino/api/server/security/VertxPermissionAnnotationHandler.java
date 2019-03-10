package org.dominokit.domino.api.server.security;

import io.reactivex.Single;
import io.vertx.reactivex.ext.auth.User;

import java.lang.annotation.Annotation;

public class VertxPermissionAnnotationHandler extends AuthorizingAnnotationHandler {

	@Override
	public Single<Boolean> assertAuthorized(Annotation authzSpec) {
		if(authzSpec instanceof RequiresPermissions){
			User user = getUser();
			if(user == null)
				return Single.error(new AuthorizationException("User required"));
			Single<Boolean> ret = Single.just(true);
			for(String perm : ((RequiresPermissions) authzSpec).value()){
				ret = user.rxIsAuthorized(perm).zipWith(ret, (a, b) -> a && b);
			}
			return ret;
		}
		return Single.just(true);
	}
}
