package org.dominokit.domino.gwt.client.history;

import org.dominokit.domino.api.shared.history.HistoryToken;
import org.dominokit.domino.client.commons.history.StateHistoryToken;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class JsStateHistoryTokenTest {

    private StateHistoryToken make(String token) {
        return new StateHistoryToken(token);
    }

    @Test(expected = HistoryToken.TokenCannotBeNullException.class)
    public void createWithNullToken_shouldThrowException() {
        make(null);
    }

    @Test
    public void createTokenWithSlashOnly_thenTokenValueShouldBeEmptyString() {
        assertThat(make("/").value()).isEmpty();
    }

    @Test
    public void createTokenWithSlashAndSinglePath_thenTokenValueShouldBeTheSinglePathString() {
        assertThat(make("/somePath").path()).isEqualTo("somePath");
    }

    @Test
    public void createTokenWithSlashAndMultiPaths_thenTokenValueShouldBeThePathsString() {
        assertThat(make("/firstPath/secondPath/thirdPath").path()).isEqualTo("firstPath/secondPath/thirdPath");
    }

    @Test
    public void createTokenWithSinglePath_whenCheckingForStartingWithPathPassingFirstPath_thenShouldReturnTrue() {
        assertThat(make("firstPath").startsWithPath("firstPath")).isTrue();
    }

    @Test
    public void createTokenWithMultiPaths_whenCheckingForStartingWithPathPassingFirstPath_thenShouldReturnTrue() {
        assertThat(make("firstPath/secondPath/thirdPath").startsWithPath("firstPath")).isTrue();
    }

    @Test
    public void createTokenWithMultiPaths_whenCheckingForStartingWithPathPassingWrongPath_thenShouldReturnFalse() {
        assertThat(make("firstPath/secondPath/thirdPath").startsWithPath("wrongPath")).isFalse();
    }

    @Test
    public void createTokenWithMultiPaths_whenCheckingForStartingWithPassingValidMultiPaths_thenShouldReturnTrue() {
        assertThat(make("firstPath/secondPath/thirdPath").startsWithPath("firstPath/secondPath")).isTrue();
    }

    @Test
    public void createTokenWithMultiPaths_whenCheckingForStartingWithPassingWrongMultiPaths_thenShouldReturnFalse() {
        assertThat(make("firstPath/secondPath/thirdPath").startsWithPath("firstPath/second")).isFalse();
    }

    @Test
    public void createTokenWithMultiPaths_whenCheckingForStartingWithPassingEmptyPath_thenShouldReturnFalse() {
        assertThat(make("firstPath/secondPath/thirdPath").startsWithPath("")).isFalse();
    }

    @Test
    public void createTokenWithMultiPaths_whenCheckingForStartingWithPassingNullPath_thenShouldReturnFalse() {
        assertThat(make("firstPath/secondPath/thirdPath").startsWithPath(null)).isFalse();
    }

    @Test
    public void createTokenWithMultiPaths_whenCheckingForStartingWithPassingLongerPath_thenShouldReturnFalse() {
        assertThat(make("firstPath/secondPath/thirdPath").startsWithPath("firstPath/secondPath/thirdPath/fourthPath"))
                .isFalse();
    }

    //-------------------------

    @Test
    public void createTokenWithMultiPaths_whenCheckingForEndsWithPathPassingFirstPath_thenShouldReturnTrue() {
        assertThat(make("firstPath/secondPath/thirdPath").endsWithPath("thirdPath")).isTrue();
    }

    @Test
    public void createTokenWithMultiPaths_whenCheckingForEndsWithPathPassingWrongPath_thenShouldReturnFalse() {
        assertThat(make("firstPath/secondPath/thirdPath").endsWithPath("wrongPath")).isFalse();
    }

    @Test
    public void createTokenWithMultiPaths_whenCheckingForEndWithPassingValidMultiPaths_thenShouldReturnTrue() {
        assertThat(make("firstPath/secondPath/thirdPath").endsWithPath("secondPath/thirdPath")).isTrue();
    }

    @Test
    public void createTokenWithMultiPaths_whenCheckingForEndsWithPassingWrongMultiPaths_thenShouldReturnFalse() {
        assertThat(make("firstPath/secondPath/thirdPath").endsWithPath("Path/thirdPath")).isFalse();
        assertThat(make("firstPath/secondPath/thirdPath").endsWithPath("secondPath/third")).isFalse();
    }

    @Test
    public void createTokenWithMultiPaths_whenCheckingForEndsWithPassingEmptyPath_thenShouldReturnFalse() {
        assertThat(make("firstPath/secondPath/thirdPath").endsWithPath("")).isFalse();
    }

    @Test
    public void createTokenWithMultiPaths_whenCheckingForEndsWithPassingNullPath_thenShouldReturnFalse() {
        assertThat(make("firstPath/secondPath/thirdPath").endsWithPath(null)).isFalse();
    }

    @Test
    public void createTokenWithMultiPaths_whenCheckingForEndsWithPassingLongerPath_thenShouldReturnFalse() {
        assertThat(make("firstPath/secondPath/thirdPath").endsWithPath("firstPath/secondPath/thirdPath/fourthPath"))
                .isFalse();
    }

    //--------------------------

    @Test
    public void givenStateHistoryToken_whenCheckingForContainsNullPath_thenShouldReturnFalse() {
        assertThat(make("firstPath/secondPath/thirdPath").containsPath(null)).isFalse();
    }

    @Test
    public void givenStateHistoryToken_whenCheckingForContainsEmptyPath_thenShouldReturnFalse() {
        assertThat(make("firstPath/secondPath/thirdPath").containsPath("")).isFalse();
    }

    @Test
    public void givenStateHistoryToken_whenCheckingForContainsASinglePathThatExist_thenShouldReturnTrue() {
        assertThat(make("firstPath/secondPath/thirdPath").containsPath("firstPath")).isTrue();
        assertThat(make("firstPath/secondPath/thirdPath").containsPath("secondPath")).isTrue();
        assertThat(make("firstPath/secondPath/thirdPath").containsPath("thirdPath")).isTrue();
    }

    @Test
    public void givenStateHistoryToken_whenCheckingForContainsMultiPathsThatNonExist_thenShouldReturnFalse() {
        assertThat(make("firstPath/secondPath/thirdPath/forthPath").containsPath("firstPath/nonExist")).isFalse();
    }

    @Test
    public void givenStateHistoryToken_whenCheckingForContainsMultiPathsThatExist_thenShouldReturnTrue() {
        assertThat(make("firstPath/secondPath/thirdPath/forthPath").containsPath("firstPath/secondPath")).isTrue();
        assertThat(make("firstPath/secondPath/thirdPath/forthPath").containsPath("secondPath/thirdPath")).isTrue();
        assertThat(make("firstPath/secondPath/thirdPath/forthPath").containsPath("thirdPath/forthPath")).isTrue();
        assertThat(make("firstPath/secondPath/thirdPath/forthPath").containsPath("secondPath/thirdPath/forthPath"))
                .isTrue();
        assertThat(make("firstPath/secondPath/thirdPath/forthPath").containsPath("secondPath/thirdPath/forthPath"))
                .isTrue();
        assertThat(make("firstPath/secondPath/thirdPath/forthPath")
                .containsPath("firstPath/secondPath/thirdPath/forthPath")).isTrue();
        assertThat(make("firstPath/secondPath/thirdPath/firstPath/forthPath").containsPath("firstPath/forthPath"))
                .isTrue();
        assertThat(make("firstPath/secondPath/thirdPath/firstPath/forthPath").containsPath("firstPath/thirdPath"))
                .isFalse();
    }

    //-------------------------------


    @Test
    public void givenAnEmptyStateHistoryToken_thenPathShouldBeEmpty() {
        assertThat("").isEqualTo(make("").path());
    }

    @Test
    public void givenAnSinglePathStateHistoryToken_thenPathShouldBeSinglePath() {
        assertThat("firstPath").isEqualTo(make("firstPath").path());
    }

    @Test
    public void givenMultiPathStateHistoryToken_thenPathShouldBeTheMultiPathsString() {
        assertThat("firstPath/secondPath").isEqualTo(make("firstPath/secondPath").path());
    }

    @Test
    public void givenMultiPathStateHistoryTokenWithQueryParameters_thenPathShouldBeTheMultiPathsString() {
        assertThat("firstPath/secondPath").isEqualTo(make("firstPath/secondPath?param=value").path());
    }

    @Test
    public void givenMultiPathStateHistoryTokenWithUrlFragments_thenPathShouldBeTheMultiPathsString() {
        assertThat("firstPath/secondPath").isEqualTo(make("firstPath/secondPath#fragment").path());
        assertThat("firstPath/secondPath").isEqualTo(make("firstPath/secondPath/#fragment").path());
        assertThat("firstPath/secondPath").isEqualTo(make("firstPath/secondPath/!#fragment").path());
        assertThat("firstPath/secondPath").isEqualTo(make("firstPath/secondPath?param=value#fragment").path());
    }

    @Test
    public void givenEmptyStateHistoryToken_thenPathsShouldBeEmpty() {
        assertThat(make("").paths()).isEmpty();
    }

    @Test
    public void givenSinglePathStateHistoryToken_thenPathsShouldBeTheFirstPathEntry() {
        assertThat(make("firstPath").paths()).containsExactly("firstPath");
        assertThat(make("/firstPath").paths()).containsExactly("firstPath");
        assertThat(make("/firstPath/").paths()).containsExactly("firstPath");
        assertThat(make("firstPath/").paths()).containsExactly("firstPath");
    }

    @Test
    public void givenMultiPathStateHistoryToken_thenPathsShouldContainsAllPathsEntries() {
        assertThat(make("firstPath/secondPath/thirdPath").paths())
                .containsExactly("firstPath", "secondPath", "thirdPath");
        assertThat(make("/firstPath/secondPath/thirdPath").paths())
                .containsExactly("firstPath", "secondPath", "thirdPath");
        assertThat(make("/firstPath/secondPath/thirdPath/").paths())
                .containsExactly("firstPath", "secondPath", "thirdPath");
        assertThat(make("firstPath/secondPath/thirdPath/").paths())
                .containsExactly("firstPath", "secondPath", "thirdPath");
    }

    @Test
    public void givenAnEmptyPathStateHistoryToken_thenQueryShouldReturnEmptyQuery() {
        assertThat(make("").query()).isEmpty();
    }

    @Test
    public void givenSinglePathStateHistoryToken_thenQueryShouldReturnEmptyQuery() {
        assertThat(make("firstPath").query()).isEmpty();
    }

    @Test
    public void givenPathWithParameterStateHistoryToken_thenQueryShouldReturnTheParamtersString() {
        assertThat(make("firstPath?param=value").query()).isEqualTo("param=value");
    }

    @Test
    public void givenPathWithParameterAndUrlFragmentStateHistoryToken_thenQueryShouldReturnTheParamtersStringOnly() {
        assertThat(make("firstPath?param=value#fragment").query()).isEqualTo("param=value");
        assertThat(make("firstPath?param1=value1&param2=value2#fragment").query()).isEqualTo("param1=value1&param2=value2");
    }

    @Test
    public void givenStateHistoryToken_whenAppendPath_thenTheNewPathShouldEndWithTheAppendedPath() {
        assertThat(make("").appendPath("firstPath").path()).isEqualTo("firstPath");
        assertThat(make("").appendPath(null).path()).isEqualTo("null");
        assertThat(make("firstPath").appendPath("secondPath").path()).isEqualTo("firstPath/secondPath");
        assertThat(make("firstPath?param=value").appendPath("secondPath").path()).isEqualTo("firstPath/secondPath");
        assertThat(make("firstPath").appendPath("secondPath/thirdPath").path())
                .isEqualTo("firstPath/secondPath/thirdPath");
        assertThat(make("firstPath").appendPath("secondPath?param=value").path()).isEqualTo("firstPath/secondPath");
        assertThat(make("firstPath").appendPath("secondPath?param=value#fragment").path())
                .isEqualTo("firstPath/secondPath");
    }

    @Test
    public void givenStateHistoryToken_whenReplacePath_thenThePathShouldBeReplacedWithTheReplacement() {
        assertThat(make("").replacePath("firstPath", "secondPath").path()).isEmpty();
        assertThat(make("firstPath").replacePath("firstPath", "secondPath").path()).isEqualTo("secondPath");
        assertThat(make("firstPath/secondPath").replacePath("secondPath", "thirdPath").path())
                .isEqualTo("firstPath/thirdPath");
        assertThat(make("firstPath/secondPath").replacePath("secondPath", "thirdPath/forthPath").path())
                .isEqualTo("firstPath/thirdPath/forthPath");
        assertThat(make("firstPath/secondPath").replacePath("firstPath/secondPath", "thirdPath").path())
                .isEqualTo("thirdPath");
        assertThat(make("firstPath/secondPath").replacePath("secondPath", "thirdPath?param=value").path())
                .isEqualTo("firstPath/thirdPath");
        assertThat(make("firstPath/secondPath").replacePath("secondPath", "thirdPath?param=value#fragment").path())
                .isEqualTo("firstPath/thirdPath");
    }

    @Test
    public void givenStateHistoryToken_whenReplaceLastPath_thenThePathShouldBeReplacedWithTheReplacement() {
        assertThat(make("").replaceLastPath("secondPath").path()).isEmpty();
        assertThat(make("firstPath").replaceLastPath("secondPath").path()).isEqualTo("secondPath");
        assertThat(make("firstPath/secondPath").replaceLastPath("thirdPath").path()).isEqualTo("firstPath/thirdPath");
        assertThat(make("firstPath/secondPath").replaceLastPath(null).path()).isEqualTo("firstPath/null");
    }

    @Test
    public void givenStateHistoryToken_whenReplaceAllPath_thenThePathShouldBeAllReplacedwithTheReplacement() {
        assertThat(make("").replaceAllPaths("firstPath").path()).isEqualTo("firstPath");
        assertThat(make("firstPath/secondPath").replaceAllPaths("thirdPath/forthPath").path()).isEqualTo("thirdPath/forthPath");
        assertThat(make("firstPath/secondPath").replaceAllPaths(null).path()).isEqualTo("null");
    }

    @Test
    public void givenStateHistoryToken_whenClearPath_thenThePathShouldBeEmpty() {
        assertThat(make("firstPath/secondPath").clearPaths().path()).isEmpty();
    }

    @Test
    public void givenStateHistoryToken_whenRemovingPath_thenThePathShouldBeRemoved() throws Exception {
        assertThat(make("").removePath("firstPath").path()).isEmpty();
        assertThat(make("firstPath").removePath("firstPath").path()).isEmpty();
        assertThat(make("firstPath/secondPath").removePath("secondPath").path()).isEqualTo("firstPath");
        assertThat(make("firstPath/secondPath/thirdPath").removePath("secondPath/thirdPath").path()).isEqualTo("firstPath");
    }

    @Test
    public void givenStateHistoryToken_whenAskingForQueryParameters_thenShouldReturnMapOfParameters() throws Exception {
        assertThat(make("").queryParameters()).isEmpty();
        assertThat(make("firstPath/secondPath").queryParameters()).isEmpty();
        assertThat(make("firstPath?param=value").queryParameters()).containsExactly(entry("param", "value"));
        assertThat(make("firstPath?param1=value1&param2=value2").queryParameters())
                .containsExactly(entry("param1", "value1"), entry("param2", "value2"));
    }

    @Test
    public void givenStateHistoryToken_whenAskingForHavingParameter_thenShouldReturnTrueIfFound() throws Exception {
        assertThat(make("").hasQueryParameter("param1")).isFalse();
        assertThat(make("firstPath?param1=value1").hasQueryParameter("param1")).isTrue();
        assertThat(make("firstPath?param1=value1").hasQueryParameter("value1")).isFalse();
    }

    @Test
    public void givenStateHistoryToken_whenAskingForParameterValue_thenShouldReturnTheValue() throws Exception {
        assertThat(make("").parameterValue("param1")).isNull();
        assertThat(make("firstPath?param1=value1").parameterValue("param1")).isEqualTo("value1");
        assertThat(make("firstPath?param1=value1").parameterValue("value1")).isNull();
    }

    @Test
    public void givenStateHistoryToken_whenAppendingParameter_thenTheParameterShouldBeAdded() throws Exception {
        assertThat(make("firstPath").appendParameter("param1", "value1").queryParameters()).contains(entry("param1", "value1"));
        assertThat(make("firstPath").appendParameter(null, "value1").queryParameters()).contains(entry("null", "value1"));
        assertThat(make("firstPath").appendParameter("param1", null).queryParameters()).contains(entry("param1", "null"));
    }

    @Test
    public void givenStateHistoryToken_whenReplacingParameter_thenTheParameterShouldBeReplaced() throws Exception {
        assertThat(make("firstPath").replaceParameter("param1", "param2", "value2").queryParameters())
                .doesNotContain(entry("param2", "value2"));
        assertThat(make("firstPath?param1=value1").replaceParameter("param1", "param2", "value2").queryParameters())
                .containsExactly(entry("param2", "value2"));
        assertThat(make("firstPath?param1=value1&param2=value2").replaceParameter("param1", "param3", "value2").queryParameters())
                .containsExactly(entry("param3", "value2"), entry("param2", "value2"));
    }

    @Test
    public void givenStateHistoryToken_whenReplacingAllQuery_thenTheQueryShouldBeReplacedWithTheNewOne() throws Exception {
        assertThat(make("").replaceQuery("param=value").queryParameters()).containsExactly(entry("param", "value"));
        assertThat(make("firstPath?param1=value1").replaceQuery("param2=value2").queryParameters()).containsExactly(entry("param2", "value2"));
        assertThat(make("firstPath?param1=value1&param2=value2").replaceQuery("param3=value3").queryParameters()).containsExactly(entry("param3", "value3"));
    }

    @Test
    public void givenStateHistoryToken_whenClearingQuery_thenTheQueryShouldBeCleared() throws Exception {
        assertThat(make("").clearQuery().queryParameters()).isEmpty();
        assertThat(make("firstPath?param=value").clearQuery().queryParameters()).isEmpty();
    }

    @Test
    public void givenStateHistoryToken_whenRemovingParameter_thenTheParameterShouldBeRemovedFromTheQuery() throws Exception {
        assertThat(make("").removeParameter("param").queryParameters()).isEmpty();
        assertThat(make("firstPath?param=value").removeParameter("param").queryParameters()).isEmpty();
        assertThat(make("firstPath?param=value").removeParameter("param1").queryParameters())
                .containsExactly(entry("param", "value"));
    }

    @Test
    public void givenStateHistoryToken_whenAskingForFragment_thenShouldReturnIt() throws Exception {
        assertThat(make("").fragment()).isEmpty();
        assertThat(make("#fragment").fragment()).isEqualTo("fragment");
        assertThat(make("firstPath?param=value#fragment").fragment()).isEqualTo("fragment");
    }

    @Test
    public void givenStateHistoryToken_whenRemovingFragment_thenTheFragmentShouldBeRemoved() throws Exception {
        assertThat(make("").clearFragments().fragment()).isEmpty();
        assertThat(make("#fragment").clearFragments().fragment()).isEmpty();
    }

    @Test
    public void givenStateHistoryToken_whenSettingFragment_thenTheFragmentShouldBeReplacedWithTheNewValue() throws Exception {
        assertThat(make("").appendFragment("fragment").fragment()).isEqualTo("fragment");
        assertThat(make("#fragment").appendFragment("newFragment").fragment()).isEqualTo("fragment/newFragment");
    }

    @Test
    public void givenStateHistoryToken_whenAskingForTokenValue_thenShouldReturnUrlString() throws Exception {
        assertThat(make("").value()).isEmpty();
        assertThat(make("firstPath").value()).isEqualTo("firstPath");
        assertThat(make("firstPath?param=value").value()).isEqualTo("firstPath?param=value");
        assertThat(make("firstPath?param=value#fragment").value()).isEqualTo("firstPath?param=value#fragment");
        assertThat(make("firstPath?param=value#fragment").
                replacePath("firstPath", "secondPath").
                value()).isEqualTo("secondPath?param=value#fragment");
    }

    @Test
    public void givenStateHistoryToken_whenClearingHistoryToken_thenTheValueShouldBeEmpty() throws Exception {
        assertThat(make("").clear().value()).isEmpty();
        assertThat(make("firstPath").clear().value()).isEmpty();
        assertThat(make("firstPath?param=value").clear().value()).isEmpty();
        assertThat(make("firstPath?param=value#fragment").clear().value()).isEmpty();
    }
}
