package racing.domain.car;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import racing.domain.Location;
import racing.domain.Name;
import racing.domain.fuel.Fuel;
import racing.domain.fuel.RandomFuel;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class CarsTest {
    /*
    요구 사항은 ','를 기준으로 이름을 입력 받지만
    * @CsvSource의 기본 구분 문자가 ',' 이기 때문에
    * 테스트 에서는 이름 구분자를 '|' 로 변경
    */
    private static final String NAME_DELIMITER = "\\|";
    private Cars initCars(String strNames) {
        String[] strNameSplitValues = strNames.split(NAME_DELIMITER);

        return new Cars(
                Arrays.stream(strNameSplitValues)
                        .map(Name::new)
                        .collect(Collectors.toMap(
                                i -> i, Car::new
                        ))
        );
    }

    private String newAnonymousName(int identity) {
        return String.valueOf(identity);
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
                    iCar.checkLocation(location)
            ).withFailMessage("요청한대로 이동하지 않았습니다.")
                    .isTrue();
        }
    }

    @ValueSource(strings = "A|AA|AAA")
    @ParameterizedTest
    public void addTest(String strNames) {
        initCars(strNames);
    }

    @ValueSource(strings = "AA|AA|AAA")
    @ParameterizedTest
    public void addTest_IllegalStateException(String strNames) {
        assertThatThrownBy(() -> {
            Cars cars = new Cars();
            String[] splitNames = strNames.split(NAME_DELIMITER);
            List<Car> carList = Arrays.stream(splitNames)
                    .map(Name::new)
                    .map(Car::new)
                    .collect(Collectors.toList());
            for (Car iCar : carList) {
                cars.add(iCar);
            }
        }).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("best Car 테스트")
    @CsvSource({
            "A|B|C,A|C,100",
            "A|B|C,A,100",
            "A|B|C|D|E|F|G,G|B,100"
    })
    @ParameterizedTest
    public void bestCarsTest(String strCarNames, String strWinnerNames, int turnSize) {
        Cars cars = initCars(strCarNames);
        List<Name> winnerNames = Arrays.stream(strWinnerNames.split(NAME_DELIMITER))
                .map(Name::new)
                .collect(Collectors.toList());

        // 이동
        for(Car iCar : cars)
            moveCar(iCar, turnSize, winnerNames.contains(iCar.name()));

        Cars winners = cars.betCars();
        for(Name iName : winnerNames) {
            assertThat(winners.containsName(iName))
                    .withFailMessage("예상한 우승자가 포함되어 있지 않습니다.")
                    .isTrue();
        }
        assertThat(winners.size() == winnerNames.size())
                .withFailMessage("우승자의 수가 예상한 수와 다릅니다.")
                .isTrue();
    }
    @DisplayName("best Car Random Fuel 테스트")
    @CsvSource({
            "A|B|C|D|E|F|G|H|I|J|K,10000",
            "A|B|C|D|E|F|G|H|I|J|K,1000000",
    })

    @ParameterizedTest
    public void bestCarsTest_Random(String strCarNames, int turnSize) {
        Cars cars = initCars(strCarNames);
        cars.moveAll(RandomFuel.getInstance());

        Cars winners = cars.betCars();

        assertThat(winners.size())
                .withFailMessage("우승자가 0명 입니다.")
                .isNotEqualTo(0);
        assertThat(winners.size())
                .withFailMessage("모두 우승자 입니다.")
                .isNotEqualTo(cars.size());
    }

    private void moveCar(Car car, int turnSize, boolean movement) {
        for (int i = 0; i < turnSize; i++) {
            car.move(
                    movement ? Fuel.full() : Fuel.empty()
            );
        }
    }
}