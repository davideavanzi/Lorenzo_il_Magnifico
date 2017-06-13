package it.polimi.ingsw.lim.network.client;

/**
 * Created by nico.
 */
public abstract class AbsClient {
    private String address;
    private int port;

    /**
     * This constructor is called when the player want to play with default settings
     */
    public AbsClient() {
        this.address = "localhost";
        this.port = 1099;
    }

    /**
     * This constructor is called if the player want to change the address or the port
     * @param address ip address of the server
     * @param port number on which the server is listening
     */
    public AbsClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    /**
     * Return the server's ip
     * @return
     */
    protected String getAddress() {
        return address;
    }

    /**
     * Set the server's ip
     * @return
     */
    protected int getPort() {
        return port;
    }

    /**
     *
     */
    //protected abstract void connect();

}
