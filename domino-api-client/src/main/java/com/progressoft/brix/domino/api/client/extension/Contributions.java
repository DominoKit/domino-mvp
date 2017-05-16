package com.progressoft.brix.domino.api.client.extension;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.shared.extension.ExtensionPoint;

public class Contributions {

    private Contributions(){

    }

    public static void apply(Class<? extends ExtensionPoint> extensionPointInterface, ExtensionPoint extensionPoint){
        ClientApp.make().applyContributions(extensionPointInterface, extensionPoint);
    }
}
