package it.polimi.ingsw.lim.network.server.socket;

import it.polimi.ingsw.lim.controller.GameController;
import it.polimi.ingsw.lim.exceptions.BadRequestException;
import it.polimi.ingsw.lim.model.FamilyMember;

import static it.polimi.ingsw.lim.Log.getLog;
import static it.polimi.ingsw.lim.network.CommunicationConstants.*;

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
    private ArrayList<String> command;

    /**
     * The controller of the game.
     */
    GameController gameController;

    public ClientCommandHandler() {
        gameController = handlerCallback.getUser().getRoom().getGameController();
    }

    /**
     * Parse the input object for calling the corrispondent method.
     * @param obj
     */
    void requestHandler(Object obj) {
        getLog().log(Level.INFO,() -> "[COMMAND_HANDLER]: Handling command: "+obj);
        if(obj instanceof String) {
            command = new ArrayList<>(Arrays.asList(((String) obj).split(SPLITTER_REGEX)));
            String commandIdentifier = command.get(0);
            if (commandIdentifier.equals(CHAT)) {
                handlerCallback.getUser().getRoom().chatMessageToRoom(command.get(1), command.get(2));
            } else if (commandIdentifier.equals(FAMILY_MEMBER)) {
                placeFMcmd();
            } else if (commandIdentifier.equals(EXCOMMUNICATION)) {
                //todo chiamare metodo su gameController
                handlerCallback.commandValidator(EXCOMMUNICATION, EXCOMMUNICATION_OK , true);
            } else if (commandIdentifier.equals(CHOOSE_FAVOR)) {
                //Todo trasformare String in arrayList<Integer>
                //gameController.performCfActivation();
                handlerCallback.commandValidator(CHOOSE_FAVOR, CHOOSE_FAVOR_OK , true);
            } else if (commandIdentifier.equals(OPTIONAL_BP_PICK)) {
                //TODO metodo in GC a cui passo il booleano bpPayment
                handlerCallback.commandValidator(OPTIONAL_BP_PICK, OPTIONAL_BP_PICK_OK , true);
            } else {
                getLog().log(Level.SEVERE, () ->"[COMMAND_HANDLER]: Invalid message identifier: "+commandIdentifier);
            }
        }
    }

    private void placeFMcmd() {
        FamilyMember fm = handlerCallback.getUser().getPlayer().getFamilyMember(command.get(1));
        int servants = Integer.parseInt(command.get(4));
        try {
            if (command.get(2).contains(TOWER)) {
                String twrColor = command.get(2).replace(" Tower", "");
                int floor = Integer.parseInt(command.get(3));
                gameController.moveInTower(fm, twrColor, floor, servants);
            } else if (command.get(2).equalsIgnoreCase(MARKET)) {
                int marketSlot = Integer.parseInt(command.get(3));
                gameController.moveInMarket(fm, marketSlot, servants);
            } else if (command.get(2).equalsIgnoreCase(PRODUCTION)) {
                gameController.moveInProduction(fm, servants);
            } else if (command.get(2).equalsIgnoreCase(HARVEST)) {
                gameController.moveInHarvest(fm, servants);
            } else if (command.get(2).equalsIgnoreCase(COUNCIL)) {
                gameController.moveInCouncil(fm, servants);
            }
            handlerCallback.commandValidator(FAMILY_MEMBER, FAMILY_MEMBER_OK , true);
        } catch (BadRequestException e) {
            handlerCallback.commandValidator(FAMILY_MEMBER, e.getMessage(), false);
        }
    }
}
