package com.progressoft.brix.domino.api.shared.history;

import java.util.List;
import java.util.Map;

public interface HistoryToken {

    boolean startsWithPath(String path);
    boolean endsWithPath(String path);
    boolean containsPath(String path);
    List<String> paths();
    String path();
    String query();
    boolean hasQueryParameter(String name);
    Map<String, String> queryParameters();
    String queryParam(String name);
    HistoryToken appendPath(String path);
    HistoryToken appendParameter(String name, String value);
    HistoryToken replacePath(String path, String replacement);
    HistoryToken replaceParam(String name, String replacementName, String replacementValue);
    HistoryToken replaceLastPath(String replacement);
    HistoryToken replaceLastParam(String name, String replacementName, String replacementValue);
    HistoryToken replaceAllPath(String newPath);
    HistoryToken replaceAllQuery(String newQuery);
    HistoryToken clearQuery();
    HistoryToken removeQuery(String name);
    HistoryToken clearPath();
    HistoryToken removePath(String path);
    HistoryToken clear();
    String value();

    class TokenCannotBeNullException extends RuntimeException{
    }

    class InvalidTokenException extends RuntimeException{
    }
}
