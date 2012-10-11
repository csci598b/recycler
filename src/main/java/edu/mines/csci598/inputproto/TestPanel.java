package edu.mines.csci598.inputproto;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import lombok.NonNull;

/**
 * A JPanel for testing the input system with a mouse.
 */
public class TestPanel extends JPanel {
   private final JLabel label;

   /**
    * Create an empty testing panel.
    */
   public TestPanel(InputEventListener listener) {
      super();

      this.label = new JLabel("A label!");
      this.label.setOpaque(true);
      this.label.setBackground(Color.RED);
      this.label.addMouseMotionListener(new MouseListenerAdapter(listener));
      this.add(this.label);

      this.addMouseMotionListener(new MouseListenerAdapter(listener));
      this.setBackground(Color.BLUE);
   }

   /**
    * Listens for mouse movement and translates to Input events.
    */
   private class MouseListenerAdapter extends MouseMotionAdapter {
      @NonNull
      private final InputEventListener listener;
      @NonNull
      private final Pointer pointer;

      public MouseListenerAdapter(InputEventListener listener) {
         this.listener = listener;
         this.pointer = new Pointer();
         System.out.println("registered!");
      }

      @Override
      public void mouseMoved(MouseEvent e) {
         int x = e.getX();
         int y = e.getY();
         System.out.println("at " + x + ", " + y);

         Position position = new Position(x, y);
         this.listener.handMoved(new InputEvent(this.pointer, position));
      }
   }

   @Override
   public Dimension getMinimumSize() {
      return new Dimension(100, 100);
   }
}
