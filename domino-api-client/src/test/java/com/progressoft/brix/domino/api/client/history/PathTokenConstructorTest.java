package com.progressoft.brix.domino.api.client.history;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PathTokenConstructorTest {

    public static final String SECOND_TOKEN = "_path=somePath2&parameter=value&parameter2=value2";
    public static final String FIRST_TOKEN = "_path=somePath&parameter=value&parameter2=value2";
    private TokenConstruct constructor;
    private Token firstTestToken;
    private Token secondTestToken;

    @Before
    public void setUp() {
        constructor = new PathTokenConstructor();
        firstTestToken=PathToken.make("somePath").appendParameter("parameter", "value")
                .appendParameter("parameter2", "value2");
        secondTestToken=PathToken.make("somePath2").appendParameter("parameter", "value")
                .appendParameter("parameter2", "value2");
    }

    @Test
    public void givenNewlyCreatedConstructor_thenGeneratedUrlShouldBeEmpty() {
        assertEquals("", constructor.toUrl());
    }

    @Test
    public void givenHistoryConstructor_appendingNoParametersHistoryToken_then_generatedUrlShouldContainAppendedTokenPath()
            {
        assertEquals("_path=somePath", constructor.append(PathToken.make("somePath")).toUrl());
    }

    @Test
    public void givenHistoryConstructor_appendingTowNoParametersHistoryToken_then_generatedUrlShouldContainBothAppendedTokenPathsSeparatedbyAndSymbol()
            {
        assertEquals("_path=somePath&_path=anotherPath",
                constructor.append(PathToken.make("somePath")).append(PathToken.make("anotherPath")).toUrl());
    }

    @Test
    public void givenHistoryConstructor_appendingHistoryTokenWithParameters_then_generatedUrlShouldContainHistoryTokenString()
            {
        assertEquals("_path=somePath&parameter=value",
                constructor.append(PathToken.make("somePath").appendParameter("parameter", "value")).toUrl());
    }

    @Test
    public void givenHistoryConstructor_appendingHistoryTokenWithManyParameters_then_generatedUrlShouldContainHistoryTokenString()
            {
        assertEquals(FIRST_TOKEN, constructor
                .append(firstTestToken).toUrl());
    }

    @Test
    public void givenHistoryConstructor_appendingHistoryTokensWithManyParameters_then_generatedUrlShouldContainAllHistoryTokensString()
            {
        assertEquals(FIRST_TOKEN + "&" + SECOND_TOKEN, constructor
                .append(firstTestToken)
                .append(secondTestToken).toUrl());
    }

    @Test
    public void givenHistoryConstructor_replacingAllTokensWithAnotherToken_then_generatedUrlShouldContainOnlyTheReplacerTokenStringOnly()
            {
        assertEquals("_path=somePath3", constructor
                .append(firstTestToken)
                .append(secondTestToken).replaceAll(PathToken.make("somePath3")).toUrl());

        assertEquals("_path=somePath3&parameter=value", constructor
                .append(firstTestToken)
                .append(secondTestToken).replaceAll(PathToken.make("somePath3").appendParameter("parameter", "value")).toUrl());
    }

    @Test
    public void givenHistoryConstructor_replacingLastTokenWithAnotherToken_then_generatedUrlShouldContainTheOldTokensExceptTheLastOneConcatenatedWithTheNewOne()
            {
        assertEquals(FIRST_TOKEN + "&_path=somePath3", constructor
                .append(firstTestToken)
                .append(secondTestToken).replaceLast(PathToken.make("somePath3")).toUrl());

        constructor.clear();

        assertEquals(FIRST_TOKEN + "&_path=somePath3&parameter=value", constructor
                .append(firstTestToken)
                .append(secondTestToken).replaceLast(PathToken.make("somePath3").appendParameter("parameter", "value")).toUrl());
    }

    @Test
    public void givenHistoryConstructor_clearingTheConstructor_then_generatedUrlShouldBeEmpty()
            {
        assertEquals("", constructor
                .append(firstTestToken)
                .append(secondTestToken).clear().toUrl());
    }

    @Test
    public void givenHistoryConstructor_clearingTheToken_then_generatedUrlShouldAllTokenExceptTheLastOne()
            {
        assertEquals(FIRST_TOKEN, constructor
                .append(firstTestToken)
                .append(secondTestToken).clearLast().toUrl());
    }

    @Test
    public void givenHistoryConstructor_appendingTokenManyTimesWithAppendOnce_generatedUrlShouldContainThatTokenOnlyOnce()
            {
        assertEquals(FIRST_TOKEN, constructor
                .appendOnce(firstTestToken).appendOnce(firstTestToken).toUrl());
    }

    @Test
    public void givenHistoryConstructor_replacingAllSimilarPathTokens_generatedUrlShouldContainThatTokenOnlyOnce()
            {

        assertEquals(FIRST_TOKEN, constructor
                .append(firstTestToken).append(firstTestToken).replaceAllOfSamePath(firstTestToken).toUrl());

        constructor.clear();

        assertEquals(SECOND_TOKEN + "&" + FIRST_TOKEN, constructor
                .append(firstTestToken).append(secondTestToken).append(firstTestToken).replaceAllOfSamePath(firstTestToken).toUrl());
    }
}
