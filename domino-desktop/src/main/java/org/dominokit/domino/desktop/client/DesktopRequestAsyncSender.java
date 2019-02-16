package org.dominokit.domino.desktop.client;

import com.fasterxml.jackson.databind.SerializationFeature;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import org.apache.commons.beanutils.BeanUtils;
import org.dominokit.domino.api.client.ServiceRootMatcher;
import org.dominokit.domino.api.client.annotations.service.ServiceRoot;
import org.dominokit.domino.api.client.events.ServerRequestEventFactory;
import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.api.shared.request.FailedResponseBean;
import org.dominokit.domino.api.shared.request.ResponseBean;
import org.dominokit.domino.client.commons.request.AbstractRequestAsyncSender;

import javax.ws.rs.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static io.vertx.ext.web.handler.CSRFHandler.DEFAULT_HEADER_NAME;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class DesktopRequestAsyncSender extends AbstractRequestAsyncSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopRequestAsyncSender.class);
    public static final int RESPONSE_TYPE_INDEX = 1;

    static {
        Json.mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    private final WebClient webClient;
    private String csrfToken;

    public DesktopRequestAsyncSender(ServerRequestEventFactory requestEventFactory) {
        super(requestEventFactory);
        webClient = WebClient.create(Vertx.vertx());
    }

    @Override
    protected void sendRequest(ServerRequest request, ServerRequestEventFactory requestEventFactory) {
        HttpMethod method = getHttpMethod(request);


        String absoluteURI = buildPath(request);
        HttpRequest<Buffer> httpRequest = webClient.requestAbs(method, absoluteURI);

        if (nonNull(csrfToken))
            httpRequest.putHeader(DEFAULT_HEADER_NAME, csrfToken);
        ((Map<String, String>) request.headers()).forEach(httpRequest::putHeader);

        httpRequest.putHeader("Content-Type", "application/json");
        if (HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method) || HttpMethod.PATCH.equals(method))
            httpRequest.putHeader("Accept", "application/json");

        ParameterizedType parameterizedType = (ParameterizedType) request.getClass().getGenericSuperclass();
        Type responseType = parameterizedType.getActualTypeArguments()[RESPONSE_TYPE_INDEX];

        try {
            Class<? extends ResponseBean> clazz = (Class<? extends ResponseBean>) Class.forName(responseType.getTypeName());
            httpRequest.sendJson(request.requestBean(), event -> {
                if (event.succeeded()) {
                    this.csrfToken = event.result().headers().getAll("Set-Cookie")
                            .stream()
                            .filter(header -> header.startsWith("XSRF-TOKEN"))
                            .map(header -> header.substring(0, header.indexOf(";")).replace("XSRF-TOKEN=", ""))
                            .findFirst().orElse(csrfToken);

                    requestEventFactory.makeSuccess(request, Json.decodeValue(event.result().body(), clazz)).fire();
                } else
                    requestEventFactory.makeFailed(request, new FailedResponseBean(event.cause())).fire();
            });
        } catch (ClassNotFoundException e) {
            requestEventFactory.makeFailed(request, new FailedResponseBean(e));
        }
    }

    private HttpMethod getHttpMethod(ServerRequest request) {

        if (nonNull(request.getClass().getAnnotation(GET.class))) {
            return HttpMethod.GET;
        }

        if (nonNull(request.getClass().getAnnotation(POST.class))) {
            return HttpMethod.POST;
        }

        if (nonNull(request.getClass().getAnnotation(PUT.class))) {
            return HttpMethod.PUT;
        }

        if (nonNull(request.getClass().getAnnotation(DELETE.class))) {
            return HttpMethod.DELETE;
        }

        if (nonNull(request.getClass().getAnnotation(PATCH.class))) {
            return HttpMethod.PATCH;
        }

        if (nonNull(request.getClass().getAnnotation(OPTIONS.class))) {
            return HttpMethod.OPTIONS;
        }

        if (nonNull(request.getClass().getAnnotation(HEAD.class))) {
            return HttpMethod.HEAD;
        }

        return HttpMethod.GET;
    }

    private String buildPath(ServerRequest request) {
        Path pathAnnotation= request.getClass().getAnnotation(Path.class);
        ServiceRoot serviceRootAnnotation= request.getClass().getAnnotation(ServiceRoot.class);
        String path = formattedPath(getPathParams(pathAnnotation.value()), request.requestBean(), pathAnnotation.value());

        String serviceRoot;
        if (serviceRootAnnotation.value().isEmpty()) {
            serviceRoot = ServiceRootMatcher.matchedServiceRoot(path);
        } else {
            serviceRoot = serviceRootAnnotation.value();
        }
        if (serviceRoot.endsWith("/") || pathAnnotation.value().startsWith("/"))
            return (serviceRoot + path).replace("//", "/");
        return (serviceRoot + "/" + path);
    }

    private String formattedPath(Collection<String> pathParams, Object arguments, String path) {
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

    private String getNestedValue(Object arguments, String p) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (!isNestedNull(arguments, p.split("\\.")))
            return BeanUtils.getProperty(arguments, p);
        return "";
    }

    private boolean isNested(String p) {
        return p.contains(".");
    }

    private boolean isNestedNull(Object arguments, String[] nestedParts) {
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
