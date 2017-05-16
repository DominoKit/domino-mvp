package com.progressoft.brix.domino.apt.client.path;


import com.progressoft.brix.domino.api.client.annotations.Path;
import com.progressoft.brix.domino.api.client.history.RequestFromPath;
import com.progressoft.brix.domino.api.client.history.TokenizedPath;
import com.progressoft.brix.domino.api.client.request.Request;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RequestFromPathResolver {

    public static final String IMPORT = "import ";
    private Map.Entry<Element, String> entry;

    RequestFromPathResolver(Map.Entry<Element, String> entry) {
        this.entry = entry;
    }

    String requestFromPathImplementation() {
        if (isMapperValuePresent())
            return customMapperImplementation();
        return anonymousImplementation();
    }

    public Set<String> imports() {
        if (isMapperValuePresent())
            return customMapperImports();
        return anonymousImports();
    }

    private boolean isMapperValuePresent() {
        return annotationMirror().getElementValues().keySet().stream().anyMatch(this::isMapperValue);
    }

    private String customMapperImplementation() {
        return "new " + mapperValue().asElement().getSimpleName().toString() + "()";
    }

    private DeclaredType mapperValue() {
        return (DeclaredType) annotationMirror().getElementValues().entrySet().stream().filter(e -> isMapperValue(e.getKey()))
                .findAny().orElseThrow(IllegalArgumentException::new).getValue().getValue();
    }

    private AnnotationMirror annotationMirror() {
        return entry.getKey().getAnnotationMirrors()
                .stream().filter(a -> a.getAnnotationType().toString().equals(Path.class.getCanonicalName()))
                .findAny().orElseThrow(IllegalArgumentException::new);
    }

    private boolean isMapperValue(ExecutableElement e) {
        return "mapper".equals(e.getSimpleName().toString());
    }

    private String anonymousImplementation() {
        return "new RequestFromPath() {\n"
                + "\t\t\t@Override\n\t\t\tpublic Request buildRequest(TokenizedPath path) {\n"
                + "\t\t\t\treturn new " + entry.getKey().getSimpleName() + "(" + new PathParametersResolver(entry).parametersAsString() + ");\n"
                + "\t\t\t}\n"
                + "\t\t}";
    }

    private Set<String> customMapperImports() {
        Set<String> imports = new HashSet<>();
        imports.add(IMPORT + mapperValue().asElement().asType().toString() + ";\n");
        return imports;
    }

    private Set<String> anonymousImports() {
        Set<String> imports = new HashSet<>();
        imports.addAll(pathParametersImports());
        imports.add(IMPORT + Request.class.getCanonicalName() + ";\n");
        imports.add(IMPORT + TokenizedPath.class.getCanonicalName() + ";\n");
        imports.add(IMPORT + RequestFromPath.class.getCanonicalName() + ";\n");
        imports.add(IMPORT + ((TypeElement) entry.getKey()).getQualifiedName() + ";\n");
        return imports;
    }

    private Collection<? extends String> pathParametersImports() {
        return new PathParametersResolver(entry).imports();
    }
}
