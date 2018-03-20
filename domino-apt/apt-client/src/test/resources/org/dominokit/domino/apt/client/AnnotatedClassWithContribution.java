package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.client.annotations.Contribute;
import org.dominokit.domino.api.shared.extension.Contribution;
import org.dominokit.domino.api.shared.extension.MainExtensionPoint;

@Contribute
public class AnnotatedClassWithContribution implements Contribution<MainExtensionPoint> {

    @Override
    public void contribute(MainExtensionPoint extensionPoint) {
        //for code generation testing only
    }
}