package Lab4;
import java.util.Random;

public class AlternativeSelector {
    private static final Random random = new Random();

    public String select(String group) {
        // split alternatives
        String[] alternatives = group.split("\\|");

        // randomly select an alternative
        return alternatives[random.nextInt(alternatives.length)];
    }
}