package klondike.model.hw02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 *Class that represents the game of Klondike Solitaire. It makes use of A Card class to keep track
 * of every Card and uses stacks and queues to keep track of where the cards are and the different
 * piles in the game.
 */
public class BasicKlondike implements KlondikeModel {

  boolean gameStarted;
  ArrayList<Stack<KlondikeCard>> piles;
  ArrayList<Stack<KlondikeCard>> foundation;
  int numDraw;
  LinkedList<KlondikeCard> drawnCards;
  Queue<KlondikeCard> drawPile;

  /**
   * Constructor for the Klondike Model. Initializes foundation, drawPile, and drawnCards.
   */
  public BasicKlondike() {

    foundation = new ArrayList<>(4);
    for (int curFoundation = 0; curFoundation < 4; curFoundation++) {
      foundation.add(new Stack<>());
    }
    drawPile = new LinkedList<>() {};
    drawnCards = new LinkedList<>();
  }

  /**
   * Creates and returns a deck of 52 cards with a unique rank and suit.
   *
   * <p>@return a deck of 52 cards represented by a list
   */
  @Override
  public List createNewDeck() {

    List deck = new ArrayList();
    for (Suit suit : Suit.values()) {
      for (Rank rank : Rank.values()) {

        deck.add(new KlondikeCard(suit, rank));
      }
    }

    return deck;
  }

  /**
   * Checks for exceptions then sets up the game before declaring the game as started.
   *
   * @param deck     the deck to be dealt
   * @param shuffle  if {@code false}, use the order as given by {@code deck},
   *                 otherwise use a randomly shuffled order
   * @param numPiles number of piles to be dealt
   * @param numDraw  maximum number of draw cards available at a time
   */
  @Override
  public void startGame(List deck, boolean shuffle, int numPiles, int numDraw) {

    //checks for exceptions
    if (gameStarted) {
      throw new IllegalStateException("Game has already been started");
    }
    if (deck == null || deck.isEmpty()) {
      throw new IllegalArgumentException("Deck cannot be null or empty");
    }
    if (deck.size() != Suit.values().length * Rank.values().length) {
      throw new IllegalArgumentException("Deck must contain 52 cards");
    }
    if (numPiles <= 0) {
      throw new IllegalArgumentException("Number of piles must be greater than 0");
    }
    if (numDraw <= 0) {
      throw new IllegalArgumentException("Number of draw must be greater than 0");
    }

    //checks if all 52 cards are unique
    this.numDraw = numDraw;
    List<KlondikeCard> thisDeck = (List<KlondikeCard>) deck;
    List<KlondikeCard> tempDeck = new ArrayList<>(this.createNewDeck());
    for (KlondikeCard card : thisDeck) {

      if (!tempDeck.contains(card)) {
        throw new IllegalArgumentException("Deck contains invalid card");
      }
      tempDeck.remove(card);
    }

    if (shuffle) {
      Collections.shuffle(deck);
    }

    //creates the piles
    piles = new ArrayList<>(numPiles);
    for (int curPile = 0; curPile < numPiles; curPile++) {
      Stack<KlondikeCard> pile = new Stack<>();
      for (int curCard = 0; curCard <= curPile; curCard++) {
        pile.push((KlondikeCard) deck.remove(0));
      }
      pile.peek().setVisibility(true);
      piles.add(pile);
    }
    //adds the remaining cards to the draw pile
    while (!deck.isEmpty()) {
      drawPile.add((KlondikeCard) deck.remove(0));
    }

    this.numDraw = numDraw;
    drawCards();
    gameStarted = true;
  }

  /**
   * Moves the intended number of cards from the source pile to the destination pile.
   *
   * @param srcPile  the 0-based index (from the left) of the pile to be moved
   * @param numCards how many cards to be moved from that pile
   * @param destPile the 0-based index (from the left) of the destination pile for the moved cards
   */
  @Override
  public void movePile(int srcPile, int numCards, int destPile) {

    //sends off to helper method to checks for exceptions before checking method specific exceptions
    moveChecker(destPile);
    if (srcPile < 0 || srcPile >= piles.size()) {
      throw new IllegalArgumentException("Illegal pile, source pile not within bounds");
    }
    if (numCards < 0 || numCards > piles.get(srcPile).size()) {
      throw new IllegalArgumentException(
          "Illegal pile, source pile does not contain enough cards | numCards subzero");
    }

    //moves all cards from the source pile to destination pile
    for (int numCard = 0; numCard < numCards; numCard++) {

      piles.get(destPile).push(piles.get(srcPile).pop());
    }

    piles.get(srcPile).peek().setVisibility(true);
  }

  /**
   * Helper method that draws the cards.
   */
  private void drawCards() {

    //puts the currently drawn cards back to draw pile
    while (!drawnCards.isEmpty()) {
      KlondikeCard oldCard = drawnCards.removeFirst();
      oldCard.setVisibility(false);
      drawPile.add(oldCard);
    }

    //draw up to numDraw new cards
    for (int i = 0; i < numDraw && !drawPile.isEmpty(); i++) {
      KlondikeCard newCard = drawPile.remove();
      newCard.setVisibility(true);
      drawnCards.addLast(newCard);
    }
  }

  /**
   * Moves the drawn card to the intended pile.
   *
   * @param destPile the 0-based index (from the left) of the destination pile for the card
   */
  @Override
  public void moveDraw(int destPile) {

    //sends to helper method to check for exceptions before checking for method specific exceptions
    moveChecker(destPile);
    if (drawnCards.isEmpty()) {
      throw new IllegalArgumentException("Card has not been drawn");
    }

    //pushes drawn card onto pile
    piles.get(destPile).push(drawnCards.removeFirst());
    drawCards();
  }

  /**
   * Helper method that checks for exceptions shared between move methods (pile version).
   *
   * @param destPile the destination pile to check for exceptions
   */
  private void moveChecker(int destPile) {

    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't been started");
    }
    if (destPile < 0 || destPile >= piles.size()) {
      throw new IllegalArgumentException("Illegal pile, destination pile not within bounds");
    }
  }

  /**
   * Moves a card from intended pile to intended foundation.
   *
   * @param srcPile        the 0-based index (from the left) of the pile to move a card
   * @param foundationPile the 0-based index (from the left) of the foundation pile to place card
   */
  @Override
  public void moveToFoundation(int srcPile, int foundationPile) {

    //sends off the helper method to check for exceptions before checking method specific exceptions
    moveToFoundationChecker(foundationPile);
    if (srcPile < 0 || srcPile >= piles.size()) {
      throw new IllegalArgumentException("Illegal pile, source pile not within bounds");
    }

    //gets the piles top card and moves it to foundation
    foundation.get(foundationPile).push(piles.get(srcPile).pop());
    piles.get(srcPile).peek().setVisibility(true);
  }

  /**
   * Move drawn card to intended foundation.
   *
   * @param foundationPile the 0-based index (from the left) of the foundation pile to place card
   */
  @Override
  public void moveDrawToFoundation(int foundationPile) {

    //sends to helper method to check for exceptions
    moveToFoundationChecker(foundationPile);
    if (drawnCards.isEmpty()) {
      throw new IllegalArgumentException("Card has not been drawn");
    }

    //gets the given foundation pile and pushes the drawn card onto it
    foundation.get(foundationPile).push(drawnCards.getLast());
    drawCards();
  }

  /**
   * helper method that checks for exceptions shared between move methods (foundation version).
   *
   * @param foundationPile the intended foundation pile that needs checking
   */
  private void moveToFoundationChecker(int foundationPile) {

    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't been started");
    }
    if (foundationPile < 0 || foundationPile >= foundation.size()) {
      throw new IllegalArgumentException("Illegal pile, foundation pile not within bounds");
    }
  }

  /**
   * discards the drawn cards back into the draw pile.
   */
  @Override
  public void discardDraw() {
    //checks for exceptions
    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't been started");
    }
    if (drawnCards.isEmpty()) {
      throw new IllegalArgumentException("Card has not been drawn");
    }

    //discards the cards from drawn cards and sets their visibility to false.
    KlondikeCard discarded = drawnCards.removeFirst();
    discarded.setVisibility(false);
    drawPile.add(discarded);
    drawCards();
  }

  /**
   * Gets and returns the total number of piles still in play (not empty).
   *
   * @return total number of piles still in play
   */
  @Override
  public int getNumRows() {

    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't been started");
    }

    int toReturn = 0;
    for (int curPile = 0; curPile < piles.size(); curPile++) {

      if (!piles.get(curPile).isEmpty()) {
        toReturn++;
      }
    }
    return toReturn;
  }

  /**
   * Gets and returns the total number of piles (empty and not empty).
   *
   * @return total numebr of piles
   */
  @Override
  public int getNumPiles() {

    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't been started");
    }
    return piles.size();
  }

  //gets the total number of cards per draw
  @Override
  public int getNumDraw() {

    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't been started");
    }

    return numDraw;
  }

  /**
   * Checks and returns if game is over.
   *
   * @return a boolean representing game over
   */
  @Override
  public boolean isGameOver() {

    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't been started");
    }
    //checks if all the cards are in the foundation, if so game over
    if (allCardsInFoundation()) {
      return true;
    }

    //checks if any of the draw Cards can be moved
    // to either the foundation or pile, if so then game isnt over
    for (KlondikeCard drawnCard : drawnCards) {
      if (canMoveToFoundation(drawnCard)
          || canMoveToAnyPile(drawnCard)) {
        return false;
      }
    }

    //Checks if the top card of each pile can be moved to foundation, if so then isnt game
    for (Stack<KlondikeCard> pile : piles) {
      if (!pile.isEmpty()) {
        KlondikeCard topCard = pile.peek();
        if (canMoveToFoundation(topCard)
            || canMoveToAnyPile(topCard)) {
          return false;
        }
      }
    }
    //no moves available, therefore game over
    return true;
  }

  /**
   * helper method to check if all the cards are in the foundation.
   *
   * @return true or false representing if all cards are in the foundation
   */
  private boolean allCardsInFoundation() {

    int cardCount = 0;
    for (Stack<KlondikeCard> foundationPile : foundation) {
      cardCount += foundationPile.size();
    }

    return cardCount == Suit.values().length * Rank.values().length;
  }

  /**
   * helper method that checks if the card is able to move to the foundation.
   *
   * @param card the card we are checkign
   * @return true or false representing if the card can move
   */
  private boolean canMoveToFoundation(KlondikeCard card) {
    for (Stack<KlondikeCard> foundationPile : foundation) {
      if (foundationPile.isEmpty()) {
        if (card.getRank() == Rank.ACE) {
          return true;
        }
      } else {
        KlondikeCard topCard = foundationPile.peek();
        if (topCard.getSuit() == card.getSuit()
            && card.getRank().ordinal() == topCard.getRank().ordinal() + 1) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * helper method that checks if the drawn card can move to a pile.
   *
   * @param card the card we are checking
   * @return true or false representing if it can move
   */
  private boolean canMoveToAnyPile(KlondikeCard card) {
    for (Stack<KlondikeCard> pile : piles) {
      if (pile.isEmpty()) {
        if (card.getRank() == Rank.KING) {
          return true;
        }
      } else {
        KlondikeCard topCard = pile.peek();
        boolean oppositeColor = (card.getSuit().isRed() != topCard.getSuit().isRed());
        boolean rankOneLower = card.getRank().ordinal() == topCard.getRank().ordinal() - 1;
        if (oppositeColor && rankOneLower) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Gets and returns the score.
   *
   * @return the score
   */
  @Override
  public int getScore() {

    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't been started");
    }
    int score = 0;
    for (Stack<KlondikeCard> foundationPile : foundation) {

      if (!foundationPile.isEmpty()) {
        score += foundationPile.peek().getRank().compareTo(Rank.ACE) + 1;
      }
    }

    return score;
  }

  /**
   * Gets and returns the pile hight of the given pile.
   *
   * @param pileNum the 0-based index (from the left) of the pile
   * @return the height of the given pile
   */
  @Override
  public int getPileHeight(int pileNum) {
    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't been started");
    }
    if (pileNum < 0 || pileNum >= piles.size()) {
      throw new IllegalArgumentException("Illegal pile, number not within bounds");
    }

    return piles.get(pileNum).size();
  }

  /**
   * Gets and returns a copy of the Card at the given indexes.
   *
   * @param pileNum column of the desired card (0-indexed from the left)
   * @param card    row of the desired card (0-indexed from the top)
   * @return a copy of the card at the given indexes
   */
  @Override
  public Card getCardAt(int pileNum, int card) {

    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't been started");
    }
    if (pileNum < 0 || pileNum >= piles.size()) {
      throw new IllegalArgumentException("Illegal pile index");
    }
    if (card < 0 || card >= getPileHeight(pileNum)) {
      throw new IllegalArgumentException("Illegal card index");
    }

    KlondikeCard original = piles.get(pileNum).get(card);
    KlondikeCard copy = new KlondikeCard(original.getSuit(), original.getRank());
    copy.setVisibility(original.isVisible());

    return copy;
  }

  /**
   * Gets and returns a copy of the card at the top of the given foundation.
   *
   * @param foundationPile 0-based index (from the left) of the foundation pile
   * @return returns a copy of the card at the top of the given foundation
   */
  @Override
  public Card getCardAt(int foundationPile) {
    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't been started");
    }
    if (foundationPile < 0 || foundationPile >= piles.size()) {
      throw new IllegalArgumentException("Illegal foundation, number not within bounds");
    }
    if (foundation.get(foundationPile).isEmpty()) {
      return null;
    }

    KlondikeCard original = foundation.get(foundationPile).peek();
    KlondikeCard copy = new KlondikeCard(original.getSuit(), original.getRank());
    copy.setVisibility(original.isVisible());

    return copy;
  }

  /**
   * Gets and returns the visibility of the card at the given indexes.
   *
   * @param pileNum column of the desired card (0-indexed from the left)
   * @param card    row of the desired card (0-indexed from the top)
   * @return The card at the given indexes
   */
  @Override
  public boolean isCardVisible(int pileNum, int card) {
    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't been started");
    }
    if (pileNum < 0 || pileNum >= piles.size()) {
      throw new IllegalArgumentException("Illegal pile, number not within bounds");
    }
    if (card < 0 || card >= getPileHeight(pileNum)) {
      throw new IllegalArgumentException("Illegal card, number not within bounds");
    }

    return getCardAt(pileNum, card).isVisible();
  }

  /**
   * Gets and returns a copy of the current cards that have been drawn.
   *
   * @return a copy of the current cards that have been drawn.
   */
  @Override
  public List getDrawCards() {
    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't been started");
    }

    List<KlondikeCard> copy = new ArrayList<>();
    for (KlondikeCard c : drawnCards) {
      KlondikeCard clone = new KlondikeCard(c.getSuit(), c.getRank());
      clone.setVisibility(c.isVisible());
      copy.add(clone);
    }
    return copy;
  }

  /**
   * Gets and returns the number of foundations.
   *
   * @return the number of foundations.
   */
  @Override
  public int getNumFoundations() {
    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't been started");
    }

    return foundation.size();
  }
}
