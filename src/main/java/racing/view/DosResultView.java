package racing.view;

import racing.domain.car.entity.Car;
import racing.domain.car.vo.Location;
import racing.domain.game.vo.Turn;
import racing.domain.game.vo.Turns;
import util.RepeatString;

import java.util.Map;

import static racing.view.DosResultView.Text.*;

public class DosResultView implements ResultView {
    private static final String NAME_DELIMITER = ",";
    private static final String NEW_LINE = "\n";
    private static final String LOCATION_REPEAT_UNIT = "-";

    private void printResultTitle() {
        System.out.println(RESULT_TITLE);
        System.out.println();
    }

    private void printTurn(Turn turn) {
        for (Map.Entry<Car, Location> iEntry : turn.entrySet()) {
            Car iCar = iEntry.getKey();
            Location iLocation = iEntry.getValue();

            System.out.println(
                    CAR_LOCATION.formatString(iCar, new RepeatString(iLocation, LOCATION_REPEAT_UNIT))
            );
        }
        System.out.println();
    }

    private void printWinners(Turns turns) {
        StringBuilder builder = new StringBuilder();
        for (String iCarName : turns.lastTurn().findWinnerNames()) {
            builder.append(iCarName)
                    .append(NAME_DELIMITER);
        }
        String strWinners = builder.substring(0, builder.length() - 1); // 우승자는 반드시 나온다.

        System.out.println(WINNERS.formatString(strWinners));
    }

    @Override
    public void printResult(Turns turns) {
        printResultTitle();

        for (Turn iTurn : turns) {
            printTurn(iTurn);
        }
        printWinners(turns);
    }

    @Override
    public void printException(Exception e) {
        System.out.println(EXCEPTION.formatString(e.getMessage()));
    }

    protected enum Text {
        CAR_LOCATION("%s : %s"),
        RESULT_TITLE("실행 결과"),
        EXCEPTION("[ERROR] %s"),
        WINNERS("%s가 최종 우승 했습니다.");

        private final String text;

        Text(String text) {
            this.text = text;
        }

        public String formatString(String... objs) {
            return formatString((Object[]) objs);
        }

        public String formatString(Object... objs) {
            return String.format(text, objs);
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
