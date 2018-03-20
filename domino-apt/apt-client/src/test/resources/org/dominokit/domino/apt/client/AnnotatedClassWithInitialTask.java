package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.client.ClientStartupTask;
import org.dominokit.domino.api.client.annotations.StartupTask;

@StartupTask
public class AnnotatedClassWithInitialTask implements ClientStartupTask {

    @Override
    public void execute() {
        //for generation testing only
    }
}