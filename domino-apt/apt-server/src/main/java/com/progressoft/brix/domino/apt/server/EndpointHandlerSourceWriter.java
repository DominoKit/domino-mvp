package com.progressoft.brix.domino.apt.server;


import com.progressoft.brix.domino.api.server.ServerApp;
import com.progressoft.brix.domino.api.server.entrypoint.VertxEntryPointContext;
import com.progressoft.brix.domino.api.server.handler.CallbackRequestHandler;
import com.progressoft.brix.domino.api.server.handler.RequestHandler;
import com.progressoft.brix.domino.apt.commons.*;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public class EndpointHandlerSourceWriter extends JavaSourceWriter {

    private static final String OVERRIDE = "@Override";
    private static final int REQUEST_IMPORT_INDEX = 1;
    private static final int RESPONSE_IMPORT_INDEX = 2;
    private final JavaSourceBuilder sourceBuilder;
    private final FullClassName request;
    private final FullClassName response;

    public EndpointHandlerSourceWriter(ProcessorElement element) {
        super(element);
        this.sourceBuilder = new JavaSourceBuilder(element.simpleName() +
                "EndpointHandler");
        List<String> handlerImports;
        if (element.isImplementsGenericInterface(RequestHandler.class)) {
            handlerImports = new FullClassName(processorElement.getInterfaceFullQualifiedGenericName(RequestHandler.class)).allImports();
        } else {
            handlerImports = new FullClassName(processorElement.getInterfaceFullQualifiedGenericName(CallbackRequestHandler.class)).allImports();
        }

        this.request = new FullClassName(handlerImports.get(REQUEST_IMPORT_INDEX));
        this.response = new FullClassName(handlerImports.get(RESPONSE_IMPORT_INDEX));
    }

    @Override
    public String write() {
        sourceBuilder.onPackage(processorElement.elementPackage())
                .imports(Handler.class.getCanonicalName())
                .imports(Json.class.getCanonicalName())
                .imports(RoutingContext.class.getCanonicalName())
                .imports(ServerApp.class.getCanonicalName())
                .imports(request.asImport())
                .imports(response.asImport())
                .imports(VertxEntryPointContext.class.getCanonicalName())
                .imports(HttpMethod.class.getCanonicalName())
                .withModifiers(new ModifierBuilder().asPublic())
                .implement("Handler<RoutingContext>");

        writeBody();

        return sourceBuilder.build();
    }


    private void writeBody() {
        writeHandleMethod();
    }

    private void writeHandleMethod() {

        MethodBuilder methodBuilder = sourceBuilder.method("handle")
                .annotate(OVERRIDE)
                .withModifier(new ModifierBuilder().asPublic())
                .returns("void")
                .takes("RoutingContext", "routingContext")
                .block("try {")
                .block("\tServerApp serverApp = ServerApp.make();")
                .block("\t"+request.asSimpleName() + " requestBody;")
                .block("\tHttpMethod method=routingContext.request().method();")
                .block("\n\t\t\tif(HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method) || HttpMethod.PATCH.equals(method)){")
                .block("\t\trequestBody=Json.decodeValue(routingContext.getBodyAsString(), " +
                        request.asSimpleName() + ".class);")
                .block("\t}else {")
                .block("\t\trequestBody = new " +request.asSimpleName() + "();")
                .block("\t}\n");
        if (processorElement.isImplementsGenericInterface(RequestHandler.class))
            completeHandler(methodBuilder);
        else
            completeHandlerCallback(methodBuilder);
    }

    private void completeHandler(MethodBuilder methodBuilder) {
        methodBuilder.block(response.asSimpleName() + " response=(" + response.asSimpleName() +
                ")serverApp.executeRequest(requestBody, new VertxEntryPointContext(routingContext, serverApp.serverContext().config(), routingContext.vertx()));")
                .block("routingContext.response()\n" +
                        "                .putHeader(\"content-type\", \"application/json\")\n" +
                        "                .end(Json.encode(response));")
                .block("} catch(Exception e) {")
                .block("\troutingContext.fail(e);")
                .block("}")
                .end();
    }

    private void completeHandlerCallback(MethodBuilder methodBuilder) {
       sourceBuilder.imports(CallbackRequestHandler.class.getCanonicalName());
        methodBuilder.block("\tserverApp.executeCallbackRequest(requestBody, new VertxEntryPointContext(routingContext, serverApp.serverContext().config(), routingContext.vertx()), new CallbackRequestHandler.ResponseCallback() {", false)
                .block("\n\t\t\t\t@Override\n" +
                        "\t\t\t\tpublic void complete(Object response) {")
                .block("\t\t\troutingContext.response()\n" +
                        "\t\t                .putHeader(\"content-type\", \"application/json\")\n" +
                        "\t\t                .end(Json.encode((" + response.asSimpleName() + ")response));")
                .block("\t\t}\n")
                .block("\t\t@Override\n" +
                        "\t\t\t\tpublic void redirect(String location) {\n" +
                        "\t\t\t\t\troutingContext.response().setStatusCode(302).putHeader(\"Location\",location).end();\n"+
                        "\t\t\t\t}")
                .block("\t});\n")
                .block("} catch(Exception e) {")
                .block("\troutingContext.fail(e);")
                .block("}")
                .end();
    }
}
