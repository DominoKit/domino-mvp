package org.dominokit.domino.api.shared.history;

import java.util.Map;

public interface NormalizedToken {
    HistoryToken getToken();

    Map<String, String> getPathParameters();

    String getPathParameter(String name);

    boolean containsPathParameter(String name);

    boolean isEmptyPathParameters();

    Map<String, String> getFragmentParameters();

    String getFragmentParameter(String name);

    boolean containsFragmentParameter(String name);

    boolean isEmptyFragmentParameters();
}
