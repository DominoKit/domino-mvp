package com.progressoft.brix.domino.test.apt;

import javax.annotation.processing.Processor;


public class SingleInputSource implements InputSource {

    private String inputClassName;

    public SingleInputSource(String inputClassName) {
        this.inputClassName = inputClassName;
    }

    @Override
    public SingleTargetProcessor withProcessor(Processor processor) {
        return new SingleTargetProcessor(inputClassName, processor);
    }
}
