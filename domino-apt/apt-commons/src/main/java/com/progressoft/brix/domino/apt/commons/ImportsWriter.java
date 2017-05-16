package com.progressoft.brix.domino.apt.commons;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class ImportsWriter {

    private final Set<String> imports=new HashSet<>();

    public ImportsWriter addImport(String importItem){
        if(Objects.nonNull(importItem) && !"".equals(importItem))
            imports.add(importItem.replace("[]",""));
        return this;
    }

    public ImportsWriter addAll(Stream<String> importItems){
        importItems.forEach(this::addImport);
        return this;
    }

    public Stream<String> allImports(){
        return imports.stream();
    }

    public String asImportsString(){
        StringBuilder sb=new StringBuilder();
        imports.forEach(importItm -> sb.append(importItm).append("\n"));
        return sb.toString();
    }
}
