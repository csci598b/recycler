package edu.mines.csci598.inputproto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A position on the screen in addition to associated information.
 */
@Data
@AllArgsConstructor
public class Position {
   private final int x;
   private final int y;
   private final double xVelocity;
   private final double yVelocity;
   private final double xAcceleration;
   private final double yAcceleration;

   /**
    * Create a new position given an existing position.
    */
   public Position(Position position) {
      this.x = position.x;
      this.y = position.y;
      this.xVelocity = position.xVelocity;
      this.yVelocity = position.yVelocity;
      this.xAcceleration = position.xAcceleration;
      this.yAcceleration = position.yAcceleration;
   }
}
