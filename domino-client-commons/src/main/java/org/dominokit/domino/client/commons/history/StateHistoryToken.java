package org.dominokit.domino.client.commons.history;

import org.dominokit.domino.api.shared.history.HistoryToken;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

public class StateHistoryToken implements HistoryToken {

    private static final String QUERY_REGEX = "\\?";
    private static final String FRAGMENT_REGEX = "\\#";
    private static final int NAME_INDEX = 0;
    private static final int VALUE_INDEX = 1;
    private List<String> paths = new LinkedList<>();
    private Map<String, String> queryParameters = new HashMap<>();
    private List<String> fragments = new LinkedList<>();

    public StateHistoryToken(String token) {
        if (isNull(token))
            throw new TokenCannotBeNullException();
        this.paths.addAll(asPathsList(token));
        this.queryParameters.putAll(asQueryParameters(token));
        this.fragments.addAll(parseFragments(token));
    }

    private String getPathToRoot(String token, String root) {
        if (token.isEmpty())
            return root;
        return token.endsWith("/") ? token + root : token + "/" + root;
    }

    private List<String> parseFragments(String token) {
        if (token.contains("#"))
            return asPathsList(token.split(FRAGMENT_REGEX)[1]);
        return new LinkedList<>();
    }

    @Override
    public boolean startsWithPath(String path) {
        if (isEmpty(path))
            return false;
        return startsWith(paths(), asPathsList(path));
    }

    @Override
    public boolean fragmentsStartsWith(String fragment) {
        if (isEmpty(fragment))
            return false;
        return startsWith(fragments(), asPathsList(fragment));
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

    @Override
    public boolean endsWithFragment(String fragment) {
        if (isEmpty(fragment))
            return false;
        return endsWith(fragments(), asPathsList(fragment));
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

    @Override
    public boolean containsFragment(String fragment) {
        if (isEmpty(fragment))
            return false;
        return contains(fragments(), asPathsList(fragment));
    }

    private boolean contains(List<String> paths, List<String> targets) {
        return IntStream.rangeClosed(0, paths.size() - targets.size())
                .anyMatch(i -> isOrderedEquals(paths.subList(i, i + targets.size()), targets));
    }

    private boolean isOrderedEquals(List<String> subList, List<String> targets) {
        return IntStream.of(0, subList.size() - 1).allMatch(i -> subList.get(i).equals(targets.get(i)));
    }

    @Override
    public List<String> paths() {
        return paths;
    }

    @Override
    public List<String> fragments() {
        return fragments;
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
    public HistoryToken appendFragment(String fragment) {
        fragments.addAll(asPathsList(fragment));
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
    public HistoryToken replaceFragment(String fragment, String replacement) {
        this.paths = asPathsList(fragment().replace(fragment, replacement));
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
    public HistoryToken replaceLastFragment(String replacement) {
        if (!this.fragments.isEmpty()) {
            this.fragments.remove(fragments.size() - 1);
            this.fragments.add(replacement);
        }
        return this;
    }

    @Override
    public HistoryToken replaceAllPaths(String newPath) {
        this.paths = asPathsList(newPath);
        return this;
    }

    @Override
    public HistoryToken replaceAllFragments(String newFragment) {
        this.fragments = asPathsList(newFragment);
        return this;
    }

    @Override
    public HistoryToken replaceQuery(String newQuery) {
        this.queryParameters = parsedParameters(newQuery);
        return this;
    }

    @Override
    public HistoryToken clearQuery() {
        this.queryParameters.clear();
        return this;
    }

    @Override
    public HistoryToken removeParameter(String name) {
        this.queryParameters.remove(name);
        return this;
    }

    @Override
    public HistoryToken clearPaths() {
        this.paths.clear();
        return this;
    }

    @Override
    public HistoryToken clearFragments() {
        this.fragments.clear();
        return this;
    }

    @Override
    public HistoryToken removePath(String path) {
        this.paths.removeAll(asPathsList(path));
        return this;
    }

    @Override
    public HistoryToken removeFragment(String fragment) {
        this.fragments.removeAll(asPathsList(fragment));
        return this;
    }

    @Override
    public HistoryToken clear() {
        clearPaths();
        clearQuery();
        clearFragments();
        return this;
    }

    @Override
    public String fragment() {
        return String.join("/", fragments());
    }

    @Override
    public String value() {
        return path() + appendQuery(query()) + appendFragment();
    }


    private String appendFragment() {
        return isEmpty(fragment()) ? "" : "#" + fragment();
    }

    private String appendQuery(String query) {
        return isEmpty(query) ? "" : "?" + query;
    }

    private List<String> asPathsList(String pathValue) {
        if (isNull(pathValue))
            return asPathsList("null");
        return Arrays.stream(splittedPaths(pathValue)).filter(p -> !p.isEmpty()).collect(
                Collectors.toCollection(LinkedList::new));
    }

    private String[] splittedPaths(String pathString) {
        return parsePathPart(pathString).split("/");
    }

    private String parsePathPart(String pathString) {
        return pathString.replace("!", "").split(QUERY_REGEX)[0].split(FRAGMENT_REGEX)[0];
    }

    private boolean isEmpty(String path) {
        return isNull(path) || path.isEmpty();
    }

    private boolean isValidSize(List<String> paths, List<String> targets) {
        return !targets.isEmpty() && targets.size() <= paths.size();
    }

    private Map<String, String> asQueryParameters(String token) {
        if (!token.contains("?"))
            return new HashMap<>();
        return parsedParameters(queryPart(token));
    }

    private Map<String, String> parsedParameters(String queryString) {
        return Stream.of(queryString.split("&")).map(part -> part.split("=")).collect(Collectors.toMap(e -> e[NAME_INDEX], e -> e[VALUE_INDEX]));
    }

    private String queryPart(String token) {
        return token.split(QUERY_REGEX)[1].split(FRAGMENT_REGEX)[0];
    }
}
