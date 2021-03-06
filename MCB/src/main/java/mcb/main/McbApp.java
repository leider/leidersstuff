package mcb.main;

import java.awt.EventQueue;

import javax.swing.UIManager;

import mcb.persistenz.PersistenceStore;
import mcb.ui.adresse.AdresseFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class McbApp {
  private static void initializeLookAndFeel() {
    try {
      String osName = System.getProperty("os.name");
      if (osName.startsWith("Windows")) {
        UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
      } else if (osName.startsWith("Mac")) {
        // do nothing, use the Mac Aqua L&f
      } else {
        UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
      }
    } catch (Exception e) {
      // Likely the Looks library is not in the class path; ignore.
    }
  }

  public static void main(String[] args) {
    McbApp.initializeLookAndFeel();
    Runnable runnable = new Runnable() {
      @SuppressWarnings("unused")
      public void run() {
        try {
          new AdresseFrame(new PersistenceStore());
        } catch (Exception e) {
          McbApp.LOGGER.fatal("Failed to launch App", e);
        }
      }
    };
    if (EventQueue.isDispatchThread()) {
      runnable.run();
    } else {
      EventQueue.invokeLater(runnable);
    }
  }

  static final Logger LOGGER = LogManager.getLogger();

}
