package it.polimi.ingsw.lim.parser;

import com.sun.xml.internal.ws.developer.SerializationFeature;
import it.polimi.ingsw.lim.controller.GameController;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Game;
import it.polimi.ingsw.lim.model.Tower;
import it.polimi.ingsw.lim.model.cards.Card;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.File;
import java.io.IOException;

import static it.polimi.ingsw.lim.parser.KeyConst.*;

/**
 * The task of this class is to write in a file all the game parameters, in order to make the game persistent
 */
public class Writer {
    public static String boardWriter(Board board ) {
        String pathToWriterFile = PATH_TO_WRITER_FILE;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        File file = new File(pathToWriterFile);
        try{
            if (file.createNewFile());
            mapper.writeValue(file, board);
        }catch (IOException e){
            e.printStackTrace();
        }
        return pathToWriterFile;
    }


    public static Board readerBoard(String pathToWriterFile){
        ObjectMapper mapper = new ObjectMapper();
        Board board = null;
        try {
            board = mapper.readValue(new File(pathToWriterFile), Board.class);
        }catch (IOException e){
            e.printStackTrace();
        }
        return board;
    }

}
