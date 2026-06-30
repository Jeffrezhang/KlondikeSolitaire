
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import klondike.model.hw02.Card;
import klondike.model.hw02.Klondike;
import klondike.model.hw02.KlondikeCard;
import klondike.model.hw02.Rank;
import org.junit.Test;

/**
 * A class for testing the KlondikeModel. All tests
 * in this class cannot create Card type objects. Instead,
 * the tests use the createNewDeck method to help create
 * example games.
 */
public class KlondikeModelTests {

  /**
   * Checks if the deck is created properly.
   */
  @Test
  public void testCreateNewDeck_has52UniqueCards() {
    Klondike game = new Klondike();
    List<KlondikeCard> deck = game.createNewDeck();

    //check deck size
    assertEquals(52, deck.size());

    //check uniqueness
    Set<KlondikeCard> uniqueCards = new HashSet<>(deck);
    assertEquals(52, uniqueCards.size());

    //count aces and kings using loops
    int aces = 0;
    int kings = 0;
    for (KlondikeCard card : deck) {
      if (card.getRank() == Rank.ACE) {
        aces++;
      } else if (card.getRank() == Rank.KING) {
        kings++;
      }
    }

    assertEquals(4, aces);
    assertEquals(4, kings);
  }

  /**
   * Checks if exceptions are thrown properly before starting game.
   */
  @Test
  public void testMethodsThrowBeforeStart() {
    Klondike game = new Klondike();

    assertThrows(IllegalStateException.class, game::getNumPiles);
    assertThrows(IllegalStateException.class, game::getNumRows);
    assertThrows(IllegalStateException.class, game::getNumDraw);
    assertThrows(IllegalStateException.class, game::getNumFoundations);
    assertThrows(IllegalStateException.class, game::getDrawCards);
    assertThrows(IllegalStateException.class, game::getScore);
    assertThrows(IllegalStateException.class, game::isGameOver);
    assertThrows(IllegalStateException.class, () -> game.getPileHeight(0));
    assertThrows(IllegalStateException.class, () -> game.getCardAt(0, 0));
    assertThrows(IllegalStateException.class, () -> game.getCardAt(0));
  }

  /**
   * Checks if the game is set up properly.
   */
  @Test
  public void testStartGame_dealsCascadeCorrectly() {
    Klondike game = new Klondike();
    List<KlondikeCard> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);

    assertEquals(7, game.getNumPiles());
    assertEquals(4, game.getNumFoundations());

    for (int pile = 0; pile < 7; pile++) {
      assertEquals(pile + 1, game.getPileHeight(pile));
      for (int row = 0; row < pile; row++) {
        assertEquals("?", game.getCardAt(pile, row).toString());
      }
      assertNotEquals("?", game.getCardAt(pile, pile).toString());
    }

    List<KlondikeCard> draw = game.getDrawCards();
    assertTrue(draw.size() >= 1 && draw.size() <= 3);
  }

  /**
   * Checks if thrown when empty or no deck.
   */
  @Test
  public void testStartGame_rejectsNullOrEmptyDeck() {
    Klondike game = new Klondike();
    assertThrows(IllegalArgumentException.class, () -> game.startGame(null, false, 7, 3));
    assertThrows(IllegalArgumentException.class, () -> game.startGame(new ArrayList<>(), false, 7, 3));
  }

  /**
   * Checks if thrown when deck is the wrong size.
   */
  @Test
  public void testStartGame_rejectsWrongDeckSize() {
    Klondike game = new Klondike();
    List<KlondikeCard> deck = game.createNewDeck();
    deck.remove(0);
    assertThrows(IllegalArgumentException.class, () -> game.startGame(deck, false, 7, 3));
  }

  /**
   * Checks if thrown when there are duplicate cards.
   */
  @Test
  public void testStartGame_rejectsDuplicateCardInDeck() {
    Klondike game = new Klondike();
    List<KlondikeCard> deck = game.createNewDeck();
    deck.set(1, deck.get(0));
    assertThrows(IllegalArgumentException.class, () -> game.startGame(deck, false, 7, 3));
  }

  /**
   * Checks if thrown when there's illegal pile or draw counts.
   */
  @Test
  public void startGame_rejectsBadPilesOrDrawCounts() {
    Klondike game = new Klondike();
    List<KlondikeCard> deck = game.createNewDeck();

    assertThrows(IllegalArgumentException.class, () -> game.startGame(deck, false, 0, 1));
    assertThrows(IllegalArgumentException.class, () -> game.startGame(deck, false, 7, 0));
  }

  /**
   * Checks if thrown when game is already started.
   */
  @Test
  public void testStartGame_rejectsSecondStart() {
    Klondike game = new Klondike();
    List<KlondikeCard> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);
    assertThrows(IllegalStateException.class, () -> game.startGame(deck, false, 7, 3));
  }

  /**
   * Checks if discard draw changes card visibility.
   */
  @Test
  public void testDiscardDraw_changesVisibleCards() {
    Klondike game = new Klondike();
    List<KlondikeCard> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);

    List<KlondikeCard> first = game.getDrawCards();
    game.discardDraw();
    List<KlondikeCard> second = game.getDrawCards();

    assertNotEquals(first.toString(), second.toString());
  }

  /**
   * Checks if thrown when trying to access illegal pile index when moving piles.
   */
  @Test
  public void testMovePile_throwsOnBadIndices() {
    Klondike game = new Klondike();
    List<KlondikeCard> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);

    assertThrows(IllegalArgumentException.class, () -> game.movePile(-1, 1, 0));
    assertThrows(IllegalArgumentException.class, () -> game.movePile(0, 1, 7));
    assertThrows(IllegalArgumentException.class, () -> game.movePile(0, -1, 1));
  }

  /**
   * Checks if thrown when trying to access illegal foundation index
   * or source pile index when moving to foundation.
   */
  @Test
  public void testMoveToFoundation_badIndicesThrow() {
    Klondike game = new Klondike();
    List<KlondikeCard> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);

    assertThrows(IllegalArgumentException.class, ()
        -> game.moveToFoundation(-1, 0));
    assertThrows(IllegalArgumentException.class, ()
        -> game.moveToFoundation(0, -1));
    assertThrows(IllegalArgumentException.class, ()
        -> game.moveToFoundation(7, 0));
    assertThrows(IllegalArgumentException.class, ()
        -> game.moveToFoundation(0, 4));
  }

  /**
   * Checks if a new copy is created when returning drawn cards.
   */
  @Test
  public void testGetDrawCards_returnsDefensiveCopy() {
    Klondike game = new Klondike();
    List<KlondikeCard> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);

    List<KlondikeCard> first = game.getDrawCards();
    first.clear();
    List<KlondikeCard> second = game.getDrawCards();

    assertFalse(second.isEmpty());
  }

  /**
   * Checks if a new copy is created when returning card at.
   */
  @Test
  public void testGetCardAt_returnsDefensiveCopy() {
    Klondike game = new Klondike();
    List<KlondikeCard> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 1);

    Card top = game.getCardAt(0, game.getPileHeight(0) - 1);
    String before = top.toString();

    if (top instanceof KlondikeCard) {
      ((KlondikeCard) top).setVisibility(false);
    }

    String after = game.getCardAt(0, game.getPileHeight(0) - 1).toString();
    assertEquals(before, after);
  }

  /**
   * Tests if the score starts at zero and game isn't over when game is started.
   */
  @Test
  public void testScoreStartsAtZeroAndNotGameOver() {
    Klondike game = new Klondike();
    List<KlondikeCard> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);

    assertEquals(0, game.getScore());
    assertFalse(game.isGameOver());
  }
}
