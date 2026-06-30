package klondike.model.hw02;

/**
 * Represents the Suit of the Card.
 */
public enum Suit {
  CLUBS, DIAMONDS, HEARTS, SPADES;

  public boolean isRed() {
    return this == HEARTS || this == DIAMONDS;
  }
}
