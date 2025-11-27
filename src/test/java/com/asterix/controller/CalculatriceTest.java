import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatriceTest {

    @Test
    public void testAddition() {
        Calculatrice calc = new Calculatrice();
        // Ce test va exécuter la méthode additionner, donc JaCoCo va la marquer comme "vue"
        assertEquals(6, calc.additionner(2, 3));
    }
}