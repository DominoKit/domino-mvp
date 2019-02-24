package org.dominokit.domino.api.shared.history;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class StateHistoryToken implements HistoryToken {

    private static final String QUERY_REGEX = "\\?";
    private static final String FRAGMENT_REGEX = "\\#";
    private List<String> paths = new LinkedList<>();
    private List<Parameter> queryParameters = new LinkedList<>();
    private List<String> fragments = new LinkedList<>();

    public StateHistoryToken(String token) {
        if (isNull(token))
            throw new TokenCannotBeNullException();
        this.paths.addAll(asPathsList(token));
        this.queryParameters.addAll(asQueryParameters(token));
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
        return queryParameters.stream().map(parameter -> parameter.key + "=" + parameter.value).collect(Collectors.joining("&"));
    }

    @Override
    public boolean hasQueryParameter(String name) {
        Optional<Parameter> param = queryParameters.stream()
                .filter(parameter -> parameter.key.equals(name))
                .findFirst();

        if (param.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Map<String, String> queryParameters() {
        Map<String, String> parameters = new HashMap<>();
        queryParameters.forEach(parameter -> parameters.put(parameter.key, parameter.value));
        return parameters;
    }

    @Override
    public String getQueryParameter(String name) {
        Optional<Parameter> param = queryParameters.stream()
                .filter(parameter -> parameter.key.equals(name))
                .findFirst();

        if (param.isPresent()) {
            return param.get().value;
        } else {
            return null;
        }
    }

    private Parameter getParameter(String name) {
        Optional<Parameter> param = queryParameters.stream()
                .filter(parameter -> parameter.key.equals(name))
                .findFirst();

        if (param.isPresent()) {
            return param.get();
        } else {
            return null;
        }
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
        if (nonNull(name) && !name.trim().isEmpty()) {
            this.queryParameters.add(new Parameter(name, value));
        }
        return this;
    }

    @Override
    public HistoryToken replacePath(String path, String replacement) {
        List<String> paths = asPathsList(path());
        if(paths.contains(path)) {
            int i = paths.lastIndexOf(path);
            paths.add(i, replacement);
            paths.remove(i + 1);
            this.paths = paths;
        }
        return this;
    }

    @Override
    public HistoryToken replacePaths(String path, String replacement) {
        this.paths = asPathsList(path().replace(path, replacement));
        return this;
    }

    @Override
    public HistoryToken replaceFragment(String fragment, String replacement) {

        List<String> fragments = asPathsList(fragment());
        if(fragments.contains(fragment)) {
            int i = fragments.lastIndexOf(fragment);
            fragments.add(i, replacement);
            fragments.remove(i + 1);
            this.fragments = fragments;
        }
        return this;
    }

    @Override
    public HistoryToken replaceFragments(String fragment, String replacement) {
        this.fragments = asPathsList(fragment().replace(fragment, replacement));
        return this;
    }

    @Override
    public HistoryToken replaceParameter(String name, String replacementName, String replacementValue) {
        if (hasQueryParameter(name)) {
            Parameter param = getParameter(name);
            this.queryParameters.add(this.queryParameters.indexOf(param), new Parameter(name, replacementValue));
            this.queryParameters.remove(param);
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
    public HistoryToken removeLastPath() {
        if (!this.paths.isEmpty()) {
            this.paths.remove(paths.size() - 1);
        }
        return this;
    }

    @Override
    public HistoryToken removeLastFragment() {
        if (!this.fragments.isEmpty()) {
            this.fragments.remove(fragments.size() - 1);
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
    public boolean isEmpty() {
        return paths.isEmpty() && queryParameters.isEmpty() && fragments.isEmpty();
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

    private List<Parameter> asQueryParameters(String token) {

        String queryString = queryPart(token);
        if (isNull(queryString) || queryString.trim().isEmpty()) {
            return new LinkedList<>();
        }
        return parsedParameters(queryString);
    }

    private List<Parameter> parsedParameters(String queryString) {
        return Stream.of(queryString.split("&"))
                .map(part -> part.split("="))
                .map(keyValue -> new Parameter(keyValue[0], keyValue[1]))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private String queryPart(String token) {
        if (token.contains("?")) {
            return token.split(QUERY_REGEX)[1].split(FRAGMENT_REGEX)[0];
        } else {
            int firstPathSeparator = token.indexOf("/");
            if(token.contains("&") || token.contains("=") || token.contains("#")) {
                if (firstPathSeparator < 0 || token.indexOf("&") < firstPathSeparator || token.indexOf("=") < firstPathSeparator || token.indexOf("#") < firstPathSeparator) {
                    return token.split(FRAGMENT_REGEX)[0];
                } else {
                    return "";
                }
            }
        }
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StateHistoryToken)) return false;
        StateHistoryToken that = (StateHistoryToken) o;

        return paths.equals(that.paths) && fragments.equals(that.fragments) && queryParameters.equals(that.queryParameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paths, queryParameters, fragments);
    }

    private static class Parameter {
        private String key;
        private String value;

        public Parameter(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Parameter)) return false;
            Parameter parameter = (Parameter) o;
            return Objects.equals(key, parameter.key) &&
                    Objects.equals(value, parameter.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }
}
