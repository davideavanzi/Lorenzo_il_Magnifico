package it.polimi.ingsw.lim.parser;

import it.polimi.ingsw.lim.controller.GameController;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Game;
import it.polimi.ingsw.lim.model.cards.Card;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

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

    public static Board reader(String pathToWriterFile){
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.reader(Board.class);
        Board board = null;
        try {
            board = reader.readValue(new File(pathToWriterFile));
        }catch (IOException e){
            e.printStackTrace();
        }
        return board;
    }

    public static void main (String args[]){
        GameController gameController = new GameController();
        gameController.createGame();
        try {
            writer(gameController.getBoard());
            reader(PATH_TO_WRITER_FILE);
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }
}
