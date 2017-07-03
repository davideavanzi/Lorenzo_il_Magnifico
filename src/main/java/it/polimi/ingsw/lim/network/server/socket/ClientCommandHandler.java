package it.polimi.ingsw.lim.network.server.socket;

import it.polimi.ingsw.lim.controller.GameController;
import it.polimi.ingsw.lim.model.FamilyMember;

import static it.polimi.ingsw.lim.Log.getLog;
import static it.polimi.ingsw.lim.network.ServerConstants.*;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

/**
 * Created by nico.
 */
class ClientCommandHandler {

    /**
     * Link every SocketClientHandler to the corrispondent Client Command Handler.
     * @param handlerCallback
     */
    ClientCommandHandler(SocketClientHandler handlerCallback) {
        this.handlerCallback = handlerCallback;
    }

    /**
     * A callback to the client handler.
     */
    private SocketClientHandler handlerCallback;

    /**
     * The client's command is stored here.
     */
    ArrayList<String> command;

    /**
     * Parse the input object for calling the corrispondent method.
     * @param obj
     */
    void requestHandler(Object obj) {
        getLog().log(Level.INFO,() -> "[COMMAND_HANDLER]: Handling command: "+obj);
        if(obj instanceof String) {
            command = new ArrayList<>(Arrays.asList(((String) obj).split(SPLITTER_REGEX)));
            String commandIdentifier = command.get(0);
            if(commandIdentifier.equals(FAMILY_MEMBER)) {
                placeFMcmd();
            } else if (commandIdentifier.equals(CHAT)) {
                handlerCallback.getUser().getRoom().chatMessageToRoom(command.get(1), command.get(2));
            } else {
                getLog().log(Level.SEVERE, () ->"[COMMAND_HANDLER]: Invalid message indentifier: "+commandIdentifier);
            }
        }
    }

    private void placeFMcmd() {
        GameController gc = handlerCallback.getUser().getRoom().getGameController();
        FamilyMember fm = handlerCallback.getUser().getPlayer().getFamilyMember(command.get(1));
        int servants = Integer.parseInt(command.get(4));
        if (command.get(2).contains(TOWER)) {
            String twrColor = command.get(2).replace(" Tower", "");
            int floor = Integer.parseInt(command.get(3));
            gc.moveInTower(fm, twrColor, floor, servants); //familyMember, towerColor, floorNum, servants
        } else if (command.get(2).equalsIgnoreCase(MARKET)) {
            int marketSlot = Integer.parseInt(command.get(3));
            gc.moveInMarket(fm, marketSlot, servants);
        } else if (command.get(2).equalsIgnoreCase(PRODUCTION)) {
            gc.moveInProduction(fm, servants);
        } else if (command.get(2).equalsIgnoreCase(HARVEST)) {
            gc.moveInHarvest(fm, servants);
        }else if (command.get(2).equalsIgnoreCase(COUNCIL)) {
            gc.moveInCouncil(fm, servants);
        }
    }
}
