package com.progressoft.brix.domino.gwt.client.history;

import com.progressoft.brix.domino.api.shared.history.StateToken;
import org.junit.Test;

public class DominoStateTokenTest {

    @Test(expected = StateToken.TokenCannotBeNullException.class)
    public void createWithNullToken_shouldThrowException() throws Exception {
        new DominoStateToken(null);
    }
//
//    @Test(expected = StateToken.TokenCannotBeNullException.class)
//    public void createWithNullToken_shouldThrowException() throws Exception {
//        new DominoStateToken(null);
//    }<
}
