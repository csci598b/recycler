package edu.mines.csci598.inputproto;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.jna.IntegerType;
import lombok.NonNull;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.Queue;
import java.lang.Math;

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

      this.label = new JLabel("Test Panel!");
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

      private ArrayBlockingQueue<Double> xPositions = new ArrayBlockingQueue(10);
      private ArrayBlockingQueue<Double> yPositions = new ArrayBlockingQueue(10);
      private ArrayBlockingQueue<Double> xVelocities = new ArrayBlockingQueue(10);
      private ArrayBlockingQueue<Double> yVelocities = new ArrayBlockingQueue(10);


      private double getSmoothedDerivative(double value, ArrayBlockingQueue<Double> previousValues) {
          int size = previousValues.size();
          int currentValueTotal = 0;
          Object[] previousValuesArray = previousValues.toArray();
          if(size > 2){
              if((Double) previousValuesArray[0] > (Double) previousValuesArray[size - 1]){
                  if((Double) previousValuesArray[size - 1] < value){
                      previousValues.clear();
                      return 0.0;
                  }
              }
              if((Double) previousValuesArray[0] < (Double) previousValuesArray[size - 1]){
                  if((Double) previousValuesArray[size - 1] > value){
                      previousValues.clear();
                      return 0.0;
                  }
              }
          }
          for(int i = 0; i < size; i++){
              currentValueTotal += value - (Double) previousValuesArray[i];
          }
          return currentValueTotal / Math.max(size, 1);

      }
      private void addElement(double value, ArrayBlockingQueue<Double> previousValues) {
          if(previousValues.remainingCapacity() == 0){
              previousValues.poll();
          }
          previousValues.offer(value);
          return;
      }


       public MouseListenerAdapter(InputEventListener listener) {
         this.listener = listener;
         this.pointer = new Pointer();
         System.out.println("registered!");
      }

      @Override
      public void mouseMoved(MouseEvent e) {
         int x = e.getX();
         int y = e.getY();
         //int xVelocity = x - xPrevious;
         //int yVelocity = y - yPrevious;
         //xPrevious = x;
         //yPrevious = y;

         double xVelocity = this.getSmoothedDerivative(x, xPositions);
         double yVelocity = this.getSmoothedDerivative(y, yPositions);
         double xAcceleration = this.getSmoothedDerivative(xVelocity, xVelocities);
         double yAcceleration = this.getSmoothedDerivative(yVelocity, yVelocities);

         this.addElement(x, xPositions);
         this.addElement(y, yPositions);
         this.addElement(xVelocity, xVelocities);
         this.addElement(yVelocity, yVelocities);


         System.out.println("at " + x + ", " + y);

         Position position = new Position(x, y, xVelocity, yVelocity, xAcceleration, yAcceleration);
         this.listener.handMoved(new InputEvent(this.pointer, position));
      }
   }

   @Override
   public Dimension getMinimumSize() {
      return new Dimension(100, 100);
   }
}
