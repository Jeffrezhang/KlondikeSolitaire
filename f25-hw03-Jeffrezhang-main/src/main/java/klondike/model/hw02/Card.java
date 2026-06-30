package klondike.model.hw02;

/**
 * This (essentially empty) interface marks the idea of cards.  You will need to
 * implement this interface in order to use your model.
 *
 * <p>The only behavior guaranteed by this class is its {@link Card#toString()} method,
 * which will render the card as specified in the assignment.
 *
 * <p>If you need more behaviors to be public, you must make a new interface that extends
 * this one and exposes those behaviors.
 */
public interface Card {

  /**
   * Gets the Rank of the Card and returns it.
   *
   * @return Rank of the Card
   */
  Rank getRank();

  /**
   * Gets the Suit of the card and returns it.
   *
   * @return Suit of the Card
   */
  Suit getSuit();

  /**
   * Turns the card into a String readable format and returns it.
   *
   * @return the String format of the Card
   */
  String toString();

  /**
   * Returns whether the Card is visible (Face up Or Face Down).
   *
   * @return the visibility of the Card
   */
  boolean isVisible();

  /**
   * Sets the visibility of the Card.
   *
   * @param visible A boolean that represents the intended visibilty the Card will be set as
   */
  void setVisibility(boolean visible);

  /**
   * Checks if the Card is equal to the given Card.
   *
   * @param card the other Card that is to be checked with our Card
   * @return A boolean that represents if the Cards are equal or not
   */
  boolean equals(Object card);

  /**
   * Creates and returns the hashcode of the Card.
   *
   * @return the hashcode of the Card
   */
  int hashCode();
}