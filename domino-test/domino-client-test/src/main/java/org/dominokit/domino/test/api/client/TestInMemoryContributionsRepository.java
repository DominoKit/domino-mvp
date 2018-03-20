package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.shared.extension.Contribution;
import org.dominokit.domino.api.shared.extension.ExtensionPoint;
import org.dominokit.domino.client.commons.extensions.InMemoryContributionRepository;

import java.util.HashMap;
import java.util.Map;

public class TestInMemoryContributionsRepository extends InMemoryContributionRepository {

    protected final Map<String, Contribution> testContributions = new HashMap<>();

    @Override
    public void registerContribution(Class<? extends ExtensionPoint> extensionPoint, Contribution contribution) {
        super.registerContribution(extensionPoint, contribution);
        testContributions.put(contribution.getClass().getCanonicalName(), contribution);
    }

    public <C extends Contribution> C getContribution(Class<C> contributionClass) {
        return (C) testContributions.get(contributionClass.getCanonicalName());
    }
}
