package org.dominokit.domino.apt.client.processors.module.client.contributions;

import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;

class ContributionEntry implements AbstractRegisterMethodWriter.ItemEntry {
    protected final String contribution;
    protected final String extensionPoint;

    protected ContributionEntry(String contribution, String extensionPoint) {
        this.contribution = contribution;
        this.extensionPoint = extensionPoint;
    }
}
