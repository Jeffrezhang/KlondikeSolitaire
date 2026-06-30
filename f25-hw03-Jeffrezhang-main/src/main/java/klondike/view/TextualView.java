package klondike.view;

import java.io.IOException;

/**
 * A marker interface for all text-based views, to be used in the Klondike game.
 */
public interface TextualView {

  /**
   * Turns the board into a readable string using a Stringbuilder. Making use of a string builder.
   *
   * @return A string representing the current board in a viewable format.
   */
  String toString();

  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   *
   * @throws IOException if the rendering fails for some reason.
   */
  void render() throws IOException;
}
