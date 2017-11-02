package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.annotations.Contribute;
import com.progressoft.brix.domino.api.shared.extension.Contribution;
import com.progressoft.brix.domino.api.shared.extension.MainExtensionPoint;
import javax.annotation.Generated;

@Generated("com.progressoft.brix.domino.apt.client.processors.inject.InjectContextProcessor")
@Contribute
public class InjectContributionPresenterInterfaceContributionToMainExtensionPoint implements Contribution<MainExtensionPoint> {

    @Override
    public void contribute(MainExtensionPoint extensionPoint) {
        new InjectContributionPresenterInterfaceCommand()
                .onPresenterReady(presenter -> presenter.onMainExtensionPointContextReceived(extensionPoint.context()))
                .send();
    }
}