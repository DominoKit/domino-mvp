package org.dominokit.domino.api.shared.request;

import java.io.Serializable;

public interface ResponseBean extends Serializable {
    VoidResponse VOID_RESPONSE = new VoidResponse();
}
