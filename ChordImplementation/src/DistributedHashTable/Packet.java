package DistributedHashTable;

import java.io.Serializable;

/**
 * Created by messenge on 05/11/2016.
 */
public class Packet implements Serializable{
    private String ipaddress = "";
    private int port;
    public Packet(String ipaddress, int port)
    {
        this.ipaddress = ipaddress;
        this.port = port;
    }

    public String getIpAddress() {
        return ipaddress;
    }

    public int getPort() {
        return port;
    }
}
