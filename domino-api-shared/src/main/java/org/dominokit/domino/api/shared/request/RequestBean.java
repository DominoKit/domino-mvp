package org.dominokit.domino.api.shared.request;

import java.io.Serializable;

public interface RequestBean extends Serializable {
    VoidRequest VOID_REQUEST = new VoidRequest();
}
