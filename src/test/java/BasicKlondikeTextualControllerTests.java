import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringReader;
import java.util.List;
import klondike.controller.KlondikeTextualController;
import klondike.model.hw02.Card;
import klondike.model.hw02.BasicKlondike;

/**
 * Tests for the KlondikeTextualController.
 * Covers constructor validation, argument handling, move logic, quitting, and integration.
 */
public class BasicKlondikeTextualControllerTests {

  @Test
  public void testConstructorThrowsOnNullReadable() {
    assertThrows(IllegalArgumentException.class, () ->
        new KlondikeTextualController(null, new StringBuilder()));
  }

  @Test
  public void testConstructorThrowsOnNullAppendable() {
    assertThrows(IllegalArgumentException.class, () ->
        new KlondikeTextualController(new StringReader(""), null));
  }

  @Test
  public void testPlayGameThrowsForNullModel() {
    KlondikeTextualController controller =
        new KlondikeTextualController(new StringReader(""), new StringBuilder());
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();

    assertThrows(IllegalArgumentException.class, () ->
        controller.playGame(null, deck, false, 7, 3));
  }

  @Test
  public void testPlayGameThrowsForNullDeck() {
    KlondikeTextualController controller =
        new KlondikeTextualController(new StringReader(""), new StringBuilder());
    BasicKlondike model = new BasicKlondike();

    assertThrows(IllegalArgumentException.class, () ->
        controller.playGame(model, null, false, 7, 3));
  }

  @Test
  public void testPlayGameThrowsForInvalidPileCount() {
    KlondikeTextualController controller =
        new KlondikeTextualController(new StringReader(""), new StringBuilder());
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();

    assertThrows(IllegalArgumentException.class, () ->
        controller.playGame(model, deck, false, -1, 3));
  }

  @Test
  public void testPlayGameThrowsForInvalidDrawCount() {
    KlondikeTextualController controller =
        new KlondikeTextualController(new StringReader(""), new StringBuilder());
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();

    assertThrows(IllegalArgumentException.class, () ->
        controller.playGame(model, deck, false, 7, 0));
  }

  @Test
  public void testInvalidMovePileCommandThrows() {
    StringReader input = new StringReader("move-pile 10 3 1");
    StringBuilder output = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(input, output);
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();

    controller.playGame(model, deck, false, 7, 3);
    String result = output.toString();
    assertTrue(result.toLowerCase().contains("error"));
  }

  @Test
  public void testInvalidMoveDrawCommandThrows() {
    StringReader input = new StringReader("move-draw 99");
    StringBuilder output = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(input, output);
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();

    controller.playGame(model, deck, false, 7, 3);
    String result = output.toString();
    assertTrue(result.toLowerCase().contains("error"));
  }

  @Test
  public void testValidQuitCommandDoesNotThrow() {
    StringReader input = new StringReader("q");
    StringBuilder output = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(input, output);
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();

    try {
      controller.playGame(model, deck, false, 7, 3);
    } catch (Exception e) {
      fail("No exception expected on quit, got: " + e.getMessage());
    }
  }

  @Test
  public void testGetScoreCommandDoesNotThrow() {
    StringReader input = new StringReader("get-score q");
    StringBuilder output = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(input, output);
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();

    try {
      controller.playGame(model, deck, false, 7, 3);
    } catch (Exception e) {
      fail("No exception expected on get-score command, got: " + e.getMessage());
    }
  }

  @Test
  public void testWelcomeAndMenuPrintedOnStart() {
    StringReader input = new StringReader("q");
    StringBuilder output = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(input, output);
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();

    controller.playGame(model, deck, false, 7, 3);
    String result = output.toString();

    assertTrue(result.contains("Welcome to Klondike Solitaire!"));
    assertTrue(result.contains("Supported user instructions"));
  }

  @Test
  public void testUndefinedCommandPrintsErrorMessage() {
    StringReader input = new StringReader("blahcommand q");
    StringBuilder output = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(input, output);
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();

    controller.playGame(model, deck, false, 7, 3);
    assertTrue(output.toString().contains("Undefined instruction"));
  }

  @Test
  public void testQuitCommandEndsGame() {
    StringReader input = new StringReader("q");
    StringBuilder output = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(input, output);
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();

    controller.playGame(model, deck, false, 7, 3);
    assertTrue(output.toString().contains("Thank you for using playing!"));
  }

  @Test
  public void testBadArgumentCountMovePileDoesNotThrow() {
    StringReader input = new StringReader("move-pile 1 2 q");
    StringBuilder output = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(input, output);
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();

    try {
      controller.playGame(model, deck, false, 7, 3);
    } catch (Exception e) {
      fail("Controller should handle wrong argument count gracefully.");
    }
  }

  @Test
  public void testIgnoreInvalidCharacters() {
    StringReader input = new StringReader("move-pile ? ? ? q");
    StringBuilder output = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(input, output);
    BasicKlondike model = new BasicKlondike();
    List<Card> deck = model.createNewDeck();

    controller.playGame(model, deck, false, 7, 3);
    assertTrue(output.toString().contains("Wrong number of arguments"));
  }

  @Test
  public void testControllerCanBeReusedAcrossGames() {
    StringReader input = new StringReader("q");
    StringBuilder output = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(input, output);
    BasicKlondike model1 = new BasicKlondike();
    BasicKlondike model2 = new BasicKlondike();

    List<Card> deck1 = model1.createNewDeck();
    List<Card> deck2 = model2.createNewDeck();

    controller.playGame(model1, deck1, false, 7, 3);
    controller.playGame(model2, deck2, false, 7, 3);

    String result = output.toString();
    assertTrue(result.contains("Welcome to Klondike Solitaire!"));
  }
}
