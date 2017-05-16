package com.progressoft.brix.domino.api.client.history;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

public class UrlPathTokenizer {

    private final String pathDelimiter;
    private final String parameterDelimiter;


    public UrlPathTokenizer(String pathDelimiter, String parameterDelimiter) {
        this.pathDelimiter = pathDelimiter;
        this.parameterDelimiter = parameterDelimiter;
    }

    public Deque<TokenizedPath> tokenize(String url) {
        if (Objects.isNull(url) || "".equals(url))
            return new LinkedList<>();
        return tokenizedUrl(url);
    }

    private Deque<TokenizedPath> tokenizedUrl(String url) {
        Deque<TokenizedPath> tokens = new LinkedList<>();
        Arrays.stream(url.split(parameterDelimiter)).forEach(s -> parsPart(tokens, s));
        return tokens;
    }

    private void parsPart(Deque<TokenizedPath> tokens, String token) {
        if (token.startsWith(pathDelimiter))
            tokens.addLast(new UrlTokenizedPath(getPath(token)));
        else
            ((UrlTokenizedPath)tokens.getLast()).addParameter(getParameterName(token), getParameterValue(token));
    }

    private String getParameterName(String token) {
        return token.split("=")[0];
    }

    private String getParameterValue(String token) {
        return token.split("=").length > 1 ? token.split("=")[1] : "";
    }

    private String getPath(String token) {
        return token.split("=")[1];
    }

}
