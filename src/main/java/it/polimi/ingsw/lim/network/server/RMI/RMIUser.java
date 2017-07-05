package it.polimi.ingsw.lim.network.server.RMI;

import it.polimi.ingsw.lim.controller.User;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.network.client.RMI.RMIClientInterf;
import org.codehaus.jackson.annotate.JsonIgnore;

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

    RMIUser(){
        super();
        new Thread(new RMIAliveness(this)).start();
    }

    public void setRci(RMIClientInterf rci){
        this.rci = rci;
    }

    public RMIClientInterf getRci(){
        return rci;
    }


    @Override
    public void sendChatMessage(String sender, String message) {
        try {
            RMIServer.sendChatMessageToClient(sender, message, this.rci);
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI]: Remote error sending chat message to client.");
        }
    }

    @Override
    public void gameMessage(String message) {
        try {
            RMIServer.sendGameMessageToClient(message, this.rci);
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI]: Remote error sending game message to client.");
        }
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

    @JsonIgnore
    @Override
    public void isPlayerTurn(boolean isPlaying) {
        try {
            RMIServer.setPlayerTurn(isPlaying, this.rci);
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI]: Remote error sending board and arrayList of player to client.");
        }
    }

    public void askProductionOptions(ArrayList<ArrayList<Assets[]>> options) {

    }

    @Override
    public void chooseFavor(int amount) {}

    @Override
    public void broadcastMessage(String message) {
    }

    @Override
    public void askForOptionalBpPick(int requirement, int cost) {
    }

    @Override
    public void askForExcommunication() {
        try {
            RMIServer.excommunicationChoice(this.rci);
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI]: Remote error sending board and arrayList of player to client.");
        }
    }

    @Override
    public void notifyFastTowerMove(HashMap<String, Integer> baseStr, Assets optionalPickDiscount) {

    }

    @Override
    public void notifyFastProduction(int baseStr) {

    }

    @Override
    public void notifyFastHarvest(int baseStr) {

    }

    @Override
    public void gameError(String message) {

    }

    private void ping() throws RemoteException {
      this.rci.isAlive();
    }

    private class RMIAliveness implements Runnable{
        private RMIUser user;

        RMIAliveness(RMIUser user) {
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
