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
}
