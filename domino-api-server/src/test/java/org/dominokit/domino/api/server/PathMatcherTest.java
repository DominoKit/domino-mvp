package org.dominokit.domino.api.server;

import org.dominokit.domino.api.server.handler.PathMatcher;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PathMatcherTest {

    public static final String EXACT_PATH = "exact/path";

    @Test(expected = NullPointerException.class)
    public void createWithNullPath_shouldThrowException() throws Exception {
        new PathMatcher(null);
    }

    @Test
    public void givenExactPath_whenCheckingForMatchingPassingWrongPath_shouldReturnFalse() throws Exception {
        assertThat(new PathMatcher(EXACT_PATH).isMatch("wrong/path")).isFalse();
    }

    @Test
    public void givenExactPath_whenCheckingForMatchingPassingMatchPath_shouldReturnTrue() throws Exception {
        assertThat(new PathMatcher(EXACT_PATH).isMatch(EXACT_PATH)).isTrue();
    }

    @Test
    public void givenExactPath_whenCheckingForMatchingPassingNull_shouldReturnFalse() throws Exception {
        assertThat(new PathMatcher(EXACT_PATH).isMatch(null)).isFalse();
    }

    @Test
    public void assertExpressionsMatching() throws Exception {
        assertThat(new PathMatcher("path/:id").isMatch("path/not/match")).isFalse();
        assertThat(new PathMatcher("path/:id").isMatch("path/1")).isTrue();
        assertThat(new PathMatcher("path/:id").isMatch("path/someId")).isTrue();
        assertThat(new PathMatcher("path/:id/:name").isMatch("path/1")).isFalse();
        assertThat(new PathMatcher("path/:id/:name").isMatch("path/someId/someName")).isTrue();
        assertThat(new PathMatcher("path/{id}").isMatch("path/1")).isTrue();
        assertThat(new PathMatcher("path/{id}/{name}").isMatch("path/1/name")).isTrue();
        assertThat(new PathMatcher("path/[id]").isMatch("path/1")).isTrue();
        assertThat(new PathMatcher("path/[id]/[name]").isMatch("path/1/name")).isTrue();
        assertThat(new PathMatcher("path/:id/{name}").isMatch("path/1/name")).isTrue();
    }

    @Test
    public void assertExpressionsMatchingWithSpecialCharacters() throws Exception {
        assertThat(new PathMatcher("path/file.:extension").isMatch("path/file.txt")).isTrue();
        assertThat(new PathMatcher("path/file$$:name").isMatch("path/file$$someName")).isTrue();
        assertThat(new PathMatcher("path/(:name)").isMatch("path/(someName)")).isTrue();
        assertThat(new PathMatcher("path/name+:id").isMatch("path/name+1")).isTrue();
    }
}
