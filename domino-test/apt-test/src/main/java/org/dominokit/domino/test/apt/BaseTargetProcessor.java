package org.dominokit.domino.test.apt;

import com.google.testing.compile.CompileTester;
import com.google.testing.compile.JavaFileObjects;

import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.nio.charset.Charset;

public interface BaseTargetProcessor {

    default void generates(String result, String... rest) {
        compilesWithoutErrors()
                .and()
                .generatesSources(JavaFileObjects.forSourceString("", result), asJavaFileObjectsArray(rest));
    }

    default JavaFileObject[] asJavaFileObjectsArray(String... rest) {
        JavaFileObject[] result = new JavaFileObject[rest.length];
        for (int i = 0; i < rest.length; i++)
            result[i] = JavaFileObjects.forSourceString("", rest[i]);
        return result;
    }

    CompileTester.SuccessfulCompilationClause compilesWithoutErrors();
    CompileTester.UnsuccessfulCompilationClause failsToCompile();

    default void generatesResource(String basePackage, String fileName, String content) {
        compilesWithoutErrors()
                .and()
                .generatesFileNamed(StandardLocation.SOURCE_OUTPUT, basePackage, fileName).withStringContents(Charset.defaultCharset(), content);
    }
}
