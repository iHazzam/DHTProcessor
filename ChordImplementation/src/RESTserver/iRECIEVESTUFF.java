package RESTserver;

import DistributedHashTable.CompletedTask;

import java.io.FileInputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by messenge on 31/10/2016.
 */
public interface iRECIEVESTUFF extends Remote {
    ArrayList<CompletedTask> getAllUserTasks(int uid) throws RemoteException;
    CompletedTask getTaskByID(int uid, int taskid) throws RemoteException;
    void exportTask(CompletedTask task) throws RemoteException;
}
