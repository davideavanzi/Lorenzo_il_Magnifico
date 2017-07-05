package it.polimi.ingsw.lim.network.server.RMI;

import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.controller.GameController;
import it.polimi.ingsw.lim.controller.User;
import it.polimi.ingsw.lim.exceptions.BadRequestException;
import it.polimi.ingsw.lim.exceptions.LoginFailedException;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.FamilyMember;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.network.client.RMI.RMIClientInterf;
import it.polimi.ingsw.lim.network.server.MainServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import static it.polimi.ingsw.lim.network.CommunicationConstants.FAMILY_MEMBER;
import static it.polimi.ingsw.lim.network.CommunicationConstants.FAMILY_MEMBER_OK;
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

    @Override
    public void excommunicationChoice(boolean choice) throws RemoteException {
        //todo chiamare metodo su gameController
    }

    static void excommunicationChoice(RMIClientInterf rmiClient) throws RemoteException {
        rmiClient.askUserForExcommunication();
    }

    @Override
    public void moveInCouncil(String fmColor, int servants, String username, RMIClientInterf rmiClient) throws RemoteException {
        GameController gc = MainServer.getUserFromUsername(username).getRoom().getGameController();
        FamilyMember fm = MainServer.getUserFromUsername(username).getPlayer().getFamilyMember(fmColor);
        try {
            gc.moveInCouncil(fm, servants);
            rmiClient.commandValidator(FAMILY_MEMBER, FAMILY_MEMBER_OK , true);
        } catch (BadRequestException e) {
            rmiClient.commandValidator(FAMILY_MEMBER, FAMILY_MEMBER_OK , false);
        }
    }

    @Override
    public void moveInHarvest(String fmColor, int servants, String username, RMIClientInterf rmiClient) throws RemoteException {
        GameController gc = MainServer.getUserFromUsername(username).getRoom().getGameController();
        FamilyMember fm = MainServer.getUserFromUsername(username).getPlayer().getFamilyMember(fmColor);
        try {
            gc.moveInHarvest(fm, servants);
            rmiClient.commandValidator(FAMILY_MEMBER, FAMILY_MEMBER_OK , true);
        } catch (BadRequestException e) {
            rmiClient.commandValidator(FAMILY_MEMBER, FAMILY_MEMBER_OK , false);
        }
    }

    @Override
    public void moveInProduction(String fmColor, int servants, String username, RMIClientInterf rmiClient) throws RemoteException {
        GameController gc = MainServer.getUserFromUsername(username).getRoom().getGameController();
        FamilyMember fm = MainServer.getUserFromUsername(username).getPlayer().getFamilyMember(fmColor);
        try {
            gc.moveInProduction(fm, servants);
            rmiClient.commandValidator(FAMILY_MEMBER, FAMILY_MEMBER_OK , true);
        } catch (BadRequestException e) {
            rmiClient.commandValidator(FAMILY_MEMBER, FAMILY_MEMBER_OK , false);
        }
    }

    @Override
    public void moveInMarket(String fmColor, int marketSlot, int servants, String username, RMIClientInterf rmiClient) throws RemoteException {
        GameController gc = MainServer.getUserFromUsername(username).getRoom().getGameController();
        FamilyMember fm = MainServer.getUserFromUsername(username).getPlayer().getFamilyMember(fmColor);
        try {
            gc.moveInMarket(fm, marketSlot, servants);
            rmiClient.commandValidator(FAMILY_MEMBER, FAMILY_MEMBER_OK , true);
        } catch (BadRequestException e) {
            rmiClient.commandValidator(FAMILY_MEMBER, FAMILY_MEMBER_OK , false);
        }
    }

    @Override
    public void moveInTower(String fmColor, String twrColor, int floor, int servants, String username, RMIClientInterf rmiClient) throws RemoteException {
        GameController gc = MainServer.getUserFromUsername(username).getRoom().getGameController();
        FamilyMember fm = MainServer.getUserFromUsername(username).getPlayer().getFamilyMember(fmColor);
        try {
            gc.moveInTower(fm, twrColor, floor, servants);
            rmiClient.commandValidator(FAMILY_MEMBER, FAMILY_MEMBER_OK , true);
        } catch (BadRequestException e) {
            rmiClient.commandValidator(FAMILY_MEMBER, FAMILY_MEMBER_OK , false);
        }
    }

    /**
     * This method sends a chat message to the user.
     * @param sender
     * @param message
     */
     static void sendChatMessageToClient(String sender, String message, RMIClientInterf rmiClient) throws RemoteException {
        rmiClient.receiveChatMessageFromServer(sender, message);
     }

    /**
     * Broadcast the chat message to all roommates.
     * @param sender who send the chat message.
     * @param message the chat message.
     * @throws RemoteException
     */
    @Override
    public void receiveChatMessageFromClient(String sender, String message) throws RemoteException {
        MainServer.getUserFromUsername(sender).getRoom().chatMessageToRoom(sender, message);
    }

    static void sendGameMessageToClient(String message, RMIClientInterf rmiClient) throws RemoteException {
        rmiClient.receiveGameMessageFromServer(message);
    }

    /**
     * Every turn the updated board and player's ArrayList is broadcast to all roommates.
     * @param board the game board.
     * @param players the player ArrayList.
     * @param rmiClient the client's reference.
     * @throws RemoteException
     */
    static void sendGameToClient(Board board, ArrayList<Player> players, RMIClientInterf rmiClient) throws RemoteException {
        rmiClient.updateClientGame(board, players);
    }

    static void setPlayerTurn(Boolean state, RMIClientInterf rmiClient) throws RemoteException {
        rmiClient.isUserPlaying(state);
    }

    /**
     * The rmi login method. It's used for users authentication.
     * @param username
     * @param rmiClient
     * @throws RemoteException
     */
    public void login(String username, String password, RMIClientInterf rmiClient) throws RemoteException, LoginFailedException {
        try {
            if (MainServer.getJDBC().isAlreadySelectedUserName(username)) {
                if (MainServer.getJDBC().isUserContained(username, password)) {
                    addUserToRoom(new RMIUser(username, rmiClient));
                    Log.getLog().log(Level.INFO, "[LOGIN]: Success login. Welcome back ".concat(username));
                } else {
                    Log.getLog().log(Level.SEVERE, "[LOGIN]: Bad password or username ".concat(username).concat("already selected?"));
                    return;
                }
            } else {
                MainServer.getJDBC().insertRecord(username, password);
                User user = new RMIUser(username, rmiClient);
                user.setRoom(addUserToRoom(user));
                Log.getLog().log(Level.INFO, "[LOGIN]: Success login");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.getLog().log(Level.SEVERE, "[SQL]: Login failed");
        }
    }

    @Override
    public void initializeConnection(RMIClientInterf rmiClient) throws RemoteException {
        String[] loginInformation = rmiClient.askLogin(null);
        while (true) {
            try {
                login(loginInformation[0], loginInformation[1], rmiClient);
                rmiClient.startListenToInput();
                return;
            } catch (LoginFailedException e) {
                loginInformation = rmiClient.askLogin(e.getMessage());
            }
        }
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
