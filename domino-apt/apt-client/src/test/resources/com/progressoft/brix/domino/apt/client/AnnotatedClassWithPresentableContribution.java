package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.annotations.Contribute;
import com.progressoft.brix.domino.api.client.annotations.AutoRequest;
import com.progressoft.brix.domino.api.shared.extension.Contribution;
import com.progressoft.brix.domino.api.shared.extension.MainExtensionPoint;

@Contribute
@AutoRequest(presenters={ContributionPresenterInterface.class}, method="onMainContextReceived")
public class AnnotatedClassWithPresentableContribution implements Contribution<MainExtensionPoint> {

    @Override
    public void contribute(MainExtensionPoint extensionPoint) {
    }
}