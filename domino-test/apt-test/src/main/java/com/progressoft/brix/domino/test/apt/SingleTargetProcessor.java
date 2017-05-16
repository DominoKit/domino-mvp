package com.progressoft.brix.domino.test.apt;

import com.google.common.truth.Truth;
import com.google.testing.compile.CompileTester;
import com.google.testing.compile.JavaFileObjects;

import javax.annotation.processing.Processor;

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class SingleTargetProcessor implements BaseTargetProcessor {

    private final String inputClassName;
    private final Processor processor;

    public SingleTargetProcessor(String inputClassName, Processor processor) {
        this.inputClassName = inputClassName;
        this.processor = processor;
    }

    @Override
    public CompileTester.SuccessfulCompilationClause compilesWithoutErrors() {
        return Truth.assert_().about(javaSource()).that(JavaFileObjects.forResource(inputClassName))
                .processedWith(processor).compilesWithoutError();
    }

    @Override
    public CompileTester.UnsuccessfulCompilationClause failsToCompile() {
        return Truth.assert_().about(javaSource()).that(JavaFileObjects.forResource(inputClassName))
                .processedWith(processor).failsToCompile();
    }

}
