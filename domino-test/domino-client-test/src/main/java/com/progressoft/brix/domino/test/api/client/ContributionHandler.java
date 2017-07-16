package com.progressoft.brix.domino.test.api.client;

import com.progressoft.brix.domino.api.shared.extension.Contribution;

@FunctionalInterface
public interface ContributionHandler {
    void handle(Contribution contribution);
}
