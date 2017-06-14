package it.polimi.ingsw.lim.network.ui;

import java.rmi.RemoteException;

/**
 * Created by nico.
 */
public abstract class AbsUI {

    public AbsUI() {

    }

    /**
     * This method is used for set the network protocol from the selected ui
     */
    public abstract void setNetworkSettings() throws RemoteException;
}
