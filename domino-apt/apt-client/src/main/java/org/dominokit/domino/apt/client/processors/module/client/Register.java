package org.dominokit.domino.apt.client.processors.module.client;

import org.apache.commons.lang.exception.ExceptionUtils;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.util.Set;
import java.util.TreeSet;

import static java.util.Objects.isNull;

public class Register {

    private Set<String> items;
    private final Messager messager;
    private final ProcessingEnvironment processingEnv;
    private final String fileName;

    public Register(String fileName, Set<String> items, Messager messager,
                    ProcessingEnvironment processingEnv) {

        this.items = items;
        this.messager = messager;
        this.processingEnv = processingEnv;
        this.fileName=fileName;
    }

    public Set<String> readItems() {
        if (isNull(items)) {
            items = new TreeSet<>();
            try {
                FileObject resource = processingEnv.getFiler()
                        .getResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/domino/"+fileName);
                new BufferedReader(new InputStreamReader(resource.openInputStream())).lines().forEach(items::add);
            } catch (IOException notFoundException) {
                messager.printMessage(Diagnostic.Kind.NOTE, "File not found 'META-INF/domino/"+fileName+"'");
            }
        }

        return items;
    }


    public void writeItems() {
        try {
            FileObject resource = processingEnv.getFiler()
                    .createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/domino/"+fileName);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(resource.openOutputStream()));
            items.forEach(out::println);
            out.close();
        } catch (IOException ex) {
            messager.printMessage(Diagnostic.Kind.ERROR, ExceptionUtils.getFullStackTrace(ex));
        }
    }

}
