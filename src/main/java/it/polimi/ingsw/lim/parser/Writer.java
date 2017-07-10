package it.polimi.ingsw.lim.parser;

import it.polimi.ingsw.lim.controller.Room;
import it.polimi.ingsw.lim.model.Game;
import it.polimi.ingsw.lim.utils.Log;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.File;
import java.io.IOException;

import static it.polimi.ingsw.lim.parser.KeyConst.*;

/**
 * The task of this class is to write in a file all the game parameters, in order to make the game persistent
 */
public class Writer {
    /**
     * this method is needed to write a game on a .json file
     * @param game this is the game to be written on the file
     * @param id is the id of the game's room
     * @return the path to the file
     */
    public static String gameWriter(Game game, int id) {
        String pathToWriterFile = (PATH_TO_WRITER_GAME_FILE + id).concat(EXTENTION);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        File file = new File(pathToWriterFile);
        try{
            file.createNewFile();
            mapper.writeValue(file, game);
        }catch (IOException e){
            Log.getLog().severe("[WRITER]:Unable to open or create game writer file "+id);
            return null;
        }
        return pathToWriterFile;
    }

    /**
     * this method is needed to read a game from a .json file
     * @param id is the id of the game's room
     * @return the game red
     * @throws IOException if the file isn't found in the path
     */
    public static Game gameReader(int id) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        Game game;
        String pathToWriterGameFile = PATH_TO_WRITER_GAME_FILE + id + EXTENTION;
        game = mapper.readValue(new File(pathToWriterGameFile), Game.class);
        return game;
    }

    /**
     * this method is needed to write a room on a .json file
     * @param id is the id of the room
     * @return the path to the file
     */
    public static String roomWriter(Room room, int id){
        String pathToWriterFile = (PATH_TO_WRITER_ROOM_FILE + id).concat(EXTENTION);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        File file = new File(pathToWriterFile);
        try{
            file.createNewFile();
            mapper.writeValue(file, room);
        }catch (IOException e){
            Log.getLog().severe("[WRITER]:Unable to open or create room writer file "+id);
        }
        return pathToWriterFile;
    }

    /**
     * this method is needed to read a room from a .json file
     * @return the room red
     */
    public static Room readerRoom(File file) {
        ObjectMapper mapper = new ObjectMapper();
        Room room = null;
        try {
            room = mapper.readValue(file, Room.class);
        } catch (IOException e) {
            Log.getLog().severe("[READER]:Unable to open room writer file");
        }
        return room;
    }
}
