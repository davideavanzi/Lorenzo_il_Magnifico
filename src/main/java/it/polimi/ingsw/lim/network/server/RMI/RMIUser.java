package it.polimi.ingsw.lim.network.server.RMI;

import it.polimi.ingsw.lim.controller.User;
import it.polimi.ingsw.lim.network.client.RMI.RMIClientInterf;

/**
 * Created by nico.
 */
public class RMIUser extends User {
    RMIClientInterf rci;

    public RMIUser(String username, RMIClientInterf rci) {
        super(username);
        this.rci = rci;
    }

    @Override
    public void chatMessage(String sender, String message) {
        RMIServer.chatMessageToClient(sender, message, this.rci);
    }
}
