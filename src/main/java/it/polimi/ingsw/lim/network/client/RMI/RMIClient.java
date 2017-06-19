package it.polimi.ingsw.lim.network.client.RMI;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.network.client.AbsClient;
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
public class RMIClient extends AbsClient implements RMIClientInterf {
    RMIServerInterf rmiServer;

    /**
     * RMI client constructor
     */
    public RMIClient() {
        super();
    }

    public RMIClient(String address, int port) {
        super(address, port);
    }

    public void connect() throws ClientNetworkException {
        try {
            Registry registry = LocateRegistry.getRegistry(getAddress(), getPort());
            rmiServer = (RMIServerInterf)Naming.lookup("rmi://" + getAddress() + "/lim");
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
