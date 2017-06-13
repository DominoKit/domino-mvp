package com.progressoft.brix.domino.apt.client.processors.module.client.requests;

import com.progressoft.brix.domino.api.client.request.LazyRequestRestSenderLoader;
import com.progressoft.brix.domino.api.client.request.RequestRestSender;
import com.progressoft.brix.domino.api.client.request.RequestRestSendersRegistry;
import com.progressoft.brix.domino.apt.commons.FullClassName;
import com.progressoft.brix.domino.apt.commons.JavaSourceBuilder;
import com.progressoft.brix.domino.apt.commons.MethodBuilder;
import com.progressoft.brix.domino.apt.commons.ModifierBuilder;

import java.util.Set;

public class SendersWriter {

    private final JavaSourceBuilder sourceBuilder;

    public SendersWriter(JavaSourceBuilder sourceBuilder) {
        this.sourceBuilder = sourceBuilder;
    }

    public void write(Set<String> senders){

        if(!senders.isEmpty()){
            sourceBuilder.imports(RequestRestSendersRegistry.class.getCanonicalName())
            .imports(LazyRequestRestSenderLoader.class.getCanonicalName())
            .imports(RequestRestSender.class.getCanonicalName());

            MethodBuilder methodBuilder = this.sourceBuilder.method("registerRequestRestSenders");
            methodBuilder.annotate("@Override")
                    .withModifier(new ModifierBuilder().asPublic())
                    .returnsVoid()
                    .takes("RequestRestSendersRegistry", "registry");
            senders.stream().map(this::parseEntry)
                    .forEach(e-> registerSender(e, methodBuilder));
            methodBuilder.end();
        }
    }

    private void registerSender(SenderEntry e, MethodBuilder methodBuilder) {
        importSender(e);
        FullClassName request=new FullClassName(e.request);
        FullClassName sender=new FullClassName(e.sender);

        methodBuilder.line("registry.registerRequestRestSender("+request.asSimpleName()+".class.getCanonicalName(),\n" +
                "\t\t\t\tnew LazyRequestRestSenderLoader() {\n" +
                "\t\t\t\t\t@Override\n" +
                "\t\t\t\t\tprotected RequestRestSender make() {\n" +
                "\t\t\t\t\t\treturn new "+sender.asSimpleName()+"();\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t});");

    }

    private void importSender(SenderEntry e) {
        sourceBuilder.imports(e.request).imports(e.sender);
    }

    private SenderEntry parseEntry(String senderEntry) {
        String[] senderPair=senderEntry.split(":");
        return new SenderEntry(senderPair[0], senderPair[1]);
    }

    private class SenderEntry {
        private final String sender;
        private final String request;

        public SenderEntry(String sender, String request) {
            this.sender = sender;
            this.request = request;
        }
    }
}
