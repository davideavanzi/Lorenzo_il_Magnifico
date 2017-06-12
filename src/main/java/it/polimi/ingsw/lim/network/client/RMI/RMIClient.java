package it.polimi.ingsw.lim.network.client.RMI;

import it.polimi.ingsw.lim.network.client.AbsClient;
import it.polimi.ingsw.lim.network.client.MainClientInterface;
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

    public RMIClient(String address, int port) throws RemoteException {
        super(address, port);
    }

    public void createLobby(String roomName) throws RemoteException {
        rmiServer.createRoom(roomName, this);
    }

    public void joinFirstRoom() {

    }

    public void connectRMI(String address, int port) throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(address, port);
            rmiServer = (RMIServerInterf)Naming.lookup("rmi://" + address + "/lim");
            UnicastRemoteObject.exportObject(this, 0);
            System.out.println("You have been connected in RMI mode.");
        } catch(NotBoundException nbe) {
            System.out.println("The element is not bound to the registry");
        } catch(RemoteException re) {
            System.out.println("Could not connect to RMI server");
        } catch(MalformedURLException mue) {
            System.out.println("URL unreachable");
        }
    }
}
