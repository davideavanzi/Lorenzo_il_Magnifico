package it.polimi.ingsw.lim.network.server.socket;

import it.polimi.ingsw.lim.controller.GameController;
import it.polimi.ingsw.lim.exceptions.BadRequestException;
import it.polimi.ingsw.lim.model.FamilyMember;

import java.util.ArrayList;
import java.util.logging.Level;

import static it.polimi.ingsw.lim.network.CommunicationConstants.*;
import static it.polimi.ingsw.lim.utils.Log.getLog;

/**
 * This class handles commands received from client and forwards them to the game controller.
 */
@SuppressWarnings("SQUID.1166")
class ClientCommandHandler {

    /**
     * The controller of the game.
     */
    GameController gameController;
    /**
     * Store the input command.
     */
    Object[] command;
    /**
     * A callback to the client handler.
     */
    private SocketClientHandler handlerCallback;

    /**
     * Link every SocketClientHandler thread to the correspondent ClientCommandHandler.
     * @param handlerCallback
     */
    ClientCommandHandler(SocketClientHandler handlerCallback) {
        this.handlerCallback = handlerCallback;
    }

    public ClientCommandHandler() {
        gameController = handlerCallback.getUser().getRoom().getGameController();
    }

    /**
     * Parse the input object for calling the correspondent method.
     * @param obj the packet sent by client
     */
    void requestHandler(Object obj) {
        if(obj instanceof Object[]) {
            command = (Object[]) obj;
            String commandID = (String)command[0];
            getLog().log(Level.INFO,() -> "[COMMAND_HANDLER]: Handling command: "+commandID);
            if (commandID.equals(CHAT)) {
                handlerCallback.getUser().getRoom().chatMessageToRoom((String)command[1], (String)command[2]);
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
                            .applyExcommAnswer(handlerCallback.getUser(), (Boolean)command[1]);
                    handlerCallback.commandValidator(EXCOMMUNICATION, EXCOMMUNICATION_OK, true);
                } catch (BadRequestException e) {
                    handlerCallback.commandValidator(EXCOMMUNICATION, e.getMessage(), false);
                }
            } else if (commandID.equals(CHOOSE_LEADER_DRAFT)) {
                try {
                    gameController.getRoomCallback().getDraftRound().applyChoice(handlerCallback.getUser().getUsername(),
                            (Integer)command[1]);
                    handlerCallback.commandValidator(CHOOSE_LEADER_DRAFT, CHOOSE_LEADER_DRAFT_OK, true);
                } catch (BadRequestException e) {
                    handlerCallback.commandValidator(CHOOSE_LEADER_DRAFT, e.getMessage(), false);
                }
            } else if (commandID.equals(LORENZO_MEDICI)) {
                try {
                    gameController.applyLeaderCopyChoice((Integer)command[1], handlerCallback.getUser());
                    handlerCallback.commandValidator(LORENZO_MEDICI, LORENZO_MEDICI_OK, true);
                } catch (BadRequestException e) {
                    handlerCallback.commandValidator(LORENZO_MEDICI, e.getMessage(), false);
                }
            } else if (commandID.equals(LORENZO_MONTEFELTRO)) {
                try {
                    gameController.applyLeaderFmBonus((String)command[1], handlerCallback.getUser());
                    handlerCallback.commandValidator(LORENZO_MONTEFELTRO, LORENZO_MONTEFELTRO_OK, true);
                } catch (BadRequestException e) {
                    handlerCallback.commandValidator(LORENZO_MONTEFELTRO, e.getMessage(), false);
                }
            } else if (commandID.equals(ACTIVATE_LEADER)) {
                try {
                    gameController.activateLeader((Integer)command[1], handlerCallback.getUser());
                    handlerCallback.commandValidator(ACTIVATE_LEADER, ACTIVATE_LEADER_OK, true);
                } catch (BadRequestException e) {
                    handlerCallback.commandValidator(ACTIVATE_LEADER, e.getMessage(), false);
                }
            } else if (commandID.equals(DEPLOY_LEADER)) {
                try {
                    gameController.deployLeader((Integer) command[1], handlerCallback.getUser());
                    handlerCallback.commandValidator(DEPLOY_LEADER, DEPLOY_LEADER_OK, true);
                } catch (BadRequestException e) {
                    handlerCallback.commandValidator(DEPLOY_LEADER, e.getMessage(), false);
                }
            } else if (commandID.equals(DISCARD_LEADER)) {
                try {
                    gameController.discardLeader((Integer) command[1], handlerCallback.getUser());
                handlerCallback.commandValidator(DISCARD_LEADER, DISCARD_LEADER_OK, true);
                } catch (BadRequestException e) {
                    handlerCallback.commandValidator(DISCARD_LEADER, e.getMessage(), false);
                }
            } else if (commandID.equals(CHOOSE_FAVOR)) {
                try {
                    gameController.performCfActivation((ArrayList<Integer>) command[1]);
                    handlerCallback.commandValidator(CHOOSE_FAVOR, CHOOSE_FAVOR_OK, true);
                } catch (BadRequestException e) {
                    handlerCallback.commandValidator(CHOOSE_FAVOR, e.getMessage(), false);
                }
            } else if (commandID.equals(OPTIONAL_BP_PICK)) {
                try {
                    gameController.confirmTowerMove((Boolean)command[1]);
                    handlerCallback.commandValidator(OPTIONAL_BP_PICK, OPTIONAL_BP_PICK_OK, true);
                } catch (BadRequestException e) {
                    handlerCallback.commandValidator(OPTIONAL_BP_PICK, e.getMessage(), false);                }
            } else if (commandID.equals(CHOOSE_PRODUCTION)) {
                try {
                    gameController.confirmProduction((ArrayList<Integer>)command[1]);
                    handlerCallback.commandValidator(CHOOSE_PRODUCTION, CHOOSE_PRODUCTION_OK, true);
                } catch (BadRequestException e) {
                    handlerCallback.commandValidator(CHOOSE_PRODUCTION, e.getMessage(), false);
                }
            } else if (commandID.equals(SERVANTS_PRODUCTION)) {
                try {
                    gameController.performFastProduction((Integer)command[1], handlerCallback.getUser());
                    handlerCallback.commandValidator(SERVANTS_PRODUCTION, SERVANTS_PRODUCTION_OK, true);
                } catch (BadRequestException e) {
                    handlerCallback.commandValidator(SERVANTS_PRODUCTION, e.getMessage(), false);
                }
            } else if (commandID.equals(SERVANTS_HARVEST)) {
                try {
                    gameController.performFastHarvest((Integer)command[1], handlerCallback.getUser());
                    handlerCallback.commandValidator(SERVANTS_HARVEST, SERVANTS_HARVEST_OK, true);
                } catch (BadRequestException e) {
                    handlerCallback.commandValidator(SERVANTS_HARVEST, e.getMessage(), false);
                }
            } else if (commandID.equals(PICK_FROM_TOWER)) {
                try {
                    gameController.performFastTowerMove((Integer)command[1], (String)command[2],
                            (Integer)command[3],handlerCallback.getUser());
                    handlerCallback.commandValidator(PICK_FROM_TOWER, PICK_FROM_TOWER_OK, true);
                } catch (BadRequestException e) {
                    handlerCallback.commandValidator(PICK_FROM_TOWER, e.getMessage(), false);
                }
            } else if (commandID.equals(ACTIVATE_LEADER)) {

            } else if (commandID.equals(DEPLOY_LEADER)) {

            } else if (commandID.equals(DISCARD_LEADER)) {

            } else {
                getLog().log(Level.SEVERE, () ->"[COMMAND_HANDLER]: Invalid message identifier: "+commandID);
            }
        }
    }

    private void placeFMcmd() throws BadRequestException {
        FamilyMember fm = handlerCallback.getUser().getPlayer().getFamilyMember((String)command[1]);
        int servants = Integer.parseInt((String)command[4]);
        if (((String)command[2]).contains(TOWER)) {
            String twrColor = ((String)command[2]).replace(" Tower", "");
            int floor = (Integer)command[3];
            gameController.moveInTower(fm, twrColor, floor, servants);
        } else if (((String)command[2]).equalsIgnoreCase(MARKET)) {
            int marketSlot = (Integer)command[3];
            gameController.moveInMarket(fm, marketSlot, servants);
        } else if (((String)command[2]).equalsIgnoreCase(PRODUCTION)) {
            gameController.moveInProduction(fm, servants);
        } else if (((String)command[2]).equalsIgnoreCase(HARVEST)) {
            gameController.moveInHarvest(fm, servants);
        } else if (((String)command[2]).equalsIgnoreCase(COUNCIL)) {
            gameController.moveInCouncil(fm, servants);
        }
    }
}
