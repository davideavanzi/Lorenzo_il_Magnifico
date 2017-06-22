package it.polimi.ingsw.lim.network.client.socket;

import it.polimi.ingsw.lim.Lock;
import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.network.client.ServerInterface;
import it.polimi.ingsw.lim.ui.UIController;

import java.io.*;
import java.net.Socket;
import static it.polimi.ingsw.lim.network.SocketConstants.*;

/**
 * Created by nico.
 */
public class SocketClient implements Runnable, ServerInterface {
    private String address = "localhost";
    private int port = 8989;
    ObjectOutputStream objFromClient;
    ObjectInputStream objToClient;
    ServerCommandHandler commandHandler;
    Lock lock;

    /**
     * Socket client constructor
     */
    public SocketClient(UIController uiController) {
        this.commandHandler = new ServerCommandHandler(this, uiController);
        uiController.setClientProtocol(this);
        this.lock = new Lock();
        try {
            lock.lock();
        } catch (InterruptedException e) {
            //TODO: Handle :(
        }

    }

    /**
     * @return the server's address
     */
    public String getAddress() {return address;}

    /**
     * @return the server's socket port
     */
    public int getPort() {return port;}


    public void sendLogin(String username) throws ClientNetworkException {
        try {
            objFromClient.writeObject(LOGIN+SPLITTER+username);
            objFromClient.flush();
            objFromClient.reset();
        } catch (IOException e) {
            throw new ClientNetworkException("Could not send login information to server", e);
        }
    }

    public void chatMessageToServer(String sender, String message) throws ClientNetworkException{
        try {
            objFromClient.writeObject("CHAT"+SPLITTER+sender+SPLITTER+message);
            objFromClient.flush();
            objFromClient.reset();
        } catch (IOException e) {
            throw new ClientNetworkException("Could not send chat message to server", e);
        }
    }
    private void waitFromServer() throws ClientNetworkException {
        try {
            lock.lock();
        } catch (InterruptedException e) {
        }
        while(true) {
                try {
                    Object command = objToClient.readObject();
                    commandHandler.requestHandler(command);
                } catch (IOException | ClassNotFoundException e) {
                    throw new ClientNetworkException("Could not get command from server", e);
                    //return;
                }

        }
    }

    /**
     * This method is used for connect to the socket server
     * @throws ClientNetworkException raised if the client could not connect to server
     */
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

    public void run() {
        try {
            //connect();
            waitFromServer();
        } catch (ClientNetworkException e) {
            UIController.getClientUI().printMessageln(e.getMessage());
        }
    }

    private boolean isConnected() {
        return (this.objToClient != null);
    }

}

/*
            Scanner command = new Scanner(System.in);
            String commandToSend;
            while(true) {
                System.out.println("Write a command: ");
                commandToSend = command.nextLine();
                objFromClient.writeObject(commandToSend);
                System.out.println("[Server]: "+ objToClient.readObject());
            }
*/