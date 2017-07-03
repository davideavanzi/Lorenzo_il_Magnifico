package it.polimi.ingsw.lim.network.client.RMI;

import com.sun.org.apache.regexp.internal.RE;
import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.network.client.ServerInterface;
import it.polimi.ingsw.lim.network.server.RMI.RMIServerInterf;
import it.polimi.ingsw.lim.ui.UIController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import static it.polimi.ingsw.lim.network.ServerConstants.*;

/**
 * Created by nico.
 */
public class RMIClient implements RMIClientInterf, ServerInterface {
    /**
     * RMI server's ip address.
     */
    private String address = "localhost";

    /**
     * RMI server's port number.
     */
    private int port = 1099;

    /**
     * Instance of RMI server interface.
     */
    RMIServerInterf rmiServer;

    /**
     * The UI controller's reference.
     */
    UIController uiCallback;

    /**
     * RMI client constructor.
     */
    public RMIClient(UIController uiCallback) {
        this.uiCallback = uiCallback;
    }

    /**
     * @return the server's address.
     */
    private String getAddress() {return address;}

    /**
     * @return the server's rmi port.
     */
    private int getPort() {return port;}

    @Override
    public void updateClientGame(Board board, ArrayList<Player> players) {
        uiCallback.updateBoard(board);
        uiCallback.updatePlayers(players);
    }

    @Override
    public void isUserPlaying(Boolean state) throws RemoteException {
        uiCallback.setIsMyTurn(state);
    }

    @Override
    public int askUserServants(int minimum) throws RemoteException {
        return uiCallback.getClientUI().sendServantsToServer(minimum);
    }

    @Override
    public void placeFM(String fmColor, ArrayList<String> destination, String servants, String username) throws ClientNetworkException {
        int destArgs = Integer.parseInt(destination.get(1)); //floor's number or market slot
        int servantsNum = Integer.parseInt(servants);
        if (destination.get(0).contains(TOWER)) {
            String twrColor = destination.get(0).replace(" Tower", "");
            try {
                rmiServer.moveInTower(fmColor, twrColor, destArgs, servantsNum, username);
            } catch (RemoteException e) {
                throw new ClientNetworkException("[RMI]: Could not send place family member command to server", e);
            }
        } else if (destination.get(0).equalsIgnoreCase(MARKET)) {
            try {
                rmiServer.moveInMarket(fmColor, destArgs, servantsNum, username);
            } catch (RemoteException e) {
                throw new ClientNetworkException("[RMI]: Could not send move to market command to server", e);
            }
        } else if (destination.get(0).equalsIgnoreCase(PRODUCTION)) {
            try {
                rmiServer.moveInProduction(fmColor, destArgs, servantsNum, username) ;
            } catch (RemoteException e) {
                throw new ClientNetworkException("[RMI]: Could not send production command to server", e);
            }
        } else if (destination.get(0).equalsIgnoreCase(HARVEST)) {
            try {
                rmiServer.moveInHarvest(fmColor, destArgs, servantsNum, username);
            } catch (RemoteException e) {
                throw new ClientNetworkException("[RMI]: Could not send harvest command to server", e);
            }
        } else if (destination.get(0).equalsIgnoreCase(COUNCIL)) {
            try {
                rmiServer.moveInCouncil(fmColor, destArgs, servantsNum, username);
            } catch (RemoteException e) {
                throw new ClientNetworkException("[RMI]: Could not send move to council command to server", e);
            }
        }
    }

    /**
     * Send a chat message to server.
     * @param sender the username of the sender.
     * @param message the chat message.
     * @throws ClientNetworkException
     */
    @Override
    public void chatMessageToServer(String sender, String message) throws ClientNetworkException {
        try {
            rmiServer.chatMessageFromClient(sender, message);
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not send a message to server", e);
        }
    }

    /**
     * Print the chat message received.
     * @param sender the username of the sender.
     * @param message the chat message.
     */
    @Override
    public void chatMessageFromServer(String sender, String message) {
        uiCallback.getClientUI().printChatMessage(sender, message);
    }

    @Override
    public void startListen() throws RemoteException {
        uiCallback.startWaitRequest();
    }

    @Override
    public String[] askLogin(String errorMsg) throws RemoteException {
        if(errorMsg != null) uiCallback.getClientUI().printError(errorMsg);
        return uiCallback.sendLoginInfo();
    }

    private void exportObject() throws ClientNetworkException {
        try {
            UnicastRemoteObject.exportObject(this, 0);
        } catch (RemoteException e) {
            throw new ClientNetworkException(e.getMessage(), e);
        }
    }

    /**
     * It's called to establish the connection between server and client.
     * @throws ClientNetworkException
     */
    @Override
    public void connect() throws ClientNetworkException {
        try {
            LocateRegistry.getRegistry(getAddress(), getPort());
            rmiServer = (RMIServerInterf)Naming.lookup("rmi://" + getAddress() + "/lim");
            UnicastRemoteObject.exportObject(rmiServer, 0);
            exportObject();
            rmiServer.initializeConnection(this);
        } catch (NotBoundException | RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not connect to RMI server", e);
        } catch (MalformedURLException e) {
            throw new ClientNetworkException("[RMI]: URL not correct", e);
        }
    }

    /**
     * Empty method used to check if an rmi connected user is alive.
     * @throws RemoteException
     */
    @Override
    public void isAlive() throws RemoteException { }
}

