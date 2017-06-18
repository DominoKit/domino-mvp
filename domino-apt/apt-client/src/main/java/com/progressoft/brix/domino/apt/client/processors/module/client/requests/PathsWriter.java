package com.progressoft.brix.domino.apt.client.processors.module.client.requests;

import com.progressoft.brix.domino.api.client.history.PathToRequestMapperRegistry;
import com.progressoft.brix.domino.api.client.history.RequestFromPath;
import com.progressoft.brix.domino.api.client.history.TokenizedPath;
import com.progressoft.brix.domino.apt.commons.FullClassName;
import com.progressoft.brix.domino.apt.commons.JavaSourceBuilder;
import com.progressoft.brix.domino.apt.commons.MethodBuilder;
import com.progressoft.brix.domino.apt.commons.ModifierBuilder;
import com.progressoft.brix.domino.api.client.request.Request;

import java.util.*;
import java.util.stream.Collectors;

public class PathsWriter {

    private final JavaSourceBuilder sourceBuilder;

    public PathsWriter(JavaSourceBuilder sourceBuilder) {
        this.sourceBuilder = sourceBuilder;
    }

    public void write(Set<String> paths){

        if(!paths.isEmpty()){
            sourceBuilder.imports(PathToRequestMapperRegistry.class.getCanonicalName())
            .imports(RequestFromPath.class.getCanonicalName())
            .imports(TokenizedPath.class.getCanonicalName())
            .imports(Request.class.getCanonicalName());

            MethodBuilder methodBuilder = this.sourceBuilder.method("registerPathMappers");
            methodBuilder.annotate("@Override")
                    .withModifier(new ModifierBuilder().asPublic())
                    .returnsVoid()
                    .takes("PathToRequestMapperRegistry", "registry");
            paths.stream().map(this::parseEntry)
                    .forEach(e-> registerPath(e, methodBuilder));
            methodBuilder.end();
        }
    }

    private void registerPath(PathEntry e, MethodBuilder methodBuilder) {
        importPath(e);
        FullClassName request=new FullClassName(e.request);

        if("_".equals(e.mapper))
            writeDefaultMapper(e, methodBuilder, request);
        else
            writeCustomMapper(e, methodBuilder);

    }

    private void writeDefaultMapper(PathEntry e, MethodBuilder methodBuilder, FullClassName request) {
        methodBuilder.block("registry.registerMapper(\""+e.path+"\", new RequestFromPath() {\n" +
                "                @Override\n" +
                "                public Request buildRequest(TokenizedPath path) {\n" +
                "                    return new "+request.asSimpleName()+"("+appendParameters(e.parameters)+");\n" +
                "                }\n" +
                "            });");
    }

    private String appendParameters(Set<Parameter> parameters) {
        StringBuilder builder=new StringBuilder();
        parameters.stream().forEach(p->{
            FullClassName converter=new FullClassName(p.converter);
            sourceBuilder.imports(p.converter);
            builder.append("new ").append(converter.asSimpleName()).append("().convert(path.getParameter(\"").append(p.name).append("\")),");
        });

        String result=builder.toString();
        if(result.endsWith(","))
            result=result.substring(0, result.length()-1).replace(",", ",\n\t\t\t\t\t\t\t\t\t\t\t\t");
        return result;
    }

    private void writeCustomMapper(PathEntry e, MethodBuilder methodBuilder) {
        sourceBuilder.imports(e.mapper);
        FullClassName mapper=new FullClassName(e.mapper);
        methodBuilder.block("registry.registerMapper(\"somePath\", new "+mapper.asSimpleName()+"());");
    }

    private void importPath(PathEntry e) {
        sourceBuilder.imports(e.request);
    }

    private PathEntry parseEntry(String requestPath) {
        String[] pathValues=requestPath.split(":");
        return new PathEntry(pathValues[0], pathValues[1], pathValues[2], parseParameters(pathValues[3]));
    }

    private Set<Parameter> parseParameters(String parametersString){
        if("_".equals(parametersString))
            return Collections.EMPTY_SET;
        return Arrays.asList(parametersString.split(",")).stream().map(this::parseParameter).collect(Collectors.toSet());
    }

    private Parameter parseParameter(String parameterPair) {
        String[] parsedParameter=parameterPair.split(";");
        return new Parameter(parsedParameter[0],parsedParameter[1]);
    }

    private class PathEntry {
        private final String request;
        private final String path;
        private final String mapper;
        private final Set<Parameter> parameters;

        PathEntry(String request, String path, String mapper,
                         Set<Parameter> parameters) {
            this.request = request;
            this.path = path;
            this.mapper = mapper;
            this.parameters = parameters;
        }
    }

    private class Parameter{
        private final String name;
        private final String converter;

        Parameter(String name, String converter) {
            this.name = name;
            this.converter = converter;
        }
    }
}
