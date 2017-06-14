package com.progressoft.brix.domino.apt.client.processors.module.client;

import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.client.InitializeTask;
import com.progressoft.brix.domino.api.client.annotations.*;
import com.progressoft.brix.domino.api.client.mvp.presenter.Presentable;
import com.progressoft.brix.domino.api.client.mvp.view.View;
import com.progressoft.brix.domino.api.client.request.ClientRequest;
import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.api.client.request.RequestRestSender;
import com.progressoft.brix.domino.api.shared.extension.Contribution;
import com.progressoft.brix.domino.apt.commons.BaseProcessor;
import com.progressoft.brix.domino.apt.commons.FullClassName;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.io.Writer;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


//@AutoService(Processor.class)
public class ModuleAnnotationProcessor extends BaseProcessor {

    private static final Logger LOGGER = Logger.getLogger(
            ModuleAnnotationProcessor.class.getName());

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

            presenters=presentersRegister.readItems();
            views=viewsRegister.readItems();
            requests=requestRegister.readItems();
            initialTasks=initialTasksRegister.readItems();
            contributions=contributionsRegister.readItems();
            senders=sendersRegister.readItems();

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

            collectPresenters(roundEnv);
            collectViews(roundEnv);
            collectRequests(roundEnv);
            collectInitialTasks(roundEnv);
            collectContributions(roundEnv);
            collectSenders(roundEnv);
        }catch (Exception e){
            messager.printMessage(Diagnostic.Kind.ERROR, ExceptionUtils.getFullStackTrace(e));
        }
        return true;
    }

    private void collectSenders(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(RequestSender.class).stream()
                .filter(e -> validateElementKind(e, ElementKind.CLASS))
                .map(this::newProcessorElement)
                .filter(e -> e.isImplementsGenericInterface(RequestRestSender.class))
                .collect(Collectors.toSet())
                .forEach(s -> senders.add(s.fullQualifiedNoneGenericName() + ":" + getSenderRequest(s.asTypeElement())));
    }

    private void collectContributions(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(Contribute.class).stream()
                .filter(e -> validateElementKind(e, ElementKind.CLASS))
                .map(this::newProcessorElement)
                .filter(e->e.isImplementsGenericInterface(Contribution.class))
                .collect(Collectors.toSet())
                .forEach(c -> contributions.add(c.fullQualifiedNoneGenericName() + ":" + getContributionExtensionPoint(c)));
    }

    private String getContributionExtensionPoint(ProcessorElement element) {
        String contribution=element.getInterfaceFullQualifiedGenericName(Contribution.class);
        return new FullClassName(contribution).allImports().get(1);
    }

    private void collectInitialTasks(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(InitialTask.class).stream()
                .filter(e -> validateElementKind(e, ElementKind.CLASS))
                .map(this::newProcessorElement)
                .filter(i->i.isImplementsInterface(InitializeTask.class))
                .collect(Collectors.toSet())
                .forEach(i -> initialTasks.add(i.fullQualifiedNoneGenericName()));
    }

    private void collectRequests(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(Request.class).stream()
                .filter(e -> validateElementKind(e, ElementKind.CLASS))
                .map(this::newProcessorElement)
                .filter(this::isRequestClass)
                .collect(Collectors.toSet())
                .forEach(r -> requests.add(r.fullQualifiedNoneGenericName() + ":" + getRequestPresenter(r)));
    }

    private String getRequestPresenter(ProcessorElement element) {
        return new FullClassName(typeUtils.capture(element.asTypeElement().getSuperclass()).toString()).allImports().get(1);
    }


    private void collectPresenters(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(Presenter.class).stream()
                .filter(e -> validateElementKind(e, ElementKind.CLASS))
                .map(this::newProcessorElement)
                .filter(e -> e.isImplementsInterface(Presentable.class))
                .collect(Collectors.toSet())
                .forEach(p -> presenters.add(p.fullQualifiedNoneGenericName() + ":" + getPresentableInterface(p)));
    }

    private void collectViews(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(UiView.class).stream()
                .filter(e -> validateElementKind(e, ElementKind.CLASS))
                .map(this::newProcessorElement)
                .filter(this::isView)
                .collect(Collectors.toSet())
                .forEach(v -> views.add(v.fullQualifiedNoneGenericName() + ":" + getViewPresenter(v.asTypeElement())));
    }


    private String getPresentableInterface(ProcessorElement element) {
        TypeMirror typeMirror = element.asTypeElement().getInterfaces().stream().filter(this::isPresentableInterface)
                .collect(Collectors.toSet()).stream().findFirst().orElseThrow(IllegalArgumentException::new);

        return typeMirror.toString();
    }

    private boolean isPresentableInterface(TypeMirror implementedInterface) {
        return typeUtils.isAssignable(implementedInterface,
                elementUtils.getTypeElement(Presentable.class.getCanonicalName()).asType());
    }
    private boolean isRequestClass(ProcessorElement element) {
        String superType=newProcessorElement(typeUtils.asElement(element.asTypeElement().getSuperclass())).fullQualifiedNoneGenericName();
        return superType.equals(ClientServerRequest.class.getCanonicalName()) || superType.equals(ClientRequest.class.getCanonicalName());
    }


    private boolean isView(ProcessorElement element){
        element.asTypeElement().getInterfaces().stream().filter(this::isViewInterface)
                .collect(Collectors.toSet()).stream().findFirst().orElseThrow(IllegalArgumentException::new);
        return true;
    }

    private boolean isViewInterface(TypeMirror implementedInterface) {
        return newProcessorElement(MoreTypes.asTypeElement(typeUtils, implementedInterface)).isImplementsGenericInterface(View.class);
    }

    private void generateModuleConfiguration(ProcessorElement element) {
        try (Writer sourceWriter = obtainSourceWriter(element.elementPackage(),
                element.getAnnotation(ClientModule.class).name() + "NewConfiguration")) {

            String clazz = new ModuleConfigurationSourceWriter(element, presenters, views, requests, initialTasks, contributions, senders).write();
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


    protected Element getViewPresenter(Element e) {
        AnnotationMirror annotationMirror = MoreElements.getAnnotationMirror(e, UiView.class).get();
        return getProviderInterface(annotationMirror);
    }

    protected Element getSenderRequest(Element e) {
        AnnotationMirror annotationMirror = MoreElements.getAnnotationMirror(e, RequestSender.class).get();
        return getProviderInterface(annotationMirror);
    }

    private Element getProviderInterface(AnnotationMirror providerAnnotation) {
        Map<? extends ExecutableElement, ? extends AnnotationValue> valueIndex =
                providerAnnotation.getElementValues();

        AnnotationValue value = valueIndex.values().iterator().next();
        return ((DeclaredType) value.getValue()).asElement();
    }
}
