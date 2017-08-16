package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.ClientStartupTask;
import com.progressoft.brix.domino.api.client.annotations.InitialTask;

@InitialTask
public class AnnotatedClassWithInitialTask implements ClientStartupTask {

    @Override
    public void execute() {
    }
}