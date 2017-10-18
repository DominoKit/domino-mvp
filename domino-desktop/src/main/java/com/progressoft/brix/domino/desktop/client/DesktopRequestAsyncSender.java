package com.progressoft.brix.domino.desktop.client;

import com.progressoft.brix.domino.api.client.ServiceRootMatcher;
import com.progressoft.brix.domino.api.client.annotations.Path;
import com.progressoft.brix.domino.api.client.events.ServerRequestEventFactory;
import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.api.client.request.Request;
import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import com.progressoft.brix.domino.client.commons.request.AbstractRequestAsyncSender;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;

public class DesktopRequestAsyncSender extends AbstractRequestAsyncSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopRequestAsyncSender.class);

    private final Vertx vertx;
    private final WebClient webClient;

    public DesktopRequestAsyncSender(ServerRequestEventFactory requestEventFactory) {
        super(requestEventFactory);
        vertx = Vertx.vertx();
        webClient = WebClient.create(vertx);
    }

    @Override
    protected void sendRequest(ClientServerRequest request, ServerRequestEventFactory requestEventFactory) {
        Path pathAnnotation = request.getClass().getAnnotation(Path.class);
        HttpMethod method = HttpMethod.valueOf(pathAnnotation.method());
        String classifier=request.getClass().getAnnotation(com.progressoft.brix.domino.api.client.annotations.Request.class).classifier();



        String absoluteURI = buildPath(pathAnnotation, request.arguments());
        HttpRequest<Buffer> httpRequest = webClient.requestAbs(method, absoluteURI);

        if(classifier.isEmpty())
            httpRequest.putHeader("REQUEST_KEY", request.arguments().getRequestKey());
        else
            httpRequest.putHeader("REQUEST_KEY", request.arguments().getRequestKey()+"_"+classifier);

        httpRequest.putHeader("Content-Type", "application/json");
        if (HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method) || HttpMethod.PATCH.equals(method))
            httpRequest.putHeader("Accept", "application/json");

        ParameterizedType parameterizedType = (ParameterizedType) request.getClass().getGenericSuperclass();
        Type type = parameterizedType.getActualTypeArguments()[2];

        try {
            Class<? extends ServerResponse> clazz = (Class<? extends ServerResponse>) Class.forName(type.getTypeName());
            httpRequest.sendJson(request.arguments(), event -> {
                if (event.succeeded())
                    requestEventFactory.makeSuccess(request, Json.decodeValue(event.result().body(), clazz)).fire();
                else
                    requestEventFactory.makeFailed(request, event.cause()).fire();
            });
        } catch (ClassNotFoundException e) {
            requestEventFactory.makeFailed(request, e);
        }


    }

    private String buildPath(Path pathAnnotation, ServerRequest arguments) {

        String path = formattedPath(getPathParams(pathAnnotation.value()), arguments, pathAnnotation.value());

        String serviceRoot;
        if (pathAnnotation.serviceRoot().isEmpty()) {
            serviceRoot = ServiceRootMatcher.matchedServiceRoot(path);
        } else {
            serviceRoot = pathAnnotation.serviceRoot();
        }
        if (serviceRoot.endsWith("/") || pathAnnotation.value().startsWith("/"))
            return (serviceRoot + path).replace("//", "/");
        return (serviceRoot + "/" + path);
    }

    private String formattedPath(Collection<String> pathParams, ServerRequest arguments, String path) {
        final String[] processedPath = {path};
        pathParams.forEach(p -> {
            try {
                String value;
                if (isNested(p)) {
                    value = getNestedValue(arguments, p);
                } else
                    value = BeanUtils.getProperty(arguments, p);

                processedPath[0] = processedPath[0].replace("{" + p + "}", isNull(value) ? "" : value);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                processedPath[0] = processedPath[0].replace("{" + p + "}", p);
            }
        });

        return processedPath[0];
    }

    private String getNestedValue(ServerRequest arguments, String p) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (!isNestedNull(arguments, p.split("\\.")))
            return BeanUtils.getProperty(arguments, p);
        return "";
    }

    private boolean isNested(String p) {
        return p.contains(".");
    }

    private boolean isNestedNull(ServerRequest arguments, String[] nestedParts) {
        return IntStream.range(0, nestedParts.length - 1).anyMatch(i -> {
            try {
                return isNull(BeanUtils.getProperty(arguments, nestedParts[i]));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                LOGGER.error("Error while getting nested value", e);
                return false;
            }
        });
    }

    private Collection<String> getPathParams(String path) {
        Set<String> paramsNames = new HashSet<>();
        Matcher pathParamMatcher = Pattern.compile("\\{(.*?)\\}").matcher(path);
        while (pathParamMatcher.find()) {
            paramsNames.add(pathParamMatcher.group().replace("{", "").replace("}", ""));
        }

        return paramsNames;
    }

}
