package org.dominokit.domino.test.apt;

import javax.annotation.processing.Processor;


public class SingleInputSource implements InputSource {

    private String inputClassName;

    public SingleInputSource(String inputClassName) {
        this.inputClassName = inputClassName;
    }

    @Override
    public BaseTargetProcessor withProcessors(Processor processor, Processor... rest) {
        return new SingleTargetProcessor(inputClassName, processor, rest);
    }

    @Override
    public SingleTargetProcessor withProcessor(Processor processor) {
        return new SingleTargetProcessor(inputClassName, processor);
    }
}
