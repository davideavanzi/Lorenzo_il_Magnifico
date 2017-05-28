package it.polimi.ingsw.lim.network.server.RMI;

import it.polimi.ingsw.lim.Log;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.logging.Level;

/**
 * Created by Nico.
 */
public class RMIServer extends UnicastRemoteObject implements RMIServerInterf {

    public RMIServer() throws RemoteException {}

    /**
     * Start RMI Server.
     * @param port number of RMI server
     * @throws RemoteException
     */
    public void RMIServerStart(int port) throws RemoteException {
        Registry registry = createRegistry(port);
        RMIServerInterf rmiInt = this;
        registry.rebind("RMIServInt", rmiInt);
    }

    /**
     * Create a new registry only if there isn't any already existing.
     * @param port number
     * @return the registry just created
     * @throws RemoteException
     */
    private Registry createRegistry(int port) throws RemoteException {
        Registry reg;
        try {
            reg = LocateRegistry.createRegistry(port);
            System.out.println("RMI Registry created");
        } catch (RemoteException re) {
            Log.getLog().log(Level.SEVERE, "RMI Registry already created", re);
            reg = LocateRegistry.getRegistry(port);
            System.out.println("RMI Registry loaded");
        }
        return reg;
    }
}
