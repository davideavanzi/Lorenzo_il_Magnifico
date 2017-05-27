package it.polimi.ingsw.lim.network.client;

import it.polimi.ingsw.lim.network.server.RMI.RMIServerInterf;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by nico.
 */
public class RMIClient extends UnicastRemoteObject implements ClientInterf {

    private RMIServerInterf rmiServer;

    public RMIClient() throws RemoteException {
        super();
    }

    public void connectRMI(String address, int port) throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(address, port);
            rmiServer = (RMIServerInterf)registry.lookup("RMIServerInt");
        } catch (NotBoundException nbe) {
            System.out.println("The element is not bound to the registry");
        }
    }
}
