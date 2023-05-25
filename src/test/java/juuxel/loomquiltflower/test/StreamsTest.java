package juuxel.loomquiltflower.test;

import juuxel.loomquiltflower.impl.util.Streams;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StreamsTest {
    @Test
    void iterateTest() {
        List<Integer> nums = List.of(1, 10, 2, 35);
        List<Integer> iterated = Streams.iterate(nums, List::size, List::get).toList();
        assertThat(iterated).isEqualTo(nums);
    }

    @Test
    void iterateTestEmpty() {
        List<Object> nums = List.of();
        List<Object> iterated = Streams.iterate(nums, List::size, List::get).toList();
        assertThat(iterated).isEmpty();
        assertThat(iterated).isEqualTo(nums);
    }
}
