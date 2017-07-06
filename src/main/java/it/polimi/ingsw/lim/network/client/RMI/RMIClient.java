package it.polimi.ingsw.lim.network.client.RMI;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.network.client.ServerInterface;
import it.polimi.ingsw.lim.network.server.RMI.RMIServerInterf;
import it.polimi.ingsw.lim.ui.CLI;
import it.polimi.ingsw.lim.ui.UIController;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import static it.polimi.ingsw.lim.network.CommunicationConstants.*;

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
    public void commandValidator(String command, String message, boolean outcome) throws RemoteException {
        uiCallback.getClientUI().commandManager(command, message, outcome);
    }

    @Override
    public void optionalBpPick(boolean bpPayment, String username) throws ClientNetworkException {
        try {
            rmiServer.optionalBpPick(bpPayment, username, this);
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not send battle point payment request to server", e);
        }
    }

    @Override
    public void askPlayerForBpPick() throws RemoteException {
        uiCallback.getClientUI().commandAdder(OPTIONAL_BP_PICK);
    }

    @Override
    public void favorChoice(ArrayList<Integer> favorChoice, String username) throws ClientNetworkException {
        try {
            rmiServer.favorChoice(favorChoice, username, this);
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not send council choice to server", e);
        }
    }

    @Override
    public void askPlayerForFavor(int favorAmount) throws RemoteException {
        uiCallback.getClientUI().commandAdder(CHOOSE_FAVOR);
        uiCallback.setFavorAmount(favorAmount);
    }

    @Override
    public void excommunicationChoice(boolean choice, String username) throws ClientNetworkException {
        try {
            rmiServer.excommunicationChoice(choice, username, this);
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not send excommunication choice to server", e);
        }
    }

    @Override
    public void askPlayerForExcommunication() throws RemoteException {
        uiCallback.getClientUI().commandAdder(EXCOMMUNICATION);
    }

    @Override
    public void placeFM(String fmColor, ArrayList<String> destination, String servants, String username) throws ClientNetworkException {
        int destArgs = Integer.parseInt(destination.get(1)); //floor's number or market slot
        int servantsNum = Integer.parseInt(servants);
        try {
            if (destination.get(0).contains(TOWER)) {
                String twrColor = destination.get(0).replace(" Tower", "");
                rmiServer.moveInTower(fmColor, twrColor, destArgs, servantsNum, username, this);
            } else if (destination.get(0).equalsIgnoreCase(MARKET)) {
                rmiServer.moveInMarket(fmColor, destArgs, servantsNum, username, this);
            } else if (destination.get(0).equalsIgnoreCase(PRODUCTION)) {
                rmiServer.moveInProduction(fmColor, servantsNum, username, this);
            } else if (destination.get(0).equalsIgnoreCase(HARVEST)) {
                rmiServer.moveInHarvest(fmColor, servantsNum, username, this);
            } else if (destination.get(0).equalsIgnoreCase(COUNCIL)) {
                rmiServer.moveInCouncil(fmColor, servantsNum, username, this);
            }
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not contact the server to place family member", e);
        }
    }

    /**
     * Send a chat message to server.
     * @param sender the username of the sender.
     * @param message the chat message.
     * @throws ClientNetworkException
     */
    @Override
    public void sendChatMessageToServer(String sender, String message) throws ClientNetworkException {
        try {
            rmiServer.receiveChatMessageFromClient(sender, message);
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not send chat message to server", e);
        }
    }

    /**
     * Print the chat message received.
     * @param sender the username of the sender.
     * @param message the chat message.
     */
    @Override
    public void receiveChatMessageFromServer(String sender, String message) {
        uiCallback.getClientUI().printChatMessage(sender, message);
    }

    @Override
    public void receiveGameMessageFromServer(String message) throws RemoteException {
        uiCallback.getClientUI().printGameMessage(message);
    }

    @Override
    public void startListenToInput() throws RemoteException {
        uiCallback.getClientUI().waitForRequest();
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

