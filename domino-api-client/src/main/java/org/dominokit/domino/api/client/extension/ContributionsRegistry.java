package org.dominokit.domino.api.client.extension;

import org.dominokit.domino.api.shared.extension.Contribution;
import org.dominokit.domino.api.shared.extension.ExtensionPoint;

@FunctionalInterface
public interface ContributionsRegistry {
    void registerContribution(Class<? extends ExtensionPoint> extensionPoint, Contribution contribution);
}
