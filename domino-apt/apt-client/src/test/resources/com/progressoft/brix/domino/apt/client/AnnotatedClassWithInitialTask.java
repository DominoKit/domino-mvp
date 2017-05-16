package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.InitializeTask;
import com.progressoft.brix.domino.api.client.annotations.InitialTask;

@InitialTask
public class AnnotatedClassWithInitialTask implements InitializeTask {

    @Override
    public void execute() {
    }
}