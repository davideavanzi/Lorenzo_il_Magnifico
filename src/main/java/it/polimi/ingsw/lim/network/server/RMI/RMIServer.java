package it.polimi.ingsw.lim.network.server.RMI;

import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.controller.Room;
import it.polimi.ingsw.lim.controller.User;
import it.polimi.ingsw.lim.network.server.ClientInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;

import static it.polimi.ingsw.lim.network.server.MainServer.addUserToRoom;

/**
 * Created by Nico.
 */
public class RMIServer extends UnicastRemoteObject implements RMIServerInterf, ClientInterface {

    /**
     * Default constructor
     * @throws RemoteException
     */
    public RMIServer() throws RemoteException {}

    /**
     * Room reference
     * @param username
     * @throws RemoteException
     */
    private Room room;

    public void login(String username, ClientInterface interf) throws RemoteException {
        //TODO: sistema di autenticazione (salvare utenti in un file/db, se utente esistente se vuole caricare stat.)
        this.room = addUserToRoom(new User(username, interf));
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
            Log.getLog().log(Level.INFO, "[RMI]: RMI Registry created");
        } catch (RemoteException e) {
            Log.getLog().log(Level.WARNING, "[RMI]: RMI Registry already created", e);
            LocateRegistry.getRegistry(port);
            Log.getLog().log(Level.INFO, "[RMI]: RMI Registry loaded");
        }
    }

    /**
     * Start RMI Server
     * @param port number of RMI server
     * @throws RemoteException
     */
    public void deployServer(int port) {
        try {
            createRegistry(port);
            RMIServerInterf rmiSerInt = this;
            Naming.rebind("lim", rmiSerInt);
        } catch(RemoteException e) {
            Log.getLog().log(Level.SEVERE, "[RMI]: Could not deploy RMI server", e);
        } catch(MalformedURLException e) {
            Log.getLog().log(Level.SEVERE, "[RMI]: URL unreachable", e);
        }
    }

    @Override
    public void printToClient(String message) {

    }

    public void chatMessageFromUser(String message) {

    }

    /**
     * This method sends a chat message to the user.
     * @param sender
     * @param message
     */
    @Override
    public void chatMessage(String sender, String message) {

    }

    @Override
    public int askForServants(int minimum) {
      return 0;
    }
}
