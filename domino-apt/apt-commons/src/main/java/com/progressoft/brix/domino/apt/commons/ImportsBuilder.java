package com.progressoft.brix.domino.apt.commons;

import java.util.LinkedList;
import java.util.List;

public class ImportsBuilder {

    private final List<String> imports=new LinkedList<>();

    public void addImport(String fullClassName){
        if(!imports.contains(fullClassName.trim()) && fullClassName.contains("."))
            imports.add(fullClassName.trim());
    }

    public String writeImports(){
        StringBuilder sb=new StringBuilder();
        imports.forEach(i-> sb.append("import ").append(i).append(";\n"));
        return sb.toString();
    }

    public boolean hasImports(){
        return !imports.isEmpty();
    }
}
