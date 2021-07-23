package racing.car;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import racing.exception.DuplicateKeyException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class CarsTest {
    private static final char NAME_WORD = 'A';
    private static final String NAME_DELIMITER = "\\|";
    private Cars initCars(String strNames) {
        String[] strNameSplitValues = strNames.split(NAME_DELIMITER);

        Cars cars = new Cars();
        cars.addAll(
                Arrays.stream(strNameSplitValues)
                        .map(Name::new)
                        .map(Car::new)
                        .collect(Collectors.toList())
        );
        return cars;
    }

    private String newAnonymousName(int size) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size + 1; i++)
            builder.append(NAME_WORD);
        return builder.toString();
    }

    private String sizeToNames(int size) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.append(
                    newAnonymousName(i)).append("|");
        }
        if (builder.length() > 0)
            return builder.substring(0, builder.length() - 1);
        return builder.toString();
    }

    @ValueSource(ints = {
            10, 100, 1000
    })
    @DisplayName("Car Add Test")
    @ParameterizedTest
    public void addTest(int size) {
        Cars cars = initCars(
                sizeToNames(size)
        );

        assertThat(cars.size())
                .isEqualTo(size);
    }

    // cars.iterator().hasNext() 를 이용해서 테스트를 만들어보면 어떨까요? :)
    @ValueSource(ints = { 1, 100, 1000 })
    @DisplayName("Car Iterator Test With For")
    @ParameterizedTest
    public void carIteratorForTest(int size) {
        Cars cars = initCars(
                sizeToNames(size));

        Iterator<Car> iterator = cars.iterator();
        for (int i = 0; i < size; i++) {
            iterator.next();
        }
        assertThat(iterator.hasNext())
                .isEqualTo(false);
    }

    @ValueSource(ints = { 1, 100, 1000 })
    @DisplayName("Car Test With While")
    @ParameterizedTest
    public void carIteratorWhileTest(int size) {
        Cars cars = initCars(
                sizeToNames(size)
        );

        Iterator<Car> iterator = cars.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
    }

    // 요구사항 "주어진 횟수 동안 n대의 자동차는 전진 또는 멈출 수 있다." 에 대한 테스트를 추가 해보면 어떨까요?
    // 꼭 Cars가 아니여도 상관이 없습니다. 🤔
    @CsvSource({
            "5,100,0,0",
            "5,100,1,0",
            "5,100,2,0",
            "5,100,3,0",
            "5,100,4,100",
            "5,100,5,100",
            "5,100,6,100",
            "5,100,7,100",
            "5,100,8,100",
            "5,100,9,100"
    })
    @DisplayName("주어진 횟수 동안 n대의 자동차는 전진 또는 멈출 수 있다.")
    @ParameterizedTest
    public void moveAllTest(int carSize, int turnSize, int fuelValue, int locationValue) {
        Location location = new Location(locationValue);
        Cars cars = initCars(
                sizeToNames(carSize)
        );
        Fuel fuel = new Fuel(fuelValue);

        for (int i = 0; i < turnSize; i++) {
            cars.moveAll(fuel);
        }

        for (Car iCar : cars) {
            assertThat(
                    iCar.getLocation()
            ).withFailMessage("요청한대로 이동하지 않았습니다.")
                    .isEqualTo(location);
        }
    }

    @ValueSource(strings = "A|AA|AAA")
    @ParameterizedTest
    public void addAllTest(String strNames) {
        initCars(strNames);
    }

    @ValueSource(strings = "AA|AA|AAA")
    @ParameterizedTest
    public void addAllTest_DuplicateKeyException(String strNames) {
        assertThatThrownBy(() ->
            initCars(strNames)
        ).isInstanceOf(DuplicateKeyException.class);
    }
}