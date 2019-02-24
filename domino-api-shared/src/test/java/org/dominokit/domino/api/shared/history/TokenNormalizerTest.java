package org.dominokit.domino.api.shared.history;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenNormalizerTest {

    public static final String SAMPLE_TOKEN = "path1/path2/path3?param1=value1&param2=value2&param3=value3#fragment1/fragment2/fragment3";

    @Test
    public void givenEmptyTargetToken_thenShouldReturnOriginalToken() {
        assertThat(TokenNormalizer.normalize(SAMPLE_TOKEN, null).getToken()).isEqualTo(new StateHistoryToken(SAMPLE_TOKEN));
        assertThat(TokenNormalizer.normalize(SAMPLE_TOKEN, "").getToken()).isEqualTo(new StateHistoryToken(SAMPLE_TOKEN));
        assertThat(TokenNormalizer.normalize(SAMPLE_TOKEN, "  ").getToken()).isEqualTo(new StateHistoryToken(SAMPLE_TOKEN));
    }

    @Test
    public void givenNoParameterTargetToken_thenShouldReturnOriginalToken() {
        NormalizedToken normalized = TokenNormalizer.normalize(SAMPLE_TOKEN, SAMPLE_TOKEN);
        assertThat(normalized.getToken()).isEqualTo(new StateHistoryToken(SAMPLE_TOKEN));
    }

    @Test
    public void givenPathParameterTargetToken_thenShouldReturnSubstituteToken() {
        NormalizedToken normalized = TokenNormalizer.normalize(SAMPLE_TOKEN, "path1/:pathParam/path3");
        assertThat(normalized.getToken()).isEqualTo(new StateHistoryToken(SAMPLE_TOKEN.replace("path2",":pathParam")));
        assertThat(normalized.getPathParameters().size()).isEqualTo(1);
        assertThat(normalized.getPathParameter("pathParam")).isEqualTo("path2");

        normalized = TokenNormalizer.normalize(SAMPLE_TOKEN, "path1/:pathParam/:pathParam2");
        assertThat(normalized.getToken()).isEqualTo(new StateHistoryToken(SAMPLE_TOKEN
                .replace("path2",":pathParam")
                .replace("path3",":pathParam2")));

        assertThat(normalized.getPathParameters().size()).isEqualTo(2);
        assertThat(normalized.getPathParameter("pathParam")).isEqualTo("path2");
        assertThat(normalized.getPathParameter("pathParam2")).isEqualTo("path3");
    }

    @Test
    public void givenQueryParameterTargetToken_thenShouldReturnSubstituteToken() {
        NormalizedToken normalized = TokenNormalizer.normalize(SAMPLE_TOKEN, "path1/path2/path3?param1=:value1param&param2=value2&param3=value3");
        assertThat(normalized.getToken()).isEqualTo(new StateHistoryToken(SAMPLE_TOKEN.replace("value1", ":value1param")));
        assertThat(normalized.getPathParameters()).isEmpty();

        normalized = TokenNormalizer.normalize(SAMPLE_TOKEN, "path1/path2/path3?param1=:value1param&param2=:value2param&param3=value3");
        assertThat(normalized.getToken()).isEqualTo(new StateHistoryToken(SAMPLE_TOKEN.replace("value1",":value1param")
        .replace("value2", ":value2param")));
        assertThat(normalized.getPathParameters()).isEmpty();
    }

    @Test
    public void givenFragmentTargetToken_thenShouldReturnSubstituteToken() {
        NormalizedToken normalized = TokenNormalizer.normalize(SAMPLE_TOKEN, "path1/path2/path3?param1=value1&param2=value2&param3=value3#:fragment1param");
        assertThat(normalized.getToken()).isEqualTo(new StateHistoryToken(SAMPLE_TOKEN.replace("fragment1", ":fragment1param")));
        assertThat(normalized.getPathParameters()).isEmpty();
        assertThat(normalized.getFragmentParameters().size()).isEqualTo(1);
        assertThat(normalized.getFragmentParameter("fragment1param")).isEqualTo("fragment1");

        normalized = TokenNormalizer.normalize(SAMPLE_TOKEN, "path1/path2/path3?param1=value1&param2=value2&param3=value3#:fragment1param/:fragment2param/fragment3");
        assertThat(normalized.getToken()).isEqualTo(new StateHistoryToken(SAMPLE_TOKEN.replace("fragment1",":fragment1param")
                .replace("fragment2", ":fragment2param")));
        assertThat(normalized.getPathParameters()).isEmpty();
        assertThat(normalized.getFragmentParameters().size()).isEqualTo(2);
        assertThat(normalized.getFragmentParameter("fragment1param")).isEqualTo("fragment1");
        assertThat(normalized.getFragmentParameter("fragment2param")).isEqualTo("fragment2");
    }

    @Test
    public void testNormalizeEnd(){
        NormalizedToken normalized = TokenNormalizer.normalizeTail(SAMPLE_TOKEN, ":path3param?param1=value1&param2=value2&param3=value3#fragment1/fragment2/:fragment3param");
        StateHistoryToken expected = new StateHistoryToken(SAMPLE_TOKEN
                .replace("path3", ":path3param")
                .replace("fragment3", ":fragment3param"));

        assertThat(normalized.getPathParameters().size()).isEqualTo(1);
        assertThat(normalized.getFragmentParameters().size()).isEqualTo(1);
        assertThat(normalized.getPathParameter("path3param")).isEqualTo("path3");
        assertThat(normalized.getFragmentParameter("fragment3param")).isEqualTo("fragment3");

        assertThat(normalized.getToken()).isEqualTo(expected);
    }

    @Test
    public void testNormalizeEndNoPath(){
        NormalizedToken normalized = TokenNormalizer.normalizeTail(SAMPLE_TOKEN, "param1=value1&param2=value2&param3=:value3param#fragment1/fragment2/:fragment3param");
        StateHistoryToken expected = new StateHistoryToken(SAMPLE_TOKEN
                .replace("value3", ":value3param")
                .replace("fragment3", ":fragment3param"));

        System.out.println(normalized.getToken().value());
        System.out.println(expected.value());

        assertThat(normalized.getPathParameters()).isEmpty();
        assertThat(normalized.getFragmentParameters().size()).isEqualTo(1);
        assertThat(normalized.getFragmentParameter("fragment3param")).isEqualTo("fragment3");

        assertThat(normalized.getToken()).isEqualTo(expected);
    }

    @Test
    public void testNormalizeTokenWithDuplicatePaths(){
        HistoryToken historyToken = new StateHistoryToken(SAMPLE_TOKEN);
        historyToken.appendPath("path3")
        .clearFragments()
        .clearQuery();

        NormalizedToken normalized = TokenNormalizer.normalizePathTail(historyToken.value(), "path1/path2/:pathx/:pathy");
        StateHistoryToken expected = new StateHistoryToken("path1/path2/:pathx/:pathy");

        System.out.println(normalized.getToken().value());
        System.out.println(expected.value());

        assertThat(normalized.getToken()).isEqualTo(expected);
    }
}
