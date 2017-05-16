package com.progressoft.brix.domino.api.client.history;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HistoryTokenTest {

    private Token token;

    @Before
    public void setUp() {
        this.token= PathToken.make("somePath");
    }

    @Test(expected = PathToken.InvalidTokenPathException.class)
    public void creatingHistoryTokenWithNullPath_shouldThrowExceptionT() {
        PathToken.make(null);
    }

    @Test(expected = PathToken.InvalidTokenPathException.class)
    public void creatingHistoryTokenWithEmptyPath_shouldThrowExceptionT() {
        PathToken.make("");
    }

    @Test(expected = PathToken.InvalidTokenPathException.class)
    public void creatingHistoryTokenWithSpacesOnlyPath_shouldThrowExceptionT() {
        PathToken.make("   ");
    }

    @Test(expected = PathToken.InvalidTokenPathException.class)
    public void creatingHistoryTokenWithPathContainingParametersSeparator_shouldThrowException() {
        PathToken.make("pathPart"+ PathToken.PARAMETERS_SEPARATOR+"someOtherpart");
    }

    @Test(expected = PathToken.InvalidTokenPathException.class)
    public void creatingHistoryTokenWithPathContainingParameterValueSeparator_shouldThrowException() {
        PathToken.make("pathPart"+ PathToken.PARAMETER_VALUE_SEPARATOR+"someOtherpart");
    }

    @Test
    public void creatingHistoryTokenForSpecificPath_shouldMapToPathWithNameStringValue() {
       assertEquals("_path=somePath",token.asTokenString());
    }

    @Test(expected = PathToken.InvalidParameterNameOrValueException.class)
    public void havingHistoryToken_appendingParameterWithNullName_shouldThrowException() {
        token.appendParameter(null, "value1");
    }

    @Test(expected = PathToken.InvalidParameterNameOrValueException.class)
    public void havingHistoryToken_appendingParameterWithEmptyName_shouldThrowException() {
        token.appendParameter("", "value1");
    }

    @Test(expected = PathToken.InvalidParameterNameOrValueException.class)
    public void havingHistoryToken_appendingParameterWithOnlySpacesName_shouldThrowException() {
        token.appendParameter("   ", "value1");
    }

    @Test(expected = PathToken.InvalidParameterNameOrValueException.class)
    public void havingHistoryToken_appendingParameterWithNameContainingParametersSeparator_shouldThrowException() {
        token.appendParameter("parameter"+ PathToken.PARAMETERS_SEPARATOR, "value1");
    }

    @Test(expected = PathToken.InvalidParameterNameOrValueException.class)
    public void havingHistoryToken_appendingParameterWithNameContainingParameterValueSeparator_shouldThrowException() {
        token.appendParameter("parameter"+ PathToken.PARAMETER_VALUE_SEPARATOR, "value1");
    }

    @Test(expected = PathToken.InvalidParameterNameOrValueException.class)
    public void havingHistoryToken_appendingParameterWithNameContainingPathKeywordValueSeparator_shouldThrowException() {
        token.appendParameter("parameter"+ PathToken.PATH_MARKER, "value1");
    }

    @Test(expected = PathToken.InvalidParameterNameOrValueException.class)
    public void havingHistoryToken_appendingParameterWithValueContainingParametersSeparator_shouldThrowException() {
        token.appendParameter("parameter", "value1"+ PathToken.PARAMETERS_SEPARATOR);
    }

    @Test(expected = PathToken.InvalidParameterNameOrValueException.class)
    public void havingHistoryToken_appendingParameterWithValueContainingParameterValueSeparator_shouldThrowException() {
        token.appendParameter("parameter", "value1"+ PathToken.PARAMETER_VALUE_SEPARATOR);
    }

    @Test(expected = PathToken.InvalidParameterNameOrValueException.class)
    public void havingHistoryToken_appendingParameterWithValueContainingPathKeyWord_shouldThrowException() {
        token.appendParameter("parameter", "value1"+ PathToken.PATH_MARKER);
    }

    @Test
    public void havingHistoryTokenWithSpecificPath_appendingParameterToThatToken_shouldResultInConcatenatedPathAndParameters() {
        assertEquals("_path=somePath&parameter1=value1", token.appendParameter("parameter1", "value1").asTokenString());
    }
}
