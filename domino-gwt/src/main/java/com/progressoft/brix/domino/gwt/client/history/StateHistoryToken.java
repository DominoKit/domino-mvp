package com.progressoft.brix.domino.gwt.client.history;

import com.progressoft.brix.domino.api.shared.history.HistoryToken;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;

public class StateHistoryToken implements HistoryToken {

    private static final String QUERY_REGEX = "\\?";
    private final String token;
    private List<String> paths = new LinkedList<>();

    public StateHistoryToken(String token) {
        if (isNull(token))
            throw new TokenCannotBeNullException();
        this.token = token;
        this.paths.addAll(asPathsList(token));
    }

    private List<String> asPathsList(String pathValue) {
        if (isEmptyPathString(pathValue))
            return new LinkedList<>();
        return Arrays.stream(splittedPaths(pathValue)).filter(p -> !isEmptyPathString(p)).collect(
                Collectors.toList());
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

    private boolean isEmpty(String path) {
        return isNull(path) || path.isEmpty();
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

    private boolean isValidSize(List<String> paths, List<String> targets) {
        return !targets.isEmpty() && targets.size() < paths.size();
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

    private String[] splittedPaths(String pathString) {
        return pathString.replace("!", "").split(QUERY_REGEX)[0].split("\\#")[0].split("/");
    }

    private boolean isEmptyPathString(String pathValue) {
        return isNull(pathValue) || pathValue.isEmpty();
    }

    @Override
    public String path() {
        return String.join("/", paths());
    }

    @Override
    public String query() {
        if (token.contains("?"))
            return extractQueryString(this.token.split(QUERY_REGEX)[1]);
        return "";
    }

    private String extractQueryString(String queryAndFragment) {
        if (queryAndFragment.contains("#"))
            return queryAndFragment.substring(0, queryAndFragment.indexOf('#'));
        return queryAndFragment;
    }

    @Override
    public boolean hasQueryParameter(String name) {
        return false;
    }

    @Override
    public Map<String, String> queryParameters() {
        return null;
    }

    @Override
    public String queryParam(String name) {
        return null;
    }

    @Override
    public HistoryToken appendPath(String path) {
        if (isEmpty(path))
            paths.add(path);
        else
            paths.addAll(asPathsList(path));
        return this;
    }

    @Override
    public HistoryToken appendParameter(String name, String value) {
        return null;
    }

    @Override
    public HistoryToken replacePath(String path, String replacement) {
        this.paths = asPathsList(path().replace(path, replacement));
        return this;
    }

    @Override
    public HistoryToken replaceParam(String name, String replacementName, String replacementValue) {
        return null;
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
        return null;
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
        return null;
    }

    @Override
    public HistoryToken removePath(String path) {
        return null;
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
}
