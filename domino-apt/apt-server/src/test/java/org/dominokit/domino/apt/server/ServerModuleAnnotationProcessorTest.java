package org.dominokit.domino.apt.server;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.processing.Processor;
import java.io.IOException;
import java.io.InputStream;

import static org.dominokit.domino.test.apt.ProcessorAssert.assertProcessing;

public class ServerModuleAnnotationProcessorTest {


    public static final String BASE_PACKAGE = "org/dominokit/domino/apt/server/";

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
        assertProcessing(BASE_PACKAGE + "noHandlers/HandlerNotImplementingRequestHandlerInterface.java",
                BASE_PACKAGE + "package-info.java")
                .withProcessor(serverModuleAnnotationProcessor).failsToCompile();
    }

    @Test
    public void givenAClassAnnotatedAsHandlerImplementingRequestHandlerInterface_whenCompiling_thenShouldCompile()
            throws Exception {
        assertProcessing(
                BASE_PACKAGE + "HandlerImplementingRequestHandlerInterface.java", BASE_PACKAGE + "package-info.java")
                .withProcessor(serverModuleAnnotationProcessor)
                .compilesWithoutErrors();
    }

    @Test
    public void givenAClassAnnotatedAsHandlerImplementingRequestHandlerInterface_whenCompiling_thenShouldCompileAndGenerateServerModuleWithTheHandlerRegistered()
            throws Exception {
        assertProcessing(
                BASE_PACKAGE + "HandlerImplementingRequestHandlerInterface.java",
                BASE_PACKAGE + "singleHandler/package-info.java")
                .withProcessor(serverModuleAnnotationProcessor)
                .generates(getExpectedResultFileContent("singleHandler/TestServerModule.java"));
    }

    @Test
    public void givenManyClassesAnnotatedAsHandlerImplementingRequestHandlerInterface_whenCompiling_thenShouldCompileAndGenerateServerModuleWithThoseHandlersRegistered()
            throws Exception {
        assertProcessing(
                BASE_PACKAGE + "FirstHandler.java",
                BASE_PACKAGE + "SecondHandler.java",
                BASE_PACKAGE + "ThirdHandler.java",
                BASE_PACKAGE + "multiHandlers/package-info.java")
                .withProcessor(serverModuleAnnotationProcessor)
                .generates(getExpectedResultFileContent("multiHandlers/TestServerModule.java"));
    }

    @Test
    public void givenAClassAnnotatedAsInterceptorImplementingInterceptorInterface_whenCompiling_thenShouldCompileAndGenerateServerModuleWithTheInterceptorRegistered()
            throws Exception {
        assertProcessing(
                BASE_PACKAGE + "FirstInterceptor.java",
                BASE_PACKAGE + "singleInterceptor/package-info.java")
                .withProcessor(serverModuleAnnotationProcessor)
                .generates(getExpectedResultFileContent("singleInterceptor/TestServerModule.java"));
    }

    @Test
    public void givenManyClassesAnnotatedAsInterceptorImplementingInterceptorInterface_whenCompiling_thenShouldCompileAndGenerateServerModuleWithThoseInterceptorsRegistered()
            throws Exception {
        assertProcessing(
                BASE_PACKAGE + "FirstInterceptor.java",
                BASE_PACKAGE + "SecondInterceptor.java",
                BASE_PACKAGE + "ThirdInterceptor.java",
                BASE_PACKAGE + "multiInterceptors/package-info.java")
                .withProcessor(serverModuleAnnotationProcessor)
                .generates(getExpectedResultFileContent("multiInterceptors/TestServerModule.java"));
    }

    @Test
    public void givenAClassAnnotatedAsGlobalInterceptorImplementingGlobalInterceptorInterface_whenCompiling_thenShouldCompileAndGenerateServerModuleWithTheGlobalInterceptorRegistered()
            throws Exception {
        assertProcessing(
                BASE_PACKAGE + "FirstGlobalInterceptor.java",
                BASE_PACKAGE + "singleGlobalInterceptor/package-info.java")
                .withProcessor(serverModuleAnnotationProcessor)
                .generates(getExpectedResultFileContent("singleGlobalInterceptor/TestServerModule.java"));
    }

    @Test
    public void givenManyClassesAnnotatedAsGlobalInterceptorImplementingGlobalInterceptorInterface_whenCompiling_thenShouldCompileAndGenerateServerModuleWithThoseGlobalInterceptorsRegistered()
            throws Exception {
        assertProcessing(
                BASE_PACKAGE + "FirstGlobalInterceptor.java",
                BASE_PACKAGE + "SecondGlobalInterceptor.java",
                BASE_PACKAGE + "ThirdGlobalInterceptor.java",
                BASE_PACKAGE + "multiGlobalInterceptors/package-info.java")
                .withProcessor(serverModuleAnnotationProcessor)
                .generates(getExpectedResultFileContent("multiGlobalInterceptors/TestServerModule.java"));
    }

    @Test
    public void givenManyClassesAnnotatedByServerModuleAnnotations_whenCompiling_thenShouldCompileAndGenerateServerModuleWithThoseClassesRegistered()
            throws Exception {
        assertProcessing(
                BASE_PACKAGE + "FirstHandler.java",
                BASE_PACKAGE + "SecondHandler.java",
                BASE_PACKAGE + "ThirdHandler.java",

                BASE_PACKAGE + "FirstInterceptor.java",
                BASE_PACKAGE + "SecondInterceptor.java",
                BASE_PACKAGE + "ThirdInterceptor.java",

                BASE_PACKAGE + "FirstGlobalInterceptor.java",
                BASE_PACKAGE + "SecondGlobalInterceptor.java",
                BASE_PACKAGE + "ThirdGlobalInterceptor.java",

                BASE_PACKAGE + "FirstHandlerEndpointHandler.java",
                BASE_PACKAGE + "SecondHandlerEndpointHandler.java",
                BASE_PACKAGE + "ThirdHandlerEndpointHandler.java",

                BASE_PACKAGE + "multiMix/package-info.java")
                .withProcessor(serverModuleAnnotationProcessor)
                .generates(getExpectedResultFileContent("multiMix/TestServerModule.java"));
    }

    @Test
    public void givenRequestHandler_whenProcess_shouldGenerateEndpointHandler() throws Exception {
        assertProcessing(BASE_PACKAGE + "FirstHandler.java")
                .withProcessor(new EndpointsProcessor())
                .generates(getExpectedResultFileContent("FirstHandlerEndpointHandler.java"));
    }
}
