package study;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SetTest {
    private Set<Integer> numbers;

    // Test 어노테이션 이전에 실행
    @BeforeEach
    void setUp() {
        numbers = new HashSet<>();
        numbers.add(1);
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
    }

    @Test
    void size() {
        assertThat(numbers).hasSize(3);
    }

    @AfterEach
    void tearDown() {

    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void contains(int no) {
        assertThat(numbers.contains(no)).isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {"1:true", "4:false"}, delimiter = ':')
    void contains2(int no, boolean result) {
        assertThat(numbers.contains(no)).isEqualTo(result);
    }
}
