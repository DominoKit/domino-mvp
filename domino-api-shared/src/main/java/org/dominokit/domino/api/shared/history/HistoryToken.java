package org.dominokit.domino.api.shared.history;

import java.util.List;
import java.util.Map;

public interface HistoryToken {

    boolean startsWithPath(String path);

    boolean fragmentsStartsWith(String fragment);

    boolean endsWithPath(String path);

    boolean endsWithFragment(String fragment);

    boolean containsPath(String path);

    boolean containsFragment(String fragment);

    List<String> paths();

    List<String> fragments();

    String path();

    HistoryToken appendPath(String path);

    HistoryToken replacePath(String path, String replacement);

    HistoryToken replaceLastPath(String replacement);

    HistoryToken replaceLastFragment(String replacement);

    HistoryToken replaceAllPaths(String newPath);

    HistoryToken clearPaths();

    HistoryToken clearFragments();

    HistoryToken removePath(String path);

    Map<String, String> queryParameters();

    boolean hasQueryParameter(String name);

    String parameterValue(String name);

    HistoryToken appendFragment(String fragment);

    HistoryToken appendParameter(String name, String value);

    HistoryToken replaceFragment(String fragment, String replacement);

    HistoryToken replaceParameter(String name, String replacementName, String replacementValue);

    HistoryToken removeParameter(String name);

    HistoryToken replaceAllFragments(String newFragment);

    HistoryToken replaceQuery(String newQuery);

    HistoryToken clearQuery();

    String query();

    String fragment();

    HistoryToken removeFragment(String fragment);

    HistoryToken clear();

    String value();

    class TokenCannotBeNullException extends RuntimeException {
    }
}
