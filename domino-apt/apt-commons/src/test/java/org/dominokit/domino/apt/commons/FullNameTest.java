package org.dominokit.domino.apt.commons;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class FullNameTest {

    @Test(expected = FullClassName.InvalidClassName.class)
    public void creatingFullClassNameWithNull_shouldThrowException() throws Exception {
        new FullClassName(null);
    }

    @Test(expected = FullClassName.InvalidClassName.class)
    public void creatingFullClassNameWithEmptyString_shouldThrowException() throws Exception {
        new FullClassName("");
    }

    @Test(expected = FullClassName.InvalidClassName.class)
    public void creatingFullClassNameWithSpacesOnly_shouldThrowException() throws Exception {
        new FullClassName("     ");
    }

    @Test
    public void gettingSimpleClassNameFromClassFullName() throws Exception {
        assertEquals("Test", new FullClassName("aaa.bbb.ccc.Test").asSimpleName());
        assertEquals("Test", new FullClassName("Test").asSimpleName());
    }

    @Test
    public void gettingPackageFromClassFullName() throws Exception {
        assertEquals("aaa.bbb.ccc", new FullClassName("aaa.bbb.ccc.Test").asPackage());
        assertEquals("", new FullClassName("Test").asPackage());
    }

    @Test
    public void gettingImportFromClassFullName() throws Exception {
        assertEquals("aaa.bbb.ccc.Test", new FullClassName("aaa.bbb.ccc.Test").asImport());
        assertEquals("Test", new FullClassName("Test").asImport());
    }

    @Test
    public void gettingSimpleClassNameFromClassFullNameWithSingleGeneric() throws Exception {
        assertEquals("Test", new FullClassName("aaa.bbb.ccc.Test<ddd.eee.fff.A>").asSimpleName());
        assertEquals("Test", new FullClassName("Test<ddd.eee.fff.A>").asSimpleName());
        assertEquals("Test", new FullClassName("Test<A>").asSimpleName());
    }

    @Test
    public void gettingPackageFromClassFullNameWithSingleGeneric() throws Exception {
        assertEquals("aaa.bbb.ccc", new FullClassName("aaa.bbb.ccc.Test<ddd.eee.fff.A>").asPackage());
        assertEquals("aaa.bbb.ccc", new FullClassName("aaa.bbb.ccc.Test<A>").asPackage());
        assertEquals("", new FullClassName("Test<ddd.eee.fff.A>").asPackage());
        assertEquals("", new FullClassName("Test<A>").asPackage());
    }

    @Test
    public void gettingImportFromClassFullNameWithSingleGeneric() throws Exception {
        assertEquals("aaa.bbb.ccc.Test", new FullClassName("aaa.bbb.ccc.Test<ddd.eee.fff.A>").asImport());
        assertEquals("aaa.bbb.ccc.Test", new FullClassName("aaa.bbb.ccc.Test<A>").asImport());
        assertEquals("Test", new FullClassName("Test<ddd.eee.fff.A>").asImport());
        assertEquals("Test", new FullClassName("Test<A>").asImport());
    }

    @Test
    public void gettingSimpleGenericClassNameFromClassFullNameWithSingleGeneric() throws Exception {
        assertEquals("Test<A>", new FullClassName("aaa.bbb.ccc.Test<ddd.eee.fff.A>").asSimpleGenericName());
        assertEquals("Test<A>", new FullClassName("Test<ddd.eee.fff.A>").asSimpleGenericName());
        assertEquals("Test<A>", new FullClassName("Test<A>").asSimpleGenericName());
    }

    @Test
    public void gettingAllImportsFromClassFullNameWithSingleGeneric() throws Exception {
        String expected_1 = "aaa.bbb.ccc.Test\n" + "ddd.eee.fff.A\n";
        assertEquals(expected_1, importsAsOneString(new FullClassName("aaa.bbb.ccc.Test<ddd.eee.fff.A>").allImports()));

        String expected_2="aaa.bbb.ccc.Test\n" +"A\n";
        assertEquals(expected_2, importsAsOneString(new FullClassName("aaa.bbb.ccc.Test<A>").allImports()));

        String expected_3="Test\n"+ "ddd.eee.fff.A\n";
        assertEquals(expected_3, importsAsOneString(new FullClassName("Test<ddd.eee.fff.A>").allImports()));

        String expected_4="Test\n" +"A\n";
        assertEquals(expected_4, importsAsOneString(new FullClassName("Test<A>").allImports()));
    }

    @Test
    public void gettingSimpleClassNameFromClassFullNameWithMultiGeneric() throws Exception {
        assertEquals("Test", new FullClassName("aaa.bbb.ccc.Test<ddd.eee.fff.A,ggg.hhh.iii.B>").asSimpleName());
        assertEquals("Test", new FullClassName("Test<ddd.eee.fff.A,ggg.hhh.iii.B>").asSimpleName());
        assertEquals("Test", new FullClassName("Test<A,B>").asSimpleName());
    }

    @Test
    public void gettingPackageFromClassFullNameWithMultiGeneric() throws Exception {
        assertEquals("aaa.bbb.ccc", new FullClassName("aaa.bbb.ccc.Test<ddd.eee.fff.A,ggg.hhh.iii.B>").asPackage());
        assertEquals("aaa.bbb.ccc", new FullClassName("aaa.bbb.ccc.Test<A,B>").asPackage());
        assertEquals("", new FullClassName("Test<ddd.eee.fff.A,ggg.hhh.iii.B>").asPackage());
        assertEquals("", new FullClassName("Test<A,B>").asPackage());
    }

    @Test
    public void gettingImportFromClassFullNameWithMultiGeneric() throws Exception {
        assertEquals("aaa.bbb.ccc.Test", new FullClassName("aaa.bbb.ccc.Test<ddd.eee.fff.A,ggg.hhh.iii.B>").asImport());
        assertEquals("aaa.bbb.ccc.Test", new FullClassName("aaa.bbb.ccc.Test<A,B>").asImport());
        assertEquals("Test", new FullClassName("Test<ddd.eee.fff.A,ggg.hhh.iii.B>").asImport());
        assertEquals("Test", new FullClassName("Test<A,B>").asImport());
    }

    @Test
    public void gettingSimpleGenericClassNameFromClassFullNameWithMultiGeneric() throws Exception {
        assertEquals("Test<A,B>", new FullClassName("aaa.bbb.ccc.Test<ddd.eee.fff.A,ggg.hhh.iii.B>").asSimpleGenericName());
        assertEquals("Test<A,B>", new FullClassName("Test<ddd.eee.fff.A,ggg.hhh.iii.B>").asSimpleGenericName());
        assertEquals("Test<A,B>", new FullClassName("Test<A,B>").asSimpleGenericName());
    }

    @Test
    public void gettingAllImportsFromClassFullNameWithMultiGeneric() throws Exception {
        String expected_1 = "aaa.bbb.ccc.Test\n" + "ddd.eee.fff.A\n"+"ggg.hhh.iii.B\n";
        assertEquals(expected_1, importsAsOneString(new FullClassName("aaa.bbb.ccc.Test<ddd.eee.fff.A,ggg.hhh.iii.B>").allImports()));

        String expected_2="aaa.bbb.ccc.Test\n" +"A\n"+"B\n";
        assertEquals(expected_2, importsAsOneString(new FullClassName("aaa.bbb.ccc.Test<A,B>").allImports()));

        String expected_3="Test\n"+ "ddd.eee.fff.A\n"+"ggg.hhh.iii.B\n";
        assertEquals(expected_3, importsAsOneString(new FullClassName("Test<ddd.eee.fff.A,ggg.hhh.iii.B>").allImports()));

        String expected_4="Test\n" +"A\n"+"B\n";
        assertEquals(expected_4, importsAsOneString(new FullClassName("Test<A,B>").allImports()));

        assertEquals(expected_3, importsAsOneString(new FullClassName("Test<ddd.eee.fff.A,ggg.hhh.iii.B,ggg.hhh.iii.B>").allImports()));

    }

    private String importsAsOneString(List<String> imports) {
        StringBuilder sb = new StringBuilder();
        imports.forEach(s -> sb.append(s).append("\n"));
        return sb.toString();
    }

}
