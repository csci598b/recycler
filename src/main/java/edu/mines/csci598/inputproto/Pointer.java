package edu.mines.csci598.inputproto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A pointing device, such as a hand.
 */
@Data
@AllArgsConstructor
public class Pointer {
   private final int id;

   private static int NEXT_ID = 0;

   public Pointer() {
      this.id = NEXT_ID++;
   }
}
