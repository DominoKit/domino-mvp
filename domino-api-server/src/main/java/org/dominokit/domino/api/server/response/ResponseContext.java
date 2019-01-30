package org.dominokit.domino.api.server.response;

import org.dominokit.domino.api.shared.request.ResponseBean;

import java.util.List;

public interface ResponseContext<T extends ResponseBean> {

    ResponseContext<T> putHeader(String name, String value);

    ResponseContext<T> putHeader(String name, Iterable<String> values);

    ResponseContext<T> statusCode(int statusCode);

    void end();

    void end(T body);

    void end(T[] bodyArray);

    void end(List<T> bodyList);

    void end(String body);

    void endHandler(ResponseEndHandler endHandler);
}
