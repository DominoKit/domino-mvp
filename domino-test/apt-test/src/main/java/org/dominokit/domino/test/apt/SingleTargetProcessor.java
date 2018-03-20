package org.dominokit.domino.test.apt;

import com.google.common.truth.Truth;
import com.google.testing.compile.CompileTester;
import com.google.testing.compile.JavaFileObjects;

import javax.annotation.processing.Processor;

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class SingleTargetProcessor implements BaseTargetProcessor {

    private final String inputClassName;
    private final Processor processor;
    private final Processor[] rest;

    public SingleTargetProcessor(String inputClassName, Processor processor) {
        this.inputClassName = inputClassName;
        this.processor = processor;
        this.rest=new Processor[]{};
    }

    public SingleTargetProcessor(String inputClassName, Processor processor, Processor[] rest) {

        this.inputClassName = inputClassName;
        this.processor = processor;
        this.rest = rest;
    }

    @Override
    public CompileTester.SuccessfulCompilationClause compilesWithoutErrors() {
        return Truth.assert_().about(javaSource()).that(JavaFileObjects.forResource(inputClassName))
                .processedWith(processor, rest).compilesWithoutError();
    }

    @Override
    public CompileTester.UnsuccessfulCompilationClause failsToCompile() {
        return Truth.assert_().about(javaSource()).that(JavaFileObjects.forResource(inputClassName))
                .processedWith(processor, rest).failsToCompile();
    }

}
