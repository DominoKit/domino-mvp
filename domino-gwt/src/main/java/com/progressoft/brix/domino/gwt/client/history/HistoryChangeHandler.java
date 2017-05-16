package com.progressoft.brix.domino.gwt.client.history;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.progressoft.brix.domino.api.client.history.PathToRequestMappersRepository;
import com.progressoft.brix.domino.api.client.history.TokenConstruct;
import com.progressoft.brix.domino.api.client.history.TokenizedPath;
import com.progressoft.brix.domino.api.client.history.UrlPathTokenizer;
import com.progressoft.brix.domino.api.client.request.Request;

import java.util.Deque;
import java.util.Objects;

public class HistoryChangeHandler implements ValueChangeHandler<String> {

    public static final String PATH = "_path";
    public static final String PARAMETER_SEPARATOR = "&";

    private final PathToRequestMappersRepository mappersRepository;
    private final TokenConstruct tokenConstruct;

    public HistoryChangeHandler(PathToRequestMappersRepository repository, TokenConstruct tokenConstruct) {
        this.mappersRepository = repository;
        this.tokenConstruct = tokenConstruct;
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        Deque<TokenizedPath> tokens = new UrlPathTokenizer(PATH, PARAMETER_SEPARATOR).tokenize(event.getValue());

        if (!tokens.isEmpty()) {
            Request rootRequest = mapTokenToRequest(tokens.pop());
            chainRequest(rootRequest, tokens);
            tokenConstruct.clear();
            rootRequest.send();
        }

    }

    private void chainRequest(Request rootRequest, Deque<TokenizedPath> tokens) {
        if (Objects.nonNull(tokens.peek()))
            buildChain(rootRequest, tokens, mapTokenToRequest(tokens.pop()));
    }

    private void buildChain(Request rootRequest, Deque<TokenizedPath> tokens, Request chain) {
        rootRequest.chainRequest(chain);
        chainRequest(chain, tokens);
    }

    private Request mapTokenToRequest(TokenizedPath rootPath) {
        return mappersRepository.getMapper(rootPath.path()).buildRequest(rootPath);
    }
}
