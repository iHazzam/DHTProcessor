package RESTserver;


import DistributedHashTable.CompletedTask;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by messenge on 31/10/2016.
 */
public class RestServerRMI implements iRECIEVESTUFF, Runnable {

     MultivaluedHashMap<Integer, CompletedTask> mvhm = new MultivaluedHashMap<Integer,CompletedTask>();


    public RestServerRMI(){
        try{

            Registry registry = LocateRegistry.getRegistry(2001); //access the Restserver RMI registry
            iRECIEVESTUFF stub = (iRECIEVESTUFF) UnicastRemoteObject.exportObject(this,0);
            registry.rebind("RestServerRMI", stub);

            new Thread(this).start();

        }
        catch(RemoteException re)
        {
            re.printStackTrace();
            System.exit(28);
        }
    }


    public ArrayList<CompletedTask> getAllUserTasks(int uid){
        return (ArrayList<CompletedTask>) mvhm.get(uid);

    }
    public CompletedTask getTaskByID(int uid, int taskid){
        ArrayList<CompletedTask> temp = getAllUserTasks(uid);
        for (CompletedTask ct:temp) {
            if(ct.getTaskID() == taskid)
            {
                return ct;
            }
        }
        return null;
    }

















    public void exportTask(CompletedTask task)
    {
        int uid = task.getSenderID();
        mvhm.add(uid,task);
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(10000);
                System.out.println("I AM ALIVE");
            }
            catch(Exception e)
            {
                System.out.println("Interrupted!");
        }
        }
    }
}
