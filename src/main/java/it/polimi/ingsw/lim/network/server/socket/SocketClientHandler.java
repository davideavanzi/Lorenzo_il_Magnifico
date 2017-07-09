package it.polimi.ingsw.lim.network.server.socket;

import it.polimi.ingsw.lim.utils.Log;
import it.polimi.ingsw.lim.controller.User;
import it.polimi.ingsw.lim.exceptions.LoginFailedException;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.MainServer;

import static it.polimi.ingsw.lim.utils.Log.*;
import static it.polimi.ingsw.lim.network.CommunicationConstants.*;
import static it.polimi.ingsw.lim.MainServer.addUserToRoom;
import static it.polimi.ingsw.lim.MainServer.isUserAlreadyLoggedIn;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * This class handles the connection to a socket client.
 * Every client are on a thread that send command to this class.
 * The socket server is always listen for request, even if it is waiting for a specific response to an action.
 * The socket send to client the command game throw this class, sending Object to output stream.
 */
public class SocketClientHandler implements Runnable {

    /**
     * Socket for communicate with client.
     */
    private Socket socketClient;

    /**
     * User's reference.
     */
    private User user;

    /**
     * Show if the client is logged.
     */
    private boolean isClientLogged = true;

    /**
     * Input and output stream for the client-server communication.
     */
    private ObjectOutputStream objFromServer;
    private ObjectInputStream objToServer;

    /**
     * Socket client command handler.
     */
    private ClientCommandHandler clientCommandHandler;

    /**
     * Default constructor.
     * @param socketClient
     */
    SocketClientHandler(Socket socketClient) {
        this.socketClient = socketClient;
        clientCommandHandler = new ClientCommandHandler(this);
    }

    /**
     * @return the user.
     */
    public User getUser() { return user; }

    /**
     * Validate a command.
     * @param command the input command
     * @param message
     * @param outcome boolean, if true the command is successful
     */
    void commandValidator(String command, String message, boolean outcome) {
        sendObjectToClient(new Object[] {CMD_VALIDATOR, command, message, outcome});
    }

    void askClientToChooseLeaderToCopy(ArrayList<String> copyableLeaders) {
        sendObjectToClient(new Object[] {LORENZO_MEDICI, copyableLeaders});
    }

    /**
     * Send to client the commandID of a bonus harvest action.
     * @param baseStr the minimum of servants to deploy
     */
    void askClientForFastHarvest(int baseStr) {
        sendObjectToClient(new Object[] {SERVANTS_HARVEST, baseStr});
    }
    /**
     * Send to client the commandID of a bonus production action.
     * @param baseStr the minimum of servants to deploy
     */
    void askClientForFastProduction(int baseStr) {
        sendObjectToClient(new Object[] {SERVANTS_PRODUCTION, baseStr});
    }

    /**
     * Send to client the commandID of a bonus production action.
     * @param baseStr the minimum of servants to deploy
     * @param optionalPickDiscount a resource's discount on a specific action
     */
    void askClientForFastTowerMove(HashMap<String, Integer> baseStr, Assets optionalPickDiscount) {
        sendObjectToClient(new Object[] {PICK_FROM_TOWER, baseStr, optionalPickDiscount});
    }

    /**
     * Send to client the commandID of the choose production command.
     * @param options the production
     */
    void askClientForProductionOption(ArrayList<ArrayList<Object[]>> options) {
        sendObjectToClient(new Object[] {CHOOSE_PRODUCTION, options});
    }

    /**
     * Send to client the commandID of the optional Battle Point command.
     */
    void askClientForOptionalBpPick() {
        sendObjectToClient(new Object[] {OPTIONAL_BP_PICK});
    }

    /**
     * Send to client the commandID of the choose council's favor command.
     * @param favorAmount the number of favor to choose
     */
    void askClientForFavor(int favorAmount) {
        sendObjectToClient(new Object[] {CHOOSE_FAVOR, favorAmount});
    }

    /**
     * Send to client the commandID of excommunication choice command.
     */
    void askClientForExcommunication() {
        sendObjectToClient(new Object[] {EXCOMMUNICATION});
    }

    /**
     * Send to client the commandID of gameMessage.
     * @param message
     */
    void gameMessageToClient(String message) {
        sendObjectToClient(new Object[] {GAME_MSG, message});
    }

    /**
     * This method sends a chat message to the user
     * @param sender
     * @param message
     */
    void chatMessageToClient(String sender, String message) {
        sendObjectToClient(new Object[] {CHAT, sender ,message});
    }

    /**
     * It's used for the updated board and ArrayList of connected (to the server) users.
     * @param board
     * @param players
     */
    void sendGameToClient(Board board, ArrayList<Player> players) {
        sendObjectToClient(new Object[] {BOARD, board});
        sendObjectToClient(new Object[] {PLAYERS, players});
    }

    /**
     * Notify the player if it is his turn.
     * @param isPlaying if true is player's turn
     */
    void sendIfUserPlaying(boolean isPlaying) {
        sendObjectToClient(new Object[] {TURN, isPlaying});
    }

    /**
     * Notify the player that the game is started.
     */
    void sendNoficationStartGame() {
        sendObjectToClient(new Object[] {START_GAME});
    }

    /**
     * This method is the only that write object to the socket client.
     * @param obj
     * @throws IOException
     */
    private void sendObjectToClient(Object obj) {
        try {
            objFromServer.writeObject(obj);
            objFromServer.flush();
            objFromServer.reset();
        } catch (IOException e) {
            getLog().log(Level.SEVERE, "[SOCKET]: Could not send object to the client", e);
        }
    }

    /**
     * Login method.
     * Add to database the username and the password of new client.
     * Check if the inserted password is correct, then add user to room.
     * @param username the player's name
     * @param password the player's password
     * @param handlerCallback the socket client handler callback
     * @throws LoginFailedException if username or password are wrong
     */
    public void login(String username, String password, SocketClientHandler handlerCallback) throws LoginFailedException {
        try {
            if (MainServer.getJDBC().isAlreadySelectedUserName(username)) {
                if (MainServer.getJDBC().isUserContained(username, password)) {
                    if (isUserAlreadyLoggedIn(username)) throw new LoginFailedException("You are already logged in!");
                    this.user = new SocketUser(username, handlerCallback);
                    addUserToRoom(this.user);
                    Log.getLog().log(Level.INFO, "[LOGIN]: Login successful. Welcome back ".concat(username));
                } else {
                    Log.getLog().log(Level.SEVERE, "[LOGIN]: Bad password or username ".concat(username)
                            .concat(" already selected?"));
                    throw new LoginFailedException("[LOGIN]: Bad password or username ".concat(username)
                            .concat(" already selected?"));
                }
            } else {
                MainServer.getJDBC().insertRecord(username, password);
                this.user = new SocketUser(username, handlerCallback);
                addUserToRoom(this.user);
                Log.getLog().log(Level.INFO, "[LOGIN]: Login successful");
            }
        } catch (SQLException e) {
            Log.getLog().log(Level.SEVERE, "[SQL]: Login failed");
            throw new LoginFailedException("[SQL]: Login failed");
        }
    }

    /**
     * This method listen for object sent by the server.
     * The object is passed to a parser that call the right method based on the commandID.
     * In this way socket provided a non-blocking connection;
     * The server is always listen for command sent to client even when it wait a answer for a request previously sent.
     */
    private void waitRequest() {
        int tries = 0;
        while(true) {
            try {
                Object command = objToServer.readObject();
                clientCommandHandler.requestHandler(command);
            } catch (IOException | ClassNotFoundException e) {
                getLog().log(Level.SEVERE, "[SOCKET]: Could not receive object from client, " +
                        "maybe client is offline?\nRetrying " + (2 - tries) + " times.");
                tries++;
                if (tries == 3) {
                    this.user.hasDied();
                    return;
                }
            }
        }
    }

    /**
     * Assuming that the client can't be manipulated, this method force the login server-side.
     * @return if the login is successful
     */
    private boolean loginRequest() {
        int loginFailed = 0;
        while (true) {
            sendObjectToClient(new Object[] {LOGIN_REQUEST});
            try {
                Object loginInfo = objToServer.readObject();
                Object[] command = (Object[])loginInfo;
                if (command[0].equals(LOGIN)) {
                    login((String)command[1], (String)command[2], this);
                    sendObjectToClient(new Object[] {LOGIN_SUCCESSFUL});
                    isClientLogged = true;
                    return isClientLogged;
                }
            } catch (IOException | ClassNotFoundException e) {
                getLog().log(Level.SEVERE, ("[SOCKET]: Could not receive login information from client, retrying "
                        + (2 - loginFailed) + " times"), e);
                loginFailed++;
                if (loginFailed == 3) {
                    isClientLogged = false;
                    return isClientLogged;
                }
            } catch (LoginFailedException e) {
                sendObjectToClient(new Object[] {LOGIN_FAILED, e.getMessage()});
            }
        }
    }


    /**
     * Create I/O stream for socket connection.
     */
    private void createStream() {
        try {
            // Input and output stream
            this.objFromServer = new ObjectOutputStream(socketClient.getOutputStream());
            objFromServer.flush();
            this.objToServer = new ObjectInputStream(socketClient.getInputStream());
        } catch (IOException e) {
            getLog().log(Level.SEVERE, "[SOCKET]: Could not create I/O stream", e);
        }
    }

    /**
     * Create the I/O socket stream, run until the login is successful then listen for a client command
     */
    @Override
    public void run() {
        createStream();
        if (loginRequest())
            waitRequest();
    }
}