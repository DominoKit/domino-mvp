package org.dominokit.domino.apt.client;

import org.apache.commons.io.IOUtils;
import org.dominokit.domino.apt.client.processors.group.RequestFactoryProcessor;
import org.dominokit.domino.apt.client.processors.handlers.RequestPathProcessor;
import org.dominokit.domino.apt.client.processors.inject.ListenToDominoEventProcessor;
import org.dominokit.domino.apt.client.processors.module.client.ClientModuleAnnotationProcessor;
import org.dominokit.domino.apt.client.processors.module.client.ConfigurationProviderAnnotationProcessor;
import org.dominokit.domino.apt.client.processors.module.client.presenters.PresenterCommandProcessor;
import org.junit.Ignore;
import org.junit.Test;

import javax.annotation.processing.Processor;
import java.io.IOException;
import java.io.InputStream;

import static org.dominokit.domino.test.apt.ProcessorAssert.assertProcessing;

public class ClientModuleAnnotationProcessorTest {

    private static final String BASE_PACKAGE = "org/dominokit/domino/apt/client/";

    private ClientModuleAnnotationProcessor processor() {
        return new ClientModuleAnnotationProcessor();
    }

    private String getExpectedResultFileContent(String resourceName) throws IOException {
        try (InputStream resourceInputStream = this.getClass().getResourceAsStream("results/" + resourceName)) {
            return IOUtils.toString(resourceInputStream, "UTF-8");
        }
    }

    @Test
    public void givenNotAnnotatedClassShouldDoNothing() throws Exception {
        assertProcessing(BASE_PACKAGE + "NotAnnotatedClass.java")
                .withProcessor(processor())
                .compilesWithoutErrors();
    }

    @Test
    public void givenAnnotatedClassWithClientModule_ShouldGenerateClassImplementsModuleConfigurations() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithClientModuleWithNameTest.java")
                .withProcessors(providerProcessor(), processor())
                .generates(getExpectedResultFileContent("TestModuleConfiguration.java"),
                        getExpectedResultFileContent("TestModuleConfiguration_Provider.java"));
    }

    private Processor providerProcessor() {
        return new ConfigurationProviderAnnotationProcessor();
    }

    @Test
    public void givenClassAnnotatedWithPresenter_ShouldAppendPresenterRegistrationToModuleConfiguration() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithClientModuleWithPresenterRegistration.java",
                BASE_PACKAGE + "DefaultAnnotatedClassWithPresenter.java")
                .withProcessor(processor())
                .generates(getExpectedResultFileContent("PresenterRegistrationModuleConfiguration.java"));
    }

    @Test
    public void givenTwoClassesAnnotatedWithPresenter_ShouldAppendPresentersRegistrationsToModuleConfiguration() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithClientModuleWithPresentersRegistrations.java",
                BASE_PACKAGE + "FirstAnnotatedClassWithPresenter.java",
                BASE_PACKAGE + "SecondAnnotatedClassWithPresenter.java")
                .withProcessor(processor())
                .generates(getExpectedResultFileContent("PresentersRegistrationsModuleConfiguration.java"));
    }

    @Test(expected = RuntimeException.class)
    @Ignore
    public void givenClassAnnotatedWithPresenterAndNotImplementsPresentableInterface_WhenProcess_ShouldThrowException() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithClientModuleWithPresenterRegistration.java",
                BASE_PACKAGE + "InvalidPresenterClass.java")
                .withProcessor(processor())
                .failsToCompile();

    }

    @Test
    public void givenClassAnnotatedWithUiView_WhenProcess_ShouldAddRegistrationLineToModuleConfiguration() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithUiView.java",
                BASE_PACKAGE + "AnnotatedClassWithClientModuleWithUiViewRegistration.java")
                .withProcessor(processor())
                .generates(getExpectedResultFileContent("UiViewRegistrationModuleConfiguration.java"));

    }

    @Test
    public void givenClassAnnotatedWithRequest_WhenProcess_ShouldAddRegistrationLineToModuleConfiguration() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithRequest.java",
                BASE_PACKAGE + "AnnotatedClassWithClientModuleWithRequestRegistrations.java")
                .withProcessor(processor())
                .generates(getExpectedResultFileContent("RequestRegistrationsModuleConfiguration.java"));
    }

    @Test(expected = RuntimeException.class)
    @Ignore
    public void givenClassAnnotatedWithRequestAndNotImplementsRequestInterface_WhenProcess_ShouldThrowException() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithClientModuleWithRequestRegistrations.java",
                BASE_PACKAGE + "InvalidRequestClass.java",
                BASE_PACKAGE + "PresenterInterface.java")
                .withProcessor(processor())
                .failsToCompile();
    }

    @Test
    public void givenClassAnnotatedWithInitialTask_WhenProcess_ShouldAddRegistrationLineToModuleConfiguration() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithInitialTask.java",
                BASE_PACKAGE + "AnnotatedClassWithClientModuleWithInitialTaskRegistrations.java")
                .withProcessor(processor())
                .generates(getExpectedResultFileContent("InitialTaskRegistrationsModuleConfiguration.java"));
    }

    @Test
    public void givenClassAnnotatedWithListener_WhenProcess_ShouldAddRegistrationLineToModuleConfiguration() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithListener.java",
                BASE_PACKAGE + "AnnotatedClassWithClientModuleWithListenerRegistrations.java")
                .withProcessor(processor())
                .generates(getExpectedResultFileContent("ListenersRegistrationsModuleConfiguration.java"));
    }


    @Test
    public void givenPresenterMethodAnnotatedWithInjectContext_WhenProcessWithInjectContextProcessor_ShouldGenerateListenerForThatPresenterClass() throws Exception {
        assertProcessing(BASE_PACKAGE + "PresenterInterface.java",
                BASE_PACKAGE + "PresenterInterfaceCommand.java"
        )
                .withProcessor(new ListenToDominoEventProcessor())

                .generates(getExpectedResultFileContent("PresenterInterfaceListenerForMainDominoEvent.java"));
    }

    @Test
    public void givenPresenter_WhenProcessWithPresenterCommandProcessor_ShouldGeneratePresenterCommandClass() throws Exception {
        assertProcessing(BASE_PACKAGE + "SomePresenter.java")
                .withProcessor(new PresenterCommandProcessor())
                .generates(getExpectedResultFileContent("SomePresenterCommand.java"));
    }

    @Test(expected = RuntimeException.class)
    @Ignore
    public void givenClassAnnotatedWithListenerAndNotImplementsRequiredInterface_WhenProcess_ShouldThrowException() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithClientModuleWithListenerRegistrations.java",
                BASE_PACKAGE + "InvalidListenerClass.java")
                .withProcessor(processor()).failsToCompile();
    }

    @Test(expected = RuntimeException.class)
    public void givenClassAnnotatedWithHandlerPathButNotExtendingServerRequest_whenProcess_shouldThrowException()
            throws Exception {
        assertProcessing(
                BASE_PACKAGE + "InvalidHandlerPathRequestClass.java")
                .withProcessor(new RequestPathProcessor())
                .failsToCompile();

    }

    @Test
    public void givenClassAnnotatedWithHandlerPath_whenProcess_shouldCompileWithoutErrors()
            throws Exception {
        assertProcessing(BASE_PACKAGE + "SomeRequest.java",
                BASE_PACKAGE + "SomeResponse.java",
                BASE_PACKAGE + "AnnotatedClassWithHandlerPath.java")
                .withProcessor(new RequestPathProcessor())
                .compilesWithoutErrors();
    }

    @Test
    public void givenClassAnnotatedWithHandlerPath_whenProcess_shouldGenerateSenderWithServiceRootEntry()
            throws Exception {
        assertProcessing(BASE_PACKAGE + "SomeRequest.java",
                BASE_PACKAGE + "SomeResponse.java",
                BASE_PACKAGE + "AnnotatedClassWithHandlerPathWithServiceRoot.java")
                .withProcessor(new RequestPathProcessor())
                .generates(getExpectedResultFileContent(
                        "AnnotatedClassWithHandlerPathWithServiceRootSender.java"));
    }

    @Test
    public void givenClassAnnotatedWithHandlerPathAndNoServiceRoot_whenProcess_shouldGenerateSenderServiceRootMatcher()
            throws Exception {
        assertProcessing(BASE_PACKAGE + "SomeRequest.java",
                BASE_PACKAGE + "SomeResponse.java",
                BASE_PACKAGE + "AnnotatedClassWithHandlerPathWithoutServiceRoot.java")
                .withProcessor(new RequestPathProcessor())
                .generates(getExpectedResultFileContent(
                        "AnnotatedClassWithHandlerPathWithoutServiceRootSender.java"));
    }

    @Test
    public void givenClassAnnotatedWithRequestSender_whenProcess_shouldRegisterItInTheClientModule() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithRequestSender.java",
                BASE_PACKAGE + "AnnotatedClassWithHandlerPath.java",
                BASE_PACKAGE + "SomeRequest.java",
                BASE_PACKAGE + "SomeResponse.java",
                BASE_PACKAGE + "AnnotatedClassWithClientModuleForRequestSender.java")
                .withProcessor(processor())
                .generates(getExpectedResultFileContent(
                        "RequestSendersRegistrationsModuleConfiguration.java"));
    }

    @Test
    public void givenInterfaceAnnotatedWithRequestGroup_whenProcess_shouldGenerateFactory() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedInterfaceWithRequestGroup.java",
                BASE_PACKAGE + "SomeRequest.java",
                BASE_PACKAGE + "SomeResponse.java")
                .withProcessor(new RequestFactoryProcessor())
                .generates(getExpectedResultFileContent(
                        "AnnotatedInterfaceWithRequestGroupFactory.java"));
    }
}
