package klondike.view;

import static java.lang.System.out;

import java.util.List;
import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeCard;
import klondike.model.hw02.KlondikeModel;


/**
 * Class that implements textual view to represent the Klondike Model in a textual manner. Uses
 * String Builders to build this view
 */
public class KlondikeTextualView implements TextualView {

  private final BasicKlondike model;
  private StringBuilder boardBuilder;

  /**
   * Constructor of the textual view takes in the model and initializes it onto the
   * model instance variable.
   *
   * @param model the model we are taking in
   */
  public KlondikeTextualView(KlondikeModel<?> model) {

    if (model == null) {
      throw new NullPointerException("model is null");
    }

    this.model = (BasicKlondike) model;
  }

  /**
   * Construtor of textual view that takes in a model and an appendable and initializes it
   * onto the model and appendable of the object.
   *
   * @param model the model we are taking in
   * @param ap the appendable we are taking in
   */
  public KlondikeTextualView(BasicKlondike model, Appendable ap) {

    if (model == null) {
      throw new NullPointerException("model is null");
    }

    this.model = model;
    this.boardBuilder = (StringBuilder) ap;
  }

  /**
   * Turns the board into a readable string using a Stringbuilder.
   * It first builds and appends the draw portion
   * then the foundation portion, then lastly it builds
   * and appends the piles portion of the current board.
   *
   * @return A string representing the current board in a viewable format.
   */
  public Appendable build() {

    //builds the Draw portion by going through the current drawn cards and prints them in order
    List<KlondikeCard> drawCards = model.getDrawCards();
    boardBuilder.append("Draw:");
    if (!drawCards.isEmpty()) {
      boardBuilder.append(' ');
      for (int curDraw = 0; curDraw < drawCards.size(); curDraw++) {
        if (curDraw > 0) {
          boardBuilder.append(", ");
        }
        boardBuilder.append(drawCards.get(curDraw).toString());
      }
    }
    boardBuilder.append('\n');

    //builds the foundation portion by getting the foundation
    // cards and printing them or <none> if foundation holds no card currently
    boardBuilder.append("Foundation: ");
    int totalFoundations = model.getNumFoundations();
    for (int curFoundation = 0; curFoundation < totalFoundations; curFoundation++) {
      if (curFoundation > 0) {
        boardBuilder.append(", ");
      }
      Card topCard = model.getCardAt(curFoundation);
      boardBuilder.append(topCard == null ? "<none>" : topCard.toString());
    }
    boardBuilder.append('\n');

    //builds the pile portion in the format given by the assignment
    int numPiles = model.getNumPiles();
    int maxPileHeight = 0;
    for (int curPile = 0; curPile < numPiles; curPile++) {
      maxPileHeight = Math.max(maxPileHeight, model.getPileHeight(curPile));
    }

    for (int row = 0; row < maxPileHeight; row++) {
      for (int curPile = 0; curPile < numPiles; curPile++) {
        if (curPile > 0) {
          boardBuilder.append("  ");
        }
        int pileHeight = model.getPileHeight(curPile);

        //if pile is empty shows an X only at the first row
        if (pileHeight == 0) {
          boardBuilder.append(row == 0 ? "X" : " ");
        } else if (row >= pileHeight) {
          //if curPile has fewer cards than the current row
          boardBuilder.append(" ");
        } else { //if the pile has enough cards and has a card in this row
          Card currentCard = model.getCardAt(curPile, row);
          boardBuilder.append(currentCard.toString());
        }
      }
      if (row < maxPileHeight - 1) {
        boardBuilder.append('\n');
      }
    }

    return boardBuilder;
  }

  @Override
  public String toString() {

    return build().toString();
  }

  @Override
  public void render() {

    out.append(this.toString());
  }
}
