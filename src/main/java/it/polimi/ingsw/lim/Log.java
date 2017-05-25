package it.polimi.ingsw.lim;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
            logFile = new FileHandler("/tmp/"+fileName);
            log.addHandler(logFile);
            SimpleFormatter formatter = new SimpleFormatter();
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

