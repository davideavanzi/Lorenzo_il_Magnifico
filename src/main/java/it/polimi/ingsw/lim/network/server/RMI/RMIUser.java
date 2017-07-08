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
import java.util.logging.Level;

import static it.polimi.ingsw.lim.Log.getLog;
import static java.lang.Thread.sleep;

/**
 *
 */
public class RMIUser extends User {
    /**
     * rmi Client reference.
     */
    @JsonIgnore
    transient RMIClientInterf rci;

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

    RMIUser() {
        super();
        new Thread(new RMIAliveness(this)).start();
    }

    @JsonIgnore
    public void setRci(RMIClientInterf rci) {
        this.rci = rci;
    }

    @JsonIgnore
    public RMIClientInterf getRci() {
        return rci;
    }


    @Override
    public void notifyFastHarvest(int baseStr) {
        if (!this.getIsAlive())
            return;
        try {
            RMIServer.askClientForFastHarvest(baseStr, this.rci);
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI]: Remote error sending bonus harvest action request to client.");
        }
    }

    @Override
    public void notifyFastProduction(int baseStr) {
        if (!this.getIsAlive())
            return;
        try {
            RMIServer.askClientForFastProduction(baseStr, this.rci);
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI]: Remote error sending bonus production action request to client.");
        }
    }

    @Override
    public void notifyFastTowerMove(HashMap<String, Integer> baseStr, Assets optionalPickDiscount) {
        if (!this.getIsAlive())
            return;
        try {
            RMIServer.askClientForFastTowerMove(baseStr,optionalPickDiscount, this.rci);
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI]: Remote error sending bonus tower action request to client.");
        }
    }

    @JsonIgnore
    @Override
    public void askForProductionOptions(ArrayList<ArrayList<Object[]>> options) {
        if (!this.getIsAlive())
            return;
        try {
            RMIServer.askClientForProductionOptions(options, this.rci);
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI]: Remote error sending production choice request to client.");
        }
    }

    @Override
    public void askForOptionalBpPick() {
        if (!this.getIsAlive())
            return;
        try {
            RMIServer.askClientForOptionalBpPick(this.rci);
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI]: Remote error sending BP/resources request to client.");
        }
    }

    @Override
    public void askForExcommunication() {
        if (!this.getIsAlive())
            return;
        try {
            RMIServer.askClientForExcommunication(this.rci);
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI]: Remote error sending board and arrayList of player to client.");
        }
    }

    @Override
    public void askForCouncilFavor(int favorAmount) {
        if (!this.getIsAlive())
            return;
        try {
            RMIServer.askClientForFavor(favorAmount, this.rci);
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI]: Remote error sending favour to client.");
        }
    }

    @Override
    public void sendChatMessage(String sender, String message) {
        if (!this.getIsAlive())
            return;
        try {
            RMIServer.sendChatMessageToClient(sender, message, this.rci);
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI]: Remote error sending chat message to client.");
        }
    }

    @Override
    public void gameMessage(String message) {
        if (!this.getIsAlive())
            return;
        try {
            RMIServer.sendGameMessageToClient(message, this.rci);
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI]: Remote error sending game message to client.");
        }
    }

    @Override
    public void broadcastMessage(String message) {

    }

    /**
     *
     * @param board the game board
     * @param players arrayList of connected player
     */
    @Override
    public void sendGameUpdate(Board board, ArrayList<Player> players) {
        if (!this.getIsAlive())
            return;
        try {
            RMIServer.sendGameToClient(board, players, this.rci);
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI]: Remote error sending board and arrayList of player to client.");
        }
    }

    @JsonIgnore
    @Override
    public void isPlayerRound(boolean isPlaying) {
        if (!this.getIsAlive())
            return;
        try {
            RMIServer.setPlayerTurn(isPlaying, this.rci);
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI]: Remote error sending board and arrayList of player to client.");
        }
    }

    @Override
    public void notifyGameStart() {
        try {
            RMIServer.sendStartGameNotification(this.rci);
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI]: Remote error sending board and arrayList of player to client.");
        }
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
            } catch (RemoteException | NullPointerException e) {
                user.hasDied();
            }
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void notifyEndGame(ArrayList<Player> players){
        //todo implementare
    }


    @Override
    public void chooseLeaderToCopy(ArrayList<String> copyableLeaders){
        //todo implementare
    }

}
