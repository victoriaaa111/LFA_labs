package Lab4;
import java.util.Random;

public class RepetitionHandler {
    private static final Random random = new Random();
    private final int maxRepeat;

    public RepetitionHandler(int maxRepeat) {
        this.maxRepeat = maxRepeat;
    }

    public String handleRepetition(char repetitionType, char baseChar) {
        int repeatCount;

        switch (repetitionType) {
            case '+':
                // at least 1, up to max repeat
                repeatCount = random.nextInt(1, maxRepeat + 1);
                break;
            case '*':
                // 0 to max repeat
                repeatCount = random.nextInt(0, maxRepeat + 1);
                break;
            default:
                repeatCount = 0;
        }

        return repeatString(baseChar, repeatCount);
    }

    public String handleWildcardRepetition(char baseChar) {
        // special repetition with random count
        int repeatCount = random.nextInt(1, maxRepeat + 1);
        return repeatString(baseChar, repeatCount);
    }

    private String repeatString(char baseChar, int count) {
        StringBuilder repeated = new StringBuilder();
        for (int i = 0; i < count; i++) {
            repeated.append(baseChar);
        }
        return repeated.toString();
    }
}