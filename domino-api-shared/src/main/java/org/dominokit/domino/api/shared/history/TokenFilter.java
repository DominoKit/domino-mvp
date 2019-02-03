package org.dominokit.domino.api.shared.history;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static java.util.Objects.nonNull;

public interface TokenFilter {

    Logger LOGGER = LoggerFactory.getLogger(TokenFilter.class);

    boolean filter(HistoryToken token);

    default NormalizedToken normalizeToken(String token) {
        return null;
    }

    static TokenFilter exactMatch(String matchingToken) {
        return new TokenFilter.ExactMatchFilter(matchingToken);
    }

    static TokenFilter startsWith(String prefix) {
        return new TokenFilter.StartsWithFilter(prefix);
    }

    static TokenFilter endsWith(String postfix) {
        return new TokenFilter.EndsWithFilter(postfix);
    }

    static TokenFilter contains(String part) {
        return new TokenFilter.ContainsFilter(part);
    }

    static TokenFilter any() {
        return new TokenFilter.AnyFilter();
    }

    static TokenFilter exactFragmentMatch(String matchingToken) {
        return new TokenFilter.ExactFragmentFilter(matchingToken);
    }

    static TokenFilter startsWithFragment(String prefix) {
        return new TokenFilter.StartsWithFragmentFilter(prefix);
    }

    static TokenFilter endsWithFragment(String postfix) {
        return new TokenFilter.EndsWithFragmentFilter(postfix);
    }

    static TokenFilter containsFragment(String part) {
        return new TokenFilter.ContainsFragmentFilter(part);
    }

    static TokenFilter anyFragment() {
        return new TokenFilter.AnyFragmentFilter();
    }

    static TokenFilter hasPathFilter(String path) {
        return new HasPathFilter(path);
    }

    static TokenFilter hasPathsFilter(String... paths) {
        return new HasPathsFilter(paths);
    }

    static TokenFilter exactPathFilter(String path) {
        return new ExactPathFilter(path);
    }

    static TokenFilter startsWithPathFilter(String path) {
        return new StartsWithPathFilter(path);
    }

    static TokenFilter endsWithPathFilter(String path) {
        return new EndsWithPathFilter(path);
    }

    static TokenFilter anyPathFilter() {
        return new AnyPathFilter();
    }

    static TokenFilter isEmpty(){
        return new EmptyFilter();
    }

    class AnyFilter implements TokenFilter {
        @Override
        public boolean filter(HistoryToken token) {
            return true;
        }

        @Override
        public NormalizedToken normalizeToken(String token) {
            return new DefaultNormalizedToken(token);
        }
    }

    class ExactMatchFilter implements TokenFilter {
        private final String matchingToken;

        ExactMatchFilter(String matchingToken) {
            this.matchingToken = matchingToken;
        }

        @Override
        public boolean filter(HistoryToken token) {
            return token.value().equals(matchingToken);
        }

        @Override
        public NormalizedToken normalizeToken(String token) {
            return TokenNormalizer.normalize(token, matchingToken);
        }
    }

    class StartsWithFilter implements TokenFilter {
        private final String prefix;

        StartsWithFilter(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public boolean filter(HistoryToken token) {
            return token.value().startsWith(prefix);
        }

        @Override
        public NormalizedToken normalizeToken(String token) {
            return TokenNormalizer.normalize(token, prefix);
        }
    }

    class EndsWithFilter implements TokenFilter {
        private final String postfix;

        EndsWithFilter(String postfix) {
            this.postfix = postfix;
        }

        @Override
        public boolean filter(HistoryToken token) {
            return token.value().endsWith(postfix);
        }

        @Override
        public NormalizedToken normalizeToken(String token) {
            return TokenNormalizer.normalizeTail(token, postfix);
        }
    }

    class ContainsFilter implements TokenFilter {
        private final String matchingPart;

        ContainsFilter(String matchingPart) {
            this.matchingPart = matchingPart;
        }

        @Override
        public boolean filter(HistoryToken token) {
            return token.value().contains(matchingPart);
        }

        @Override
        public NormalizedToken normalizeToken(String token) {
            if (token.contains(":")) {
                throw new UnsupportedOperationException("Contains filter cannot normalize token, please remove all variable from filter!");
            }
            return new DefaultNormalizedToken(token);
        }
    }

    class ContainsFragmentFilter implements TokenFilter {
        private final String matchingPart;

        ContainsFragmentFilter(String matchingPart) {
            this.matchingPart = matchingPart;
        }

        @Override
        public boolean filter(HistoryToken token) {
            return token.fragment().contains(matchingPart);
        }

        @Override
        public NormalizedToken normalizeToken(String token) {
            if (token.contains(":")) {
                throw new UnsupportedOperationException("Contains fragment filter cannot normalize token, please remove all variable from filter!");
            }
            return new DefaultNormalizedToken(token);
        }
    }

    class ExactFragmentFilter implements TokenFilter {
        private final String matchingPart;

        ExactFragmentFilter(String matchingPart) {
            this.matchingPart = matchingPart;
        }

        @Override
        public boolean filter(HistoryToken token) {
            return token.fragment().equals(matchingPart);
        }

        @Override
        public NormalizedToken normalizeToken(String token) {
            return TokenNormalizer.normalizeFragments(token, matchingPart);
        }
    }

    class StartsWithFragmentFilter implements TokenFilter {
        private final String prefix;

        StartsWithFragmentFilter(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public boolean filter(HistoryToken token) {
            return token.fragment().startsWith(prefix);
        }

        @Override
        public NormalizedToken normalizeToken(String token) {
            return TokenNormalizer.normalizeFragments(token, prefix);
        }
    }

    class EndsWithFragmentFilter implements TokenFilter {
        private final String postfix;

        EndsWithFragmentFilter(String postfix) {
            this.postfix = postfix;
        }

        @Override
        public boolean filter(HistoryToken token) {
            return token.fragment().endsWith(postfix);
        }

        @Override
        public NormalizedToken normalizeToken(String token) {
            return TokenNormalizer.normalizeFragmentsTail(token, postfix);
        }
    }

    class AnyFragmentFilter implements TokenFilter {

        @Override
        public boolean filter(HistoryToken token) {
            return nonNull(token.fragment()) && !token.fragment().isEmpty();
        }

        @Override
        public NormalizedToken normalizeToken(String token) {
            return new DefaultNormalizedToken(token);
        }
    }

    class HasPathFilter implements TokenFilter {
        private final String path;

        HasPathFilter(String path) {
            this.path = path;
        }

        @Override
        public boolean filter(HistoryToken token) {
            return token.paths().contains(path);
        }

        @Override
        public NormalizedToken normalizeToken(String token) {
            if (token.contains(":")) {
                throw new UnsupportedOperationException("Has path filter cannot normalize token, please remove all variable from filter!");
            }
            return new DefaultNormalizedToken(token);
        }
    }

    class HasPathsFilter implements TokenFilter {
        private final String[] path;

        HasPathsFilter(String... path) {
            this.path = path;
        }

        @Override
        public boolean filter(HistoryToken token) {
            return token.paths().containsAll(Arrays.asList(path));
        }

        @Override
        public NormalizedToken normalizeToken(String token) {
            if (token.contains(":")) {
                throw new UnsupportedOperationException("Has paths filter cannot normalize token, please remove all variable from filter!");
            }
            return new DefaultNormalizedToken(token);
        }
    }

    class ExactPathFilter implements TokenFilter {
        private final String path;

        ExactPathFilter(String path) {
            this.path = path;
        }

        @Override
        public boolean filter(HistoryToken token) {
            return token.path().equals(path);
        }

        @Override
        public NormalizedToken normalizeToken(String token) {
            return TokenNormalizer.normalizePaths(token, path);
        }
    }

    class StartsWithPathFilter implements TokenFilter {
        private final String path;

        StartsWithPathFilter(String path) {
            this.path = path;
        }

        @Override
        public boolean filter(HistoryToken token) {
            return token.path().startsWith(path);
        }

        @Override
        public NormalizedToken normalizeToken(String token) {
            return TokenNormalizer.normalizePaths(token, path);
        }
    }

    class EndsWithPathFilter implements TokenFilter {
        private final String path;

        EndsWithPathFilter(String path) {
            this.path = path;
        }

        @Override
        public boolean filter(HistoryToken token) {
            return token.path().endsWith(path);
        }

        @Override
        public NormalizedToken normalizeToken(String token) {
            return TokenNormalizer.normalizePathTail(token, path);
        }
    }

    class AnyPathFilter implements TokenFilter {
        @Override
        public boolean filter(HistoryToken token) {
            return nonNull(token.path()) && !token.paths().isEmpty();
        }

        @Override
        public NormalizedToken normalizeToken(String token) {
            if (token.contains(":")) {
                throw new UnsupportedOperationException("Has paths filter cannot normalize token, please remove all variable from filter!");
            }
            return new DefaultNormalizedToken(token);
        }
    }

    class EmptyFilter implements TokenFilter {
        @Override
        public boolean filter(HistoryToken token) {
            return token.isEmpty();
        }

        @Override
        public NormalizedToken normalizeToken(String token) {
            return new DefaultNormalizedToken(token);
        }
    }

}
