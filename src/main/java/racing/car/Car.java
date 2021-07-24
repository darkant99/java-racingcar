package racing.car;

public class Car {
    private static final int REQUIRED_FUEL_VALUE = 4; // 자동차가 움직이기 위한 최소 연료
    private final Name name;
    private Location location;

    public Car(Name name) {
        this.name = name;
        this.location = Location.EMPTY;
    }

    public boolean checkLocation(Location location) {
        return location().equals(location);
    }

    public Location location() {
        return location;
    }

    public Name name() {
        return name;
    }


    public void move(Fuel fuel) {
        if (isMovable(fuel))
            this.location = location.add(Location.ONE_BLOCK);
    }

    private boolean isMovable(Fuel fuel) {
        return fuel.value() >= REQUIRED_FUEL_VALUE;
    }
}
