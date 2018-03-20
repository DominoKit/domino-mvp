package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.shared.extension.Contribution;

@FunctionalInterface
public interface ContributionHandler<C extends Contribution> {
    void handle(C contribution);
}
