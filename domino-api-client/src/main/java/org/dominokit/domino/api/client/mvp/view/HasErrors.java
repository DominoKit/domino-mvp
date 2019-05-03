package org.dominokit.domino.api.client.mvp.view;

import java.util.List;
import java.util.Map;

public interface HasErrors {

    void invalidateField(String fieldName, String errorMessage);
    void invalidateField(String fieldName, List<String> errorMessages);
    void clearErrorMessages(String field);

    void setViolations(Map<String, List<String>> violations);
    void clearViolations();
    Map<String, List<String>> getViolations();
}
