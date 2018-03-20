package org.dominokit.domino.apt.client;

import javax.annotation.Generated;
import org.dominokit.domino.api.client.annotations.Contribute;
import org.dominokit.domino.api.shared.extension.Contribution;
import org.dominokit.domino.api.shared.extension.MainExtensionPoint;

/**
 * This is generated class, please don't modify
 */
@Generated("org.dominokit.domino.apt.client.processors.inject.InjectContextProcessor")
@Contribute
public class InjectContributionPresenterInterfaceContributionToMainExtensionPoint implements Contribution<MainExtensionPoint> {

    @Override
    public void contribute(MainExtensionPoint extensionPoint) {
        new InjectContributionPresenterInterfaceCommand()
                .onPresenterReady(presenter -> presenter.onMainExtensionPointContextReceived(extensionPoint.context()))
                .send();
    }
}