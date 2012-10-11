package edu.mines.csci598.inputproto;

/**
 * An implementation of InputEventListener that prints the events to the
 * console.
 */
public class InputEventPrinter implements InputEventListener {
   /**
    * Invoked when the interface detects a motion event of the hand.
    */
   public void handMoved(InputEvent e) {
      System.out.println(e);
   }
}
