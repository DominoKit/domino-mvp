package com.progressoft.brix.domino.api.client.extension;

import com.progressoft.brix.domino.api.shared.extension.Contribution;
import com.progressoft.brix.domino.api.shared.extension.ExtensionPoint;

import java.util.Set;

public interface ContributionsRepository {
    void registerContribution(Class<? extends ExtensionPoint> extensionPoint, Contribution contribution);
    Set<Contribution> findExtensionPointContributions(Class<? extends ExtensionPoint> extensionPoint);
}
