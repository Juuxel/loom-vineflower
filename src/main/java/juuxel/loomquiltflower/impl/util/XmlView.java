package juuxel.loomquiltflower.impl.util;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A wrapper around a {@link NodeList}. {@code XmlView} allows viewing a list of nodes as if a caller were
 * <ul>
 *     <li>{@linkplain #get getting values from a multimap}
 *     <li>{@linkplain #filter filtering a list of multimaps}
 *     <li>{@linkplain #getTextContent reading text from a single node}.
 * </ul>
 */
public final class XmlView {
    private final NodeList nodes;

    public XmlView(NodeList nodes) {
        this.nodes = nodes;
    }

    /**
     * Creates a view around all elements of child elements with a specific tag name.
     *
     * @param tagName the tag name to query
     * @return the created view
     */
    public XmlView get(String tagName) {
        var found = new SimpleNodeList();

        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i) instanceof Element el) {
                found.addAll(el.getElementsByTagName(tagName));
            }
        }

        return new XmlView(found);
    }

    /**
     * Gets the text content of the first child node.
     *
     * @return the text, or empty if this view is empty
     */
    public Optional<String> getTextContent() {
        if (nodes.getLength() == 0) {
            return Optional.empty();
        }

        return Optional.of(nodes.item(0).getTextContent());
    }

    /**
     * Filters the child list of this view.
     *
     * @param filter a predicate that checks if a child (viewed as an {@code XmlView}) is permitted
     * @return the filtered view
     */
    public XmlView filter(Predicate<XmlView> filter) {
        var permitted = new SimpleNodeList();

        for (int i = 0; i < nodes.getLength(); i++) {
            var child = nodes.item(i);
            var childList = new SimpleNodeList();
            childList.add(child);
            if (filter.test(new XmlView(childList))) {
                permitted.add(child);
            }
        }

        return new XmlView(permitted);
    }

    private static final class SimpleNodeList implements NodeList {
        private final List<Node> nodes = new ArrayList<>();

        private SimpleNodeList() {
        }

        void add(Node node) {
            this.nodes.add(node);
        }

        void addAll(NodeList nodes) {
            for (int i = 0; i < nodes.getLength(); i++) {
                this.nodes.add(nodes.item(i));
            }
        }

        @Override
        public Node item(int index) {
            return 0 <= index && index < getLength() ? nodes.get(index) : null;
        }

        @Override
        public int getLength() {
            return nodes.size();
        }
    }
}
