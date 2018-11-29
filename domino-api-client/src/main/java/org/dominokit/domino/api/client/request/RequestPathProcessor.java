package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.client.ServiceRootMatcher;

import java.util.function.Consumer;

import static java.util.Objects.isNull;

public class RequestPathProcessor {
    private ServerRequest request;
    private String path;

    public RequestPathProcessor(ServerRequest request, String path) {
        this.request = request;
        this.path = path;
    }

    public void process(Consumer<ServerRequest> consumer){
        if(isNull(request.getUrl())){
            request.setUrl(ServiceRootMatcher.matchedServiceRoot(path)+path);
        }
        consumer.accept(request);
    }
}
