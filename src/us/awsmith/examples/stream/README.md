## `us.awsmith.examples.stream`

This package contains a simplified version of the Java 8+ `Stream` interface that implements only the necessary
functionality to demonstrate fundamental point-free functional programming concepts in as little code as possible.  All
implementations are thoroughly documented, so if you would like some explanations as to how these functional primitives
work, feel free to read through the code!  This package was initially developed for a lecture given to the Spring 2021
offering of [CS 314 Data Structures](https://www.cs.utexas.edu/~scottm/cs314/) at the University of Texas at Austin.

### `Stream`
The `Stream` interface defines the basic protocol to which all streams must conform, supporting the "holy trinity" of
functional operations &mdash; `map`, `filter`, and `reduce` &mdash; and some aggregate observations for observability.
The documentation for these operations is written in this interface and inherited by all implementing classes.  Note
that this interface makes *extremely* heavy use of Java generic methods, as does Java's built-in stream functionality;
if this syntax is unfamiliar to you, refer to the [Java tutorial](https://docs.oracle.com/javase/tutorial/extra/generics/methods.html).

### `IterableStream`
This class is an implementation of the `Stream` interface over an `Iterable` type.  External classes may only construct
this stream from an instance of an `Iterable` type to ensure external immutability; however, it also makes heavy use of
[anonymous classes](https://docs.oracle.com/javase/tutorial/java/javaOO/anonymousclasses.html) to implement intermediate
(non-mutating) operations internally.  The operations implemented in this class are also *lazy*: they wait until the
last possible moment when an aggregate (or *terminal*) operation is run to exhaust their internal `Iterator`s.  As a
result, a large number of functional operations may be chained with no performance penalty until a terminal operation is
executed upon the resultant stream.

### `StreamTest`
This class contains a small [JUnit 5](https://junit.org/junit5/) test suite for the streams implemented in this package.
You can read through the test suite to gain an understanding of the streams in use; if you choose to add your own
streams, you may also trivially modify the tests for `IterableStream` to attain relatively complete test coverage.
