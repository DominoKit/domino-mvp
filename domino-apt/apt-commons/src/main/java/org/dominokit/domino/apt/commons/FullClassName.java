package org.dominokit.domino.apt.commons;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

public class FullClassName {
    private final String completeClassName;

    public FullClassName(String completeClassName) {
        if(Objects.isNull(completeClassName) || completeClassName.trim().isEmpty())
            throw new InvalidClassName();
        this.completeClassName = completeClassName.trim().replace(" ","");
    }

    public String asSimpleName() {
        return getSimpleName(simpleFullName());
    }

    private String getSimpleName(String name) {
        return name.substring(name.lastIndexOf('.')+1, name.length());
    }

    public String asPackage() {
        return getPackage(simpleFullName());
    }

    private String getPackage(String name) {
        return name.substring(0, name.lastIndexOf('.')>-1?name.lastIndexOf('.'):0);
    }

    private String simpleFullName() {
        return new StringTokenizer(completeClassName, "<,>").nextToken();
    }

    public String asImport() {
        return simpleFullName();
    }

    public String asSimpleGenericName() {
        String result=this.completeClassName;
        StringTokenizer st=new StringTokenizer(completeClassName, "<,>");
        while(st.hasMoreTokens())
            result=result.replace(appendDot(new FullClassName(st.nextToken()).asPackage()),"");
        return result;
    }

    private String appendDot(String part) {
        return part.isEmpty()?part: dottedPart(part);
    }

    private String dottedPart(String part) {
        return part.endsWith(".")?part:(part+".");
    }

    public List<String> allImports() {
        StringTokenizer st=new StringTokenizer(completeClassName, "<,>");
        List<String> imports=new LinkedList<>();
        while (st.hasMoreTokens()){
            String s=new FullClassName(st.nextToken()).asImport();
            if(!imports.contains(s))
                imports.add(s);
        }
        return imports;
    }

    public class InvalidClassName extends RuntimeException{
    }
}
