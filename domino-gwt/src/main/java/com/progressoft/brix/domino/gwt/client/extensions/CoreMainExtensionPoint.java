package com.progressoft.brix.domino.gwt.client.extensions;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.shared.extension.MainContext;
import com.progressoft.brix.domino.api.shared.extension.MainExtensionPoint;
import com.progressoft.brix.domino.api.shared.history.DominoHistory;

public class CoreMainExtensionPoint implements MainExtensionPoint {
    @Override
    public MainContext context() {
        return () -> ClientApp.make().getHistory();
    }
}
