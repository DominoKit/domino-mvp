package com.progressoft.brix.domino.gwt.client.history;

import com.progressoft.brix.domino.api.shared.history.StateToken;

import static java.util.Objects.isNull;

public class DominoStateToken implements StateToken {
    public DominoStateToken(String token) {
        if (isNull(token))
            throw new TokenCannotBeNullException();
    }
}
