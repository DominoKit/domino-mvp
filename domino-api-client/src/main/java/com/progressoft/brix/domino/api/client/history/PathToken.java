package com.progressoft.brix.domino.api.client.history;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

public class PathToken implements Token {

    public static final String PATH_MARKER = "_path";
    public static final String PARAMETERS_SEPARATOR = "&";
    public static final String PARAMETER_VALUE_SEPARATOR = "=";

    private final String path;
    private final Deque<Parameter> parameters=new LinkedList<>();

    private PathToken(String path) {
        if(isInvalidPart(path))
            throw new InvalidTokenPathException(path);
        this.path=path;
    }

    @Override
    public String asTokenString() {
        return PATH_MARKER +PARAMETER_VALUE_SEPARATOR+path+parametersString(parameters.iterator());
    }

    private String parametersString(Iterator<Parameter> iterator) {
        if(iterator.hasNext())
            return parametersString(iterator.next().asParameterString(), iterator);
        return "";
    }

    private String parametersString(String s, Iterator<Parameter> iterator) {
        if(iterator.hasNext())
            return parametersString(iterator.next().asParameterString(), iterator)+s;
        return s;
    }

    @Override
    public Token appendParameter(String name, String value) {
        parameters.push(new Parameter(name, value));
        return this;
    }

    private boolean isInvalidPart(String name) {
        return Objects.isNull(name) || name.trim().isEmpty() || containsSpecialWords(name);
    }

    private boolean containsSpecialWords(String part) {
        return part.contains(PARAMETERS_SEPARATOR) || part.contains(PARAMETER_VALUE_SEPARATOR) || part.contains(
                PATH_MARKER);
    }

    @Override
    public boolean hasSamePath(Token token) {
        return this.path.equals(((PathToken)token).path);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        return asTokenString().equals(((PathToken) other).asTokenString());
    }

    @Override
    public int hashCode() {
        return asTokenString().hashCode();
    }

    public class InvalidTokenPathException extends RuntimeException {
        private static final long serialVersionUID = -4812121253102195040L;

        public InvalidTokenPathException(String path) {
            super(path);
        }
    }

    public class InvalidParameterNameOrValueException extends RuntimeException {
        private static final long serialVersionUID = 2707063232341325664L;

        public InvalidParameterNameOrValueException(String name, String value) {
            super("["+name+","+value+"]");
        }
    }

    public static Token make(String path){
        return new PathToken(path);
    }

    private class Parameter {
        private final String name;
        private final String value;

        public Parameter(String name, String value) {
            if(isInvalidPart(name) || isInvalidValue(value))
                throw new InvalidParameterNameOrValueException(name, value);
            this.name = name;
            this.value = value;
        }

        public String asParameterString(){
            return PARAMETERS_SEPARATOR+name+PARAMETER_VALUE_SEPARATOR+value;
        }

        private boolean isInvalidValue(String value) {
            return Objects.nonNull(value) && (containsSpecialWords(value));
        }
    }
}
