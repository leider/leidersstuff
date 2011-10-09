package mcb.frame;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;

import mcb.persistenz.PersistenceStore;

import org.apache.log4j.Logger;

public class McbApp extends JFrame {
	static final Logger LOGGER = Logger.getLogger(McbApp.class.getName());

	private static final long serialVersionUID = -6092233954906826211L;

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

}
