package org.dominokit.domino.api.server.request;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class MultiValuesMapTest {

    private static final String NON_EXIST = "nonExist";
    private static final String KEY = "key";
    private static final String KEY_2 = "key2";
    private static final String VALUE_1 = "value1";
    private static final String VALUE_2 = "value2";
    private static final String VALUE_12 = "value12";

    private DefaultMultiMap<String, String> map;

    @Before
    public void setUp() throws Exception {
        map = new DefaultMultiMap<>();
    }

    @Test
    public void givenMap_whenGettingNonExistItem_ShouldReturnNull() throws Exception {
        assertThat(map.get(NON_EXIST)).isNull();
    }

    @Test
    public void givenMap_whenGettingWithNullKey_ShouldReturnNull() throws Exception {
        assertThat(map.get(null)).isNull();
    }

    @Test
    public void givenMap_whenGettingValueForItem_ShouldReturnTheFirstValue() throws Exception {
        put(KEY, VALUE_1, VALUE_2);
        assertThat(map.get(KEY)).isEqualTo(VALUE_1);
    }

    @Test
    public void givenMap_whenGettingAllValuesForNonExistItem_ShouldReturnEmptyList() throws Exception {
        assertThat(map.getAll(NON_EXIST)).isEmpty();
    }

    @Test
    public void givenMap_whenGettingAllValuesForExistingItem_ShouldReturnListOfValues() throws Exception {
        put(KEY, VALUE_1, VALUE_2);
        assertThat(map.getAll(KEY)).containsExactly(VALUE_1, VALUE_2);
    }

    @Test
    public void givenMap_whenGettingAllEntries_thenShouldReturnAllValuesInSeparateEntry() throws Exception {
        put(KEY, VALUE_1, VALUE_2);
        put(KEY_2, VALUE_12, "value22");
        assertThat(map.entries()).containsExactly(entry(KEY, VALUE_1),
                entry(KEY, VALUE_2),
                entry(KEY_2, VALUE_12),
                entry(KEY_2, "value22"));
    }

    @Test
    public void givenMap_whenGettingAllEntriesWithNullValues_thenExcludeTheseItemsFromTheEntries() throws Exception {
        map.values.put(KEY, null);
        put(KEY_2, VALUE_12, "value22");
        assertThat(map.entries()).containsExactly(entry(KEY_2, VALUE_12),
                entry(KEY_2, "value22"));
    }

    @Test
    public void givenMap_whenCheckingForContainsNonExistItem_shouldReturnFalse() throws Exception {
        assertThat(map.contains(NON_EXIST)).isFalse();
    }

    @Test
    public void givenMap_whenCheckingForContainsExistingItem_shouldReturnTrue() throws Exception {
        put(KEY, VALUE_1);
        assertThat(map.contains(KEY)).isTrue();
    }

    @Test
    public void givenEmptyMap_whenCheckingIfEmpty_shouldReturnTrue() throws Exception {
        assertThat(map.isEmpty()).isTrue();
    }

    @Test
    public void givenNonEmptyMap_whenCheckingIfEmpty_shouldReturnFalse() throws Exception {
        put(KEY, VALUE_1);
        assertThat(map.isEmpty()).isFalse();
    }

    @Test
    public void givenMap_whenGettingAllNames_shouldReturnAllItemsNames() throws Exception {
        put(KEY, VALUE_1);
        put(KEY_2, VALUE_2);
        assertThat(map.names()).contains(KEY, KEY_2);
    }

    @Test(expected = NullPointerException.class)
    public void givenMap_whenAddWithNullKey_shouldThrowException() throws Exception {
        map.add(null, VALUE_1);
    }

    @Test(expected = NullPointerException.class)
    public void givenMap_whenAddWithNullValue_shouldThrowException() throws Exception {
        map.add(KEY, (String) null);
    }

    @Test
    public void givenMap_whenAddValueWithNewKey_shouldCreateNewListAndAddTheValue() throws Exception {
        map.add(KEY, VALUE_1);
        assertThat(map.getAll(KEY)).size().isEqualTo(1);
        assertThat(map.getAll(KEY)).containsExactly(VALUE_1);
    }

    @Test
    public void givenMap_whenAddValueWithExistingKey_shouldAppendTheValueToItsList() throws Exception {
        map.add(KEY, VALUE_1);
        map.add(KEY, VALUE_2);
        assertThat(map.getAll(KEY)).size().isEqualTo(2);
        assertThat(map.getAll(KEY)).containsExactly(VALUE_1, VALUE_2);
    }

    @Test
    public void givenMap_whenAddValuesWithExistingKey_shouldAppendTheValueToItsList() throws Exception {
        map.add(KEY, Arrays.asList(VALUE_1, VALUE_2));
        assertThat(map.getAll(KEY)).size().isEqualTo(2);
        assertThat(map.getAll(KEY)).containsExactly(VALUE_1, VALUE_2);
    }

    @Test(expected = NullPointerException.class)
    public void givenMap_whenAddValuesPassingNullList_shouldThrowException() throws Exception {
        map.add(KEY, (List<String>) null);
    }

    @Test
    public void givenMap_whenAddingAnotherMultiValueMap_shouldMergeTheTwoMaps() throws Exception {
        MultiMap<String, String> otherMap = new DefaultMultiMap<>();
        otherMap.add(KEY_2, VALUE_2);
        map.add(KEY, VALUE_1);

        map.addAll(otherMap);

        assertThat(map.entries()).containsExactly(entry(KEY, VALUE_1), entry(KEY_2, VALUE_2));
    }

    @Test
    public void givenMap_whenAddingAnotherMultiValueMapThatHasSameKey_shouldMergeTheTwoMaps() throws Exception {
        MultiMap<String, String> otherMap = new DefaultMultiMap<>();
        otherMap.add(KEY, VALUE_2);
        map.add(KEY, VALUE_1);

        map.addAll(otherMap);

        assertThat(map.entries()).containsExactly(entry(KEY, VALUE_1), entry(KEY, VALUE_2));
    }

    @Test(expected = NullPointerException.class)
    public void givenMap_whenAddingAnotherMultiValueMapPassingNull_shouldThrowException() throws Exception {
        map.addAll((MultiMap<String, String>) null);
    }

    @Test
    public void givenMap_whenAddingAnotherMap_shouldMergeTheTwoMaps() throws Exception {
        Map<String, String> otherMap = new HashMap<>();
        otherMap.put(KEY_2, VALUE_2);
        map.add(KEY, VALUE_1);
        map.addAll(otherMap);

        assertThat(map.entries()).containsExactly(entry(KEY, VALUE_1), entry(KEY_2, VALUE_2));
    }

    @Test(expected = NullPointerException.class)
    public void givenMap_whenAddingAnotherMapPassingNull_shouldThrowException() throws Exception {
        map.addAll((Map<String, String>) null);
    }

    @Test
    public void givenMap_whenSettingItem_shouldClearAllValuesAndAddThisValueOnly() throws Exception {
        map.add(KEY, VALUE_1);
        map.set(KEY, VALUE_2);

        assertThat(map.getAll(KEY)).containsExactly(VALUE_2);
    }

    @Test(expected = NullPointerException.class)
    public void givenMap_whenSettingItemPassingNullKey_shouldThrowException() throws Exception {
        map.set(null, VALUE_1);
    }

    @Test(expected = NullPointerException.class)
    public void givenMap_whenSettingItemPassingNullValue_shouldThrowException() throws Exception {
        map.set(KEY, (String) null);
    }

    @Test
    public void givenMap_whenSettingItemWithMultiValues_shouldClearAllValuesAndAddTheNewValues() throws Exception {
        map.add(KEY, VALUE_1);
        map.set(KEY, Arrays.asList(VALUE_2, VALUE_12));

        assertThat(map.getAll(KEY)).containsExactly(VALUE_2, VALUE_12);
    }

    @Test(expected = NullPointerException.class)
    public void givenMap_whenSettingItemWithNullKey_shouldThrowException() throws Exception {
        map.set(null, Arrays.asList(VALUE_2, VALUE_12));
    }

    @Test(expected = NullPointerException.class)
    public void givenMap_whenSettingItemWithNullValues_shouldThrowException() throws Exception {
        map.set(KEY, (List<String>) null);
    }

    @Test
    public void givenMap_whenSettingOtherMultiValuesMap_shouldClearAndAddNewItems() throws Exception {
        MultiMap<String, String> otherMap = new DefaultMultiMap<>();
        otherMap.add(KEY, Arrays.asList(VALUE_1, VALUE_2));
        map.setAll(otherMap);

        assertThat(map.entries()).containsExactly(entry(KEY, VALUE_1), entry(KEY, VALUE_2));
    }

    @Test(expected = NullPointerException.class)
    public void givenMap_whenSettingOtherMultiValuesMapPassingNull_shouldThrowException() throws Exception {
        map.setAll((MultiMap<String, String>) null);
    }

    @Test
    public void givenMap_whenSettingOtherMap_shouldClearAndAddNewItems() throws Exception {
        Map<String, String> otherMap = new HashMap<>();
        otherMap.put(KEY, VALUE_1);
        otherMap.put(KEY_2, VALUE_2);

        map.setAll(otherMap);

        assertThat(map.entries()).containsExactlyInAnyOrder(entry(KEY, VALUE_1), entry(KEY_2, VALUE_2));
    }

    @Test(expected = NullPointerException.class)
    public void givenMap_whenSettingOtherMapPassingNull_shouldThrowException() throws Exception {
        map.setAll((Map<String, String>) null);
    }

    @Test(expected = NullPointerException.class)
    public void givenMap_whenTryingToRemoveNullKey_shouldThrowException() throws Exception {
        map.remove(null);
    }

    @Test
    public void givenMap_whenTryingToRemoveKey_shouldRemoveItFromTheMap() throws Exception {
        map.add(KEY, VALUE_1);
        map.remove(KEY);

        assertThat(map.contains(KEY)).isFalse();
    }

    @Test
    public void givenMap_whenClearMap_thenShouldBeEmpty() throws Exception {
        map.clear();

        assertThat(map.isEmpty()).isTrue();
    }

    @Test
    public void givenMap_whenAskingForItsSize_shouldReturnNumberOfItems() throws Exception {
        map.add(KEY, VALUE_1);

        assertThat(map.size()).isEqualTo(1);
    }

    @Test
    public void givenMap_whenAskingForIterator_thenShouldReturnIteratorContainsAllValues() throws Exception {
        map.add(KEY, VALUE_1);
        map.add(KEY_2, VALUE_2);
        Iterator<Map.Entry<String, String>> iterator = map.iterator();

        assertThat(iterator).containsExactlyInAnyOrder(entry(KEY, VALUE_1), entry(KEY_2, VALUE_2));
    }

    private void put(String key, String value, String... values) {
        List<String> listValues = new LinkedList<>();
        listValues.add(value);
        listValues.addAll(Arrays.asList(values));
        map.values.put(key, listValues);
    }
}
