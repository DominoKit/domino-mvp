package com.progressoft.brix.domino.api.client.history;

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
    String appendPath(String path);
    String appendParameter(String name, String value);
    String replacePath(String path, String replacement);
    String replaceParam(String name, String replacementName, String replacementValue);
    String replaceLastPath(String path, String replacement);
    String replaceLastParam(String name, String replacementName, String replacementValue);
    String replaceAllPath(String newPath);
    String replaceAllQuery(String newQuery);
    String removeQuery();
    String removeQuery(String name);
    String removePath();
    String removePath(String path);
    String asToken();
}
