package org.dominokit.domino.remote.logging;

import com.google.gwt.core.server.StackTraceDeobfuscator;

import java.io.*;

import static java.util.Objects.nonNull;

public class DominoStackTraceDeobfuscator extends StackTraceDeobfuscator {

    private static final String SYMBOL_MAPS_DIRECTORY = "/app/gwt/extra/app/symbolMaps/";

    @Override
    protected InputStream getSourceMapInputStream(String permutation, int fragmentNumber) throws IOException {
        return getInputStream(SYMBOL_MAPS_DIRECTORY + permutation + "_sourceMap" + fragmentNumber + ".json");
    }

    @Override
    protected InputStream getSymbolMapInputStream(String permutation) throws IOException {
        return getInputStream(SYMBOL_MAPS_DIRECTORY + permutation + ".symbolMap");
    }

    @Override
    protected InputStream openInputStream(String fileName) throws IOException {
        return new FileInputStream(new File(SYMBOL_MAPS_DIRECTORY, fileName));
    }

    private InputStream getInputStream(String path) throws FileNotFoundException {
        InputStream stream = getClass().getResourceAsStream(path);
        if (nonNull(stream))
            return stream;
        throw new FileNotFoundException(path);
    }
}
