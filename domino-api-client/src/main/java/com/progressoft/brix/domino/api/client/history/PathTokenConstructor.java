package com.progressoft.brix.domino.api.client.history;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class PathTokenConstructor implements TokenConstruct {


    private static final String EMPTY_TOKEN ="";
    private Deque<Token> tokens=new LinkedList<>();

    @Override
    public String toUrl() {
        return asUrl(tokensIterator());
    }

    private String asUrl(Iterator<Token> tokenIterator){
        if(tokenIterator.hasNext())
            return concatTokensString(tokenIterator.next().asTokenString(), tokenIterator);
        return EMPTY_TOKEN;
    }

    private Iterator<Token> tokensIterator(){
        return tokens.iterator();
    }

    private String concatTokensString(String token, Iterator<Token> tokenIterator) {//NOSONAR
        if(tokenIterator.hasNext())
            return concatTokensString(tokenIterator.next().asTokenString(), tokenIterator)+"&"+ token;
        return token;
    }

    @Override
    public TokenConstruct append(Token token) {
        tokens.push(token);
        return this;
    }

    @Override
    public TokenConstruct appendOnce(Token token) {
        if(!tokens.contains(token))
            tokens.push(token);
        return this;
    }

    @Override
    public TokenConstruct replaceAll(Token token) {
        tokens.clear();
        tokens.push(token);
        return this;
    }

    @Override
    public TokenConstruct replaceLast(Token token) {
        tokens.pop();
        tokens.push(token);
        return this;
    }

    @Override
    public TokenConstruct replaceAllOfSamePath(Token token) {
        tokens.removeAll(tokens.stream().filter(t->t.hasSamePath(token)).collect(Collectors.toSet()));
        append(token);
        return this;
    }

    @Override
    public TokenConstruct clear() {
        tokens.clear();
        return this;
    }

    @Override
    public TokenConstruct clearLast() {
        tokens.pop();
        return this;
    }
}
