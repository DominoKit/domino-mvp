package com.progressoft.brix.domino.apt.client.processors.module.client;

import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.client.annotations.*;
import com.progressoft.brix.domino.apt.client.processors.module.client.contributions.ContributionsCollector;
import com.progressoft.brix.domino.apt.client.processors.module.client.initialtasks.InitialTasksCollector;
import com.progressoft.brix.domino.apt.client.processors.module.client.presenters.PresentersCollector;
import com.progressoft.brix.domino.apt.client.processors.module.client.requests.RequestsCollector;
import com.progressoft.brix.domino.apt.client.processors.module.client.requests.SendersCollector;
import com.progressoft.brix.domino.apt.client.processors.module.client.views.ViewsCollector;
import com.progressoft.brix.domino.apt.commons.BaseProcessor;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.io.Writer;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@AutoService(Processor.class)
public class ClientModuleAnnotationProcessor extends BaseProcessor {

    private static final Logger LOGGER = Logger.getLogger(
            ClientModuleAnnotationProcessor.class.getName());

    private Set<String> presenters;
    private Set<String> views;
    private Set<String> requests;
    private Set<String> initialTasks;
    private Set<String> contributions;
    private Set<String> senders;
    private Set<Element> clientModules = new HashSet<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        try {
            clientModules.addAll(roundEnv.getElementsAnnotatedWith(ClientModule.class));

            Register presentersRegister = new Register("presenters", presenters, messager, processingEnv);
            Register viewsRegister = new Register("views", views, messager, processingEnv);
            Register requestRegister = new Register("requests", requests, messager, processingEnv);
            Register initialTasksRegister = new Register("initialTasks", initialTasks, messager, processingEnv);
            Register contributionsRegister = new Register("contributions", contributions, messager, processingEnv);
            Register sendersRegister = new Register("senders", senders, messager, processingEnv);

            presenters = presentersRegister.readItems();
            views = viewsRegister.readItems();
            requests = requestRegister.readItems();
            initialTasks = initialTasksRegister.readItems();
            contributions = contributionsRegister.readItems();
            senders = sendersRegister.readItems();

            if (roundEnv.processingOver()) {
                presentersRegister.writeItems();
                viewsRegister.writeItems();
                requestRegister.writeItems();
                initialTasksRegister.writeItems();
                contributionsRegister.writeItems();
                sendersRegister.writeItems();
                if (roundEnv.processingOver())
                    clientModules.stream()
                            .filter(e -> validateElementKind(e, ElementKind.CLASS))
                            .forEach(e -> generateModuleConfiguration(newProcessorElement(e)));
                return false;
            }

            new PresentersCollector(messager, typeUtils, elementUtils, elementFactory, presenters).collectPresenters(roundEnv);
            new ViewsCollector(messager, elementFactory, views).collectViews(roundEnv);
            new RequestsCollector(messager, typeUtils, elementFactory, requests).collectRequests(roundEnv);
            new InitialTasksCollector(elementFactory, initialTasks).collectInitialTasks(roundEnv);
            new ContributionsCollector(messager, elementFactory, contributions).collectContributions(roundEnv);
            new SendersCollector(elementFactory, senders).collectSenders(roundEnv);
        } catch (Exception e) {
            messager.printMessage(Diagnostic.Kind.ERROR, ExceptionUtils.getFullStackTrace(e));
            throw e;
        }
        return true;
    }

    private void generateModuleConfiguration(ProcessorElement element) {
        try (Writer sourceWriter = obtainSourceWriter(element.elementPackage(),
                element.getAnnotation(ClientModule.class).name() + "ModuleConfiguration")) {

            String clazz = new ModuleConfigurationSourceWriter(element, presenters, views, requests, initialTasks,
                    contributions, senders).write();
            sourceWriter.write(clazz);
            sourceWriter.flush();
            sourceWriter.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "could not generate class ", e);
            messager.printMessage(Diagnostic.Kind.ERROR,
                    "could not generate class " + ExceptionUtils.getFullStackTrace(e));
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add(Presenter.class.getCanonicalName());
        annotations.add(UiView.class.getCanonicalName());
        annotations.add(Request.class.getCanonicalName());
        annotations.add(InitialTask.class.getCanonicalName());
        annotations.add(ClientModule.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
