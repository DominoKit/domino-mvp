package org.dominokit.domino.api.client.extension;

import org.dominokit.domino.api.shared.extension.Contribution;
import org.dominokit.domino.api.shared.extension.ExtensionPoint;

import java.util.Set;

public interface ContributionsRepository {
    void registerContribution(Class<? extends ExtensionPoint> extensionPoint, Contribution contribution);
    Set<Contribution> findExtensionPointContributions(Class<? extends ExtensionPoint> extensionPoint);
}
