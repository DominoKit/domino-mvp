package org.dominokit.domino.test.apt;


public class ProcessorAssert {

    private ProcessorAssert(){
    }

    public static InputSource assertProcessing(String inputClassName) {
        return new SingleInputSource(inputClassName);
    }

    public static MultipleInputSources assertProcessing(String... inputClassesNames) {
        return new MultipleInputSources(inputClassesNames);
    }
}
