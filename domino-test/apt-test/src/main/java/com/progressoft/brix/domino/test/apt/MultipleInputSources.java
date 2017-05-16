package com.progressoft.brix.domino.test.apt;

import javax.annotation.processing.Processor;

public class MultipleInputSources implements InputSource {
    private String[] inputClassesNames;

    public MultipleInputSources(String[] inputClassesNames) {
        this.inputClassesNames = inputClassesNames;
    }

    @Override
    public BaseTargetProcessor withProcessor(Processor processor) {
        return new MultipleTargetProcessor(inputClassesNames, processor);
    }
}
