package org.dominokit.domino.apt.client;

import javax.annotation.Generated;
import org.dominokit.domino.api.client.ModuleConfiguration;
import org.dominokit.domino.api.client.extension.DominoEventsRegistry;
import org.dominokit.domino.api.shared.extension.MainDominoEvent;

/**
 * This is generated class, please don't modify
 */
@Generated("org.dominokit.domino.apt.client.processors.module.client.ClientModuleAnnotationProcessor")
public class ListenersRegistrationsModuleConfiguration implements ModuleConfiguration {

    @Override
    public void registerListeners(DominoEventsRegistry registry) {
        registry.addListener(MainDominoEvent.class, new AnnotatedClassWithListener());
    }
}