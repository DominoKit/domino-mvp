package com.progressoft.brix.domino.apt.commons;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JavaSourceBuilderTest {

    @Test
    public void callingBuildForNewJavaSourceWriter_whenCallBuild_thenShouldGenerateEmptyDefaultClass()
            throws Exception {
        assertEquals("class Test{\n}", new JavaSourceBuilder("Test").build());
    }

    @Test
    public void givenJavaSourceWriterAndPackage_whenCallBuild_thenShouldGenerateClassOnThatPackage() throws Exception {
        String resultClass =
                "package com.xxx.yyy.zzz;\n\n"
                        + "class Test{\n}";
        assertEquals(resultClass, new JavaSourceBuilder("Test").onPackage("com.xxx.yyy.zzz").build());
    }

    @Test
    public void givenJavaSourceWriterGivenAnImportedClass_whenCallBuild_thenShouldGenerateClassWithImport()
            throws Exception {
        String resultClass =
                "package com.xxx.yyy.zzz;\n\n"
                        + "import com.ccc.uuu.aaa;\n\n"
                        + "class Test{\n}";
        assertEquals(resultClass,
                new JavaSourceBuilder("Test").onPackage("com.xxx.yyy.zzz").imports("com.ccc.uuu.aaa").build());
    }

    @Test
    public void givenJavaSourceWriterGivenManyImportedClasses_whenCallBuild_thenShouldGenerateClassWithImports()
            throws Exception {
        String resultClass =
                "package com.xxx.yyy.zzz;\n\n"
                        + "import com.ccc.uuu.aaa;\n"
                        + "import com.aaa.bbb.ccc;\n"
                        + "import com.ddd.eee.fff;\n\n"
                        + "class Test{\n}";
        assertEquals(resultClass,
                new JavaSourceBuilder("Test")
                        .onPackage("com.xxx.yyy.zzz")
                        .imports("com.ccc.uuu.aaa")
                        .imports("com.aaa.bbb.ccc")
                        .imports("com.ddd.eee.fff")
                        .build());
    }

    @Test
    public void givenJavaSourceWriterGivenManyImportedClassesAndAClassModifier_whenCallBuild_thenShouldGenerateClassWithImportsAndModifier()
            throws Exception {
        String publicClass =
                "package com.xxx.yyy.zzz;\n\n"
                        + "import com.ccc.uuu.aaa;\n"
                        + "import com.aaa.bbb.ccc;\n"
                        + "import com.ddd.eee.fff;\n\n"
                        + "public class Test{\n}";
        assertEquals(publicClass,
                new JavaSourceBuilder("Test")
                        .onPackage("com.xxx.yyy.zzz")
                        .withModifiers(new ModifierBuilder().asPublic())
                        .imports("com.ccc.uuu.aaa")
                        .imports("com.aaa.bbb.ccc")
                        .imports("com.ddd.eee.fff")
                        .build());

        String publicStaticClass =
                "package com.xxx.yyy.zzz;\n\n"
                        + "import com.ccc.uuu.aaa;\n"
                        + "import com.aaa.bbb.ccc;\n"
                        + "import com.ddd.eee.fff;\n\n"
                        + "public static class Test{\n}";
        assertEquals(publicStaticClass,
                new JavaSourceBuilder("Test")
                        .onPackage("com.xxx.yyy.zzz")
                        .withModifiers(new ModifierBuilder().asPublic().get().staticModifier())
                        .imports("com.ccc.uuu.aaa")
                        .imports("com.aaa.bbb.ccc")
                        .imports("com.ddd.eee.fff")
                        .build());

        String protectedStaticFinalClass =
                "package com.xxx.yyy.zzz;\n\n"
                        + "import com.ccc.uuu.aaa;\n"
                        + "import com.aaa.bbb.ccc;\n"
                        + "import com.ddd.eee.fff;\n\n"
                        + "protected static final class Test{\n}";
        assertEquals(protectedStaticFinalClass,
                new JavaSourceBuilder("Test")
                        .onPackage("com.xxx.yyy.zzz")
                        .withModifiers(new ModifierBuilder().asProtected().get().staticModifier().finalModifier())
                        .imports("com.ccc.uuu.aaa")
                        .imports("com.aaa.bbb.ccc")
                        .imports("com.ddd.eee.fff")
                        .build());

        String abstractClass =
                "package com.xxx.yyy.zzz;\n\n"
                        + "import com.ccc.uuu.aaa;\n"
                        + "import com.aaa.bbb.ccc;\n"
                        + "import com.ddd.eee.fff;\n\n"
                        + "abstract class Test{\n}";
        assertEquals(abstractClass,
                new JavaSourceBuilder("Test")
                        .onPackage("com.xxx.yyy.zzz")
                        .withModifiers(new ModifierBuilder().get().asAbstract())
                        .imports("com.ccc.uuu.aaa")
                        .imports("com.aaa.bbb.ccc")
                        .imports("com.ddd.eee.fff")
                        .build());

        String publicAbstractClass =
                "package com.xxx.yyy.zzz;\n\n"
                        + "import com.ccc.uuu.aaa;\n"
                        + "import com.aaa.bbb.ccc;\n"
                        + "import com.ddd.eee.fff;\n\n"
                        + "public abstract class Test{\n}";
        assertEquals(publicAbstractClass,
                new JavaSourceBuilder("Test")
                        .onPackage("com.xxx.yyy.zzz")
                        .withModifiers(new ModifierBuilder().asPublic().asAbstract())
                        .imports("com.ccc.uuu.aaa")
                        .imports("com.aaa.bbb.ccc")
                        .imports("com.ddd.eee.fff")
                        .build());
    }

    @Test
    public void givenJavaSourceWriter_whenExtendsAClass_whenCallBuild_thenWillGenerateClassWithExtends()
            throws Exception {
        String extendsClass =
                "package com.xxx.yyy.zzz;\n\n"
                        + "import com.ccc.uuu.aaa;\n"
                        + "import com.aaa.bbb.ccc;\n"
                        + "import com.ddd.eee.fff;\n"
                        + "import com.ggg.hhh.iii.Another;\n\n"
                        + "public class Test extends Another{\n}";
        assertEquals(extendsClass,
                new JavaSourceBuilder("Test")
                        .onPackage("com.xxx.yyy.zzz")
                        .withModifiers(new ModifierBuilder().asPublic())
                        .imports("com.ccc.uuu.aaa")
                        .imports("com.aaa.bbb.ccc")
                        .imports("com.ddd.eee.fff")
                        .extend("com.ggg.hhh.iii.Another")
                        .build());

        String extendsClassWithSingleGeneric =
                "package com.xxx.yyy.zzz;\n\n"
                        + "import com.ccc.uuu.aaa;\n"
                        + "import com.aaa.bbb.ccc;\n"
                        + "import com.ddd.eee.fff;\n"
                        + "import com.ggg.hhh.iii.Another;\n"
                        + "import kkk.lll.mmm.G;\n\n"
                        + "public class Test extends Another<G>{\n}";
        assertEquals(extendsClassWithSingleGeneric,
                new JavaSourceBuilder("Test")
                        .onPackage("com.xxx.yyy.zzz")
                        .withModifiers(new ModifierBuilder().asPublic())
                        .imports("com.ccc.uuu.aaa")
                        .imports("com.aaa.bbb.ccc")
                        .imports("com.ddd.eee.fff")
                        .extend("com.ggg.hhh.iii.Another<kkk.lll.mmm.G>")
                        .build());


        String extendsClassWithMultiGeneric =
                "package com.xxx.yyy.zzz;\n\n"
                        + "import com.ccc.uuu.aaa;\n"
                        + "import com.aaa.bbb.ccc;\n"
                        + "import com.ddd.eee.fff;\n"
                        + "import com.ggg.hhh.iii.Another;\n"
                        + "import kkk.lll.mmm.G;\n"
                        + "import nnn.ooo.ppp.N;\n\n"
                        + "public class Test extends Another<G,N>{\n}";
        assertEquals(extendsClassWithMultiGeneric,
                new JavaSourceBuilder("Test")
                        .onPackage("com.xxx.yyy.zzz")
                        .withModifiers(new ModifierBuilder().asPublic())
                        .imports("com.ccc.uuu.aaa")
                        .imports("com.aaa.bbb.ccc")
                        .imports("com.ddd.eee.fff")
                        .extend("com.ggg.hhh.iii.Another<kkk.lll.mmm.G, nnn.ooo.ppp.N>")
                        .build());
    }

    @Test(expected = JavaSourceBuilder.CouldNotExtendsMoreThanOneClass.class)
    public void givenJavaSourceWriter_whenExtendingMoreThanOnce_thenShouldThrowException() throws Exception {
        new JavaSourceBuilder("Test")
                .onPackage("com.xxx.yyy.zzz")
                .withModifiers(new ModifierBuilder().asPublic())
                .extend("aaa.bbb.ccc.A")
                .extend("ddd.eee.fff.B");

    }

    @Test
    public void givenJavaSourceWriter_whenImplementsInterface_whenCallBuild_thenWillGenerateClassWithImplements()
            throws Exception {
        String implementsClass =
                "package com.xxx.yyy.zzz;\n\n"
                        + "import com.ccc.uuu.aaa;\n"
                        + "import com.aaa.bbb.ccc;\n"
                        + "import com.ddd.eee.fff;\n"
                        + "import com.ggg.hhh.iii.Another;\n\n"
                        + "public class Test implements Another{\n}";
        assertEquals(implementsClass,
                new JavaSourceBuilder("Test")
                        .onPackage("com.xxx.yyy.zzz")
                        .withModifiers(new ModifierBuilder().asPublic())
                        .imports("com.ccc.uuu.aaa")
                        .imports("com.aaa.bbb.ccc")
                        .imports("com.ddd.eee.fff")
                        .implement("com.ggg.hhh.iii.Another")
                        .build());

        String implementsClassWithSingleGeneric =
                "package com.xxx.yyy.zzz;\n\n"
                        + "import com.ccc.uuu.aaa;\n"
                        + "import com.aaa.bbb.ccc;\n"
                        + "import com.ddd.eee.fff;\n"
                        + "import com.ggg.hhh.iii.Another;\n"
                        + "import kkk.lll.mmm.G;\n\n"
                        + "public class Test implements Another<G>{\n}";
        assertEquals(implementsClassWithSingleGeneric,
                new JavaSourceBuilder("Test")
                        .onPackage("com.xxx.yyy.zzz")
                        .withModifiers(new ModifierBuilder().asPublic())
                        .imports("com.ccc.uuu.aaa")
                        .imports("com.aaa.bbb.ccc")
                        .imports("com.ddd.eee.fff")
                        .implement("com.ggg.hhh.iii.Another<kkk.lll.mmm.G>")
                        .build());


        String implementsClassWithMultiGeneric =
                "package com.xxx.yyy.zzz;\n\n"
                        + "import com.ccc.uuu.aaa;\n"
                        + "import com.aaa.bbb.ccc;\n"
                        + "import com.ddd.eee.fff;\n"
                        + "import com.ggg.hhh.iii.Another;\n"
                        + "import kkk.lll.mmm.G;\n"
                        + "import nnn.ooo.ppp.N;\n\n"
                        + "public class Test implements Another<G,N>{\n}";
        assertEquals(implementsClassWithMultiGeneric,
                new JavaSourceBuilder("Test")
                        .onPackage("com.xxx.yyy.zzz")
                        .withModifiers(new ModifierBuilder().asPublic())
                        .imports("com.ccc.uuu.aaa")
                        .imports("com.aaa.bbb.ccc")
                        .imports("com.ddd.eee.fff")
                        .implement("com.ggg.hhh.iii.Another<kkk.lll.mmm.G, nnn.ooo.ppp.N>")
                        .build());

        String implementsManyClassWithMultiGeneric =
                "package com.xxx.yyy.zzz;\n\n"
                        + "import com.ccc.uuu.aaa;\n"
                        + "import com.aaa.bbb.ccc;\n"
                        + "import com.ddd.eee.fff;\n"
                        + "import com.ggg.hhh.iii.Another;\n"
                        + "import kkk.lll.mmm.G;\n"
                        + "import nnn.ooo.ppp.N;\n"
                        + "import aaa.bbb.ccc.Another2;\n"
                        + "import qqq.rrr.sss.S;\n"
                        + "import ttt.uuu.yyy.Y;\n\n"
                        + "public class Test implements Another<G,N>, Another2<S,Y>{\n}";
        assertEquals(implementsManyClassWithMultiGeneric,
                new JavaSourceBuilder("Test")
                        .onPackage("com.xxx.yyy.zzz")
                        .withModifiers(new ModifierBuilder().asPublic())
                        .imports("com.ccc.uuu.aaa")
                        .imports("com.aaa.bbb.ccc")
                        .imports("com.ddd.eee.fff")
                        .implement("com.ggg.hhh.iii.Another<kkk.lll.mmm.G, nnn.ooo.ppp.N>")
                        .implement("aaa.bbb.ccc.Another2<qqq.rrr.sss.S, ttt.uuu.yyy.Y>")
                        .build());
    }

    @Test
    public void givenJavaSourceWriter_whenAnnotateClass_thenShouldGenerateClassWithAnnotations() throws Exception {
        String annotatedClass =
                        "package com.xxx.yyy.zzz;\n\n"
                        + "@SomeAnnotation(value=\"somevalue\")\n"
                        + "public class Test{\n}";
        assertEquals(annotatedClass,
                new JavaSourceBuilder("Test")
                        .onPackage("com.xxx.yyy.zzz")
                        .withModifiers(new ModifierBuilder().asPublic())
                        .annotate("@SomeAnnotation(value=\"somevalue\")")
                        .build());
    }

    @Test
    public void givenJavaSourceWriter_whenAddingMethod_thenShouldGenerateClassWithMethod() throws Exception {
        String emptyVoidMethod =
                "package com.xxx.yyy.zzz;\n\n"

                        + "public class Test{\n"
                        +"\n\tpublic void someMethod(){\n\n\t}\n"
                        + "}";
        assertEquals(emptyVoidMethod,
                new JavaSourceBuilder("Test")
                        .onPackage("com.xxx.yyy.zzz")
                        .withModifiers(new ModifierBuilder().asPublic())
                        .method("someMethod")
                            .withModifier(new ModifierBuilder().asPublic())
                            .returns("void")
                            .end()
                        .build());

        String emptyWithReturnTypeMethod =
                "package com.xxx.yyy.zzz;\n\n"
                        + "import aaa.bbb.ccc.SomeReturnType;\n\n"
                        + "public class Test{\n"
                        +"\n\tprivate SomeReturnType someMethod(){\n\n\t}\n"
                        + "}";
        assertEquals(emptyWithReturnTypeMethod,
                new JavaSourceBuilder("Test")
                        .onPackage("com.xxx.yyy.zzz")
                        .withModifiers(new ModifierBuilder().asPublic())
                        .method("someMethod")
                        .withModifier(new ModifierBuilder().asPrivate())
                        .returns("aaa.bbb.ccc.SomeReturnType")
                        .end()
                        .build());

        String emptyWithGenericReturnTypeMethod =
                "package com.xxx.yyy.zzz;\n\n"
                        + "import aaa.bbb.ccc.SomeReturnType;\n"
                        + "import aaa.bbb.ccc.A;\n"
                        + "import ddd.eee.fff.B;\n\n"
                        + "public class Test{\n"
                        +"\n\tprotected SomeReturnType<A,B> someMethod(){\n\n\t}\n"
                        + "}";
        assertEquals(emptyWithGenericReturnTypeMethod,
                new JavaSourceBuilder("Test")
                        .onPackage("com.xxx.yyy.zzz")
                        .withModifiers(new ModifierBuilder().asPublic())
                        .method("someMethod")
                        .withModifier(new ModifierBuilder().asProtected())
                        .returns("aaa.bbb.ccc.SomeReturnType<aaa.bbb.ccc.A,ddd.eee.fff.B>")
                        .end()
                        .build());

        String emptyWithGenericReturnTypeAndStringParameterMethod =
                "package com.xxx.yyy.zzz;\n\n"
                        + "import aaa.bbb.ccc.SomeReturnType;\n"
                        + "import aaa.bbb.ccc.A;\n"
                        + "import ddd.eee.fff.B;\n\n"
                        + "public class Test{\n"
                        +"\n\tSomeReturnType<A,B> someMethod(String someParameter){\n\n\t}\n"
                        + "}";
        assertEquals(emptyWithGenericReturnTypeAndStringParameterMethod,
                new JavaSourceBuilder("Test")
                        .onPackage("com.xxx.yyy.zzz")
                        .withModifiers(new ModifierBuilder().asPublic())
                        .method("someMethod")
                        .withModifier(new ModifierBuilder().get())
                        .takes("String", "someParameter")
                        .returns("aaa.bbb.ccc.SomeReturnType<aaa.bbb.ccc.A,ddd.eee.fff.B>")
                        .end()
                        .build());

        String emptyWithGenericReturnTypeAndTwoParametersMethod =
                "package com.xxx.yyy.zzz;\n\n"
                        + "import ggg.hhh.iii.C;\n"
                        + "import aaa.bbb.ccc.SomeReturnType;\n"
                        + "import aaa.bbb.ccc.A;\n"
                        + "import ddd.eee.fff.B;\n\n"
                        + "public class Test{\n"
                        + "\n\t@Override"
                        +"\n\tpublic SomeReturnType<A,B> someMethod(String someParameter, C objectParameter){\n\n\t}\n"
                        + "}";
        assertEquals(emptyWithGenericReturnTypeAndTwoParametersMethod,
                new JavaSourceBuilder("Test")
                        .onPackage("com.xxx.yyy.zzz")
                        .withModifiers(new ModifierBuilder().asPublic())
                        .method("someMethod")
                        .annotate("@Override")
                        .withModifier(new ModifierBuilder().asPublic())
                        .takes("String", "someParameter")
                        .takes("ggg.hhh.iii.C", "objectParameter")
                        .returns("aaa.bbb.ccc.SomeReturnType<aaa.bbb.ccc.A,ddd.eee.fff.B>")
                        .end()
                        .build());

        String singleLineMethod =
                "package com.xxx.yyy.zzz;\n\n"
                        + "import ggg.hhh.iii.C;\n"
                        + "import aaa.bbb.ccc.SomeReturnType;\n"
                        + "import aaa.bbb.ccc.A;\n"
                        + "import ddd.eee.fff.B;\n\n"
                        + "public class Test{\n"
                        + "\n\t@Override"
                        + "\n\tpublic SomeReturnType<A,B> someMethod(String someParameter, C objectParameter){"
                        + "\n\n\t\tSomeReturnType<A,B> value=new SomeReturnType<A,B>();"
                        + "\n\t\treturn value;"
                        + "\n\t}\n"
                        + "}";
        assertEquals(singleLineMethod,
                new JavaSourceBuilder("Test")
                        .onPackage("com.xxx.yyy.zzz")
                        .withModifiers(new ModifierBuilder().asPublic())
                        .method("someMethod")
                        .annotate("@Override")
                        .withModifier(new ModifierBuilder().asPublic())
                        .takes("String", "someParameter")
                        .takes("ggg.hhh.iii.C", "objectParameter")
                        .block("SomeReturnType<A,B> value=new SomeReturnType<A,B>();")
                        .block("return value;")
                        .returns("aaa.bbb.ccc.SomeReturnType<aaa.bbb.ccc.A,ddd.eee.fff.B>")
                        .end()
                        .build());
    }
}
