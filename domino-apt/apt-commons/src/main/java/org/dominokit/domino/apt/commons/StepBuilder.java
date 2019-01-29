package org.dominokit.domino.apt.commons;

import javax.annotation.processing.ProcessingEnvironment;

public abstract class StepBuilder<T extends AbstractProcessingStep> {

    protected ProcessingEnvironment processingEnv;

    public abstract T build();

    public StepBuilder setProcessingEnv(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        return this;
    }
}
