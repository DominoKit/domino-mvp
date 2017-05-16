package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.annotations.Contribute;
import com.progressoft.brix.domino.api.shared.extension.Contribution;
import com.progressoft.brix.domino.api.shared.extension.MainExtensionPoint;

@Contribute
public class AnnotatedClassWithContribution implements Contribution<MainExtensionPoint> {

    @Override
    public void contribute(MainExtensionPoint extensionPoint) {
    }
}