package com.progressoft.brix.domino.gwt.client.history;

import com.google.gwt.user.client.History;
import com.progressoft.brix.domino.api.client.history.UrlHistory;

public class GwtUrlHistory implements UrlHistory {

    @Override
    public void apply(String urlToken) {
        History.replaceItem(urlToken, false);
    }
}
