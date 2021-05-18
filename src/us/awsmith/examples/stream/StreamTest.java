package us.awsmith.examples.stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Stream")
public class StreamTest {
    private final ArrayList<Integer> source;
    private final ArrayList<Integer> result;

    /** The number of integers to be inserted into the source list. */
    private static final int SOURCE_ELEMENTS = 10;

    public StreamTest() {
        source = new ArrayList<>();
        result = new ArrayList<>();
    }

    @BeforeEach
    public void initialiseLists() {
        source.clear();
        result.clear();

        for (int i = 0; i < SOURCE_ELEMENTS; i++) {
            source.add(i);
        }
    }

    /** Assert that the result list contains all of the specified values. */
    private void assertListContains(int... values) {
        assertEquals(result.size(), values.length);
        for (int i = 0; i < values.length; i++) {
            assertEquals(result.get(i), values[i]);
        }
    }

    @Test
    @DisplayName("Collect items from empty stream")
    public void collectEmpty() {
        source.clear();
        Stream<Integer> stream = new IterableStream<>(source);
        assertFalse(stream.collect(result));
        assertListContains();
    }

    @Test
    @DisplayName("Collect items from populated stream")
    public void collectFull() {
        Stream<Integer> stream = new IterableStream<>(source);
        assertTrue(stream.collect(result));
        assertListContains(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    @Test
    @DisplayName("Filter stream retaining all elements")
    public void filterRetainAll() {
        Stream<Integer> stream = new IterableStream<>(source).filter($ -> true);
        assertTrue(stream.collect(result));
        assertListContains(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    @Test
    @DisplayName("Filter stream retaining even elements")
    public void filterRetainEven() {
        Stream<Integer> stream = new IterableStream<>(source).filter(x -> x % 2 == 0);
        assertTrue(stream.collect(result));
        assertListContains(0, 2, 4, 6, 8);
    }

    @Test
    @DisplayName("Filter stream retaining no elements")
    public void filterRetainNone() {
        Stream<Integer> stream = new IterableStream<>(source).filter($ -> false);
        assertFalse(stream.collect(result));
        assertListContains();
    }

    @Test
    @DisplayName("Map stream values to same type as source")
    public void mapSameType() {
        Stream<Integer> stream = new IterableStream<>(source).map(x -> x % 3);
        assertTrue(stream.collect(result));
        assertListContains(0, 1, 2, 0, 1, 2, 0, 1, 2, 0);
    }

    @Test
    @DisplayName("Map stream values to different type from source")
    public void mapDifferentType() {
        Stream<Boolean> stream = new IterableStream<>(source).map(x -> x % 3 == 0);
        ArrayList<Boolean> boolResult = new ArrayList<>();
        assertTrue(stream.collect(boolResult));

        for (int i = 0; i < SOURCE_ELEMENTS; i++) {
            assertEquals(boolResult.get(i), i % 3 == 0);
        }
    }

    @Test
    @DisplayName("Empty stream reduces to initial value")
    public void reduceEmpty() {
        source.clear();
        int reduced = new IterableStream<>(source).reduce(437, Integer::sum);
        assertEquals(reduced, 437);
    }

    @Test
    @DisplayName("Populated stream reduces including initial value")
    public void reducePopulated() {
        int reduced = new IterableStream<>(source).reduce(10, Integer::sum);
        assertEquals(reduced, 55);
    }
}
