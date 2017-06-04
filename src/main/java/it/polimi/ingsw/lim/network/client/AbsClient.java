package it.polimi.ingsw.lim.network.client;

/**
 * Created by nico.
 */
public abstract class AbsClient {
    private String address = "localhost";
    private int port = 1099;

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



}
