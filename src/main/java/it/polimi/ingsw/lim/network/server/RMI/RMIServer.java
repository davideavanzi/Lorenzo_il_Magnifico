package it.polimi.ingsw.lim.network.server.RMI;

import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.network.client.RMI.RMIClientInterf;
import it.polimi.ingsw.lim.network.server.MainServer;

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
public class RMIServer extends UnicastRemoteObject implements RMIServerInterf {

    /**
     * Default constructor
     * @throws RemoteException
     */
    public RMIServer() throws RemoteException {}

    /**
     * This method sends a chat message to the user.
     * @param sender
     * @param message
     */
    public static void chatMessageToClient(String sender, String message, RMIClientInterf rci) throws RemoteException{
        rci.chatMessageFromServer(sender, message);
    }

    @Override
    public void chatMessageFromClient(String sender, String message) throws RemoteException {
        MainServer.getUserFromUsername(sender).getRoom().chatMessageToRoom(sender, message);
    }

    public void login(String username, RMIClientInterf rci) throws RemoteException {
        //TODO: sistema di autenticazione (salvare utenti in un file/db, se utente esistente se vuole caricare stat.)
        addUserToRoom(new RMIUser(username, rci));
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
}
