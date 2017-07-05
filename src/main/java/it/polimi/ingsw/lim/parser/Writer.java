package it.polimi.ingsw.lim.parser;

import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.controller.Room;
import it.polimi.ingsw.lim.model.Board;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.File;
import java.io.IOException;

import static it.polimi.ingsw.lim.parser.KeyConst.*;

/**
 * The task of this class is to write in a file all the game parameters, in order to make the game persistent
 */
public class Writer {
    public static String boardWriter(Board board) {
        String pathToWriterFile = PATH_TO_WRITER_BOARD_FILE;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        File file = new File(pathToWriterFile);
        try{
            if (file.createNewFile());
            mapper.writeValue(file, board);
        }catch (IOException e){
            Log.getLog().severe("[WRITER]:Unable to open or create board writer file");
            e.printStackTrace();
        }
        return pathToWriterFile;
    }

    public static Board readerBoard(String pathToWriterBoardFile){
        ObjectMapper mapper = new ObjectMapper();
        Board board = null;
        try {
            board = mapper.readValue(new File(pathToWriterBoardFile), Board.class);
        }catch (IOException e){
            Log.getLog().severe("[READER]:Unable to open board writer file");
            e.printStackTrace();
        }
        return board;
    }

    public static String roomWriter(Room room){
        String pathToWriterFile = PATH_TO_WRITER_ROOM_FILE;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        File file = new File(pathToWriterFile);
        try{
            if (file.createNewFile());
            mapper.writeValue(file, room);
        }catch (IOException e){
            Log.getLog().severe("[WRITER]:Unable to open or create room writer file");
            e.printStackTrace();
        }
        return pathToWriterFile;
    }

    public static Room readerRoom(String pathToWriterRoomFile){
        ObjectMapper mapper = new ObjectMapper();
        Room room = null;
        try {
            room = mapper.readValue(new File(pathToWriterRoomFile), Room.class);
        }catch (IOException e){
            Log.getLog().severe("[READER]:Unable to open board writer file");
            e.printStackTrace();
        }
        return room;
    }
}
