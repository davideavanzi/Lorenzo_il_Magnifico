package it.polimi.ingsw.lim.network.server.RMI;

import it.polimi.ingsw.lim.Log;

import java.net.MalformedURLException;
import java.rmi.Naming;
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

    public void login(String name) {
        Log.getLog().info("CLIENT CONNECTED TO SERVER WITH NAME: "+name);
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

    /**
     * Start RMI Server
     * @param port number of RMI server
     * @throws RemoteException
     */
    public void RMIServerStart(int port) throws RemoteException {
        try {
            Registry registry = createRegistry(port);
            RMIServerInterf rmiInt = this;
            Naming.rebind("lim", rmiInt);
        } catch(RemoteException re) {
            Log.getLog().log(Level.SEVERE, "Could not deploy RMI server", re);
        } catch(MalformedURLException mue) {
            Log.getLog().log(Level.SEVERE, "URL unreachable", mue);
        }
    }
}
