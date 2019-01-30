package org.dominokit.domino.api.server.context;

import org.dominokit.domino.api.server.fileuploads.FileUpload;
import org.dominokit.domino.api.server.request.MultiMap;
import org.dominokit.domino.api.server.request.RequestContext;
import org.dominokit.domino.api.server.response.ResponseContext;
import org.dominokit.domino.api.server.response.ResponseEndHandler;
import org.dominokit.domino.api.shared.request.RequestBean;
import org.dominokit.domino.api.shared.request.ResponseBean;

import java.util.List;
import java.util.Set;

public class DefaultExecutionContext<T extends RequestBean, S extends ResponseBean> implements ExecutionContext<T, S> {

    private final RequestContext<T> requestContext;
    private final ResponseContext<S> responseContext;
    private Set<FileUpload> fileUploads;

    public DefaultExecutionContext(RequestContext<T> requestContext, ResponseContext<S> responseContext, Set<FileUpload> fileUploads) {
        this.requestContext = requestContext;
        this.responseContext = responseContext;
        this.fileUploads = fileUploads;
    }

    @Override
    public T getRequestBean() {
        return requestContext.getRequestBean();
    }

    @Override
    public MultiMap<String, String> headers() {
        return requestContext.headers();
    }

    @Override
    public MultiMap<String, String> parameters() {
        return requestContext.parameters();
    }

    @Override
    public String getRequestPath() {
        return requestContext.getRequestPath();
    }

    @Override
    public ResponseContext<S> putHeader(String name, String value) {
        return responseContext.putHeader(name, value);
    }

    @Override
    public ResponseContext<S> putHeader(String name, Iterable<String> values) {
        return responseContext.putHeader(name, values);
    }

    @Override
    public ResponseContext<S> statusCode(int statusCode) {
        return responseContext.statusCode(statusCode);
    }

    @Override
    public void end() {
        responseContext.end();
    }

    @Override
    public void end(S body) {
        responseContext.end(body);
    }

    @Override
    public void end(String body) {
        responseContext.end(body);
    }

    @Override
    public void end(S[] bodyArray) {
        responseContext.end(bodyArray);
    }

    @Override
    public void end(List<S> bodyList) {
        responseContext.end(bodyList);
    }

    @Override
    public void endHandler(ResponseEndHandler endHandler) {
        responseContext.endHandler(endHandler);
    }

    @Override
    public Set<FileUpload> fileUploads() {
        return fileUploads;
    }
}
