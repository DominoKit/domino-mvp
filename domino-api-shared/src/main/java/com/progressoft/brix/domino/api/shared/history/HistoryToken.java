package com.progressoft.brix.domino.api.shared.history;

import java.util.List;
import java.util.Map;

public interface HistoryToken {

    boolean startsWithPath(String path);

    boolean endsWithPath(String path);

    boolean containsPath(String path);

    List<String> paths();

    String path();

    HistoryToken appendPath(String path);

    HistoryToken replacePath(String path, String replacement);

    HistoryToken replaceLastPath(String replacement);

    HistoryToken replaceAllPaths(String newPath);

    HistoryToken clearPaths();

    HistoryToken removePath(String path);

    Map<String, String> queryParameters();

    boolean hasQueryParameter(String name);

    String parameterValue(String name);

    HistoryToken appendParameter(String name, String value);

    HistoryToken replaceParameter(String name, String replacementName, String replacementValue);

    HistoryToken removeParameter(String name);

    HistoryToken replaceQuery(String newQuery);

    HistoryToken clearQuery();

    String query();

    HistoryToken setFragment(String fragment);

    HistoryToken removeFragment();

    String fragment();

    HistoryToken clear();

    String value();

    class TokenCannotBeNullException extends RuntimeException {
    }
}
