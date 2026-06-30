package klondike.model.hw02;

/**
 * Represents a playing card from a deck of cards. stores the cards Rank and Suit through
 * Enums
 */
public class KlondikeCard implements Card {

  private Rank cardRank;
  private Suit cardSuit;
  private boolean isVisible;

  /**
   * Constructor of A klondike Card that takes in the Card Suit and Card Rank.
   *
   * @param cardSuit The suit of the card
   * @param cardRank The rank of the card
   */
  public KlondikeCard(Suit cardSuit, Rank cardRank) {

    this.cardSuit = cardSuit;
    this.cardRank = cardRank;
  }

  /**
   * Gets and returns the rank of the Card.
   *
   * @return rank of the card
   */
  @Override
  public Rank getRank() {

    return cardRank;
  }

  /**
   * Gets and returns the Suit of the Card.
   *
   * @return the Suit of the Card
   */
  @Override
  public Suit getSuit() {

    return cardSuit;
  }

  /**
   * Gets and returns the cards visibility.
   *
   * @return the cards visibility
   */
  @Override
  public boolean isVisible() {

    return isVisible;
  }

  /**
   * Sets the visibility of this card.
   *
   * @param visible A boolean that represents the intended visibilty the Card will be set as
   */
  @Override
  public void setVisibility(boolean visible) {

    isVisible = visible;
  }

  /**
   * Creates and returns a string version of this card.
   *
   * @return string version of this card
   */
  @Override
  public String toString() {

    //Checks if Card is Visible
    if (!isVisible) {
      return ("?");
    }
    if (cardSuit == Suit.SPADES) {
      return "♠" + rankToString();
    }
    if (cardSuit == Suit.HEARTS) {
      return "♡" + rankToString();
    }
    if (cardSuit == Suit.CLUBS) {
      return "♣" + rankToString();
    }
    if (cardSuit == Suit.DIAMONDS) {
      return "♢" + rankToString();
    }

    return "";
  }

  /**
   * A helper method that turns the Rank of the Card into string format.
   *
   * @return the string format of the Rank of the Card
   */
  private String rankToString() {

    switch (cardRank) {
      default: return "";
      case ACE: return "A";
      case KING: return "K";
      case QUEEN: return "Q";
      case JACK: return "J";
      case TWO: return "2";
      case THREE: return "3";
      case FOUR: return "4";
      case FIVE: return "5";
      case SIX: return "6";
      case SEVEN: return "7";
      case EIGHT: return "8";
      case NINE: return "9";
      case TEN: return "10";
    }
  }

  /**
   * Checks if this card is equal to the other card.
   *
   * @param card that is being compared to
   * @return a boolean representing if it is equal or not
   */
  @Override
  public boolean equals(Object card) {
    if (this == card) {
      return true;
    }
    if (!(card instanceof KlondikeCard)) {
      return false;
    }
    KlondikeCard other = (KlondikeCard) card;
    return this.cardSuit == other.cardSuit && this.cardRank == other.cardRank;
  }

  /**
   * Calculates and returns the hashcode of the card.
   *
   * @return the hashcode of the card
   */
  @Override
  public int hashCode() {
    return cardSuit.hashCode() * 31 + cardRank.hashCode();
  }
}
