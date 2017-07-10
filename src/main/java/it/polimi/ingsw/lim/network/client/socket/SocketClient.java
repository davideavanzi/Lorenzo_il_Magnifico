package it.polimi.ingsw.lim.network.client.socket;

import it.polimi.ingsw.lim.utils.Lock;
import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.exceptions.LoginFailedException;
import it.polimi.ingsw.lim.network.client.ServerInterface;
import it.polimi.ingsw.lim.ui.UIController;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;

import static it.polimi.ingsw.lim.network.CommunicationConstants.*;
import static it.polimi.ingsw.lim.utils.Log.getLog;

/**
 * Created by nico.
 */
public class SocketClient implements Runnable, ServerInterface {

    /**
     * The socket server's address.
     */
    private String address = "localhost";

    /**
     * The socket server's port.
     */
    private int port = 8989;

    /**
     * The socket client's output stream.
     */
    ObjectOutputStream objFromClient;

    /**
     * The socket client's input stream.
     */
    ObjectInputStream objToClient;

    /**
     * Reference to the server command handler.
     */
    ServerCommandHandler commandHandler;

    /**
     * Reference to the UI controller.
     */
    UIController uiController;

    /**
     * The command receive from the server
     */
    Object command;

    /**
     * Declaration of the Lock.
     */
    Lock lock;

    /**
     * Socket client constructor
     */
    public SocketClient(UIController uiCallback) {
        this.uiController = uiCallback;
        this.commandHandler = new ServerCommandHandler(this, uiCallback);
        uiCallback.setClientProtocol(this);
        this.lock = new Lock();
        lock.lock();
    }

    /**
     * @return the server's address
     */
    private String getAddress() {return address;}

    /**
     * @return the server's socket port
     */
    private int getPort() {return port;}

    @Override
    public void fastHarvest(int servantsDeployed) throws ClientNetworkException {
        try {
            sendObjToServer(new Object[] {SERVANTS_HARVEST, servantsDeployed});
        } catch (IOException e) {
                throw new ClientNetworkException("[SOCKET]: Could not send bonus harvest action to server", e);
        }
    }

    @Override
    public void fastProduction(int servantsDeployed) throws ClientNetworkException {
        try {
            sendObjToServer(new Object[] {SERVANTS_PRODUCTION, servantsDeployed});
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not send bonus production actionto server", e);
        }
    }

    @Override
    public void fastTowerMove(int servantsDeployed, String towerColor, int floor) throws ClientNetworkException {
        try {
            sendObjToServer(new Object[] {PICK_FROM_TOWER, towerColor, floor});
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not send bonus tower move action to server", e);
        }
    }

    @Override
    public void productionOption(ArrayList<Integer> prodChoice) throws ClientNetworkException {
        try {
            sendObjToServer(new Object[] {CHOOSE_PRODUCTION, prodChoice});
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not send battle point payment request to server", e);
        }
    }

    @Override
    public void optionalBpPick(boolean bpPayment) throws ClientNetworkException {
        try {
            sendObjToServer(new Object[] {OPTIONAL_BP_PICK, bpPayment});
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not send battle point payment request to server", e);
        }
    }

    @Override
    public void favorChoice(ArrayList<Integer> favorChoice) throws ClientNetworkException {
        try {
            sendObjToServer(new Object[] {CHOOSE_FAVOR, favorChoice});
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not send council's favor's choice to server", e);
        }
    }

    @Override
    public void excommunicationChoice(boolean choice) throws ClientNetworkException {
        try {
            sendObjToServer(new Object[] {EXCOMMUNICATION, choice});
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not send excommunication's choice to server", e);
        }
    }

    @Override
    public void placeFM(String color, ArrayList<String> destination, String servants) throws ClientNetworkException {
        try {
            if (destination.get(1) == null)
                sendObjToServer(new Object[] {FAMILY_MEMBER, color, destination.get(0), servants});
            else
                sendObjToServer(new Object[] {FAMILY_MEMBER, color, destination.get(0), destination.get(1), servants});
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not contact the server to place family member", e);
        }
    }

    @Override
    public void sendFamilyMemberColorForLorenzoMontefeltro(String familiarColor) throws ClientNetworkException {
        try {
            sendObjToServer(new Object[] {LORENZO_MONTEFELTRO, familiarColor});
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not send the color of the familiar to boost to server", e);
        }
    }

    @Override
    public void sendCopyLeaderForLorenzoMedici(int leaderIndex) throws ClientNetworkException {
        try {
            sendObjToServer(new Object[] {LORENZO_MEDICI, leaderIndex});
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not send leader's index for coping it's ability", e);
        }
    }

    @Override
    public void leaderCardDraft(int leaderIndex) throws ClientNetworkException {
        try {
            sendObjToServer(new Object[] {CHOOSE_LEADER_DRAFT, leaderIndex});
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not send leader card draft choice to server", e);
        }
    }

    @Override
    public void leaderCardActivate(int id) throws ClientNetworkException {
        try {
            sendObjToServer(new Object[] {ACTIVATE_LEADER, id});
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not send activate leader card request to server", e);
        }
    }

    @Override
    public void leaderCardDeploy(int id) throws ClientNetworkException {
        try {
            sendObjToServer(new Object[] {CHOOSE_LEADER_DRAFT, id});
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not send deploy leader card request to server", e);
        }
    }

    @Override
    public void leaderCardDiscard(int id) throws ClientNetworkException {
        try {
            sendObjToServer(new Object[] {CHOOSE_LEADER_DRAFT, id});
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not send discard leader card request to server", e);
        }
    }

    /**
     * Send chat message through the client's output stream.
     * @param sender the username of the sender
     * @param message the chat message
     */
    @Override
    public void sendChatMessageToServer(String sender, String message) throws ClientNetworkException {
        try {
            sendObjToServer(new Object[] {CHAT, sender, message});
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not send chat message to server", e);
        }
    }

    /**
     * Send login information to server.
     * @param username
     * @throws ClientNetworkException
     */
    public void login(String username, String password) throws ClientNetworkException {
        try {
            sendObjToServer(new Object[] {LOGIN, username, password});
        } catch (IOException e) {
            throw new ClientNetworkException("[SOCKET]: Could not send login information to server", e);
        }
    }

    /**
     * This method is the only that write object to the socket server.
     * @param obj to send
     * @throws IOException
     */
    private void sendObjToServer(Object obj) throws IOException {
        objFromClient.writeObject(obj);
        objFromClient.flush();
        objFromClient.reset();
    }

    private void closeIOStream()  {
        try {
            objFromClient.flush();
            objFromClient.close();
            objToClient.close();
        } catch (IOException e) {
            getLog().log(Level.SEVERE, "[SOCKET]: Closing stream error");
        }
    }

    /**
     * Wait forever for object from server.
     * @throws ClientNetworkException
     */
    private void waitRequest() throws ClientNetworkException {
        lock.lock();
        while(true) {
            try {
                Object command = objToClient.readObject();
                commandHandler.requestHandler(command);
            } catch (IOException | ClassNotFoundException | LoginFailedException e) {
                closeIOStream();
                throw new ClientNetworkException("[SOCKET]: Could not get command from server", e);
            }
        }
    }

    /**
     * This method is used for connect to the socket server
     * @throws ClientNetworkException raised if the client could not connect to server
     */
    @Override
    public void connect() throws ClientNetworkException {
        try {
            // socket, input and output stream
            Socket socketClient = new Socket(getAddress(), getPort());
            objFromClient = new ObjectOutputStream(socketClient.getOutputStream());
            objFromClient.flush();
            objToClient = new ObjectInputStream(socketClient.getInputStream());
            lock.unlock();
        } catch(IOException e) {
            throw new ClientNetworkException("Could not create I/O stream", e);
        }
    }

    @Override
    public void run() {
        try {
            waitRequest();
        } catch (ClientNetworkException e) {
            uiController.getClientUI().printError(e.getMessage());
        }
    }
}