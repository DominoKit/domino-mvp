package org.dominokit.domino.apt.commons;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public abstract class AbstractSourceBuilder implements SourceBuilder{

    protected final Messager messager;
    protected final Filer filer;
    protected final Types types;
    protected final Elements elements;
    protected final ProcessingEnvironment processingEnv;
    protected final ProcessorUtil processorUtil;

    public AbstractSourceBuilder(ProcessingEnvironment processingEnv) {
        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
        this.types = processingEnv.getTypeUtils();
        this.elements = processingEnv.getElementUtils();
        this.processingEnv = processingEnv;
        this.processorUtil = new ProcessorUtil(processingEnv);
    }

}
