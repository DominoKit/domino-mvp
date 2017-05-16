package com.progressoft.brix.domino.apt.server;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.processing.Processor;
import java.io.IOException;
import java.io.InputStream;

import static com.progressoft.brix.domino.test.apt.ProcessorAssert.assertProcessing;

public class ServerModuleAnnotationProcessorTest {


    public static final String BASE_PACKAGE = "com/progressoft/brix/domino/apt/server/";

    private Processor serverModuleAnnotationProcessor;

    @Before
    public void setUp() throws Exception {
        serverModuleAnnotationProcessor = new ServerModuleAnnotationProcessor();
    }

    private String getExpectedResultFileContent(String resourceName) throws IOException {
        try (InputStream resourceInputStream = this.getClass().getResourceAsStream("results/" + resourceName)) {
            return IOUtils.toString(resourceInputStream, "UTF-8");
        }
    }

    @Test
    public void givenServerModuleAnnotationProcessor_whenCompilingClassThatIsNotAnnotatedWithAnyServerModuleAnnotation_thenClassShouldBeCompiledSuccessfully() throws Exception {
        assertProcessing(BASE_PACKAGE + "NoneServerModuleClass.java")
                .withProcessor(serverModuleAnnotationProcessor)
                .compilesWithoutErrors();
    }

    @Test
    public void givenAClassAnnotatedAsHandlerWithoutImplementingHandlerInterface_whenCompiling_thenCompileShouldFail()
            throws Exception {
        assertProcessing(BASE_PACKAGE + "noHandlers/HandlerNotImplementingRequestHandlerInterface.java", BASE_PACKAGE + "noHandlers/package-info.java")
                .withProcessor(serverModuleAnnotationProcessor).failsToCompile();
    }

    @Test
    public void givenAClassAnnotatedAsHandlerImplementingRequestHandlerInterface_whenCompiling_thenShouldCompile()
            throws Exception {
        assertProcessing(
                BASE_PACKAGE + "singleHandler/HandlerImplementingRequestHandlerInterface.java", BASE_PACKAGE + "singleHandler/package-info.java")
                .withProcessor(serverModuleAnnotationProcessor)
                .compilesWithoutErrors();
    }

    @Test
    public void givenAClassAnnotatedAsHandlerImplementingRequestHandlerInterface_whenCompiling_thenShouldCompileAndGenerateServerModuleWithTheHandlerRegistered()
            throws Exception {
        assertProcessing(
                BASE_PACKAGE + "singleHandler/HandlerImplementingRequestHandlerInterface.java", BASE_PACKAGE + "singleHandler/package-info.java")
                .withProcessor(serverModuleAnnotationProcessor)
                .generates(getExpectedResultFileContent("singleHandler/TestServerModule.java"));
    }

    @Test
    public void givenAClassAnnotatedHandlerImplementingCallBackRequestHandlerInterface_whenCompiling_thenShouldCompileAndGenerateServerModuleWithTheHandlerRegistered()
            throws Exception {
        assertProcessing(
                BASE_PACKAGE + "callbackHandler/HandlerImplementingRequestHandlerInterface.java", BASE_PACKAGE + "callbackHandler/package-info.java")
                .withProcessor(serverModuleAnnotationProcessor)
                .generates(getExpectedResultFileContent("callbackHandler/TestServerModule.java"));
    }

    @Test
    public void givenManyClassesAnnotatedAsHandlerImplementingRequestHandlerInterface_whenCompiling_thenShouldCompileAndGenerateServerModuleWithThoseHandlersRegistered()
            throws Exception {
        assertProcessing(
                BASE_PACKAGE + "multiHandlers/FirstHandler.java", BASE_PACKAGE + "multiHandlers/SecondHandler.java", BASE_PACKAGE + "multiHandlers/ThirdHandler.java", BASE_PACKAGE + "multiHandlers/package-info.java")
                .withProcessor(serverModuleAnnotationProcessor)
                .generates(getExpectedResultFileContent("multiHandlers/TestServerModule.java"));
    }

    @Test
    public void givenAClassAnnotatedAsInterceptorImplementingInterceptorInterface_whenCompiling_thenShouldCompileAndGenerateServerModuleWithTheInterceptorRegistered()
            throws Exception {
        assertProcessing(
                BASE_PACKAGE + "singleInterceptor/FirstInterceptor.java", BASE_PACKAGE + "singleInterceptor/package-info.java")
                .withProcessor(serverModuleAnnotationProcessor)
                .generates(getExpectedResultFileContent("singleInterceptor/TestServerModule.java"));
    }

    @Test
    public void givenManyClassesAnnotatedAsInterceptorImplementingInterceptorInterface_whenCompiling_thenShouldCompileAndGenerateServerModuleWithThoseInterceptorsRegistered()
            throws Exception {
        assertProcessing(
                BASE_PACKAGE + "multiInterceptors/FirstInterceptor.java", BASE_PACKAGE + "multiInterceptors/SecondInterceptor.java", BASE_PACKAGE + "multiInterceptors/ThirdInterceptor.java", BASE_PACKAGE + "multiInterceptors/package-info.java")
                .withProcessor(serverModuleAnnotationProcessor)
                .generates(getExpectedResultFileContent("multiInterceptors/TestServerModule.java"));
    }

    @Test
    public void givenAClassAnnotatedAsGlobalInterceptorImplementingGlobalInterceptorInterface_whenCompiling_thenShouldCompileAndGenerateServerModuleWithTheGlobalInterceptorRegistered()
            throws Exception {
        assertProcessing(
                BASE_PACKAGE + "singleGlobalInterceptor/FirstGlobalInterceptor.java", BASE_PACKAGE + "singleGlobalInterceptor/package-info.java")
                .withProcessor(serverModuleAnnotationProcessor)
                .generates(getExpectedResultFileContent("singleGlobalInterceptor/TestServerModule.java"));
    }

    @Test
    public void givenManyClassesAnnotatedAsGlobalInterceptorImplementingGlobalInterceptorInterface_whenCompiling_thenShouldCompileAndGenerateServerModuleWithThoseGlobalInterceptorsRegistered()
            throws Exception {
        assertProcessing(
                BASE_PACKAGE + "multiGlobalInterceptors/FirstGlobalInterceptor.java", BASE_PACKAGE + "multiGlobalInterceptors/SecondGlobalInterceptor.java", BASE_PACKAGE + "multiGlobalInterceptors/ThirdGlobalInterceptor.java", BASE_PACKAGE + "multiGlobalInterceptors/package-info.java")
                .withProcessor(serverModuleAnnotationProcessor)
                .generates(getExpectedResultFileContent("multiGlobalInterceptors/TestServerModule.java"));
    }

    @Test
    public void givenManyClassesAnnotatedByServerModuleAnnotations_whenCompiling_thenShouldCompileAndGenerateServerModuleWithThoseClassesRegistered()
            throws Exception {
        assertProcessing(
                BASE_PACKAGE + "multiMix/FirstHandler.java",
                BASE_PACKAGE + "multiMix/SecondHandler.java",
                BASE_PACKAGE + "multiMix/ThirdHandler.java",

                BASE_PACKAGE + "multiMix/FirstInterceptor.java",
                BASE_PACKAGE + "multiMix/SecondInterceptor.java",
                BASE_PACKAGE + "multiMix/ThirdInterceptor.java",

                BASE_PACKAGE + "multiMix/FirstGlobalInterceptor.java",
                BASE_PACKAGE + "multiMix/SecondGlobalInterceptor.java",
                BASE_PACKAGE + "multiMix/ThirdGlobalInterceptor.java",

                BASE_PACKAGE + "multiMix/FirstHandlerEndpointHandler.java",
                BASE_PACKAGE + "multiMix/SecondHandlerEndpointHandler.java",
                BASE_PACKAGE + "multiMix/ThirdHandlerEndpointHandler.java",

                BASE_PACKAGE + "multiMix/package-info.java")
                .withProcessor(serverModuleAnnotationProcessor)
                .generates(getExpectedResultFileContent("multiMix/TestServerModule.java"));
    }

    @Test
    public void givenRequestHandler_whenProcess_shouldGenerateEndpointHandler() throws Exception {
        assertProcessing(BASE_PACKAGE + "multiHandlers/FirstHandler.java")
                .withProcessor(new EndpointsProcessor())
                .generates(getExpectedResultFileContent("multiHandlers/FirstHandlerEndpointHandler.java"));
    }

    @Test
    public void givenCallbackRequestHandler_whenProcess_shouldGenerateCallbackEndpointHandler() throws Exception {
        assertProcessing(BASE_PACKAGE + "callbackHandler/HandlerImplementingRequestHandlerInterface.java")
                .withProcessor(new EndpointsProcessor())
                .generates(getExpectedResultFileContent("callbackHandler/HandlerImplementingRequestHandlerInterfaceEndpointHandler.java"));
    }
}
