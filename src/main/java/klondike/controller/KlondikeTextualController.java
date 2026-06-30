package klondike.controller;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;
import klondike.view.KlondikeTextualView;

/**
 * Class that handles the controlling of the Klondike Game by taking in inputs and outputs and
 * rendering the board in a textual view.
 */
public class KlondikeTextualController implements KlondikeController {

  private Readable reader;
  private Appendable builder;
  private BasicKlondike model;
  private int score;
  private KlondikeTextualView view;

  /**
   * Constructor fo the controller, gets the appendable and readable to set the classes
   * appendable and readable equal to it.
   *
   * @param rd a readable
   * @param ap an appendable
   * @throws IllegalArgumentException throws if the readable or appendable is null
   */
  public KlondikeTextualController(Readable rd, Appendable ap) throws IllegalArgumentException {

    if (rd == null || ap == null) {
      throw new IllegalArgumentException("Arguments can't be null");
    }

    reader = rd;
    builder = ap;

  }

  @Override
  public <C extends Card> void playGame(KlondikeModel<C> model, List<C> deck, boolean shuffle,
                                        int numPiles, int numDraw) {

    if (model == null || deck == null) {
      throw new IllegalArgumentException("Model and deck cannot be null");
    }
    if (numPiles <= 0 || numDraw <= 0) {
      throw new IllegalArgumentException("Number of piles and draw must be positive");
    }

    this.model = (BasicKlondike) model;
    this.model.startGame(deck, shuffle, numPiles, numDraw);
    view = new KlondikeTextualView(this.model, builder);

    boolean quit = false;
    Scanner sc = new Scanner(reader);
    int srcPile;
    int dstPile;
    int numCards;

    this.welcomeMessage();

    while (!quit && sc.hasNext()) {

      score = model.getScore();
      view.render();
      writeMessage(System.lineSeparator() + "Type Instructions: ");
      String line = sc.nextLine();
      String[] instructions = line.split(" ");
      switch (instructions[0]) {

        case "quit":
        case "q":
          quit = true;
          break;
        case "move-pile":
          if (instructions.length != 4) {
            writeMessage(System.lineSeparator() + "Wrong number of arguments"
                + System.lineSeparator());
            break;
          }
          try {
            srcPile = Integer.parseInt(instructions[1]);
            numCards = Integer.parseInt(instructions[2]);
            dstPile = Integer.parseInt(instructions[3]);
            model.movePile(srcPile, dstPile, numCards);
          } catch (IllegalArgumentException e) {
            writeMessage("Error: " + e.getMessage() + System.lineSeparator());
          }
          break;
        case "move-draw":
          if (instructions.length != 2) {
            writeMessage(System.lineSeparator() + "Wrong number of arguments"
                + System.lineSeparator());
            break;
          }
          try {
            dstPile = Integer.parseInt(instructions[1]);
            model.moveDraw(dstPile);
          } catch (IllegalArgumentException e) {
            writeMessage("Error: " + e.getMessage() + System.lineSeparator());
          }
          break;
        case "move-pile-foundation":
          if (instructions.length != 3) {
            writeMessage(System.lineSeparator() + "Wrong number of arguments"
                + System.lineSeparator());
            break;
          }
          try {
            srcPile = Integer.parseInt(instructions[1]);
            dstPile = Integer.parseInt(instructions[2]);
            model.moveToFoundation(srcPile, dstPile);
          } catch (IllegalArgumentException e) {
            writeMessage("Error: " + e.getMessage() + System.lineSeparator());
          }
          break;
        case "move-draw-foundation":
          if (instructions.length != 2) {
            writeMessage(System.lineSeparator() + "Wrong number of arguments"
                + System.lineSeparator());
            break;
          }
          try {
            dstPile = Integer.parseInt(instructions[1]);
            model.moveDrawToFoundation(dstPile);
          } catch (IllegalArgumentException e) {
            writeMessage("Error: " + e.getMessage() + System.lineSeparator());
          }
          break;
        case "discard-draw":
          model.discardDraw();
          break;
        case "get-score":
          writeMessage(String.valueOf(score));
          break;
        default:
          writeMessage("Undefined instruction: " + instructions[0] + System.lineSeparator());
      }
    }

    this.farewellMessage();
  }

  private void writeMessage(String message) throws IllegalStateException {
    try {
      builder.append(message);

    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  private void printMenu() throws IllegalStateException {
    writeMessage("Supported user instructions are: " + System.lineSeparator());
    writeMessage("move-pile source-pile, num-cards, dest-pile "
        + "(move amount of cards from source-pile to dest-pile)" + System.lineSeparator());
    writeMessage("move-draw dest-pile(move drawn card to dest-pile)"
        + System.lineSeparator());
    writeMessage("move-pile-foundation source-pile foundation-pile"
        + "(moves top card from source-pile to foundation-pile)" + System.lineSeparator());
    writeMessage("move-draw-foundation foundation-pile"
        + "(moves drawn card to foundation-pile)" + System.lineSeparator());
    writeMessage("discard-draw (discards drawn cards and draws new cards)"
        + System.lineSeparator());
    writeMessage("get-score (gets the score of the card)" + System.lineSeparator());
    writeMessage("q or quit (quit the program) " + System.lineSeparator());
  }

  private void welcomeMessage() throws IllegalStateException {
    writeMessage("Welcome to Klondike Solitaire!" + System.lineSeparator());
    printMenu();
  }

  private void farewellMessage() throws IllegalStateException {
    writeMessage("Thank you for using playing!");
  }
}
