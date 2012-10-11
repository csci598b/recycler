package edu.mines.csci598.inputproto;

import lombok.Data;
import lombok.NonNull;

/**
 * A class to describe input events.
 */
@Data
public class InputEvent {
   @NonNull
   private final Pointer pointer;
   @NonNull
   private final Position position;
}
