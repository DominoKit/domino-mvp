package com.progressoft.brix.domino.gwt.client.history;

import com.progressoft.brix.domino.api.shared.history.HistoryToken;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

public class StateHistoryToken implements HistoryToken {

    private static final String QUERY_REGEX = "\\?";
    private final String token;
    private List<String> paths = new LinkedList<>();
    private Map<String, String> queryParameters = new HashMap<>();

    public StateHistoryToken(String token) {
        if (isNull(token))
            throw new TokenCannotBeNullException();
        this.token = token;
        this.paths.addAll(asPathsList(token));
        this.queryParameters.putAll(asQueryParameters(token));
    }

    @Override
    public boolean startsWithPath(String path) {
        if (isEmpty(path))
            return false;
        return startsWith(paths(), asPathsList(path));
    }

    private boolean startsWith(List<String> paths, List<String> targets) {
        if (isValidSize(paths, targets))
            return IntStream.range(0, targets.size())
                    .allMatch(i -> targets.get(i).equals(paths.get(i)));
        return false;
    }

    @Override
    public boolean endsWithPath(String path) {
        if (isEmpty(path))
            return false;
        return endsWith(paths(), asPathsList(path));
    }

    private boolean endsWith(List<String> paths, List<String> targets) {
        if (isValidSize(paths, targets))
            return matchEnds(paths, targets);
        return false;
    }

    private boolean matchEnds(List<String> paths, List<String> targets) {
        int offset = paths.size() - targets.size();
        return IntStream.range(0, targets.size())
                .allMatch(i -> targets.get(i).equals(paths.get(i + offset)));
    }

    @Override
    public boolean containsPath(String path) {
        if (isEmpty(path))
            return false;
        return contains(paths(), asPathsList(path));
    }

    private boolean contains(List<String> paths, List<String> targets) {
        return Collections.indexOfSubList(paths, targets) != -1;
    }

    @Override
    public List<String> paths() {
        return paths;
    }

    @Override
    public String path() {
        return String.join("/", paths());
    }

    @Override
    public String query() {
        return queryParameters.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining("&"));
    }

    @Override
    public boolean hasQueryParameter(String name) {
        return queryParameters.containsKey(name);
    }

    @Override
    public Map<String, String> queryParameters() {
        return queryParameters;
    }

    @Override
    public String parameterValue(String name) {
        return queryParameters.get(name);
    }

    @Override
    public HistoryToken appendPath(String path) {
        paths.addAll(asPathsList(path));
        return this;
    }

    @Override
    public HistoryToken appendParameter(String name, String value) {
        this.queryParameters.put(isNull(name) ? "null" : name, isNull(value) ? "null" : value);
        return this;
    }

    @Override
    public HistoryToken replacePath(String path, String replacement) {
        this.paths = asPathsList(path().replace(path, replacement));
        return this;
    }

    @Override
    public HistoryToken replaceParameter(String name, String replacementName, String replacementValue) {
        if (hasQueryParameter(name)) {
            appendParameter(replacementName, replacementValue);
            this.queryParameters.remove(name);
        }
        return this;
    }

    @Override
    public HistoryToken replaceLastPath(String replacement) {
        if (!this.paths.isEmpty()) {
            this.paths.remove(paths.size() - 1);
            this.paths.add(replacement);
        }
        return this;
    }

    @Override
    public HistoryToken replaceLastParam(String name, String replacementName, String replacementValue) {
        return null;
    }

    @Override
    public HistoryToken replaceAllPath(String newPath) {
        this.paths = asPathsList(newPath);
        return this;
    }

    @Override
    public HistoryToken replaceAllQuery(String newQuery) {
        return null;
    }

    @Override
    public HistoryToken clearQuery() {
        return null;
    }

    @Override
    public HistoryToken removeQuery(String name) {
        return null;
    }

    @Override
    public HistoryToken clearPath() {
        this.paths = new LinkedList<>();
        return this;
    }

    @Override
    public HistoryToken removePath(String path) {
        this.paths.removeAll(asPathsList(path));
        return this;
    }

    @Override
    public HistoryToken clear() {
        return null;
    }

    @Override
    public String value() {
        if (token.startsWith("/"))
            return ignoreFirstSlash();
        return token;
    }

    private String ignoreFirstSlash() {
        return token.substring(1);
    }

    private List<String> asPathsList(String pathValue) {
        if (isNull(pathValue))
            return asPathsList("null");
        return Arrays.stream(splittedPaths(pathValue)).filter(p -> !p.isEmpty()).collect(
                Collectors.toCollection(LinkedList::new));
    }

    private String[] splittedPaths(String pathString) {
        return pathString.replace("!", "").split(QUERY_REGEX)[0].split("\\#")[0].split("/");
    }

    private boolean isEmpty(String path) {
        return isNull(path) || path.isEmpty();
    }

    private boolean isValidSize(List<String> paths, List<String> targets) {
        return !targets.isEmpty() && targets.size() < paths.size();
    }

    private Map<String, String> asQueryParameters(String token) {
        if (!token.contains("?"))
            return new HashMap<>();
        return Stream.of(queryPart(token).split("&")).map(part -> part.split("=")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
    }

    private String queryPart(String token) {
        return token.split(QUERY_REGEX)[1].split("\\#")[0];
    }
}
