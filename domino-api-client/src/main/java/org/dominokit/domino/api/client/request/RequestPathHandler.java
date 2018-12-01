package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.client.ServiceRootMatcher;
import org.dominokit.domino.api.shared.request.RequestBean;
import org.dominokit.domino.api.shared.request.ResponseBean;

import java.util.function.Consumer;

import static java.util.Objects.isNull;

public class RequestPathHandler<R extends RequestBean, S extends ResponseBean> {
    private ServerRequest request;
    private String path;
    private String customRoot;

    public RequestPathHandler(ServerRequest<R, S> request, String path, String customRoot) {
        this.request = request;
        this.path = path;
        this.customRoot = customRoot;
    }

    public void process(Consumer<ServerRequest<R, S>> consumer) {
        if (isNull(request.getUrl())) {
            String serviceRoot = (isNull(customRoot) || customRoot.isEmpty()) ? ServiceRootMatcher.matchedServiceRoot(path) : customRoot;
            request.setUrl(serviceRoot + path);
        }
        consumer.accept(request);
    }
}
