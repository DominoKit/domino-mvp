package org.dominokit.domino.api.server.context;

import org.dominokit.domino.api.server.request.RequestContext;
import org.dominokit.domino.api.server.response.ResponseContext;
import org.dominokit.domino.api.shared.request.RequestBean;
import org.dominokit.domino.api.shared.request.ResponseBean;

public interface ExecutionContext<T extends RequestBean, S extends ResponseBean> extends RequestContext<T>, ResponseContext<S>{
}
