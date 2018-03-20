package org.dominokit.domino.test.apt;

import javax.annotation.processing.Processor;

public interface InputSource {
    BaseTargetProcessor withProcessor(Processor processor);
    BaseTargetProcessor withProcessors(Processor processor, Processor... rest);
}
