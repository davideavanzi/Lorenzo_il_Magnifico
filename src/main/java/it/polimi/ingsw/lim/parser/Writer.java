package it.polimi.ingsw.lim.parser;


import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;

import static it.polimi.ingsw.lim.parser.KeyConst.*;

/**
 * The task of this class is to write in a file all the game parameters, in order to make the game persistent
 */
public class Writer {
    public static String writer(Object ob) throws IOException{
        String pathToWriterFile = PATH_TO_WRITER_FILE;
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(pathToWriterFile);
        if (file.createNewFile());
        mapper.writeValue(file, ob);
        return pathToWriterFile;
    }
}
