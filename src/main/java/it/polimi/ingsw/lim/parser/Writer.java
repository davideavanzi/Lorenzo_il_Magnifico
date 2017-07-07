package it.polimi.ingsw.lim.parser;

import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.controller.Room;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Game;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.File;
import java.io.IOException;

import static it.polimi.ingsw.lim.parser.KeyConst.*;

/**
 * The task of this class is to write in a file all the game parameters, in order to make the game persistent
 */
public class Writer {
    public static String gameWriter(Game game, int id) {
        String pathToWriterFile = (PATH_TO_WRITER_GAME_FILE + id).concat(EXTENTION);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        File file = new File(pathToWriterFile);
        try{
            file.createNewFile();
            mapper.writeValue(file, game);
        }catch (IOException e){
            Log.getLog().severe("[WRITER]:Unable to open or create board writer file");
            e.printStackTrace();
            return null;
        }
        return pathToWriterFile;
    }

    public static Game gameReader(int id) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        Game game;
        String pathToWriterGameFile = PATH_TO_WRITER_GAME_FILE + id + EXTENTION;
        game = mapper.readValue(new File(pathToWriterGameFile), Game.class);
        return game;
    }

    public static String roomWriter(Room room, int id){
        String pathToWriterFile = (PATH_TO_WRITER_ROOM_FILE + id).concat(EXTENTION);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        File file = new File(pathToWriterFile);
        try{
            file.createNewFile();
            mapper.writeValue(file, room);
        }catch (IOException e){
            Log.getLog().severe("[WRITER]:Unable to open or create room writer file");
            e.printStackTrace();
        }
        return pathToWriterFile;
    }

    public static Room readerRoom(File file){
        ObjectMapper mapper = new ObjectMapper();
        Room room = null;
        try {
            room = mapper.readValue(file, Room.class);
        }catch (IOException e){
            Log.getLog().severe("[READER]:Unable to open board writer file");
            e.printStackTrace();
        }
        return room;
    }

}
