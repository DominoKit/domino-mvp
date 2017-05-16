package com.progressoft.brix.domino.apt.client;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static com.progressoft.brix.domino.test.apt.ProcessorAssert.assertProcessing;

public class ClientModuleAnnotationProcessorTest {

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
                .withProcessor(processor())
                .generates(getExpectedResultFileContent("TestModuleConfiguration.java"));
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

    @Test
    public void givenClassAnnotatedWithPathAndNoParameters_WhenProcess_ShouldAddRegistrationLineToModuleConfiguration() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithPathAndNoParamters.java",
                BASE_PACKAGE + "AnnotatedClassWithClientModuleWithPathRegistrations.java",
                BASE_PACKAGE + "PresenterInterface.java")
                .withProcessor(processor())
                .generates(getExpectedResultFileContent("PathRegistrationsModuleConfiguration.java"));
    }

    @Test
    public void givenClassAnnotatedWithPathAndPathParameter_WhenProcess_ShouldAddRegistrationLineToModuleConfiguration() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithPathAndPathParameter.java",
                BASE_PACKAGE + "AnnotatedClassWithClientModuleWithPathAndParameterRegistrations.java",
                BASE_PACKAGE + "PresenterInterface.java")
                .withProcessor(processor())
                .generates(getExpectedResultFileContent("PathAndParameterRegistrationsModuleConfiguration.java"));
    }

    @Test
    public void givenClassAnnotatedWithPathAndCustomConverter_WhenProcess_ShouldAddRegistrationLineToModuleConfiguration() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithPathAndCustomMapper.java",
                BASE_PACKAGE + "AnnotatedClassWithClientModuleWithPathAndCustomMapperRegistrations.java",
                BASE_PACKAGE + "PresenterInterface.java",
                BASE_PACKAGE + "SampleMapper.java")
                .withProcessor(processor())
                .generates(getExpectedResultFileContent("PathAndCustomMapperRegistrationsModuleConfiguration.java"));
    }

    @Test
    public void givenClassAnnotatedWithPathAndParameterWithName_WhenProcess_ShouldAddRegistrationLineToModuleConfiguration() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithPathAndParameterWithName.java",
                BASE_PACKAGE + "AnnotatedClassWithClientModuleWithPathAndParameterWithNameRegistrations.java",
                BASE_PACKAGE + "PresenterInterface.java")
                .withProcessor(processor())
                .generates(getExpectedResultFileContent("PathAndParameterWithNameRegistrationsModuleConfiguration.java"));
    }

    @Test
    public void givenClassAnnotatedWithPathAndParameterWithConverter_WhenProcess_ShouldAddRegistrationLineToModuleConfiguration() throws Exception {
        assertProcessing(BASE_PACKAGE + "AnnotatedClassWithPathAndParameterWithConverter.java",
                BASE_PACKAGE + "AnnotatedClassWithClientModuleWithPathAndParameterWithConverterRegistrations.java",
                BASE_PACKAGE + "PresenterInterface.java",
                BASE_PACKAGE + "BigDecimalConverter.java")
                .withProcessor(processor())
                .generates(getExpectedResultFileContent("PathAndParameterWithConverterRegistrationsModuleConfiguration.java"));
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
