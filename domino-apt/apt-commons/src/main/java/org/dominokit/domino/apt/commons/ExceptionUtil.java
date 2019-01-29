package org.dominokit.domino.apt.commons;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {

    /**
     *
     * @param messager the messager to print the exception stack trace
     * @param e exception to be printed
     */
    public static void messageStackTrace(Messager messager, Exception e) {
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        messager.printMessage(Diagnostic.Kind.ERROR, "error while creating source file " + out.getBuffer().toString());
    }

}
