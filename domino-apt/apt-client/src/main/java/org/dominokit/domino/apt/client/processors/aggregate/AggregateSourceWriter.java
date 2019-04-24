package org.dominokit.domino.apt.client.processors.aggregate;

import com.squareup.javapoet.*;
import org.dominokit.domino.api.client.annotations.Aggregate;
import org.dominokit.domino.api.shared.extension.ContextAggregator;
import org.dominokit.domino.apt.commons.AbstractSourceBuilder;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AggregateSourceWriter extends AbstractSourceBuilder {

    private final ExecutableElement methodElement;
    private final Element enclosingElement;

    public AggregateSourceWriter(ExecutableElement methodElement, ProcessingEnvironment processingEnv) {
        super(processingEnv);
        this.methodElement = methodElement;
        this.enclosingElement = methodElement.getEnclosingElement();
    }

    @Override
    public List<TypeSpec.Builder> asTypeBuilder() {
        String aggregateClassName = methodElement.getAnnotation(Aggregate.class).name();

        TypeSpec.Builder aggregateType = DominoTypeBuilder.classBuilder(aggregateClassName, AggregateProcessor.class)
                .addModifiers(Modifier.PUBLIC);

        List<? extends VariableElement> parameters = methodElement.getParameters();
        parameters.forEach(p -> {
            aggregateType.addField(FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(ContextAggregator.ContextWait.class), TypeName.get(p.asType())), p.getSimpleName().toString() + "Context", Modifier.PRIVATE)
                    .initializer("$T.create()", TypeName.get(ContextAggregator.ContextWait.class))
                    .build());
        });

        aggregateType.addField(FieldSpec.builder(TypeName.get(ContextAggregator.class), "contextAggregator", Modifier.PRIVATE).build());
        aggregateType.addField(FieldSpec.builder(TypeName.get(enclosingElement.asType()), "target", Modifier.PRIVATE).build());

        aggregateType.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("contextAggregator = ContextAggregator.waitFor($T.asList("+getEventsNames(parameters)+"))\n" +
                        "                .onReady(() -> {\n" +
                        "                    target."+methodElement.getSimpleName().toString()+"("+getEventsNamesGetters(parameters)+");\n" +
                        "                })", TypeName.get(Arrays.class))
                .build());

        aggregateType.addMethod(MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(enclosingElement.asType()), "target")
                .returns(ClassName.get(elements.getPackageOf(enclosingElement).toString(), aggregateClassName))
                .addStatement("this.target = target")
                .addStatement("return this")
        .build());

        parameters.forEach(p -> {
            aggregateType.addMethod(MethodSpec.methodBuilder("complete"+processorUtil.capitalizeFirstLetter(p.getSimpleName().toString()))
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addParameter(TypeName.get(p.asType()), "value")
                    .addStatement(""+p.getSimpleName().toString()+"Context.complete(value)")

            .build());
        });

        return Collections.singletonList(aggregateType);
    }

    private String getEventsNames(List<? extends VariableElement> parameters) {
        return parameters.stream().map(p -> p.getSimpleName().toString()+"Context")
                .collect(Collectors.joining(","));
    }

    private String getEventsNamesGetters(List<? extends VariableElement> parameters) {
        return parameters.stream().map(p -> p.getSimpleName().toString()+"Context.get()")
                .collect(Collectors.joining(","));
    }
}
