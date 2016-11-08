package DistributedHashTable;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by messenge on 31/10/2016.
 */
public interface iINTERNAL extends Remote{
     int getKey() throws RemoteException;
     String getName() throws RemoteException;
     iINTERNAL getPredecessor() throws RemoteException;
     iINTERNAL getSuccessor() throws RemoteException;
     iINTERNAL findSuccessor(int key) throws RemoteException;
     Boolean assignTask(DHTTask task) throws RemoteException;
     Boolean areYouAlive() throws RemoteException;
     void carryOutTask(DHTTask task) throws RemoteException;
     void notifyNode(iINTERNAL potentialPredecessor) throws RemoteException;
}
