package it.polimi.ingsw.lim.network.client.RMI;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.network.client.ServerInterface;
import it.polimi.ingsw.lim.network.server.RMI.RMIServerInterf;
import it.polimi.ingsw.lim.ui.UIController;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

import static it.polimi.ingsw.lim.network.CommunicationConstants.*;

/**
 * Created by nico.
 */
public class RMIClient implements RMIClientInterf, ServerInterface {
    /**
     * Instance of RMI server interface.
     */
    RMIServerInterf rmiServer;
    /**
     * The UI controller's reference.
     */
    UIController uiCallback;
    /**
     * RMI server's ip address.
     */
    private String address = "localhost";
    /**
     * RMI server's port number.
     */
    private int port = 1099;

    /**
     * RMI client constructor.
     */
    public RMIClient(UIController uiCallback) {
        this.uiCallback = uiCallback;
    }

    /**
     * @return the server's address.
     */
    private String getAddress() {return address;}

    /**
     * @return the server's rmi port.
     */
    private int getPort() {return port;}

    @Override
    public void endGameNotification(ArrayList<Player> scoreboard) throws RemoteException {
        uiCallback.getClientUI().endGameMessage(scoreboard);
    }

    @Override
    public void commandValidator(String command, String message, boolean outcome) throws RemoteException {
        uiCallback.getClientUI().commandManager(command, message, outcome);
    }

    @Override
    public void askPlayerForFastHarvest(int basestr) throws RemoteException {
        uiCallback.getClientUI().commandAdder(SERVANTS_HARVEST);
    }

    @Override
    public void askPlayerForFastProduction(int baseStr) throws RemoteException {
        uiCallback.getClientUI().commandAdder(SERVANTS_PRODUCTION);
    }

    @Override
    public void askPlayerForFastTowerMove(HashMap<String, Integer> baseStr, Assets optionalPickDiscount) throws RemoteException {
        uiCallback.getClientUI().commandAdder(PICK_FROM_TOWER);
    }

    @Override
    public void askPlayerProductionOptions(ArrayList<ArrayList<Object[]>> options) throws RemoteException {
        uiCallback.getClientUI().commandAdder(CHOOSE_PRODUCTION);
    }

    @Override
    public void askPlayerForBpPick() throws RemoteException {
        uiCallback.getClientUI().commandAdder(OPTIONAL_BP_PICK);
    }

    @Override
    public void askPlayerForFavor(int favorAmount) throws RemoteException {
        uiCallback.getClientUI().commandAdder(CHOOSE_FAVOR);
        uiCallback.getTmpVar().setFavorAmount(favorAmount);
    }

    @Override
    public void askPlayerForExcommunication() throws RemoteException {
        uiCallback.getClientUI().commandAdder(EXCOMMUNICATION);
    }

    @Override
    public void startLeaderCardDraft(ArrayList<Integer> leaderOptions) throws RemoteException {
        uiCallback.getClientUI().commandAdder(CHOOSE_LEADER_DRAFT);
        uiCallback.getTmpVar().setLeaderOptions(leaderOptions);
    }

    @Override
    public void askPlayerFmToBoost() throws RemoteException {
        uiCallback.getClientUI().commandAdder(LORENZO_MONTEFELTRO);
    }

    @Override
    public void askPlayerToChooseLeaderToCopy(ArrayList<String> copyableLeaders) throws RemoteException {
        uiCallback.getClientUI().commandAdder(LORENZO_MEDICI);
        uiCallback.getTmpVar().setCopyableLeaders(copyableLeaders);
    }

    @Override
    public void isUserPlaying(Boolean state) throws RemoteException {
        uiCallback.getClientUI().notifyStartRound(state);
    }

    /**
     * Print the chat message received.
     * @param sender the username of the sender.
     * @param message the chat message.
     */
    @Override
    public void receiveChatMessageFromServer(String sender, String message) {
        uiCallback.getClientUI().printChatMessage(sender, message);
    }

    @Override
    public void receiveGameMessageFromServer(String message) throws RemoteException {
        uiCallback.getClientUI().printGameMessage(message);
    }

    @Override
    public String[] askLogin(String errorMsg) throws RemoteException {
        if(errorMsg != null) uiCallback.getClientUI().printError(errorMsg);
        return uiCallback.sendLoginInfo();
    }

    @Override
    public void startListenToInput() throws RemoteException {
        uiCallback.getClientUI().getLock().unlock();
    }

    @Override
    public void updateClientGame(Board board, ArrayList<Player> players) {
        uiCallback.updatePlayers(players);
        uiCallback.updateBoard(board);
    }

    @Override
    public void sendToClientStartGameNotification() throws RemoteException {
        uiCallback.getClientUI().notifyStartGame();
    }

    /**
     * Empty method used to check if an rmi connected user is alive.
     * @throws RemoteException
     */
    @Override
    public void isAlive() throws RemoteException { }

    @Override
    public void sendFamilyMemberColorForLorenzoMontefeltro(String fmColor) throws ClientNetworkException {
        try {
            rmiServer.familyMemberColorAbility(fmColor, uiCallback.getUsername(), this);
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not send set family member to 6 ability to server", e);
        }
    }

    @Override
    public void sendCopyLeaderForLorenzoMedici(int leaderIndex) throws ClientNetworkException {
        try {
            rmiServer.copyLeaderAbility(leaderIndex, uiCallback.getUsername(), this);
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not send copy leader ability to server", e);
        }
    }

    @Override
    public void leaderCardDraft(int leaderIndex) throws ClientNetworkException {
        try {
            rmiServer.draftLeaderCard(leaderIndex, uiCallback.getUsername(), this);
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not send leader card draft choice to server", e);
        }
    }

    @Override
    public void leaderCardActivate(int id) throws ClientNetworkException {
        try {
            rmiServer.activateLeaderCard(id, uiCallback.getUsername(), this);
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not send active leader card draft to server", e);
        }
    }

    @Override
    public void leaderCardDeploy(int id) throws ClientNetworkException {
        try {
            rmiServer.deployLeaderCard(id, uiCallback.getUsername(), this);
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not send deploy leader card to server", e);
        }
    }

    @Override
    public void leaderCardDiscard(int id) throws ClientNetworkException {
        try {
            rmiServer.discardLeaderCard(id, uiCallback.getUsername(), this);
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not send discard request to server", e);
        }
    }

    @Override
    public void fastHarvest(int servantsDeployed) throws ClientNetworkException {
        try {
            rmiServer.fastHarvest(servantsDeployed, uiCallback.getUsername(), this);
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not send bonus harvest action to server", e);
        }
    }

    @Override
    public void fastProduction(int servantsDeployed) throws ClientNetworkException {
        try {
            rmiServer.fastProduction(servantsDeployed, uiCallback.getUsername(), this);
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not send bonus production action to server", e);
        }
    }

    @Override
    public void fastTowerMove(int servantsDeployed, String towerColor, int floor) throws ClientNetworkException {
        try {
            rmiServer.fastTowerMove(servantsDeployed, towerColor, floor, uiCallback.getUsername(), this);
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not send bonus tower action to server", e);
        }
    }

    @Override
    public void productionOption(ArrayList<Integer> prodChoice) throws ClientNetworkException {
        try {
            rmiServer.productionOption(prodChoice, uiCallback.getUsername(), this);
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not production action to server", e);
        }
    }

    @Override
    public void optionalBpPick(boolean bpPayment) throws ClientNetworkException {
        try {
            rmiServer.optionalBpPick(bpPayment, uiCallback.getUsername(), this);
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not send battle point payment request to server", e);
        }
    }

    @Override
    public void favorChoice(ArrayList<Integer> favorChoice) throws ClientNetworkException {
        try {
            rmiServer.favorChoice(favorChoice, uiCallback.getUsername(), this);
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not send council choice to server", e);
        }
    }

    @Override
    public void excommunicationChoice(boolean choice) throws ClientNetworkException {
        try {
            rmiServer.excommunicationChoice(choice, uiCallback.getUsername(), this);
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not send excommunication choice to server", e);
        }
    }

    @Override
    public void placeFM(String fmColor, ArrayList<String> destination, String servants) throws ClientNetworkException {
        int destArgs = Integer.parseInt(destination.get(1)); //floor's number or market slot
        int servantsNum = Integer.parseInt(servants);
        try {
            if (destination.get(0).equalsIgnoreCase(GREEN) ||
                    destination.get(0).equalsIgnoreCase(BLUE) ||
                    destination.get(0).equalsIgnoreCase(YELLOW) ||
                    destination.get(0).equalsIgnoreCase(PURPLE)) {
                String twrColor = destination.get(0);
                rmiServer.moveInTower(fmColor, twrColor, destArgs, servantsNum, uiCallback.getUsername(), this);
            } else if (destination.get(0).equalsIgnoreCase(MARKET)) {
                rmiServer.moveInMarket(fmColor, destArgs, servantsNum, uiCallback.getUsername(), this);
            } else if (destination.get(0).equalsIgnoreCase(PRODUCTION)) {
                rmiServer.moveInProduction(fmColor, servantsNum, uiCallback.getUsername(), this);
            } else if (destination.get(0).equalsIgnoreCase(HARVEST)) {
                rmiServer.moveInHarvest(fmColor, servantsNum, uiCallback.getUsername(), this);
            } else if (destination.get(0).equalsIgnoreCase(COUNCIL)) {
                rmiServer.moveInCouncil(fmColor, servantsNum, uiCallback.getUsername(), this);
            }
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not contact the server to place family member", e);
        }
    }

    /**
     * Send a chat message to server.
     * @param sender the username of the sender.
     * @param message the chat message.
     * @throws ClientNetworkException
     */
    @Override
    public void sendChatMessageToServer(String sender, String message) throws ClientNetworkException {
        try {
            rmiServer.receiveChatMessageFromClient(sender, message);
        } catch (RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not send chat message to server", e);
        }
    }

    /**
     * It's called to establish the connection between server and client.
     * @throws ClientNetworkException
     */
    @Override
    public void connect() throws ClientNetworkException {
        try {
            LocateRegistry.getRegistry(getAddress(), getPort());
            rmiServer = (RMIServerInterf)Naming.lookup("rmi://" + getAddress() + "/lim");
            UnicastRemoteObject.exportObject(rmiServer, 0);
            exportObject();
            rmiServer.initializeConnection(this);
        } catch (NotBoundException | RemoteException e) {
            throw new ClientNetworkException("[RMI]: Could not connect to RMI server", e);
        } catch (MalformedURLException e) {
            throw new ClientNetworkException("[RMI]: URL not correct", e);
        }
    }

    private void exportObject() throws ClientNetworkException {
        try {
            UnicastRemoteObject.exportObject(this, 0);
        } catch (RemoteException e) {
            throw new ClientNetworkException(e.getMessage(), e);
        }
    }
}

