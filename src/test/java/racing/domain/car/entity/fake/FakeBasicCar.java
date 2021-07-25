package racing.domain.car.entity.fake;

import racing.domain.car.entity.BasicCar;
import racing.domain.car.vo.Location;
import racing.domain.car.vo.Name;
import racing.domain.car.vo.fuel.Fuel;

// Fake 객체
public final class FakeBasicCar extends BasicCar {
    private static final Location CHEAT_LOCATION = new Location(100);
    private Location location;
    public FakeBasicCar(Name name) {
        super(name);
        location = Location.empty();
    }

    @Override
    public Location location() {
        return location;
    }

    @Override
    public void move(Fuel fuel) {
        this.location = location().add(
                CHEAT_LOCATION
        );
    }
}