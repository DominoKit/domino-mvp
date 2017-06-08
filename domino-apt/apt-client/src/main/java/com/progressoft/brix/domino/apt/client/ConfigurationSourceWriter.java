package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import com.progressoft.brix.domino.api.client.annotations.ClientModule;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class ConfigurationSourceWriter {

    protected static final String MODEL_CONFIGURATION = "ModuleConfiguration";
    protected static final String CLOSING_PARNTHIES = "}";

    private ProcessorElement element;
    private List<ElementRegistration> elementRegistrations;

    ConfigurationSourceWriter(ProcessorElement element, List<ElementRegistration> elementRegistrations) {
        this.element = element;
        this.elementRegistrations = elementRegistrations;
    }

    String write() {
        return writePackage() +
                writeImports() +
                writeClassHeader() +
                writeImplementations() +
                CLOSING_PARNTHIES;
    }

    private String writeImplementations() {
        return elementRegistrations.stream().map(ElementRegistration::registrationMethod).collect(Collectors.joining());
    }

    private String writePackage() {
        return "package " + element.elementPackage() + ";\n";
    }

    private String writeImports() {
        return "import " + ModuleConfiguration.class.getCanonicalName() + ";\n" +
                elementRegistrations.stream().map(ElementRegistration::imports).collect(Collectors.joining());
    }

    private String writeClassHeader() {
        return "public class " + element.getAnnotation(ClientModule.class).name() + MODEL_CONFIGURATION
                + " implements ModuleConfiguration " + "{\n";
    }


    static class Builder {
        private List<ElementRegistration> registrations = new ArrayList<>();
        private ProcessorElement element;

        protected Builder withProcessorElement(ProcessorElement element) {
            this.element = element;
            return this;
        }

        protected Builder withElementRegistration(ElementRegistration registration) {
            this.registrations.add(registration);
            return this;
        }

        ConfigurationSourceWriter build() {
            return new ConfigurationSourceWriter(element, registrations);
        }
    }
}
