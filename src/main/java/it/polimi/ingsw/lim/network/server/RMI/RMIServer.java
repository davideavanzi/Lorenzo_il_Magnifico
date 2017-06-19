package it.polimi.ingsw.lim.network.server.RMI;

import static it.polimi.ingsw.lim.network.server.MainServer.*;
import static it.polimi.ingsw.lim.controller.Room.*;

import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.controller.Room;
import it.polimi.ingsw.lim.controller.User;
import it.polimi.ingsw.lim.network.client.RMI.RMIClientInterf;
import it.polimi.ingsw.lim.network.server.ClientInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.logging.Level;

/**
 * Created by Nico.
 */
public class RMIServer extends UnicastRemoteObject implements RMIServerInterf, ClientInterface {

    public RMIServer()  throws RemoteException {}

    public void createRoom(String roomName, RMIClientInterf rci) throws RemoteException {

    }

    /**
     * Create a new registry only if there isn't any already existing.
     * @param port number
     * @return the registry just created
     * @throws RemoteException
     */
    private void createRegistry(int port) throws RemoteException {
        try {
            LocateRegistry.createRegistry(port);
            Log.getLog().log(Level.INFO, "RMI Registry created");
        } catch (RemoteException re) {
            Log.getLog().log(Level.WARNING, "RMI Registry already created", re);
            LocateRegistry.getRegistry(port);
            System.out.println("RMI Registry loaded");
        }
    }

    /**
     * Start RMI Server
     * @param port number of RMI server
     * @throws RemoteException
     */
    public void startServer(int port) {
        try {
            createRegistry(port);
            RMIServerInterf rmiSerInt = this;
            Naming.rebind("lim", rmiSerInt);
            //UnicastRemoteObject.exportObject(rmiSerInt, port);
        } catch(RemoteException re) {
            Log.getLog().log(Level.SEVERE, "Could not deploy RMI server", re);
        } catch(MalformedURLException mue) {
            Log.getLog().log(Level.SEVERE, "URL unreachable", mue);
        }
    }

    @Override
    public void tellToClient(String message) {
    }
}
