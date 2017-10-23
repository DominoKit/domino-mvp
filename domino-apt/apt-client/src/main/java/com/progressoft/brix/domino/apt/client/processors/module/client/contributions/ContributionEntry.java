package com.progressoft.brix.domino.apt.client.processors.module.client.contributions;

import com.progressoft.brix.domino.apt.client.processors.module.client.AbstractRegisterMethodWriter;

class ContributionEntry implements AbstractRegisterMethodWriter.ItemEntry {
    protected final String contribution;
    protected final String extensionPoint;

    protected ContributionEntry(String contribution, String extensionPoint) {
        this.contribution = contribution;
        this.extensionPoint = extensionPoint;
    }
}
