package klondike.controller;

import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;

/**
 * Interface that represents a controller for the game of Klondike Solitaire.
 */
public interface KlondikeController {

  /**
   * Controller that plays a game of Klondike solitaire by taking in user inputs and outputing the
   * board using klondike textual view.
   *
   * @param model the model of the klondike game
   * @param deck the deck of the klondike game
   * @param shuffle if the deck should be shuffled
   * @param numPiles the number of piles to be created
   * @param numDraw the number of cards per draw
   * @param <C> the type of card used in the game
   */
  <C extends Card> void playGame(KlondikeModel<C> model,
                                 List<C> deck, boolean shuffle, int numPiles, int numDraw);
}
