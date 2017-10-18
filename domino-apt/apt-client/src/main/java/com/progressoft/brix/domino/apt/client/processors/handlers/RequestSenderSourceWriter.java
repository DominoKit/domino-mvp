package com.progressoft.brix.domino.apt.client.processors.handlers;


import com.progressoft.brix.domino.api.client.ServiceRootMatcher;
import com.progressoft.brix.domino.api.client.annotations.Path;
import com.progressoft.brix.domino.api.client.annotations.Request;
import com.progressoft.brix.domino.api.client.annotations.RequestSender;
import com.progressoft.brix.domino.api.client.request.RequestRestSender;
import com.progressoft.brix.domino.api.client.request.ServerRequestCallBack;
import com.progressoft.brix.domino.apt.commons.*;

import javax.ws.rs.HttpMethod;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;

public class RequestSenderSourceWriter extends JavaSourceWriter {

    private final JavaSourceBuilder sourceBuilder;
    private final FullClassName request;
    private final FullClassName response;
    private final String serviceRoot;
    private final String method;
    private final List<String> pathParams = new ArrayList<>();
    private final String path;

    private static final Map<String, String> consumes=new HashMap<>();

    static {
        consumes.put(HttpMethod.GET, "");
        consumes.put(HttpMethod.HEAD, "");
        consumes.put(HttpMethod.DELETE, "");
        consumes.put(HttpMethod.OPTIONS, "");

        consumes.put(HttpMethod.POST, "        @Consumes(MediaType.APPLICATION_JSON)\n");
        consumes.put(HttpMethod.PUT, "        @Consumes(MediaType.APPLICATION_JSON)\n");
        consumes.put("PATCH", "        @Consumes(MediaType.APPLICATION_JSON)\n");
    }


    public RequestSenderSourceWriter(ProcessorElement processorElement, FullClassName request, FullClassName response) {
        super(processorElement);
        this.request = request;
        this.response = response;
        this.path = processorElement.getAnnotation(Path.class).value();
        this.serviceRoot = processorElement.getAnnotation(Path.class).serviceRoot();
        this.method = processorElement.getAnnotation(Path.class).method();

        getPathParams(path);
        this.sourceBuilder = new JavaSourceBuilder(processorElement.simpleName() + "Sender");
    }

    private void getPathParams(String path) {
        Matcher pathParamMatcher= Pattern.compile("\\{(.*?)\\}").matcher(path);
        while(pathParamMatcher.find()){
            pathParams.add(pathParamMatcher.group().replace("{","").replace("}",""));
        }
    }

    @Override
    public String write() {
        this.sourceBuilder.onPackage(processorElement.elementPackage())
                .imports("com.google.gwt.core.client.GWT")
                .imports(RequestRestSender.class.getCanonicalName())
                .imports(ServerRequestCallBack.class.getCanonicalName())
                .imports(processorElement.fullQualifiedNoneGenericName())
                .imports(request.asImport())
                .imports(response.asImport())
                .imports(RequestSender.class.getCanonicalName())
                .imports("javax.ws.rs.Consumes")
                .imports("javax.ws.rs." + method)
                .imports("javax.ws.rs.PathParam")
                .imports("javax.ws.rs.Path")
                .imports("javax.ws.rs.Produces")
                .imports("javax.ws.rs.core.MediaType")
                .imports(Map.class.getCanonicalName())
                .imports("static java.util.Objects.*")
                .imports("org.fusesource.restygwt.client.*")
                .imports(ServiceRootMatcher.class.getCanonicalName())
                .annotate("@RequestSender(value = " + processorElement.simpleName() + ".class"+(hasServiceRoot()?", customServiceRoot=true":"")+")")
                .withModifiers(new ModifierBuilder().asPublic())
                .implement(RequestRestSender.class.getCanonicalName() + "<" + request.asSimpleName() + ">");

        writeBody();
        return this.sourceBuilder.build();
    }

    private void writeBody() {

        if (hasServiceRoot()) {
            sourceBuilder.codeBlock("\n\tpublic static final String SERVICE_ROOT=\"" +
                    serviceRoot + "\";");
        }

        sourceBuilder.codeBlock("\n\tpublic static final String PATH=\"" +
                path + "\";");


        sourceBuilder
                .codeBlock("\n\tpublic interface " + processorElement.simpleName() + "Service extends RestService {\n" +
                        "        @" + method + "\n" +
                        "        @Path(PATH)\n" +
                        "        @Produces(MediaType.APPLICATION_JSON)\n" +
                        consumes.get(method));


        if(pathParams.isEmpty()) {
            sourceBuilder.codeBlock(
                    "        void send("+request.asSimpleName() + " request, MethodCallback<" +
                            response.asSimpleName() + "> callback);\n" +
                            "    }\n");
        }else {
            sourceBuilder.codeBlock(
                    "        void send("+allPathParameters()+" MethodCallback<" +
                            response.asSimpleName() + "> callback);\n" +
                            "    }\n");
        }


        this.sourceBuilder.field("service")
                .withModifier(new ModifierBuilder().asPrivate())
                .ofType(processorElement.simpleName() + "Service")
                .initializedWith("GWT.create(" + processorElement.simpleName() + "Service.class)")
                .end();


        final MethodBuilder sendMethod = this.sourceBuilder.method("send")
                .annotate("@Override")
                .withModifier(new ModifierBuilder().asPublic())
                .returnsVoid()
                .takes(request.asSimpleName(), "request")
                .takes("Map<String, String>", "headers")
                .takes("ServerRequestCallBack", "callBack");



        String params="request, ";
        if(!pathParams.isEmpty())
            params= passedParams();

        Request requestAnnotation = processorElement.getAnnotation(Request.class);
        if(nonNull(requestAnnotation) && !requestAnnotation.classifier().isEmpty()){
            sendMethod.block("\theaders.put(\"REQUEST_KEY\", request.getClass().getCanonicalName()" + "+\"_"+requestAnnotation.classifier()+"\");\n");
        }else{
            sendMethod.block("\theaders.put(\"REQUEST_KEY\", request.getClass().getCanonicalName());\n");
        }

        if(hasServiceRoot())
            sendMethod.block("\t((RestServiceProxy)service).setResource(new Resource(SERVICE_ROOT, headers));\n");
        else
            sendMethod.block("\t((RestServiceProxy)service).setResource(new Resource(ServiceRootMatcher.matchedServiceRoot(PATH), headers));\n");



        sendMethod.block("\tservice.send("+params+"new MethodCallback<" + response.asSimpleName() + ">() {" +
                "\n\t\t@Override" +
                "\n\t\tpublic void onFailure(Method method, Throwable throwable) {" +
                "\n\t\t\tcallBack.onFailure(throwable);" +
                "\n\t\t}" +
                "\n" +
                "\n\t\t@Override" +
                "\n\t\tpublic void onSuccess(Method method, " + response.asSimpleName() + " response) {\n" +
                "\n\t\t\tcallBack.onSuccess(response);" +
                "\n\t\t}" +
                "\n\t});")
                .end();
    }

    private String allPathParameters() {
        StringBuilder sb=new StringBuilder();
        pathParams.forEach(p-> sb.append(pathParameter(p)));
        return sb.toString();
    }

    private String passedParams(){
        StringBuilder sb=new StringBuilder();
        pathParams.forEach(p-> sb.append("request, "));
        return sb.toString();
    }

    private String pathParameter(String name) {

        return "@PathParam(\""+name+"\") @Attribute(\""+name+"\") "+request.asSimpleName()+" "+name.replace(".","")+", ";
    }

    private boolean hasServiceRoot() {
        return !serviceRoot.trim().isEmpty();
    }


}
