package com.progressoft.brix.domino.test.apt;

import javax.annotation.processing.Processor;

@FunctionalInterface
public interface InputSource {
    BaseTargetProcessor withProcessor(Processor processor);
}
