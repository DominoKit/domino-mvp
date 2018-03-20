package org.dominokit.domino.apt.client.processors.module.client;

import com.google.auto.service.AutoService;
import org.dominokit.domino.api.client.annotations.ClientModule;
import org.dominokit.domino.apt.commons.BaseProcessor;
import org.dominokit.domino.apt.commons.ProcessorElement;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@AutoService(Processor.class)
public class ConfigurationProviderAnnotationProcessor extends BaseProcessor {

    private static final Logger LOGGER = Logger.getLogger(
            ConfigurationProviderAnnotationProcessor.class.getName());

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> modules = roundEnv.getElementsAnnotatedWith(ClientModule.class);
        modules.forEach(m -> generateModuleConfigurationProvider(newProcessorElement(m)));
        return false;
    }

    private void generateModuleConfigurationProvider(ProcessorElement element) {
        try (Writer sourceWriter = obtainSourceWriter(element.elementPackage(),
                element.getAnnotation(ClientModule.class).name() + "ModuleConfiguration_Provider")) {

            String clazz = new ConfigurationProviderSourceWriter(element).write();
            sourceWriter.write(clazz);
            sourceWriter.flush();
            sourceWriter.close();
        } catch (Exception e) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                    "could not generate class " + ExceptionUtils.getFullStackTrace(e));
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add(ClientModule.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
