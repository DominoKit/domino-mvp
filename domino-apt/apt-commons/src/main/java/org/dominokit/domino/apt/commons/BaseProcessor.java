package org.dominokit.domino.apt.commons;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

public abstract class BaseProcessor extends AbstractProcessor {

    protected Types typeUtils;
    protected Elements elementUtils;
    protected Filer filer;
    protected Messager messager;
    protected ElementFactory elementFactory;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.typeUtils=processingEnv.getTypeUtils();
        this.elementUtils=processingEnv.getElementUtils();
        this.filer=processingEnv.getFiler();
        this.messager=processingEnv.getMessager();
        this.elementFactory=new ElementFactory(elementUtils, typeUtils);
    }

    protected Writer obtainSourceWriter(String targetPackage,String className) throws IOException {
        return createSourceFile(targetPackage, className).openWriter();
    }

    protected JavaFileObject createSourceFile(String targetPackage, String className) throws IOException {
        return filer.createSourceFile(targetPackage + "." + className);
    }

    protected Writer obtainResourceWriter(String targetPackage,String className) throws IOException {
        return createResourceFile(targetPackage, className).openWriter();
    }

    protected FileObject createResourceFile(String targetPackage, String className) throws IOException {
        return filer.createResource(StandardLocation.SOURCE_OUTPUT, targetPackage, className);
    }

    protected boolean validateElementKind(Element element, ElementKind kind) {
        if (element.getKind() != kind)
            throw new ProcessingException(element, "Only "+kind+" can be annotated with @%s");
        return true;
    }

    protected ProcessorElement newProcessorElement(Element element){
        return new ProcessorElement(element, elementUtils, typeUtils, messager);
    }

    public class ElementFactory{

        private final Elements elementUtils;
        private final Types typeUtils;

        public ElementFactory(Elements elementUtils, Types typeUtils) {
            this.elementUtils = elementUtils;
            this.typeUtils = typeUtils;
        }

        public ProcessorElement make(Element element){
            return new ProcessorElement(element, this.elementUtils, this.typeUtils, messager);
        }
    }

    public interface ProcessingStep {

        void process(Set<? extends Element> elementsByAnnotation);
    }
}

