package com.progressoft.brix.domino.api.client.history;


import org.junit.Before;
import org.junit.Test;

import java.util.Deque;

import static org.junit.Assert.*;

public class PathTokenizerTest {

    private static final String PATH_DELIMITER ="_path=";
    private static final String PARAMETER_DELIMITER="&";
    private UrlPathTokenizer tokenizer;

    @Before
    public void setUp() {
        tokenizer=new UrlPathTokenizer(PATH_DELIMITER, PARAMETER_DELIMITER);
    }

    @Test
    public void givenAUrlPathTokenizer_whenTokenizeAnNullUrl_thenGeneratedTokenizedPathsSetIsEmpty() {
        assertTrue(tokenizer.tokenize(null).isEmpty());
    }

    @Test
    public void givenAUrlPathTokenizer_whenTokenizeAnEmptyUrl_thenGeneratedTokenizedPathsSetIsEmpty() {
        assertTrue(tokenizer.tokenize("").isEmpty());
    }

    @Test
    public void givenAUrlPathTokenizer_whenTokenizeAUrlOfSinglePathAndNoParameters_thenTokenizedPathsSetShouldContainSingleItemOfThatPathToken() {
        assertEquals(1, tokenizer.tokenize("_path=somePath").size());
        assertEquals("somePath", tokenizer.tokenize("_path=somePath").stream().findFirst().get().path());
    }

    @Test
    public void givenAUrlPathTokenizer_whenTokenizeAUrlOfTowPathesAndNoParameters_thenTokenizedPathsSetShouldContainTowItemsOfTowPathes() {
        Deque<TokenizedPath> pathes=tokenizer.tokenize("_path=somePath&_path=someOtherPath");
        assertEquals(2, pathes.size());
        assertEquals("somePath", pathes.pop().path());
        assertEquals("someOtherPath", pathes.pop().path());
    }

    @Test
    public void givenAUrlPathTokenizer_whenTokenizeAUrlOfSinglePathAndOneParameter_thenTokenizedPathsSetShouldContainSingleItemOfThatPathTokenWithSingleParamter() {
        TokenizedPath tokenizedPath=tokenizer.tokenize("_path=somePath&parameter1=value1").pop();
        assertEquals("somePath", tokenizedPath.path());
        assertEquals("value1", tokenizedPath.getParameter("parameter1"));
    }

    @Test
    public void givenAUrlPathTokenizer_whenTokenizeAUrlOfSinglePathAndManyParameters_thenTokenizedPathsSetShouldContainSingleItemOfThatPathTokenWithAllTheParamters() {
        TokenizedPath tokenizedPath=tokenizer.tokenize("_path=somePath&parameter1=value1&parameter2=value2").pop();
        assertEquals("somePath", tokenizedPath.path());
        assertEquals("value1", tokenizedPath.getParameter("parameter1"));
        assertEquals("value2", tokenizedPath.getParameter("parameter2"));
        assertNull(tokenizedPath.getParameter("parameter3"));
    }

    @Test
    public void givenAUrlPathTokenizer_whenTokenizeAUrlOfManyPathsAndManyParameters_thenTokenizedPathsSetShouldContainAllToknizedPathsWithAllTheParamters() {
        Deque<TokenizedPath> tokens=tokenizer.tokenize("_path=somePath&parameter1=value1&parameter2=value2&_path=someOtherPath&parameter3=value3");
        TokenizedPath firstPath=tokens.pop();
        TokenizedPath secondPath=tokens.pop();
        assertEquals("somePath", firstPath.path());
        assertEquals("value1", firstPath.getParameter("parameter1"));
        assertEquals("value2", firstPath.getParameter("parameter2"));
        assertNull(firstPath.getParameter("parameter3"));

        assertEquals("someOtherPath", secondPath.path());
        assertEquals("value3", secondPath.getParameter("parameter3"));
        assertNull(secondPath.getParameter("parameter1"));
    }

}
