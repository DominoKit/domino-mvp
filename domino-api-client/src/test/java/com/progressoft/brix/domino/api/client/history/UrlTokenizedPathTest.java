package com.progressoft.brix.domino.api.client.history;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class UrlTokenizedPathTest {

    public static final String SOME_PATH = "somePath";
    private TokenizedPath tokenizedPath;

    @Before
    public void setUp() {
        tokenizedPath =new UrlTokenizedPath(SOME_PATH);
    }

    @Test
    public void createdUrlTokenizedPath_shouldHaveAPath() {
        assertEquals(SOME_PATH, tokenizedPath.path());
    }

    @Test
    public void createdUrlTokenizedPath_shouldHaveReturnItsPath() {
        assertEquals(SOME_PATH, tokenizedPath.path());
    }

    @Test
    public void createdUrlTokenizedPathWithoutParameters_shouldNotContainAnyParameters() {
        assertFalse(tokenizedPath.containsParameter("someParameter"));
        assertNull(tokenizedPath.getParameter("someParameter"));
    }

    @Test
    public void createdUrlTokenizedPathWithParameters_shouldProvideItsParameters() {
        Map<String, String> parametersMap= new HashMap<>();
        parametersMap.put("parameter1","value1");
        parametersMap.put("parameter2", "value2");
        TokenizedPath tokenizedPath=new UrlTokenizedPath(SOME_PATH, parametersMap);
        assertTrue(tokenizedPath.containsParameter("parameter1"));
        assertEquals("value1", tokenizedPath.getParameter("parameter1"));
        assertEquals("value2", tokenizedPath.getParameter("parameter2"));
        assertFalse(tokenizedPath.containsParameter("parameter3"));
        assertNull(tokenizedPath.getParameter("parameter3"));
    }
}
