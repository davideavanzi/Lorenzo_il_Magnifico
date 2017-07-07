package it.polimi.ingsw.lim.network.server.socket;

import it.polimi.ingsw.lim.controller.GameController;
import it.polimi.ingsw.lim.exceptions.BadRequestException;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.FamilyMember;

import static it.polimi.ingsw.lim.Log.getLog;
import static it.polimi.ingsw.lim.network.CommunicationConstants.*;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 */
class ClientCommandHandler {

    /**
     * Link every SocketClientHandler thread to the correspondent ClientCommandHandler.
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
     * The controller of the game.
     */
    GameController gameController;

    /**
     * Array used to store the command send by client.
     */
    String[] command;

    public ClientCommandHandler() {
        gameController = handlerCallback.getUser().getRoom().getGameController();
    }

    /**
     * Parse the input object for calling the correspondent method.
     * @param obj
     */
    void requestHandler(Object obj) {
        getLog().log(Level.INFO,() -> "[COMMAND_HANDLER]: Handling command: "+obj);
        if(obj instanceof String) {
            Object[] cmd = (Object[])obj;
            command = (String[])cmd;
            String commandID = (String)cmd[0];
            if (commandID.equals(CHAT)) {
                handlerCallback.getUser().getRoom().chatMessageToRoom(command[1], command[2]);
            } else if (commandID.equals(FAMILY_MEMBER)) {
                try {
                    placeFMcmd();
                    handlerCallback.commandValidator(FAMILY_MEMBER, FAMILY_MEMBER_OK, true);
                } catch (BadRequestException e) {
                    handlerCallback.commandValidator(FAMILY_MEMBER, e.getMessage(), true);
                }
            } else if (commandID.equals(EXCOMMUNICATION)) {
                try {
                    gameController.getRoomCallback().getExcommunicationRound()
                            .applyExcommAnswer(handlerCallback.getUser(), Boolean.valueOf(command[1]));
                    handlerCallback.commandValidator(EXCOMMUNICATION, EXCOMMUNICATION_OK, true);
                } catch (BadRequestException e) {
                    handlerCallback.commandValidator(EXCOMMUNICATION, e.getMessage(), false);
                }
            } else if (commandID.equals(CHOOSE_FAVOR)) {
                try {
                    //gameController.performCfActivation();
                    handlerCallback.commandValidator(CHOOSE_FAVOR, CHOOSE_FAVOR_OK, true);
                } catch (BadRequestException e) {
                    handlerCallback.commandValidator(CHOOSE_FAVOR, e.getMessage(), false);
                }
            } else if (commandID.equals(OPTIONAL_BP_PICK)) {
                try {
                    //TODO metodo in GC a cui passo il booleano bpPayment
                    handlerCallback.commandValidator(OPTIONAL_BP_PICK, OPTIONAL_BP_PICK_OK, true);
                } catch (BadRequestException e) {
                    handlerCallback.commandValidator(OPTIONAL_BP_PICK, e.getMessage(), false);                }
            } else if (commandID.equals(CHOOSE_PRODUCTION)) {
                try {
                    //todo metodo su GameController
                    handlerCallback.commandValidator(CHOOSE_PRODUCTION, CHOOSE_PRODUCTION_OK, true);
                } catch (BadRequestException e) {
                    handlerCallback.commandValidator(CHOOSE_PRODUCTION, e.getMessage(), false);
                }
            } else if (commandID.equals(SERVANTS_PRODUCTION)) {
                try {
                    //todo metodo su GameController
                    handlerCallback.commandValidator(SERVANTS_PRODUCTION, SERVANTS_PRODUCTION_OK, true);
                } catch (BadRequestException e) {
                    handlerCallback.commandValidator(SERVANTS_PRODUCTION, e.getMessage(), false);
                }
            } else if (commandID.equals(SERVANTS_HARVEST)) {
                try {
                    //todo metodo su GameController
                    handlerCallback.commandValidator(SERVANTS_HARVEST, SERVANTS_HARVEST_OK, true);
                } catch (BadRequestException e) {
                    handlerCallback.commandValidator(SERVANTS_HARVEST, e.getMessage(), false);
                }
            } else if (commandID.equals(PICK_FROM_TOWER)) {
                try {
                    //todo metodo su GameController
                    handlerCallback.commandValidator(PICK_FROM_TOWER, PICK_FROM_TOWER_OK, true);
                } catch (BadRequestException e) {
                    handlerCallback.commandValidator(PICK_FROM_TOWER, e.getMessage(), false);
                }
            } else {
                getLog().log(Level.SEVERE, () ->"[COMMAND_HANDLER]: Invalid message identifier: "+commandID);
            }
        }
    }

    private void placeFMcmd() throws BadRequestException {
        FamilyMember fm = handlerCallback.getUser().getPlayer().getFamilyMember(command[1]);
        int servants = Integer.parseInt(command[4]);
        if (command[2].contains(TOWER)) {
            String twrColor = command[2].replace(" Tower", "");
            int floor = Integer.parseInt(command[3]);
            gameController.moveInTower(fm, twrColor, floor, servants);
        } else if (command[2].equalsIgnoreCase(MARKET)) {
            int marketSlot = Integer.parseInt(command[3]);
            gameController.moveInMarket(fm, marketSlot, servants);
        } else if (command[2].equalsIgnoreCase(PRODUCTION)) {
            gameController.moveInProduction(fm, servants);
        } else if (command[2].equalsIgnoreCase(HARVEST)) {
            gameController.moveInHarvest(fm, servants);
        } else if (command[2].equalsIgnoreCase(COUNCIL)) {
            gameController.moveInCouncil(fm, servants);
        }
    }

    ArrayList<Integer> unpackPacket(String[] cmd) {
        int count = 1;
        ArrayList<Integer>
        for (String charNum : cmd) {

        }
        return ;
    }
}
