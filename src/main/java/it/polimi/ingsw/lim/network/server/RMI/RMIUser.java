package it.polimi.ingsw.lim.network.server.RMI;

import it.polimi.ingsw.lim.controller.User;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.network.client.RMI.RMIClientInterf;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import static it.polimi.ingsw.lim.Log.getLog;

/**
 * Created by nico.
 */
public class RMIUser extends User {
    /**
     * rmi Client reference.
     */
    RMIClientInterf rci;

    /**
     * Constructor.
     * @param username
     * @param rci
     */
    RMIUser(String username, RMIClientInterf rci) {
        super(username);
        this.rci = rci;
    }

    /**
     *
     * @param board the game board
     * @param players arrayList of connected player
     */
    @Override
    public void sendGameUpdate(Board board, ArrayList<Player> players) {
        try {
            RMIServer.sendGameToClient(board, players, this.rci);
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI]: Remote error sending board and arrayList of player to client.");
        }
    }

    @Override
    public void sendChatMessage(String sender, String message) {
        try {
            RMIServer.chatMessageToClient(sender, message, this.rci);
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI]: Remote error sending chat message to client.");
        }
    }

    @Override
    public int askForServants(int minimumAmount) {
        return 0; //TODO: implement
    }

    @Override
    public int chooseProduction(ArrayList<Assets[]> options) {
        return 0; //TODO: implement
    }

    @Override
    public int chooseFavor(List<Assets> possibleFavors) {
        return 0;
    }

    @Override
    public void broadcastMessage(String message) {
    }

    @Override
    public void gameMessage(String message) {
    }

    @Override
    public String chooseTower(HashMap<String, Integer> possibleTowers) {
        return null;
    }

    @Override
    public int chooseFloor() {
        return 0;
    }
}
