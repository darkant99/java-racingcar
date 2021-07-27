package racing;

import racing.controller.RacingController;
import racing.domain.dto.GameRequest;
import racing.domain.dto.GameResponse;
import racing.exception.DuplicateKeyException;
import racing.exception.EmptyCarException;
import racing.exception.InvalidInputException;
import racing.view.DosInputView;
import racing.view.DosResultView;
import racing.view.InputView;
import racing.view.ResultView;

public class RacingSolution {
    public static void main(String[] args) {
        RacingSolution solution = new RacingSolution(
                new DosInputView(),
                new DosResultView()
        );
        solution.run();
    }

    private final InputView inputView;
    private final ResultView resultView;
    public RacingSolution(InputView inputView, ResultView resultView) {
        this.inputView = inputView;
        this.resultView = resultView;
    }

    public void run() {
        try {
            GameRequest gameRequest = inputView.inputGameRequest();
            GameResponse results = RacingController.getInstance().gameRun(gameRequest);
            resultView.printResult(results);
        } catch (EmptyCarException | InvalidInputException e) {
            resultView.printException(e);
        } catch (IllegalStateException e) {
            resultView.printException(
                    new DuplicateKeyException("중복된 자동차 이름이 존재 합니다.")
            );
        }
    }
}
