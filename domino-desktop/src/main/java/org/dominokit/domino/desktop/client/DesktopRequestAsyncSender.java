package org.dominokit.domino.desktop.client;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.dominokit.domino.api.client.ServiceRootMatcher;
import org.dominokit.domino.api.client.annotations.Path;
import org.dominokit.domino.api.client.events.ServerRequestEventFactory;
import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.api.shared.request.RequestBean;
import org.dominokit.domino.api.shared.request.ResponseBean;
import org.dominokit.domino.client.commons.request.AbstractRequestAsyncSender;
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
    public static final int RRESPONSE_TYPE_INDEX = 1;

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
        Path pathAnnotation = request.getClass().getAnnotation(Path.class);
        HttpMethod method = HttpMethod.valueOf(pathAnnotation.method());


        String absoluteURI = buildPath(pathAnnotation, request.requestBean());
        HttpRequest<Buffer> httpRequest = webClient.requestAbs(method, absoluteURI);

        if (nonNull(csrfToken))
            httpRequest.putHeader(DEFAULT_HEADER_NAME, csrfToken);
        ((Map<String, String>) request.headers()).forEach(httpRequest::putHeader);

        httpRequest.putHeader("Content-Type", "application/json");
        if (HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method) || HttpMethod.PATCH.equals(method))
            httpRequest.putHeader("Accept", "application/json");

        ParameterizedType parameterizedType = (ParameterizedType) request.getClass().getGenericSuperclass();
        Type responseType = parameterizedType.getActualTypeArguments()[RRESPONSE_TYPE_INDEX];

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
                    requestEventFactory.makeFailed(request, event.cause()).fire();
            });
        } catch (ClassNotFoundException e) {
            requestEventFactory.makeFailed(request, e);
        }
    }

    private String buildPath(Path pathAnnotation, RequestBean arguments) {

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

    private String formattedPath(Collection<String> pathParams, RequestBean arguments, String path) {
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

    private String getNestedValue(RequestBean arguments, String p) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (!isNestedNull(arguments, p.split("\\.")))
            return BeanUtils.getProperty(arguments, p);
        return "";
    }

    private boolean isNested(String p) {
        return p.contains(".");
    }

    private boolean isNestedNull(RequestBean arguments, String[] nestedParts) {
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
