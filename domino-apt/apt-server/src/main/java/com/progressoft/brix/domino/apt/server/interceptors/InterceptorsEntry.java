package com.progressoft.brix.domino.apt.server.interceptors;

import com.progressoft.brix.domino.apt.commons.AbstractRegisterMethodWriter;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;

public class InterceptorsEntry implements AbstractRegisterMethodWriter.ItemEntry {
    public ProcessorElement element;

    public InterceptorsEntry(ProcessorElement element) {
        this.element = element;
    }
}
