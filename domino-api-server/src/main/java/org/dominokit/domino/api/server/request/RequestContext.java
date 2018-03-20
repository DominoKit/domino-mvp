package org.dominokit.domino.api.server.request;

import org.dominokit.domino.api.shared.request.RequestBean;

public interface RequestContext<T extends RequestBean> {
    T getRequestBean();

    MultiMap<String, String> headers();

    MultiMap<String, String> parameters();

    String getRequestPath();
}
