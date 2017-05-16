package com.progressoft.brix.domino.gwt.client.extensions;

import com.progressoft.brix.domino.api.client.extension.ContributionsRepository;
import com.progressoft.brix.domino.api.shared.extension.Contribution;
import com.progressoft.brix.domino.api.shared.extension.ExtensionPoint;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class InMemoryContributionRepository implements ContributionsRepository {

    private final Map<String, Set<ContributionWrapper>> contributions = new HashMap<>();

    @Override
    public void registerContribution(Class<? extends ExtensionPoint> extensionPoint, Contribution contribution) {
        initializeExtensionPointContributions(extensionPoint);
        contributions.get(extensionPoint.getCanonicalName()).add(new ContributionWrapper(contribution));
    }

    @Override
    public Set<Contribution> findExtensionPointContributions(Class<? extends ExtensionPoint> extensionPoint) {
        initializeExtensionPointContributions(extensionPoint);
        return contributions.get(extensionPoint.getCanonicalName()).stream().map(cw -> cw.contribution).collect(
                Collectors.toSet());
    }

    private void initializeExtensionPointContributions(Class<? extends ExtensionPoint> extensionPoint) {
        if (isNull(contributions.get(extensionPoint.getCanonicalName())))
            contributions.put(extensionPoint.getCanonicalName(), new HashSet<>());
    }

    private class ContributionWrapper {

        private final Contribution contribution;

        public ContributionWrapper(Contribution contribution) {
            this.contribution = contribution;
        }

        @Override
        public boolean equals(Object other) {
            if(isNull(other))
                return false;
            return contribution.getClass().getCanonicalName().equals(((ContributionWrapper) other).contribution.getClass().getCanonicalName());
        }

        @Override
        public int hashCode() {
            return contribution.getClass().getCanonicalName().hashCode();
        }
    }
}
