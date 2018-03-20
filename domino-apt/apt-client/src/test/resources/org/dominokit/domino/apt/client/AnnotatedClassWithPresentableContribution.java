package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.client.annotations.Contribute;
import org.dominokit.domino.api.shared.extension.Contribution;
import org.dominokit.domino.api.shared.extension.MainExtensionPoint;

@Contribute
public class AnnotatedClassWithPresentableContribution implements Contribution<MainExtensionPoint> {

    @Override
    public void contribute(MainExtensionPoint extensionPoint) {
        //for generation testing only
    }
}