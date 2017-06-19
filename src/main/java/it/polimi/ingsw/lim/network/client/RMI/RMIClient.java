package it.polimi.ingsw.lim.network.client.RMI;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.network.client.ServerInteface;
import it.polimi.ingsw.lim.network.server.RMI.RMIServerInterf;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by nico.
 */
public class RMIClient implements RMIClientInterf, ServerInteface {
    private String address = "localhost";
    private int port = 1099;

    /**
     * RMI client constructor
     */
    public RMIClient() {

    }

    /**
     * @return the server's address
     */
    public String getAddress() {return address;}

    /**
     * @return the server's rmi port
     */
    public int getPort() {return port;}

    public void sendLogin(String username) {

    }

    public void connect() throws ClientNetworkException {
        try {
            Registry registry = LocateRegistry.getRegistry(getAddress(), getPort());
            RMIServerInterf rmiServer = (RMIServerInterf)Naming.lookup("rmi://" + getAddress() + "/lim");
            UnicastRemoteObject.exportObject(rmiServer, 0);
            System.out.println("You have been connected in RMI mode.");
        } catch(NotBoundException | RemoteException | MalformedURLException e) {
            throw new ClientNetworkException("Could not connect to RMI server", e);
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
