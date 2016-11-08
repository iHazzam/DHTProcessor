package RESTserver;

import DistributedHashTable.ChordNode;
import DistributedHashTable.iINTERNAL;

import java.io.Console;
import java.net.InetAddress;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by harry on 03/11/2016.
 */
public class Launcher {
    public static void main(String[] args) {
        RestServerRMI rmiserver = new RestServerRMI();
        ChordNode node1 = new ChordNode("masternode", true, args[0]);
        ChordNode node2 = new ChordNode("cyclone", false, args[0]);

    }
}
