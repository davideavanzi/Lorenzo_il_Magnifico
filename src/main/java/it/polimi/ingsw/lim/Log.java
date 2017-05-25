package it.polimi.ingsw.lim;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

import static it.polimi.ingsw.lim.Settings.LOG_PATH;

/**
 * Created by Nico.
 * This class is used for logging.
 */

public class Log {
    private static final Logger log = Logger.getLogger(Log.class.getName());

    /**
     * Create log file.
     * @throws IOException
     */
    public static void createLogFile() throws IOException {
        FileHandler logFile = null;
        String fileName = new SimpleDateFormat("dd-MM-yyyy_hh-mm-ss'.log'").format(new Date());
        try {
            logFile = new FileHandler(LOG_PATH+fileName);
            log.addHandler(logFile);
            MyFormatter formatter = new MyFormatter();
            logFile.setFormatter(formatter);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Return the Logger object.
     * @return
     */
    public static Logger getLog() {
        return log;
    }
}

/**
 * The following "MyFormatter" class is obtained from https://kodejava.org/how-do-i-create-a-custom-logger-formatter/
 * It's only purpose is to create a clean output in the logging files.
 */
class MyFormatter extends Formatter {
    // Create a DateFormat to format the logger timestamp.
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder(1000);
        builder.append(df.format(new Date(record.getMillis()))).append(" - ");
        builder.append("[").append(record.getSourceClassName()).append(".");
        builder.append(record.getSourceMethodName()).append("] - ");
        builder.append("[").append(record.getLevel()).append("] - ");
        builder.append(formatMessage(record));
        builder.append("\n");
        return builder.toString();
    }

    public String getHead(Handler h) {
        return super.getHead(h);
    }

    public String getTail(Handler h) {
        return super.getTail(h);
    }
}