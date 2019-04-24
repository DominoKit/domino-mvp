package org.dominokit.domino.api.shared.request;

import org.dominokit.domino.api.shared.history.HistoryToken;

@FunctionalInterface
public interface RequestParametersReplacer<R> {
    String replace(HistoryToken token, R request);
}
