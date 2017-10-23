package com.progressoft.brix.domino.apt.server.interceptors.global;

import com.progressoft.brix.domino.apt.commons.AbstractRegisterMethodWriter;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;

public class GlobalInterceptorsEntry implements AbstractRegisterMethodWriter.ItemEntry {
    public ProcessorElement element;

    public GlobalInterceptorsEntry(ProcessorElement element) {
        this.element = element;
    }
}
