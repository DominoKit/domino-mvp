package com.progressoft.brix.domino.api.client.extension;

import com.progressoft.brix.domino.api.shared.extension.Contribution;
import com.progressoft.brix.domino.api.shared.extension.ExtensionPoint;

@FunctionalInterface
public interface ContributionsRegistry {
    void registerContribution(Class<? extends ExtensionPoint> extensionPoint, Contribution contribution);
}
