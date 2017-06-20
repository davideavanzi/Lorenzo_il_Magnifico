package it.polimi.ingsw.lim.network.server.socket;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by nico.
 */
class ClientCommandHandler {

    ClientCommandHandler() {}

    void requestHandler(Object obj) {
        if(obj instanceof String) {
            ArrayList<String> command = new ArrayList<String>(Arrays.asList(((String) obj).split(" ")));
            if(command.get(0).equals(ANSWER_SERVANT_AMOUNT))
        }
    }
}
