package org.dominokit.domino.api.server.security;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.reactivex.ext.auth.AuthProvider;
import io.vertx.reactivex.ext.auth.oauth2.OAuth2Auth;
import io.vertx.reactivex.ext.auth.oauth2.providers.KeycloakAuth;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.OAuth2AuthHandler;
import org.dominokit.domino.api.server.AppGlobals;

public class AuthenticationProviderConfigurator {

    public AuthProvider setupAuthenticationRoutes() {

        JsonObject keycloackConfig = AppGlobals.get().getConfig().getJsonObject("keycloack");
        JsonObject securityConfig = AppGlobals.get().getConfig().getJsonObject("security");

//        OAuth2Auth authWeb = KeycloakAuth.create(AppGlobals.get().getVertx(), keycloackConfig);
        OAuth2Auth authApi = KeycloakAuth.create(AppGlobals.get().getVertx(), OAuth2FlowType.AUTH_CODE, keycloackConfig);

        // FIXME: URL
        OAuth2AuthHandler authHandler = OAuth2AuthHandler.create(authApi, AppGlobals.get().getConfig().getString("callbackUrl", "http://localhost:8080"));
        Router router = AppGlobals.get().getRouter();
        // FIXME: crazy!!
        AuthProvider authProvider = AuthProvider.newInstance(authApi.getDelegate());
//        router.route().handler(UserSessionHandler.create(authProvider));

        authHandler.setupCallback(router.get("/callback"));
//
//        JWTAuth jwtAuth = JWTAuth.create(AppGlobals.get().getVertx(), new JWTAuthOptions(new JsonObject()
//                .put("keyStore", AppGlobals.get().getConfig().getJsonObject("keystore"))));
//        AppGlobals.get().setGlobal(JWTAuth.class, jwtAuth);

//        JWTAuthHandler jwtAuthHandler = JWTAuthHandler.create(jwtAuth, "/wiki/api/token");

        // FIXME: just use different routers
        router.route("/*").handler(ctx -> {

                authHandler.handle(ctx);
        });

        return AuthProvider.newInstance(authApi.getDelegate());
    }
}
