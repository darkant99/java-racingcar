package racingcar.inputview;

public class ConsoleInputView implements InputView {
    @Override
    public void print(String message) {
        System.out.println(message);
    }
}