package com.progressoft.brix.domino.test.api.client;

import com.progressoft.brix.domino.api.shared.extension.Contribution;

@FunctionalInterface
public interface ContributionHandler<C extends Contribution> {
    void handle(C contribution);
}
