package com.progressoft.brix.domino.apt.server;

import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.server.config.ServerModule;
import com.progressoft.brix.domino.api.server.config.ServerModuleConfiguration;
import com.progressoft.brix.domino.api.server.endpoint.EndpointsRegistry;
import com.progressoft.brix.domino.api.server.handler.CallbackRequestHandler;
import com.progressoft.brix.domino.api.server.handler.Handler;
import com.progressoft.brix.domino.api.server.handler.HandlerRegistry;
import com.progressoft.brix.domino.api.server.handler.RequestHandler;
import com.progressoft.brix.domino.api.server.interceptor.GlobalRequestInterceptor;
import com.progressoft.brix.domino.api.server.interceptor.InterceptorsRegistry;
import com.progressoft.brix.domino.api.server.interceptor.RequestInterceptor;
import com.progressoft.brix.domino.apt.commons.*;

import java.util.List;

public class ServerModuleSourceWriter extends JavaSourceWriter {

    public static final String OVERRIDE = "@Override";
    public static final String REGISTRY = "registry";
    public static final String CLASS_GET_CANONICAL_NAME = ".class.getCanonicalName()";
    public static final String NEW_KEYWORD = " new ";
    private final JavaSourceBuilder sourceBuilder;

    private final List<ProcessorElement> handlers;
    private final List<ProcessorElement> interceptors;
    private final List<ProcessorElement> globalInterceptors;

    public ServerModuleSourceWriter(ProcessorElement serverModuleElement,
                                    List<ProcessorElement> handlers,
                                    List<ProcessorElement> interceptors,
                                    List<ProcessorElement> globalInterceptors) {
        super(serverModuleElement);
        this.sourceBuilder =new JavaSourceBuilder( processorElement.getAnnotation(ServerModule.class).name() +
                "ServerModule");
        this.handlers = handlers;
        this.interceptors=interceptors;
        this.globalInterceptors=globalInterceptors;
    }

    @Override
    public String write() {
         sourceBuilder.onPackage(processorElement.elementPackage())
                .imports(AutoService.class.getCanonicalName())
                .annotate("@AutoService(ServerModuleConfiguration.class)")
                .withModifiers(new ModifierBuilder().asPublic())
                .implement(ServerModuleConfiguration.class.getCanonicalName());

            writeBody();

        return sourceBuilder.build();
    }


    private void writeBody() {
        writeHandlersRegisterMethod();
        writeEndpointsRegisterMethod();
        writeInterceptorsRegisterMethod();
        writeGlobalInterceptorsRegisterMethod();
    }

    private void writeEndpointsRegisterMethod() {
        if (!handlers.isEmpty()) {
            sourceBuilder.imports(EndpointsRegistry.class.getCanonicalName());
            MethodBuilder method = sourceBuilder.method("registerEndpoints")
                    .annotate(OVERRIDE)
                    .withModifier(new ModifierBuilder().asPublic())
                    .returns("void")
                    .takes(EndpointsRegistry.class.getCanonicalName(), REGISTRY);

            handlers.forEach(e ->
                    method.block(getEndpointRegistrationLine(e)));
            method.end();
        }
    }

    private String getEndpointRegistrationLine(ProcessorElement element) {
        final FullClassName handlerEndpoint = new FullClassName(element.fullQualifiedNoneGenericName() + "EndpointHandler");
        sourceBuilder.imports(handlerEndpoint.asImport());
        String path = element.getAnnotation(Handler.class).value();

        return "registry.registerEndpoint(\"" + path + "\", new EndpointsRegistry.EndpointHandlerFactory() {\n" +
                "            @Override\n" +
                "            public " + handlerEndpoint.asSimpleName() + " get() {\n" +
                "                return new " + handlerEndpoint.asSimpleName() + "();\n" +
                "            }\n" +
                "        });";
    }

    private void writeHandlersRegisterMethod() {
        if (!handlers.isEmpty()) {
            MethodBuilder method= sourceBuilder.method("registerHandlers")
                    .annotate(OVERRIDE)
                    .withModifier(new ModifierBuilder().asPublic())
                    .returns("void")
                    .takes(HandlerRegistry.class.getCanonicalName(), REGISTRY);

            handlers.forEach(h ->
                   method.block(getHandlerRegistrationLine(h)));
            method.end();
        }
    }

    private String getHandlerRegistrationLine(ProcessorElement handler) {
        Handler handlerAnnotation = handler.getAnnotation(Handler.class);
        String classifier=handlerAnnotation.classifier().isEmpty()?"":"+\"_"+handlerAnnotation.classifier()+"\"";

        if(handler.isImplementsGenericInterface(RequestHandler.class)) {
            sourceBuilder.imports(new FullClassName(handler.getFullQualifiedGenericName()));
            FullClassName request = new FullClassName(new FullClassName(handler.getInterfaceFullQualifiedGenericName(RequestHandler.class)).allImports().get(1));
            sourceBuilder.imports(request);
            return "registry.registerHandler("+request.asSimpleGenericName()+ CLASS_GET_CANONICAL_NAME+classifier+","+ NEW_KEYWORD +handler.simpleName()+"());";
        }else{
            sourceBuilder.imports(new FullClassName(handler.getFullQualifiedGenericName()));
            FullClassName request = new FullClassName(new FullClassName(handler.getInterfaceFullQualifiedGenericName(CallbackRequestHandler.class)).allImports().get(1));
            sourceBuilder.imports(request);
            return "registry.registerCallbackHandler("+request.asSimpleGenericName()+ CLASS_GET_CANONICAL_NAME+classifier+","+NEW_KEYWORD +handler.simpleName()+"());";
        }


    }

    private void writeInterceptorsRegisterMethod() {
        if (!interceptors.isEmpty()) {
            MethodBuilder method= sourceBuilder.method("registerInterceptors")
                    .annotate(OVERRIDE)
                    .withModifier(new ModifierBuilder().asPublic())
                    .returns("void")
                    .takes(InterceptorsRegistry.class.getCanonicalName(), REGISTRY);

            interceptors.forEach(i ->
                    method.block(getInterceptorRegistrationLine(i)));
            method.end();
        }
    }

    private String getInterceptorRegistrationLine(ProcessorElement interceptor) {
        sourceBuilder.imports(new FullClassName(interceptor.getFullQualifiedGenericName()));
        FullClassName request=new FullClassName(new FullClassName(interceptor.getInterfaceFullQualifiedGenericName(RequestInterceptor.class)).allImports().get(1));
        FullClassName entryPoint=new FullClassName(new FullClassName(interceptor.getInterfaceFullQualifiedGenericName(RequestInterceptor.class)).allImports().get(2));
        sourceBuilder.imports(request);
        sourceBuilder.imports(entryPoint);

        return "registry.registerInterceptor("+request.asSimpleGenericName()+".class.getCanonicalName(), "+entryPoint.asSimpleGenericName()+
                CLASS_GET_CANONICAL_NAME +","+ NEW_KEYWORD +interceptor.simpleName()+"());";
    }

    private void writeGlobalInterceptorsRegisterMethod() {
        if (!globalInterceptors.isEmpty()) {
            MethodBuilder method= sourceBuilder.method("registerGlobalInterceptors")
                    .annotate(OVERRIDE)
                    .withModifier(new ModifierBuilder().asPublic())
                    .returns("void")
                    .takes(InterceptorsRegistry.class.getCanonicalName(), REGISTRY);

            globalInterceptors.forEach(g ->
                    method.block(getGlobalInterceptorRegistrationLine(g)));
            method.end();
        }
    }

    private String getGlobalInterceptorRegistrationLine(ProcessorElement interceptor) {
        sourceBuilder.imports(new FullClassName(interceptor.getFullQualifiedGenericName()));
        FullClassName entryPoint=new FullClassName(new FullClassName(interceptor.getInterfaceFullQualifiedGenericName(GlobalRequestInterceptor.class)).allImports().get(1));
        sourceBuilder.imports(entryPoint);

        return "registry.registerGlobalInterceptor("+entryPoint.asSimpleGenericName()+ CLASS_GET_CANONICAL_NAME +","+
                NEW_KEYWORD +interceptor.simpleName()+"());";
    }
}
