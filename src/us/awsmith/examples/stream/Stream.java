package us.awsmith.examples.stream;

import java.util.Collection;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A limited, fully serial subset of the Java 8+ Stream interface used to
 * demonstrate the implementation of fundamental functional programming
 * constructs.
 *
 * @param <E> The type of the values contained in this stream.
 */
public interface Stream<E> {
    /**
     * Add all of the elements in the fully-evaluated version of this stream to
     * the specified destination collection, detecting whether the collection
     * has changed as a result of this operation.
     *
     * @implSpec This is a terminal operation.
     * @param destination The sink to which to add all elements in this stream.
     * @param <R> The type of the <code>destination</code> sink.
     * @return true if <code>destination</code> has changed as a result of this
     *         operation; false otherwise.
     */
    <R extends Collection<E>> boolean collect(R destination);

    /**
     * Generate a new stream consisting only of the values in this stream that
     * satisfy the specified predicate.  Any values that do not satisfy the
     * predicate will be discarded.
     *
     * @implSpec This is an intermediate operation.
     * @param predicate The Boolean function to evaluate for satisfiability upon
     *                  each element in this stream.
     * @return A new stream consisting solely of the values in this stream that
     *         satisfy <code>predicate</code>.
     */
    Stream<E> filter(Predicate<E> predicate);

    /**
     * Generate a new stream consisting of the results of the specified
     * transformation function called upon each value in this stream.
     *
     * @implSpec This is an intermediate operation.
     * @param transform The function executed upon each value in this stream.
     * @param <R> The type of the values returned by <code>transform</code>.
     * @return A new stream with values transformed by <code>transform</code>.
     */
    <R> Stream<R> map(Function<E, R> transform);

    /**
     * Perform a reduction upon the elements in this stream, applying the
     * specified accumulator value to each element in the stream and the value
     * accumulated so far, which is initially given by <code>initial</code>.
     *
     * @implSpec This is a terminal operation.
     * @param initial The operand to be passed to the accumulator along with the
     *                first value in this stream.  Should almost definitely not
     *                be <code>null</code> for any practical use case.
     * @param accumulator The function to be executed upon each element of the
     *                    stream along with the value accumulated so far.
     * @return A reduction of the values in this stream by the accumulator.
     */
    E reduce(E initial, BinaryOperator<E> accumulator);
}
