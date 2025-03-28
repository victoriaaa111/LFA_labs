package Lab4;
import java.util.Random;

public class AlternativeSelector {
    private static final Random random = new Random();

    public String select(String group) {
        // Split alternatives
        String[] alternatives = group.split("\\|");

        // Randomly select an alternative
        return alternatives[random.nextInt(alternatives.length)];
    }
}