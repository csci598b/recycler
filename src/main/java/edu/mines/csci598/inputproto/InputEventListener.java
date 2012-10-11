package edu.mines.csci598.inputproto;

import java.util.EventListener;

/**
 * An interface for listeners for input events.
 */
public interface InputEventListener extends EventListener {
   /**
    * Invoked when the interface detects a motion event of the hand.
    */
   public void handMoved(InputEvent e);
}
