package it.polimi.ingsw.lim.network.server.RMI;

import it.polimi.ingsw.lim.controller.User;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.network.client.RMI.RMIClientInterf;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static it.polimi.ingsw.lim.Log.getLog;

/**
 * Created by nico.
 */
public class RMIUser extends User {
    RMIClientInterf rci;

    public RMIUser(String username, RMIClientInterf rci) {
        super(username);
        this.rci = rci;
    }

    @Override
    public void chatMessage(String sender, String message) {
        try {
            RMIServer.chatMessageToClient(sender, message, this.rci);
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI] Remote error sending chat message to client.");
        }
    }

    @Override
    public void sendGameState(Board board, ArrayList<Player> players) {

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
}
