package com.progressoft.brix.domino.apt.client.processors.module.client;

import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import com.progressoft.brix.domino.api.client.annotations.ClientModule;
import com.progressoft.brix.domino.apt.client.processors.module.client.contributions.ContributionsWriter;
import com.progressoft.brix.domino.apt.client.processors.module.client.initialtasks.InitialTasksWriter;
import com.progressoft.brix.domino.apt.client.processors.module.client.presenters.PresentersWriter;
import com.progressoft.brix.domino.apt.client.processors.module.client.requests.RequestsWriter;
import com.progressoft.brix.domino.apt.client.processors.module.client.requests.SendersWriter;
import com.progressoft.brix.domino.apt.client.processors.module.client.views.ViewWriter;
import com.progressoft.brix.domino.apt.commons.*;

import java.util.Set;

public class ModuleConfigurationSourceWriter extends JavaSourceWriter {

    private final JavaSourceBuilder sourceBuilder;
    private final Set<String> senders;
    private final Set<String> contributions;
    private final Set<String> initialTasks;
    private final Set<String> requests;
    private final Set<String> presenters;
    private final Set<String> views;

    public ModuleConfigurationSourceWriter(ProcessorElement processorElement,
                                           Set<String> presenters, Set<String> views, Set<String> requests,
                                           Set<String> initialTasks, Set<String> contributions,
                                           Set<String> senders) {
        super(processorElement);
        this.presenters = presenters;
        this.views = views;
        this.requests = requests;
        this.initialTasks = initialTasks;
        this.contributions = contributions;
        this.senders = senders;
        this.sourceBuilder = new JavaSourceBuilder(processorElement.getAnnotation(ClientModule.class).name() + "ModuleConfiguration")
                .annotate("@AutoService(ModuleConfiguration.class)");
    }

    @Override
    public String write() {
        this.sourceBuilder.onPackage(processorElement.elementPackage())
                .imports(AutoService.class.getCanonicalName())
                .withModifiers(new ModifierBuilder().asPublic())
                .implement(ModuleConfiguration.class.getCanonicalName());
        writeBody();
        return this.sourceBuilder.build();
    }

    private void writeBody() {
        new PresentersWriter(sourceBuilder).write(presenters);
        new ViewWriter(sourceBuilder).write(views);
        new RequestsWriter(sourceBuilder).write(requests);
        new InitialTasksWriter(sourceBuilder).write(initialTasks);
        new ContributionsWriter(sourceBuilder).write(contributions);
        new SendersWriter(sourceBuilder).write(senders);
    }

}
