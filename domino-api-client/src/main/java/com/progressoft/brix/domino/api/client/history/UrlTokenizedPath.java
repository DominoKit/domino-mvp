package com.progressoft.brix.domino.api.client.history;

import java.util.HashMap;
import java.util.Map;

public class UrlTokenizedPath implements TokenizedPath{

    private final String path;
    private final Map<String, String> parameters=new HashMap<>();

    public UrlTokenizedPath(String path) {
        this.path=path;
    }

    public UrlTokenizedPath(String path, Map<String, String> parameters) {
        this.path=path;
        this.parameters.putAll(parameters);
    }

    public void addParameter(String name, String value) {
        parameters.put(name, value);
    }

    @Override
    public String path() {
        return this.path;
    }

    @Override
    public String getParameter(String name) {
        return parameters.get(name);
    }

    @Override
    public boolean containsParameter(String name) {
        return parameters.containsKey(name);
    }

    @Override
    public String toString() {
        return "UrlTokenizedPath{" +
                "path='" + path + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
