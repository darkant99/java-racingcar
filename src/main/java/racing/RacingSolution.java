package racing;

import racing.car.Car;
import racing.car.Cars;
import racing.car.Fuel;
import racing.car.RandomFuel;
import racing.exception.InvalidInputException;
import racing.view.*;

import java.util.Random;

public class RacingSolution {
    public static void main(String[] args) {
        RacingSolution racingSolution = new RacingSolution(
                new DosInputView(),
                new DosResultView()
        );
        racingSolution.run();
    }

    private final InputView inputView;
    private final ResultView resultView;
    public RacingSolution(InputView inputView, ResultView resultView) {
        this.inputView = inputView;
        this.resultView = resultView;
    }

    public void run() {
        try {
            Cars cars = inputView.inputCars();
            int turnSize = inputView.inputTurnSize();

            racing(cars, turnSize);
        } catch (InvalidInputException e) {
            resultView.printException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void racing(Cars cars, int turnSize) {
        resultView.printResultTitle();
        for (int i = 0; i < turnSize; i++) {
            cars.moveAll(new RandomFuel());

            resultView.printAllCarLocation(cars);
        }
    }
}
