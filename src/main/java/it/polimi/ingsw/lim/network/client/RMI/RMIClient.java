package it.polimi.ingsw.lim.network.client.RMI;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.network.client.ServerInterface;
import it.polimi.ingsw.lim.network.server.RMI.RMIServerInterf;
import it.polimi.ingsw.lim.ui.UIController;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

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

    UIController uiController;

    /**
     * RMI client constructor.
     */
    public RMIClient(UIController uiController) {
        this.uiController = uiController;
    }

    /**
     * @return the server's address.
     */
    public String getAddress() {return address;}

    /**
     * @return the server's rmi port.
     */
    public int getPort() {return port;}


    public void sendLogin(String username) throws ClientNetworkException {
        try {
            UnicastRemoteObject.exportObject(this, 0);
            rmiServer.login(username, this);
        } catch (RemoteException e) {
            System.out.printf(e.getMessage());
            throw new ClientNetworkException("[RMI]: Login Failed", e);
        }
    }

    /**
     * It's called to establish the connection between server and client.
     * @throws ClientNetworkException
     */
    public void connect() throws ClientNetworkException {
        try {
            LocateRegistry.getRegistry(getAddress(), getPort());
            rmiServer = (RMIServerInterf)Naming.lookup("rmi://" + getAddress() + "/lim");
            UnicastRemoteObject.exportObject(rmiServer, 0);
        } catch(NotBoundException | RemoteException | MalformedURLException e) {
            throw new ClientNetworkException("[RMI]: Could not connect to RMI server", e);
        }
    }

    public void chatMessageToServer(String sender, String message) throws ClientNetworkException {
        try {
            rmiServer.chatMessageFromClient(sender, message);
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not send a message to RMI server", e);
        }
    }

    @Override
    public void chatMessageFromServer(String sender, String message) {
        uiController.getClientUI().printMessageln("[CHAT] message from "+sender+": "+message);
    }
}

/*
    public void createLobby(String roomName) throws RemoteException {
        rmiServer.createRoom(roomName, this);
    }

    public void joinFirstRoom() {

    }
 */
