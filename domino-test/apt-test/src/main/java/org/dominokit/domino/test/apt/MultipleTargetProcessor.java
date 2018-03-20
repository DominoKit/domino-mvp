package org.dominokit.domino.test.apt;

import com.google.common.truth.Truth;
import com.google.testing.compile.CompileTester;
import com.google.testing.compile.JavaFileObjects;

import javax.annotation.processing.Processor;
import javax.tools.JavaFileObject;
import java.util.Arrays;

import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

public class MultipleTargetProcessor implements BaseTargetProcessor {

    private final String[] inputClassesNames;
    private final Processor processor;
    private final Processor[] rest;

    public MultipleTargetProcessor(String[] inputClassesNames, Processor processor) {
        this.inputClassesNames = inputClassesNames;
        this.processor = processor;
        this.rest=new Processor[]{};
    }

    public MultipleTargetProcessor(String[] inputClassesNames, Processor processor, Processor[] rest) {

        this.inputClassesNames = inputClassesNames;
        this.processor = processor;
        this.rest = rest;
    }

    @Override
    public CompileTester.SuccessfulCompilationClause compilesWithoutErrors() {
        return Truth.assert_().about(javaSources()).that(Arrays.asList(asJavaFileObjectsArray()))
                .processedWith(processor, rest).compilesWithoutError();
    }

    @Override
    public CompileTester.UnsuccessfulCompilationClause failsToCompile() {
        return Truth.assert_().about(javaSources()).that(Arrays.asList(asJavaFileObjectsArray()))
                .processedWith(processor, rest).failsToCompile();
    }

    private JavaFileObject[] asJavaFileObjectsArray() {
        JavaFileObject[] result = new JavaFileObject[inputClassesNames.length];
        for (int i = 0; i < inputClassesNames.length; i++)
            result[i] = JavaFileObjects.forResource(inputClassesNames[i]);
        return result;
    }
}
