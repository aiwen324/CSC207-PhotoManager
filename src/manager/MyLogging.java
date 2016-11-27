package manager;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


// Using Single Design Pattern
public class MyLogging {

	static Logger logger;

	public Handler fileHandler;

	Formatter plainText;

	private MyLogging() throws IOException {
		// instance the logger
		logger = Logger.getLogger(MyLogging.class.getName());
		logger.setLevel(Level.ALL);
		// instance the filehandler
		fileHandler = new FileHandler("myLog.txt", true);
		fileHandler.setLevel(Level.ALL);
		// instance formatter, set formatting, and handler
		plainText = new SimpleFormatter();
		fileHandler.setFormatter(plainText);
		logger.addHandler(fileHandler);
	}

	private static Logger getLogger() {
		if (logger == null) {
			try {
				new MyLogging();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return logger;
	}

	public static void log(Level level, String msg) {
		getLogger().log(level, msg);
		//System.out.println(msg);
	}
	
	public static void log(Level level, String msg, Exception e){
		getLogger().log(level, msg, e);
	}
}
