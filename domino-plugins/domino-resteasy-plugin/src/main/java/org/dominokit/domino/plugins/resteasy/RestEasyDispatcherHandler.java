package org.dominokit.domino.plugins.resteasy;

import io.netty.buffer.ByteBufInputStream;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import io.vertx.reactivex.core.Context;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpServerResponse;
import org.dominokit.domino.api.server.PluginContext;
import org.dominokit.domino.plugins.resteasy.resteasy.PluginRequestDispatcher;
import org.dominokit.domino.plugins.resteasy.resteasy.VertxHttpResponseWithWorkaround;
import org.dominokit.domino.plugins.resteasy.spi.Plugin;
import org.jboss.resteasy.core.SynchronousDispatcher;
import org.jboss.resteasy.plugins.server.embedded.SecurityDomain;
import org.jboss.resteasy.plugins.server.vertx.RequestDispatcher;
import org.jboss.resteasy.plugins.server.vertx.VertxHttpRequest;
import org.jboss.resteasy.plugins.server.vertx.VertxHttpResponse;
import org.jboss.resteasy.plugins.server.vertx.VertxUtil;
import org.jboss.resteasy.plugins.server.vertx.i18n.LogMessages;
import org.jboss.resteasy.plugins.server.vertx.i18n.Messages;
import org.jboss.resteasy.specimpl.ResteasyHttpHeaders;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.resteasy.spi.ResteasyUriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class RestEasyDispatcherHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestEasyDispatcherHandler.class);
    private final Vertx vertx;
    private final RequestDispatcher dispatcher;
    private final String servletMappingPrefix;
    private AppGlobals appGlobals;

    public RestEasyDispatcherHandler(PluginContext context, ResteasyDeployment deployment, String servletMappingPrefix, SecurityDomain domain, List<Plugin> plugins) {
        this.vertx = context.getRxVertx();
        this.dispatcher = new PluginRequestDispatcher((SynchronousDispatcher) deployment.getDispatcher(), deployment.getProviderFactory(), domain, plugins);
        this.servletMappingPrefix = servletMappingPrefix;
        appGlobals = AppGlobals.get();
    }

    public RestEasyDispatcherHandler(PluginContext context, ResteasyDeployment deployment, String servletMappingPrefix, List<Plugin> plugins) {
        this(context, deployment, servletMappingPrefix, null, plugins);
    }

    public RestEasyDispatcherHandler(PluginContext context, ResteasyDeployment deployment, List<Plugin> plugins) {
        this(context, deployment, "", plugins);
    }

    @Override
    public void handle(RoutingContext event) {
        HttpServerRequest request = event.request();
        io.vertx.reactivex.core.http.HttpServerRequest rxRequest = io.vertx.reactivex.core.http.HttpServerRequest.newInstance(request);
        Context ctx = vertx.getOrCreateContext();
        ResteasyUriInfo uriInfo = VertxUtil.extractUriInfo(rxRequest.getDelegate(), servletMappingPrefix);
        ResteasyHttpHeaders headers = VertxUtil.extractHttpHeaders(rxRequest.getDelegate());
        HttpServerResponse response = rxRequest.response();
        VertxHttpResponse vertxResponse = new VertxHttpResponseWithWorkaround(response.getDelegate(), dispatcher.getProviderFactory(), request.method());
        VertxHttpRequest vertxRequest = new VertxHttpRequest(ctx.getDelegate(), headers, uriInfo, request.rawMethod(), dispatcher.getDispatcher(), vertxResponse, false);

        Buffer body = event.getBody();
        if (body.length() > 0) {
            ByteBufInputStream in = new ByteBufInputStream(body.getByteBuf());
            vertxRequest.setInputStream(in);
        }

        try {
            AppGlobals.set(appGlobals);
            appGlobals.injectGlobals();
            dispatcher.service(ctx.getDelegate(), rxRequest.getDelegate(), response.getDelegate(), vertxRequest, vertxResponse, true);
        } catch (Failure failure) {
            vertxResponse.setStatus(failure.getErrorCode());
            LogMessages.LOGGER.error(Messages.MESSAGES.unexpected(), failure.getCause());
        } catch (Exception ex) {
            vertxResponse.setStatus(401);
            LogMessages.LOGGER.error(Messages.MESSAGES.unexpected(), ex);
        } finally {
            AppGlobals.set(null);
        }
        if (!vertxRequest.getAsyncContext().isSuspended()) {
            try {
                vertxResponse.finish();
            } catch (IOException e) {
                LOGGER.error("Could not finish response!", e);
            }
        }
    }
}
