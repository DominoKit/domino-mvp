package org.dominokit.domino.api.shared.history;

import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

public class TokenNormalizer {

    public static DefaultNormalizedToken normalize(String original, String target) {
        if (validateToken(target)) return new DefaultNormalizedToken(new StateHistoryToken(original));

        DefaultNormalizedToken normalizedToken = new DefaultNormalizedToken();
        StateHistoryToken originalToken = new StateHistoryToken(original);
        StateHistoryToken targetToken = new StateHistoryToken(target);

        normalizePaths(normalizedToken, originalToken, targetToken);
        normalizeParameters(originalToken, targetToken);
        normalizeFragments(normalizedToken, originalToken, targetToken);

        normalizedToken.setToken(originalToken);

        return normalizedToken;
    }

    private static void normalizePaths(DefaultNormalizedToken normalizedToken, StateHistoryToken originalToken, StateHistoryToken targetToken) {
        List<String> originalPaths = originalToken.paths();
        List<String> targetPaths = targetToken.paths();

        int maxIndex = originalPaths.size() < targetPaths.size() ? originalPaths.size() : targetPaths.size();
        for (int i = 0; i < maxIndex; i++) {
            checkAndReplacePath(normalizedToken, originalToken, originalPaths, targetPaths, i, i);
        }
    }

    private static void normalizePathsTail(DefaultNormalizedToken normalizedToken, StateHistoryToken originalToken, StateHistoryToken targetToken) {
        List<String> originalPaths = originalToken.paths();
        List<String> targetPaths = targetToken.paths();

        if (originalPaths.size() > 0 && targetPaths.size() > 0) {
            int originalIndex = originalPaths.size() - 1;
            int targetIndex = targetPaths.size() - 1;
            int resultIndex = originalIndex;

            for (int i = targetIndex; resultIndex >= 0 && i >= 0; i--) {
                resultIndex = originalIndex - (targetIndex - i);
                checkAndReplacePath(normalizedToken, originalToken, originalPaths, targetPaths, resultIndex, i);
            }
        }
    }

    private static void checkAndReplacePath(DefaultNormalizedToken normalizedToken, StateHistoryToken originalToken, List<String> originalPaths, List<String> targetPaths, int resultIndex, int i) {
        String path = targetPaths.get(i);
        if (path.startsWith(":")) {
            if (resultIndex> -1 && resultIndex < originalPaths.size()) {
                String originalPath = originalPaths.get(resultIndex);
                originalToken.replacePath(originalPath, path);
                normalizedToken.addPathParameter(path.replace(":", ""), originalPath);
            }
        }
    }

    private static void normalizeParameters(StateHistoryToken originalToken, StateHistoryToken targetToken) {
        Map<String, String> originalParameters = originalToken.queryParameters();
        Map<String, String> targetParameters = targetToken.queryParameters();

        targetParameters.entrySet().forEach(entrySet -> {
            if (entrySet.getValue().startsWith(":") && originalParameters.containsKey(entrySet.getKey())) {
                originalToken.replaceParameter(entrySet.getKey(), entrySet.getKey(), entrySet.getValue());
            }
        });
    }

    private static void normalizeFragments(DefaultNormalizedToken normalizedToken, StateHistoryToken originalToken, StateHistoryToken targetToken) {
        List<String> originalFragments = originalToken.fragments();
        List<String> targetFragments = targetToken.fragments();

        int maxIndex = originalFragments.size() < targetFragments.size() ? originalFragments.size() : targetFragments.size();
        for (int i = 0; i < maxIndex; i++) {
            checkAndReplaceFragment(normalizedToken, originalToken, originalFragments, targetFragments, i, i);
        }
    }

    private static void normalizeFragmentsTail(DefaultNormalizedToken normalizedToken, StateHistoryToken originalToken, StateHistoryToken targetToken) {
        List<String> originalFragments = originalToken.fragments();
        List<String> targetFragments = targetToken.fragments();

        if (originalFragments.size() > 0 && targetFragments.size() > 0) {
            int originalIndex = originalFragments.size() - 1;
            int targetIndex = targetFragments.size() - 1;
            int resultIndex = originalIndex;

            for (int i = targetIndex; resultIndex >= 0 && i >= 0; i--) {
                resultIndex = originalIndex - (targetIndex - i);
                checkAndReplaceFragment(normalizedToken, originalToken, originalFragments, targetFragments, resultIndex, i);
            }
        }
    }

    private static void checkAndReplaceFragment(DefaultNormalizedToken normalizedToken, StateHistoryToken originalToken, List<String> originalFragments, List<String> targetFragments, int resultIndex, int i) {
        String fragment = targetFragments.get(i);
        if (fragment.startsWith(":")) {
            if (resultIndex> -1 && resultIndex < originalFragments.size()) {
                String originalFragment = originalFragments.get(resultIndex);
                originalToken.replaceFragment(originalFragment, fragment);
                normalizedToken.addFragmentParameter(fragment.replace(":", ""), originalFragment);
            }
        }
    }

    public static NormalizedToken normalizeTail(String original, String target) {
        if (validateToken(target)) return new DefaultNormalizedToken(new StateHistoryToken(original));

        DefaultNormalizedToken normalizedToken = new DefaultNormalizedToken();
        StateHistoryToken originalToken = new StateHistoryToken(original);
        StateHistoryToken targetToken = new StateHistoryToken(target);

        normalizePathsTail(normalizedToken, originalToken, targetToken);
        normalizeParameters(originalToken, targetToken);
        normalizeFragmentsTail(normalizedToken, originalToken, targetToken);

        normalizedToken.setToken(originalToken);

        return normalizedToken;
    }

    private static boolean validateToken(String target) {
        if (isNull(target) || target.trim().isEmpty() || !target.contains(":"))
            return true;
        return false;
    }

    public static NormalizedToken normalizeFragmentsTail(String original, String target) {
        if (validateToken(target)) return new DefaultNormalizedToken(new StateHistoryToken(original));

        DefaultNormalizedToken normalizedToken = new DefaultNormalizedToken();
        StateHistoryToken originalToken = new StateHistoryToken(original);
        StateHistoryToken targetToken = new StateHistoryToken(target);

        normalizeFragmentsTail(normalizedToken, originalToken, targetToken);

        normalizedToken.setToken(originalToken);

        return normalizedToken;
    }

    public static NormalizedToken normalizePathTail(String original, String target) {
        if (validateToken(target)) return new DefaultNormalizedToken(new StateHistoryToken(original));

        DefaultNormalizedToken normalizedToken = new DefaultNormalizedToken();
        StateHistoryToken originalToken = new StateHistoryToken(original);
        StateHistoryToken targetToken = new StateHistoryToken(target);

        normalizePathsTail(normalizedToken, originalToken, targetToken);

        normalizedToken.setToken(originalToken);

        return normalizedToken;
    }

    public static DefaultNormalizedToken normalizePaths(String original, String target) {
        if (validateToken(target)) return new DefaultNormalizedToken(new StateHistoryToken(original));

        DefaultNormalizedToken normalizedToken = new DefaultNormalizedToken();
        StateHistoryToken originalToken = new StateHistoryToken(original);
        StateHistoryToken targetToken = new StateHistoryToken(target);

        normalizePaths(normalizedToken, originalToken, targetToken);

        normalizedToken.setToken(originalToken);

        return normalizedToken;
    }

    public static DefaultNormalizedToken normalizeFragments(String original, String target) {
        if (validateToken(target)) return new DefaultNormalizedToken(new StateHistoryToken(original));

        DefaultNormalizedToken normalizedToken = new DefaultNormalizedToken();
        StateHistoryToken originalToken = new StateHistoryToken(original);
        StateHistoryToken targetToken = new StateHistoryToken(target);

        normalizeFragments(normalizedToken, originalToken, targetToken);

        normalizedToken.setToken(originalToken);

        return normalizedToken;
    }

    public static DefaultNormalizedToken normalizeParameters(String original, String target) {
        if (validateToken(target)) return new DefaultNormalizedToken(new StateHistoryToken(original));

        DefaultNormalizedToken normalizedToken = new DefaultNormalizedToken();
        StateHistoryToken originalToken = new StateHistoryToken(original);
        StateHistoryToken targetToken = new StateHistoryToken(target);

        normalizeParameters(originalToken, targetToken);

        normalizedToken.setToken(originalToken);

        return normalizedToken;
    }

}
