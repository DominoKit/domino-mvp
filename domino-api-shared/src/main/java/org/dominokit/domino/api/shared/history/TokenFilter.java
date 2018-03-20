package org.dominokit.domino.api.shared.history;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.nonNull;

public interface TokenFilter {

    static final Logger LOGGER= LoggerFactory.getLogger(TokenFilter.class);
    boolean filter(HistoryToken token);

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

    static TokenFilter anyFragment(){
        return new TokenFilter.AnyFragmentFilter();
    }

    class AnyFilter implements TokenFilter {
        @Override
        public boolean filter(HistoryToken token) {
            return true;
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
    }

    class AnyFragmentFilter implements TokenFilter {

        @Override
        public boolean filter(HistoryToken token) {
            return nonNull(token.fragment()) && !token.fragment().isEmpty();
        }
    }

}
