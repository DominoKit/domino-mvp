package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.apt.client.processors.contributions.ContributionClientRequestProcessor;
import com.progressoft.brix.domino.apt.client.processors.handlers.HandlerPathProcessor;
import com.progressoft.brix.domino.apt.client.processors.inject.InjectContextProcessor;
import com.progressoft.brix.domino.apt.client.processors.module.client.ClientModuleAnnotationProcessor;
import com.progressoft.brix.domino.apt.client.processors.module.client.ConfigurationProviderAnnotationProcessor;
import com.progressoft.brix.domino.test.apt.ProcessorAssert;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import javax.annotation.processing.Processor;
import java.io.IOException;
import java.io.InputStream;

import static com.progressoft.brix.domino.test.apt.ProcessorAssert.assertProcessing;

public class ClientClientModuleAnnotationProcessorTest {

    private static final String BASE_PACKAGE = "com/progressoft/brix/domino/apt/client/";

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
                BASE_PACKAGE + "DefaultAnnotatedClassWithPresenter.java",
                BASE_PACKAGE + "PresenterInterface.java")
                .withProcessor(processor())
                .generates(getExpectedResultFileContent("PresenterRegistrationModuleConfiguration.java"));
    }

    @Test
    public void givenTwoClassesAnnotatedWithPresenter_ShouldAppendPresentersRegistrationsToModuleConfiguration() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithClientModuleWithPresentersRegistrations.java",
                BASE_PACKAGE + "FirstAnnotatedClassWithPresenter.java",
                BASE_PACKAGE + "SecondAnnotatedClassWithPresenter.java",
                BASE_PACKAGE + "FirstPresenterInterface.java",
                BASE_PACKAGE + "SecondPresenterInterface.java")
                .withProcessor(processor())
                .generates(getExpectedResultFileContent("PresentersRegistrationsModuleConfiguration.java"));
    }

    @Test(expected = RuntimeException.class)
    public void givenClassAnnotatedWithPresenterAndNotImplementsPresentableInterface_WhenProcess_ShouldThrowException() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithClientModuleWithPresenterRegistration.java",
                BASE_PACKAGE + "InvalidPresenterClass.java")
                .withProcessor(processor())
                .failsToCompile();

    }

    @Test
    public void givenClassAnnotatedWithUiView_WhenProcess_ShouldAddRegistrationLineToModuleConfiguration() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithUiView.java",
                BASE_PACKAGE + "AnnotatedClassWithClientModuleWithUiViewRegistration.java",
                BASE_PACKAGE + "PresenterInterface.java")
                .withProcessor(processor())
                .generates(getExpectedResultFileContent("UiViewRegistrationModuleConfiguration.java"));

    }

    @Test
    public void givenClassAnnotatedWithRequest_WhenProcess_ShouldAddRegistrationLineToModuleConfiguration() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithRequest.java",
                BASE_PACKAGE + "AnnotatedClassWithClientModuleWithRequestRegistrations.java",
                BASE_PACKAGE + "PresenterInterface.java")
                .withProcessor(processor())
                .generates(getExpectedResultFileContent("RequestRegistrationsModuleConfiguration.java"));
    }

    @Test(expected = RuntimeException.class)
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
    public void givenClassAnnotatedWithContribution_WhenProcess_ShouldAddRegistrationLineToModuleConfiguration() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithContribution.java",
                BASE_PACKAGE + "AnnotatedClassWithClientModuleWithContributionRegistrations.java")
                .withProcessor(processor())
                .generates(getExpectedResultFileContent("ContributionRegistrationsModuleConfiguration.java"));
    }

    @Test
    public void givenClassAnnotatedWithContribution_WhenProcessWithContributionRequestProcessor_ShouldGenerateContributionClientRequestClass() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithPresentableContribution.java"
        )
                .withProcessor(new ContributionClientRequestProcessor())

                .generates(getExpectedResultFileContent("ObtainMainExtensionPointForPresenterInterfaceClientRequest.java"));
    }

    @Test
    public void givenPresenterMethodAnnotatedWithInjectContext_WhenProcessWithInjectContextProcessor_ShouldGenerateContributionForThatPresenterClass() throws Exception {
        assertProcessing(BASE_PACKAGE + "InjectContributionPresenterInterface.java"
        )
                .withProcessor(new InjectContextProcessor())

                .generates(getExpectedResultFileContent("InjectContributionPresenterInterfaceContributionToMainExtensionPoint.java"));
    }

    @Test(expected = RuntimeException.class)
    public void givenClassAnnotatedWithContributionAndNotImplementsRequiredInterface_WhenProcess_ShouldThrowException() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithClientModuleWithContributionRegistrations.java",
                BASE_PACKAGE + "InvalidContributionClass.java")
                .withProcessor(processor()).failsToCompile();
    }

    @Test(expected = RuntimeException.class)
    public void givenClassAnnotatedWithHandlerPathButNotExtendingClientServerRequest_whenProcess_shouldThrowException()
            throws Exception {
        assertProcessing(
                BASE_PACKAGE + "InvalidHandlerPathRequestClass.java")
                .withProcessor(new HandlerPathProcessor())
                .failsToCompile();

    }

    @Test
    public void givenClassAnnotatedWithHandlerPath_whenProcess_shouldCompileWithoutErrors()
            throws Exception {
        assertProcessing(BASE_PACKAGE + "PresenterInterface.java",
                BASE_PACKAGE + "SomeRequest.java",
                BASE_PACKAGE + "SomeResponse.java",
                BASE_PACKAGE + "AnnotatedClassWithHandlerPath.java")
                .withProcessor(new HandlerPathProcessor())
                .compilesWithoutErrors();
    }

    @Test
    public void givenClassAnnotatedWithHandlerPath_whenProcess_shouldGenerateSenderWithServiceRootEntry()
            throws Exception {
        assertProcessing(BASE_PACKAGE + "PresenterInterface.java",
                BASE_PACKAGE + "SomeRequest.java",
                BASE_PACKAGE + "SomeResponse.java",
                BASE_PACKAGE + "AnnotatedClassWithHandlerPathWithServiceRoot.java")
                .withProcessor(new HandlerPathProcessor())
                .generates(getExpectedResultFileContent(
                        "AnnotatedClassWithHandlerPathWithServiceRootSender.java"));
    }

    @Test
    public void givenClassAnnotatedWithHandlerPathAndNoServiceRoot_whenProcess_shouldGenerateSenderServiceRootMatcher()
            throws Exception {
        assertProcessing(BASE_PACKAGE + "PresenterInterface.java",
                BASE_PACKAGE + "SomeRequest.java",
                BASE_PACKAGE + "SomeResponse.java",
                BASE_PACKAGE + "AnnotatedClassWithHandlerPathWithoutServiceRoot.java")
                .withProcessor(new HandlerPathProcessor())
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
}
