package org.dominokit.domino.api.server.config;

import org.dominokit.domino.api.server.resource.ResourceRegistry;

public interface ServerModuleConfiguration {
    default void registerResources(ResourceRegistry registry){/*dont force implementation*/}
}