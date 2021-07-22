package racing.car;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class Car {
    private Location location;
    private int moveCount;

    public Car() {
        this.location = Location.EMPTY;
        this.moveCount = 0;
    }

    public Location getLocation() {
        return location;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void move(Distance distance) {
        ++moveCount;
        this.location = location.add(distance);
    }
}
