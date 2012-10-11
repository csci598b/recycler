package edu.mines.csci598.inputproto;

import javax.swing.JFrame;

/**
 * The main driver application for the input prototype.
 */
public class App {
   public static void main(String[] args) {
      //Pointer pointer = new Pointer(1);
      //System.out.println(pointer);

      //Position position = new Position(1, 2, 3.0, 4.0, 5.0, 6.0);
      //System.out.println(position);

      //InputEvent e = new InputEvent(pointer, position);

      InputEventPrinter printer = new InputEventPrinter();
      //printer.handMoved(e);

      JFrame frame = new JFrame();
      frame.setSize(500, 500);
      frame.setTitle("Testing Frame");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      TestPanel panel = new TestPanel(printer);
      frame.add(panel);
      frame.setVisible(true);
   }
}
