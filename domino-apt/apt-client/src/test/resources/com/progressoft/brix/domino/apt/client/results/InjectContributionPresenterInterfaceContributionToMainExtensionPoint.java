package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.annotations.AutoRequest;
import com.progressoft.brix.domino.api.client.annotations.Contribute;
import com.progressoft.brix.domino.api.shared.extension.Contribution;
import com.progressoft.brix.domino.api.shared.extension.MainExtensionPoint;
import java.lang.Override;
import javax.annotation.Generated;

@Generated("com.progressoft.brix.domino.apt.client.processors.inject.InjectContextProcessor")
@Contribute
@AutoRequest(presenters={InjectContributionPresenterInterface.class}, method="onMainExtensionPointContextReceived")
public class InjectContributionPresenterInterfaceContributionToMainExtensionPoint implements Contribution<MainExtensionPoint> {

    @Override
    public void contribute(MainExtensionPoint extensionPoint) {
        new ObtainMainExtensionPointForInjectContributionPresenterInterfaceClientRequest(extensionPoint).send();
    }
}