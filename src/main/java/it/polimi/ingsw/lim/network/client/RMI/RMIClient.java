package it.polimi.ingsw.lim.network.client.RMI;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.network.client.ServerInteface;
import it.polimi.ingsw.lim.network.server.RMI.RMIServerInterf;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by nico.
 */
public class RMIClient implements RMIClientInterf, ServerInteface {
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
     * RMI client constructor.
     */
    public RMIClient() {}

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
            rmiServer.login(username);
        } catch (RemoteException e) {
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
}

/*
    public void createLobby(String roomName) throws RemoteException {
        rmiServer.createRoom(roomName, this);
    }

    public void joinFirstRoom() {

    }
 */
