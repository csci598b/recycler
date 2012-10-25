package edu.mines.csci598B.backend;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;

import java.io.Closeable;
import java.util.LinkedList;
import java.lang.ref.WeakReference;

/**
 * The GameManager manages resources and system compatibility, and orchestrates
 * interactions between different components.
 */
public final class GameManager
implements Destroyable, Runnable {
  private final ScreenManager screen = new ScreenManager();
  private JFrame frame;
  private final LinkedList<WeakReference<Destroyable> > resources =
    new LinkedList<WeakReference<Destroyable> >();
  private GameState state;
  private final InputStatus inputStatus = new InputStatus();
  private boolean alive = true;
  private final AudioPlayer audioPlayer = new AudioPlayer(this);

  private final LinkedList<InputDriver> inputDrivers =
    new LinkedList<InputDriver>();

  /**
   * Creates a GameManager using a window with the given title
   */
  public GameManager(String title) {
    screen.installNullRepaintManager();
    screen.setFullScreen(screen.getCurrentDisplayMode());
    frame = (JFrame)screen.getFullScreenWindow();
    frame.setTitle(title);

    frame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          alive = false;
        }
      });

    audioPlayer.start();
  }

  @Override
  public void destroy() {
    audioPlayer.stopPlaying();
    for (WeakReference<Destroyable> wd: resources) {
      Destroyable d = wd.get();
      if (d != null)
        d.destroy();
    }

    screen.restoreScreen();
    try {
      audioPlayer.join();
    } catch (InterruptedException e) {}
  }

  /**
   * Adds the given Destroyable to the list of resources to destroy with the
   * manager.
   *
   * These items are destroyed before the frame is destroyed.
   */
  public synchronized void addResource(Destroyable d) {
    resources.add(new WeakReference<Destroyable>(d));
  }

  /**
   * Installs the given InputDriver into this GameManager.
   */
  public synchronized void installInputDriver(InputDriver driver) {
    driver.installInto(this);
    inputDrivers.add(driver);
  }

  /**
   * Changes the current state to the one given. This should only be used to
   * set an initial state; external control should otherwise be avoided.
   */
  public synchronized void setState(GameState neu) {
    state = neu;
  }

  /** Runs the game loop. */
  public void run() {
    long lastClock = System.nanoTime();
    while (alive && state != null) {
      synchronized (this) {
        //Determine elapsed time
        long clock = System.nanoTime();
        float et = (clock-lastClock) * 1.0e-9f;
        lastClock = clock;

        //Handle updating
        if (state != null) {
          state = state.update(et);
        }
      }

      if (state != null) {
        Graphics2D g = screen.getGraphics();
        state.draw(g);
        g.dispose();
      }
      screen.update();

      synchronized (this) {
        //Handle inputs
        if (state != null) {
          for (InputDriver driver: inputDrivers)
            driver.pumpInput(state);
        }
      }
    }
  }

  /**
   * Returns the "virtual height" (vheight) of the drawing area.
   *
   * vheight is the maximum Y coordinate, selected so that any NxN region (for
   * any N) will be square on the display (assuming square pixels).
   */
  public final float vheight() {
    return frame.getHeight() / (float)frame.getWidth();
  }

  /**
   * Returns the Frame used by this GameManager.
   */
  public Frame getFrame() { return frame; }

  /**
   * Returns a component which represents the canvas on which drawing is
   * performed.
   */
  public Component getCanvas() { return frame; }

  /**
   * Returns the shared InputStatus object.
   */
  public InputStatus getSharedInputStatus() { return inputStatus; }

  /**
   * Returns a copy of the current InputStatus.
   */
  public InputStatus getInputStatus() {
    //This would be SO much easier in C...
    InputStatus is = new InputStatus();
    for (int i = 0; i < inputStatus.bodies.length; ++i)
      is.bodies[i] = inputStatus.bodies[i];
    for (int i = 0; i < inputStatus.pointers.length; ++i)
      for (int j = 0; j < inputStatus.pointers[i].length; ++j)
        is.pointers[i][j] = inputStatus.pointers[i][j];
    return is;
  }

  /**
   * Returns the AudioPlayer managed by this GameManager.
   */
  public AudioPlayer getAudioPlayer() {
    return audioPlayer;
  }
}
