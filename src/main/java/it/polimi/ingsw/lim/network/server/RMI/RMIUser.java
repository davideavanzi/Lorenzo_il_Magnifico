package it.polimi.ingsw.lim.network.server.RMI;

import com.sun.org.apache.regexp.internal.RE;
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
import static java.lang.Thread.sleep;

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
        new Thread(new RMIAliveness(this)).start();
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
    public void isPlayerTurn(Boolean isPlaying) {
        try {
            RMIServer.setPlayerTurn(isPlaying, this.rci);
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

    @Override
    public boolean askForOptionalBpPick(int requirement, int cost) {
        return false;
    }

    @Override
    public boolean askForExcommunication() {
        return false;
    }

    public void ping() throws RemoteException {
      this.rci.isAlive();
    }

    private class RMIAliveness implements Runnable{
        private RMIUser user;

        public RMIAliveness(RMIUser user) {
            this.user = user;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    sleep(1000);
                    user.ping();
                }
            } catch (InterruptedException e) {
                getLog().log(Level.SEVERE, () -> "RMI Aliveness thread interrupted for user "+user.getUsername());
            } catch (RemoteException e) {
                user.hasDied();
            }
            Thread.currentThread().interrupt();
        }
    }
}
