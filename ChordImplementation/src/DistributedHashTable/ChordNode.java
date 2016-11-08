package DistributedHashTable;

import RESTserver.RestServer;
import RESTserver.RestServerRMI;
import RESTserver.iRECIEVESTUFF;
import com.sun.jmx.remote.internal.RMIExporter;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.crypto.Data;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Created by messenge on 31/10/2016.
 */
public class ChordNode implements Runnable, iINTERNAL{

    /** VARIABLES **/
    static final int KEY_BITS = 8;

    /**DHT NETWORK**/
    private iINTERNAL successor; //used to be ChordNode
    private int successorKey;
    private iINTERNAL predecessor;//used to be ChordNode
    private int predecessorKey;
    private int myKey;
    private String myName;
    private Boolean isMaster = false;
    private ArrayList<DHTTask> incompleteTasks = new ArrayList<DHTTask>();
    private ArrayList<CompletedTask> completedTasks = new ArrayList<CompletedTask>();

    private String restServerIP;
    private String host = "";
    private int port = -1;
    /**FINGER TABLE**/
    private int fingerTableLength;
    private Finger finger[];
    private int nextFingerFix;
    /**CONSTRUCTOR**/
    public ChordNode(String key, Boolean isMaster, String restServerIP){
        myKey = hash(key);
        myName = key;
        successor = this;
        successorKey = myKey;
        this.restServerIP = restServerIP;
        //initialise finger table (note all "node" links will be null!)
        finger = new Finger[KEY_BITS];
        for (int i = 0; i < KEY_BITS; i++)
            finger[i] = new Finger();
        fingerTableLength = KEY_BITS;

        //lookup host and port of what to join
        try {
            this.isMaster = isMaster;
            if (isMaster) {

                host = InetAddress.getLocalHost().getHostAddress();
                port = 1099;
            }
            else{
                Packet wheretojoin = getHostAndIp();
                host = wheretojoin.getIpAddress();
                port = wheretojoin.getPort();
            }
        //bind me to RMI
            iINTERNAL stub = (iINTERNAL) UnicastRemoteObject.exportObject(this,0);
            //lookup ip and port
            Registry registry = LocateRegistry.getRegistry(host, port);
            String[] listOfNodes = registry.list();
            Random random = new Random();
            if(listOfNodes.length >= 1)
            {
                int rand = random.nextInt(listOfNodes.length);
                try{
                    this.join(listOfNodes[rand], host, port, stub);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    System.exit(-26);
                }
            }
            else{
                this.join("masternode", host, port, stub);
            }

        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
        //start up the periodic maintenance thread
        System.out.println("About to start a thread");
        new Thread(this).start();


    }


    /** THINGS THAT NEED RMI **/

    //getKey - returns key (and over RMI)
    public int getKey()
    {
        return myKey;
    }

    //getPredecessor - returns predecessor - and over RMI
    public iINTERNAL getPredecessor()
    {
        return predecessor;
    }

    //getSuccessor - returns predecessor - and over RMI
    public iINTERNAL getSuccessor()
    {
        return successor;
    }

    //join - Joins the chordnode to the system
    public void join(String atNodeName, String host, int port, iINTERNAL stub)
    {
        predecessor = null;
        predecessorKey = 0;
        iINTERNAL atNode;
        Boolean recieved = false;

        /** RMI STUFF **/
        try {

            Registry registry = LocateRegistry.getRegistry(host, port);
            registry.rebind(stub.getName(), stub);
            atNode = (iINTERNAL) registry.lookup(atNodeName);
            successor = atNode; //set my successor in the ring
            successorKey = atNode.getKey();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(-2);
        }
    }

    //findSuccessor - pick who would be the successor
    public iINTERNAL findSuccessor(int key)
    {
        //find where the key fits in
        //looking for node who's key is equal or closest but greater than key
        if (successor == this || isInHalfOpenRangeR(key, myKey, successorKey))
            return successor;
        else {
            iINTERNAL close = closestPrecedingNode(key);//closest preceding to the key (not me!)
            if (close == this)
                return this;
            else
                try{
                    return close.findSuccessor(key); //recursive, look in the closest one
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                    System.exit(-10);
                    return null;//unreachable
                }
        }
    }


    //notifyNode - send a message to the node you think you are the predecessor of
    public void notifyNode(iINTERNAL potentialPredecessor)
    {
        try {
            if (predecessor == null || isInClosedRange(potentialPredecessor.getKey(), predecessor.getKey(), myKey)) {
                predecessor = potentialPredecessor;
                predecessorKey = potentialPredecessor.getKey();
                System.out.println("My key: " + myKey + " Predecessor: " + predecessorKey + " Successor: " + successorKey);
            }
        }
        catch(RemoteException e)
        {
            e.printStackTrace();
            System.exit(-5);
        }
    }

    //stabilise - balance the system by working out if you are the correct successor or predecessor to a node or not
    public void stabilise()
    {
        System.out.println("Stabilise: comes here:  " + Thread.currentThread().getName());
        iINTERNAL successorsPredecessor= null;
        try {
            successorsPredecessor = successor.getPredecessor();

        }
        catch(RemoteException e)
        {
            e.printStackTrace();
            System.exit(-7);
        }
        try {
            if(successorsPredecessor != null)
            {
                    if (isInClosedRange(successorsPredecessor.getKey(), myKey, successorKey)) {
                        successor = successorsPredecessor;
                        successorKey = successorsPredecessor.getKey();
                        System.out.println("My key: " + myKey + " Predecessor: " + predecessorKey + " Successor: " + successorKey);
                    }
                successor.notifyNode(this);
            }
            else{
                successor.notifyNode(this);
            }
        }
        catch(RemoteException e)
        {
            e.printStackTrace();
            System.exit(-8);
        }
    }

    //fixFingers - rebalance/define the finger table
    public void fixFingers()
    {
        iINTERNAL nq = findSuccessor(myKey + ((int) Math.pow(2, nextFingerFix)));
        finger[nextFingerFix].setChordNode(nq);
        try {
            finger[nextFingerFix].setKey(nq.getKey());
            nextFingerFix++;
        }
        catch(RemoteException e)
        {
            e.printStackTrace();
            System.exit(-9);
        }
        if(nextFingerFix == fingerTableLength)
        {
            nextFingerFix = 0;
        }
    }

    //checkPredecessor - if this runs, the predecessor is alive
    public void checkPredecessor()
    {
        try
        {
            int key = predecessor.getKey();
        }
        catch(Exception e) //if something breaks, predecessor is down, remove predecessor
        {
            predecessor = null;
            predecessorKey = 0;
        }
    }

    public void run()
    {
        if(isMaster)
        {
            try {
                DatagramSocket socket = new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));
                socket.setBroadcast(true);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                System.exit(-13);
            }
        }
        while (true)
        {
            try{
                Thread.sleep(1000);
            }
            catch (InterruptedException e){
                System.out.println("Interrupted");
            }

            try{
                stabilise();
            }
            catch (Exception e){e.printStackTrace();}

            try{
                fixFingers();
            }
            catch (Exception e){e.printStackTrace();}

            try{
                checkPredecessor();
            }
            catch (Exception e){e.printStackTrace();}
            if(isMaster) {
                try {
                    DatagramSocket sockety = new DatagramSocket();
                    sockety.setBroadcast(true);
                    Packet toSend = new Packet(host, port);
                    byte[] sendData = convertToBytes(toSend);

                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 8887); //get the broadcast port, try making the packet
                    sockety.send(sendPacket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                //check the master node is still alive (use iINTERNAL areYouAlive?)
                //if not alive, become the master node and start a new RMI registry
                //broadcast that you are one, everyone can rejoin?
            }
            try{
                sendCompletedTasks(restServerIP);
            }catch (Exception e){e.printStackTrace();}
        }

    }



    /**API Functions**/
    //where we process the actual methods
    public void carryOutTask(DHTTask task) {
            //use asynchronous lambda
            new Thread(() -> {
                try {
                    switch (task.getTaskToComplete()) {
                        case TEXTPROCESSING:
                            processText(task);
                            break;
                        case IMAGEPROCESSING:
                            processImage(task);
                            break;
                        case UNZIP:
                            unzip(task);
                            break;
                        case TEXTINVERT:
                            textInvert(task);
                            break;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.exit(-25);
                }

            }).start();


    }
    public Boolean assignTask(DHTTask task)
    {
            int wheretoputme = hash(task.getFilename());
            iINTERNAL putmein = findSuccessor(wheretoputme);
            try {
                putmein.carryOutTask(task);
            }
            catch(RemoteException re)
            {
                re.printStackTrace();
                System.exit(27);
            }
            return false;
    }
    public Boolean areYouAlive()
    {
        return true;
    }


    public void sendCompletedTasks(String restServerIP){
        if(!completedTasks.isEmpty())
        {
            try{
                Registry registry = LocateRegistry.getRegistry(restServerIP, 2001); //access the Restserver RMI registry
                iRECIEVESTUFF recieve = (iRECIEVESTUFF) registry.lookup("RestServerRMI");
                for (CompletedTask comp:completedTasks) {
                    recieve.exportTask(comp);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private Packet getHostAndIp()
    {
        try
        {
            DatagramSocket dgsocket = new DatagramSocket(8887, InetAddress.getByName("0.0.0.0"));
            dgsocket.setBroadcast(true);
            //until things get set
            while(true) {
                byte[] packetarrived = new byte[15000];
                DatagramPacket rcvdpckt = new DatagramPacket(packetarrived,packetarrived.length);

                dgsocket.receive(rcvdpckt);

                if(rcvdpckt.getData() != null)
                {
                    dgsocket.close();
                    return (Packet) convertFromBytes(rcvdpckt.getData()); //make it back into a packet - hope it's all there!
                }

            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-15);
        }
        return null;
    }


    /**LOCAL METHODS**/

    //closestPrecedingNode - find the one before you in the "ring"
    private iINTERNAL closestPrecedingNode(int key) //closest but before
    {
        for (int i = fingerTableLength-1; i >= 0 && i < fingerTableLength; i--) {
            if (isInClosedRange(finger[i].getKey(), myKey, key) && finger[i].getChordNode() != null)
                return finger[i].getChordNode();
        }
        return this;

    }
    private int hash(String s)
    {
        int hash = 0;

        for (int i = 0; i < s.length(); i++)
            hash = hash * 31 + (int) s.charAt(i);

        if (hash < 0) hash = hash * -1;

        return hash % ((int) Math.pow(2, KEY_BITS));
    }
    private boolean isInOpenRange(int key, int a, int b)
    {
        if (b > a) return key >= a && key <= b;
        else return key >= a || key <= b;
    }

    // x is in (a,b) ?
    private boolean isInClosedRange(int key, int a, int b)
    {
        if (b > a) return key > a && key < b;
        else return key > a || key < b;
    }

    // x is in [a,b) ?
    private boolean isInHalfOpenRangeL(int key, int a, int b)
    {
        if (b > a) return key >= a && key < b;
        else return key >= a || key < b;
    }

    // x is in (a,b] ?
    private boolean isInHalfOpenRangeR(int key, int a, int b)
    {
        if (b > a) return key > a && key <= b;
        else return key > a || key <= b;
    }
    /**ACCESSORS**/
    public String getName(){
        return myName;
    }
    // used from http://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array
    private byte[] convertToBytes(Object object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        }
    }
    // used from http://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array
    private Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
    }

    private void processText(DHTTask task) throws Exception{
        byte[] file = task.getFile();
        String filetype = task.getFiletype();
        if(filetype == ("txt")){
            //only .txt works here!
            String str = new String(file, StandardCharsets.UTF_8);
            //wordcount

            Map<String,Integer> wordMap = new HashMap<String,Integer>();
            String[] words = str.split("\\s+");//split the words into an array
            int totalWords = words.length;
            int totalWordLength = 0;
            for(String s : words)
            {
                totalWordLength += s.length();
                if(wordMap.keySet().contains(s))//if we already have this tally,  update it
                {
                    Integer count = wordMap.get(s) + 1;
                    wordMap.put(s, count);
                }
                else
                    wordMap.put(s, 1); //else start a new tally
            }

            Integer frequency = null;
            String mostFrequent = null;
            for(String s : wordMap.keySet())
            {
                Integer i = wordMap.get(s);
                if(frequency == null)
                    frequency = i;
                if(i > frequency)
                {
                    frequency = i;
                    mostFrequent = s;
                }
            }

            double averageWordLength = round(totalWordLength/totalWords, 2);

            /**write xml stuff**/
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); //singleton design pattern
            DocumentBuilder builder;
            try{
                builder = factory.newDocumentBuilder();
                Document doc = builder.newDocument();
                Element root = doc.createElement("documentprocessingsystem");
                doc.appendChild(root);
                Element results = doc.createElement("results");
                root.appendChild(results);

                //add wordcount
                Element wordcount = doc.createElement("wordcount");
                wordcount.appendChild(doc.createTextNode(wordcount.toString()));
                results.appendChild(wordcount);


                //most common word
                Element mcw = doc.createElement("mostcommonword");
                mcw.appendChild(doc.createTextNode(mostFrequent));
                results.appendChild(mcw);

                //average word length
                Element awl = doc.createElement("averagewordlength");
                awl.appendChild(doc.createTextNode(String.valueOf(averageWordLength)));
                results.appendChild(awl);

                //transform into document
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                ByteArrayOutputStream bos=new ByteArrayOutputStream();
                StreamResult result = new StreamResult(bos);
                transformer.transform(source, result);
                byte[] returnfile = bos.toByteArray();

                //create the completed task object with the "name" of the person we're returning it to, and the taskID
                CompletedTask comptask = new CompletedTask();
                comptask.setXmlfile(returnfile);
                comptask.setTaskID(task.getTaskID());
                comptask.setSenderID(task.getSenderID());
                comptask.setTitle(task.getFilename());
                completedTasks.add(comptask);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                System.exit(-23);
            }






        }
        else throw new Exception("Incorrect Filetype entered");


    }
    private void processImage(DHTTask task) throws Exception{


    }
    private void unzip(DHTTask task)  throws Exception {


    }
    private void textInvert(DHTTask task)  throws Exception{


    }
    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }



}
