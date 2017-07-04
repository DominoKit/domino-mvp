package com.progressoft.brix.domino.api.shared.history;

public interface TokenFilter {
    boolean filter(String token);

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

    static TokenFilter any(){
        return new TokenFilter.AnyFilter();
    }

    class AnyFilter implements TokenFilter {
        @Override
        public boolean filter(String token) {
            return true;
        }
    }

    class ExactMatchFilter implements TokenFilter {
        private final String matchingToken;

        ExactMatchFilter(String matchingToken) {
            this.matchingToken = matchingToken;
        }

        @Override
        public boolean filter(String token) {
            return token.equals(matchingToken);
        }
    }

    class StartsWithFilter implements TokenFilter {
        private final String prefix;

        StartsWithFilter(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public boolean filter(String token) {
            return token.startsWith(prefix);
        }
    }

    class EndsWithFilter implements TokenFilter {
        private final String postfix;

        EndsWithFilter(String postfix) {
            this.postfix = postfix;
        }

        @Override
        public boolean filter(String token) {
            return token.endsWith(postfix);
        }
    }

    class ContainsFilter implements TokenFilter {
        private final String matchingPart;

        ContainsFilter(String matchingPart) {
            this.matchingPart = matchingPart;
        }

        @Override
        public boolean filter(String token) {
            return token.contains(matchingPart);
        }
    }
}
