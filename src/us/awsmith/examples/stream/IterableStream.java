package us.awsmith.examples.stream;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An implementation of the Stream interface sourcing values from a standard
 * <code>Iterator</code>.  The values from the Iterator are not accessed until a
 * terminal operation is executed, so the underlying structure should not be
 * modified to avoid <code>ConcurrentModificationException</code>s.  Note that
 * this is <em>not</em> an issue present in Java's native Streams, as they use
 * substantially more sophisticated logic to avoid exceptions and allow for full
 * data parallelism.
 *
 * @param <E> The type of the values contained in this stream.
 */
public class IterableStream<E> implements Stream<E> {
    /**
     * The source from which all values in this stream are drawn.  Not advanced
     * until a terminal operation is executed.
     */
    private final Iterator<E> source;

    private IterableStream(Iterator<E> source) {
        this.source = source;
    }

    /**
     * Construct a new stream using an iterator sourced from the specified
     * collection.  The source should not be modified until a terminal operation
     * has been executed upon this stream.
     *
     * @param source The collection from which values in the stream are sourced.
     * @param <S> The type of the source collection.
     */
    public <S extends Iterable<E>> IterableStream(S source) {
        this(source.iterator());
    }

    @Override
    public <R extends Collection<E>> boolean collect(R destination) {
        boolean changed = false;
        while (source.hasNext()) {
            changed |= destination.add(source.next());
        }
        return changed;
    }

    @Override
    public Stream<E> filter(Predicate<E> predicate) {
        return new IterableStream<>(new Iterator<>() {
            /** The value to return upon the next call to <code>next()</code>. */
            private E nextElement;
            /** Whether to advance <code>nextElement</code> upon the next call to <code>hasNext().</code> */
            private boolean advance = true;

            public boolean hasNext() {
                if (!advance) {
                    // Shortcut: we have only exhausted this stream once the source has been exhausted.
                    return source.hasNext();
                }

                // Look for the next item in the source that satisfies the predicate.
                advance = false;
                while (source.hasNext()) {
                    E candidate = source.next();
                    if (predicate.test(candidate)) {
                        nextElement = candidate;
                        return true;
                    }
                }

                // There was no such element: stop searching henceforth.
                return false;
            }

            public E next() {
                // Once we have retrieved the current value, we can move on to the next.
                assert !advance;
                advance = true;
                return nextElement;
            }
        });
    }

    @Override
    public <R> Stream<R> map(Function<E, R> transform) {
        return new IterableStream<>(new Iterator<>() {
            public boolean hasNext() {
                // Since mapping does not reduce the number of elements, simply report from the source iterator.
                return source.hasNext();
            }

            public R next() {
                // To map, we apply the transformation function to each source value in sequence.
                assert hasNext();
                return transform.apply(source.next());
            }
        });
    }

    @Override
    public E reduce(E initial, BinaryOperator<E> accumulator) {
        E reduction = initial;
        while (source.hasNext()) {
            reduction = accumulator.apply(reduction, source.next());
        }
        return reduction;
    }
}
