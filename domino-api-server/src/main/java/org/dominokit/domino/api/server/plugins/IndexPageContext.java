package org.dominokit.domino.api.server.plugins;

import static java.util.Objects.nonNull;

public class IndexPageContext {

    public static final IndexPageContext INSTANCE = new IndexPageContext();

    private IndexPageProvider indexPageProvider = DefaultIndexPageProvider.INSTANCE;

    private IndexPageContext() {
    }

    public IndexPageProvider getIndexPageProvider() {
        return indexPageProvider;
    }

    public void setIndexPageProvider(IndexPageProvider indexPageProvider) {
        if(nonNull(indexPageProvider)) {
            this.indexPageProvider = indexPageProvider;
        }
    }
}
