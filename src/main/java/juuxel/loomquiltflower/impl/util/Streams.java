package juuxel.loomquiltflower.impl.util;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.function.ToIntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Streams {
    public static <C, T> Stream<T> iterate(C collection, ToIntFunction<C> length, ElementAtFunction<C, T> elementAt) {
        return IntStream.range(0, length.applyAsInt(collection))
            .mapToObj(i -> elementAt.elementAt(collection, i));
    }

    public static Stream<Node> of(NodeList nodes) {
        return iterate(nodes, NodeList::getLength, NodeList::item);
    }

    public interface ElementAtFunction<C, T> {
        T elementAt(C collection, int index);
    }
}
