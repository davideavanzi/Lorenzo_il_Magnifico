package it.polimi.ingsw.lim.network.server.socket;

import it.polimi.ingsw.lim.controller.User;

import static it.polimi.ingsw.lim.Log.*;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;

/**
 * Created by Nico.
 * This class handles the connection to a socket client.
 */
public class SocketClientHandler extends AbstractServer implements Runnable{

    private Socket socketClient;
    private User user;
    private boolean isThreadRunning = true;

    private ObjectOutputStream objFromServer;
    private ObjectInputStream objToServer;

    SocketClientHandler(Socket socketClient) {
        this.socketClient = socketClient;


    }

    /**
     * Simple client handler
     */
    public void run() {
        String commandReceived;

        try {
            // Input and output stream
            objFromServer = new ObjectOutputStream(socketClient.getOutputStream());
            objToServer = new ObjectInputStream(socketClient.getInputStream());
        } catch (IOException ioe) {
            getLog().log(Level.SEVERE,"Could not open I/O stream", ioe);
        }

        while (isThreadRunning) {
            try {
                // Read incoming command from the client
                commandReceived = (String)objToServer.readObject();
                System.out.println("The incoming command is: "+commandReceived);

                // Check if server is still running
                if (!(SocketServer.getIsServerRunning())) {
                    getLog().log(Level.INFO,"Server already stopped, killing the client thread");
                    objFromServer.writeObject("Server already stopped");
                    isThreadRunning = false;
                }

                // Command List
                switch (commandReceived.toLowerCase()) {
                    case "quit":
                    case "exit":
                        isThreadRunning = false;
                        getLog().log(Level.INFO,"Quitting from thread");
                        objFromServer.writeObject("Quitting...");
                        break;

                    case "stop":
                        isThreadRunning = false;
                        // TODO: Only the host can stop the server
                        SocketServer.setIsServerRunning(false);
                        getLog().log(Level.INFO,"The server has stopped");
                        objFromServer.writeObject("The server has stopped");
                        break;

                    case "help":
                        objFromServer.writeObject("Help command");
                        break;

                    default:
                        objFromServer.writeObject("Command not found: "+commandReceived);
                }
            } catch (IOException ioe) {
                getLog().log(Level.SEVERE,"Could not reading incoming command", ioe);
            } catch (ClassNotFoundException cnfe) {
                getLog().log(Level.SEVERE,"ClassNotFoundException raised", cnfe);
            }
        }

        // Close the stream and the client socket
        try {
            objToServer.close();
            objFromServer.close();
            getLog().log(Level.INFO, "The input and output stream are closed");
            socketClient.close();
            getLog().log(Level.INFO, "The server socket is closed");
        } catch (IOException ioe) {
            getLog().log(Level.SEVERE,"Could not close stream or socket", ioe);
        }
    }

    public void tellToClient(String message) {
        try {
            objFromServer.writeObject(message);
        } catch (Exception e) {
            //TODO: DAJE COSTE EXCEPTION
            e.printStackTrace();
        }

    }

}