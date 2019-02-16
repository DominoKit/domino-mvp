package org.dominokit.domino.apt.client.processors.service;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import org.dominokit.domino.api.shared.history.HistoryToken;
import org.dominokit.domino.api.shared.history.StateHistoryToken;
import org.dominokit.domino.apt.commons.ExceptionUtil;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class ReplaceParametersMethodBuilder {
    private Messager messager;
    private final String path;

    public ReplaceParametersMethodBuilder(Messager messager, String path) {
        this.messager = messager;
        this.path = path;
    }

    CodeBlock build() {
        CodeBlock.Builder replacerBuilder = CodeBlock.builder().beginControlFlow("setRequestParametersReplacer((token, bean) -> ");

        try {

            CodeBlock.Builder bodyBuilder = CodeBlock.builder()
                    .beginControlFlow("if (token.value().contains(\":\"))");
            StateHistoryToken token = new StateHistoryToken(path);

            token.paths()
                    .stream()
                    .filter(tokenPath -> tokenPath.startsWith(":"))
                    .forEach(tokenPath -> bodyBuilder.addStatement("token.replacePath(\"" + tokenPath + "\", " + convertParameterToGetter(tokenPath.replace(":", "")) + "+\"\")", Objects.class));
            token.queryParameters()
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().startsWith(":"))
                    .forEach(entry -> {
                        bodyBuilder.addStatement("token.replaceParameter(\"" + entry.getKey() + "\", \"" + entry.getKey() + "\", " + convertParameterToGetter(entry.getValue().replace(":", "")) + "+\"\")", Objects.class);
                    });

            token.fragments()
                    .stream()
                    .filter(fragment -> fragment.startsWith(":"))
                    .forEach(fragment -> bodyBuilder.addStatement("token.replaceFragment(\"" + fragment + "\", " + convertParameterToGetter(fragment.replace(":", "")) + "+\"\")", Objects.class));

            bodyBuilder.endControlFlow();
            replacerBuilder
                    .add(CodeBlock.builder()
                            .add(bodyBuilder.build())
                            .addStatement("return token.value()")
                            .build());
            replacerBuilder.endControlFlow(")");

            return replacerBuilder.build();
        } catch (Exception ex) {
            ExceptionUtil.messageStackTrace(messager, ex);
        }

        return replacerBuilder.build();
    }


    private String convertParameterToGetter(String parameter) {
        String names = parameter
                .replace("{", "")
                .replace("}", "");

        String[] fieldsNames = names.contains(".") ? names.split("\\.") : new String[]{names};

        List<String> gettersString = Arrays.asList(fieldsNames)
                .stream()
                .map(s -> "get" + s.replaceFirst(s.charAt(0) + "", (s.charAt(0) + "").toUpperCase()) + "()")
                .collect(Collectors.toList());

        String result = getterWithNullCheck(gettersString, 1);

        return result;
    }

    private String getterWithNullCheck(List<String> getters, int upTo) {

        if (upTo <= getters.size()) {
            return "$1T.isNull(bean." + getters.subList(0, upTo).stream().collect(Collectors.joining(".")) + ")?null:(" + getterWithNullCheck(getters, ++upTo) + ")";
        } else {
            return "bean." + getters.subList(0, upTo - 1).stream().collect(Collectors.joining("."));
        }
    }
}
