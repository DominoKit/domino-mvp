package com.progressoft.brix.domino.apt.client.processors.module;

import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.client.annotations.*;
import com.progressoft.brix.domino.apt.client.RegistrationFactory;
import com.progressoft.brix.domino.apt.client.RegistrationHelper;
import com.progressoft.brix.domino.apt.client.registration.*;
import com.progressoft.brix.domino.apt.commons.BaseProcessor;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@AutoService(Processor.class)
public class ClientModuleAnnotationProcessor extends BaseProcessor {

    private static final Logger LOGGER = Logger.getLogger(ClientModuleAnnotationProcessor.class.getName());

    private Map<String, List<Element>> elements = new HashMap<>();
    private Set<Element> clientModules = new HashSet<>();

    public ClientModuleAnnotationProcessor() {

        elements.put(Presenter.class.getCanonicalName(), new LinkedList<>());
        elements.put(UiView.class.getCanonicalName(), new LinkedList<>());
        elements.put(Request.class.getCanonicalName(), new LinkedList<>());
        elements.put(InitialTask.class.getCanonicalName(), new LinkedList<>());
        elements.put(Contribute.class.getCanonicalName(), new LinkedList<>());
        elements.put(Path.class.getCanonicalName(), new LinkedList<>());
        elements.put(PathParameter.class.getCanonicalName(), new LinkedList<>());
        elements.put(RequestSender.class.getCanonicalName(), new LinkedList<>());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        elements.get(Presenter.class.getCanonicalName()).addAll(roundEnv.getElementsAnnotatedWith(Presenter.class));
        elements.get(UiView.class.getCanonicalName()).addAll(roundEnv.getElementsAnnotatedWith(UiView.class));
        elements.get(Request.class.getCanonicalName()).addAll(roundEnv.getElementsAnnotatedWith(Request.class));
        elements.get(InitialTask.class.getCanonicalName()).addAll(roundEnv.getElementsAnnotatedWith(InitialTask.class));
        elements.get(Contribute.class.getCanonicalName()).addAll(roundEnv.getElementsAnnotatedWith(Contribute.class));
        elements.get(Path.class.getCanonicalName()).addAll(roundEnv.getElementsAnnotatedWith(Path.class));
        elements.get(PathParameter.class.getCanonicalName()).addAll(roundEnv.getElementsAnnotatedWith(PathParameter.class));
        elements.get(RequestSender.class.getCanonicalName())
                .addAll(roundEnv.getElementsAnnotatedWith(RequestSender.class));

        clientModules.addAll(roundEnv.getElementsAnnotatedWith(ClientModule.class));
        if (roundEnv.processingOver()) {
            clientModules.stream()
                    .filter(e -> validateElementKind(e, ElementKind.CLASS))
                    .forEach(e -> generateModuleConfiguration(newProcessorElement(e)));
        }

        return false;
    }


    private void generateModuleConfiguration(ProcessorElement element) {
        try (Writer sourceWriter = obtainSourceWriter(element.elementPackage(), element.getAnnotation(ClientModule.class).name() + ConfigurationSourceWriter.MODEL_CONFIGURATION)) {
            sourceWriter.write(createConfigurationWriter(element).write());
            sourceWriter.flush();
            sourceWriter.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "could not generate class ", e);
            messager.printMessage(Diagnostic.Kind.ERROR, "could not generate class ");
        }
    }

    private ConfigurationSourceWriter createConfigurationWriter(ProcessorElement element) {
        List<RegistrationFactory> registrationFactories = generateRegistrationFactories(element);
        ConfigurationSourceWriter.Builder builder = new ConfigurationSourceWriter.Builder()
                .withProcessorElement(element);
        registrationFactories.forEach(r -> builder.withElementRegistration(r.registration()));
        return builder.build();
    }

    private List<RegistrationFactory> generateRegistrationFactories(ProcessorElement element) {
        RegistrationHelper helper = new RegistrationHelper(this.elements, element);
        return Arrays.asList(
                new PresentersRegistrationFactory(helper),
                new RequestsRegistrationFactory(helper),
                new UiViewsRegistrationFactory(helper),
                new InitialTasksRegistrationFactory(helper),
                new ContributionRegistrationFactory(helper),
                new PathRegistrationFactory(helper),
                new RequestSenderRegistrationFactory(helper)
        );
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add(ClientModule.class.getCanonicalName());//
        annotations.add(Presenter.class.getCanonicalName());//
        annotations.add(UiView.class.getCanonicalName());//
        annotations.add(Request.class.getCanonicalName());//
        annotations.add(InitialTask.class.getCanonicalName());//
        annotations.add(Contribute.class.getCanonicalName());//
        annotations.add(Path.class.getCanonicalName());
        annotations.add(PathParameter.class.getCanonicalName());
        annotations.add(AutoRequest.class.getCanonicalName());//
        annotations.add(InjectContext.class.getCanonicalName());//
        annotations.add(RequestSender.class.getCanonicalName());//
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


}
