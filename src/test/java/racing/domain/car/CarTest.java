package racing.domain.car;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import racing.domain.Location;
import racing.domain.Name;
import racing.domain.fuel.Fuel;

import static org.assertj.core.api.Assertions.*;

class CarTest {
    private static Name ANONYMOUS;

    @BeforeAll
    public static void setUp() {
        ANONYMOUS = new Name("AAA");
    }

    @CsvSource({
            "0,100,0",
            "4,100,100",
            "9,100,100"
    })
    @DisplayName("Move 테스트")
    @ParameterizedTest
    public void moveTest(int fuelValue, int turnSize, int locationValue) {
        Location location = new Location(locationValue);
        Fuel fuel = new Fuel(fuelValue);

        Car car = new Car(ANONYMOUS);
        for (int i = 0; i < turnSize; i++)
            car.move(fuel);

        assertThat(
                car.checkLocation(location)
        ).withFailMessage("자동차가 요청한대로 행동하지 않았습니다.")
                .isTrue();
    }

    @CsvSource({
            "-1,100",
            "0,100",
            "10,100",
    })
    @DisplayName("Move IllegalArgumentException 테스트")
    @ParameterizedTest
    public void moveIllegalArgumentExceptionTest(int fuelValue, int turnSize) {
        assertThatIllegalArgumentException().isThrownBy(() ->
                moveTest(fuelValue, turnSize, -1)
        );
    }
}