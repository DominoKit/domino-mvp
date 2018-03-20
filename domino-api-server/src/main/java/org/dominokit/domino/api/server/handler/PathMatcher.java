package org.dominokit.domino.api.server.handler;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;

public class PathMatcher {

    // this regex supports ( :id, {id}, [id] )
    private static final String EXPRESSIONS_REGEX = "([\\{\\[]([A-Za-z][A-Za-z0-9_]*)[\\}\\]]|(:([A-Za-z][A-Za-z0-9_]*)))";
    private static final Pattern REGEX_SPECIAL_CHARACTERS = Pattern.compile("([\\(\\)\\$\\+\\.])");
    private final String path;
    private Pattern pattern;

    public PathMatcher(String path) {
        Objects.requireNonNull(path, "path should not be null");
        if (path.contains(":") || path.contains("{") || path.contains("["))
            createPattern(path);
        this.path = path;
    }

    private void createPattern(String path) {
        path = REGEX_SPECIAL_CHARACTERS.matcher(path).replaceAll("\\\\$1");
        Pattern pattern = Pattern.compile(EXPRESSIONS_REGEX);
        Matcher matcher = pattern.matcher(path);
        StringBuffer pathBuilder = new StringBuffer();
        int index = 0;
        while (matcher.find()) {
            String param = "p" + index;
            matcher.appendReplacement(pathBuilder, "(?<" + param + ">[^/]+)");
            index++;
        }
        matcher.appendTail(pathBuilder);
        this.pattern = Pattern.compile(pathBuilder.toString());
    }

    public boolean isMatch(String path) {
        return isExactMatch(path) || isMatchPattern(path);
    }

    private boolean isMatchPattern(String path) {
        return nonNull(pattern) && pattern.matcher(path).matches();
    }

    private boolean isExactMatch(String path) {
        return nonNull(path) && (this.path.equals(path));
    }
}
