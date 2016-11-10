package RESTserver;

/**
 * Created by harry on 07/11/2016.
 */
/*
One instance of this class is created for each incoming HTTP request (which maps to exactly one REST-annotated function of the class)
   - that instance is discarded at the end of the HTTP request
   - note that instance variables are therefore of no use because instances only exist for the duration of a single function call
   - any state must therefore be in "static" class variables; this matches the REST philosophy of "no per-client state at the server"
*/

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import javax.ws.rs.core.MediaType;

import DistributedHashTable.*;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.StreamingOutput;

import java.io.*;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

//everything on /base/files/ will hit this class
@Path("/restserver")
public class RestServer {

    //anything on /base/files/x (where x is anything) will hit this method, with "x" being provided as the "filename" parameter
    // (the actual name of the below method is irrelevant)
//    @GET
//    @Path("/getall/{uid}")
//    @Produces({ MediaType.APPLICATION_OCTET_STREAM })
//    public StreamingOutput getFilesBelongingToUser(@PathParam("uid") int uid) throws Exception {
//        return new StreamingOutput(){
//            @Override
//            public void write(OutputStream out) throws IOException, WebApplicationException {
//                BufferedOutputStream bus = new BufferedOutputStream(out);
//                try{
//
//                    //create the zip file
//                    FileInputStream fis = getAllUserCompletedTasks(uid);
//                    //end creation of zip file
//                    byte[] buffer = convertInputStreamToByteArrary(fis);
//                    bus.write(buffer);
//                }
//                catch(Exception e)
//                {
//                    e.printStackTrace();
//                    System.exit(29);
//                }
//            }
//        };
//    }
    @GET
    @Path("")
    @Produces(MediaType.TEXT_HTML)
    public Response test() throws IOException{
        return Response.status(Response.Status.OK).entity("hello world").build();
    }




    @POST
    @Path("/newTask")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_HTML)
    public Response newFile(@FormDataParam("fileToUpload") File inputfile, @FormDataParam("method") TaskDefinition method, @FormDataParam("uid") int uid) throws IOException {

        //by default files are looked for in Tomcat's root directory, so we prepend our subdirectory on there first...

        return Response.status(Response.Status.OK).entity("hello world").build();
        //String[] filenameS = inputfile.getName().split(".");
//        System.err.println(filenameS);
//        byte[] file = convertInputStreamToByteArrary(new FileInputStream(inputfile));
//        DHTTask t = new DHTTask(filenameS[0],filenameS[1],method,file,uid);
//        assignTask(t);
//        return Response.status(Response.Status.OK).build();
    }



//    public void assignTask(DHTTask task)
//    {
//        iINTERNAL fetchedDHTnode = discoverANode();
//        try{
//            fetchedDHTnode.assignTask(task);
//        }
//        catch(RemoteException e){
//            e.printStackTrace();
//            System.exit(-27);
//        }
//    }
//
//
//    ///FUNCTIONALITY////
//    public static byte[] convertInputStreamToByteArrary(InputStream in) throws IOException {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        final int BUF_SIZE = 1024;
//        byte[] buffer = new byte[BUF_SIZE];
//        int bytesRead = -1;
//        while ((bytesRead = in.read(buffer)) > -1) {
//            out.write(buffer, 0, bytesRead);
//        }
//        in.close();
//        byte[] byteArray = out.toByteArray();
//        return byteArray;
//    }
//
//    public FileInputStream getAllUserCompletedTasks(int uid){
//
//            Registry registry =null;
//            iRECIEVESTUFF recieve = null;
//            try{
//                registry = LocateRegistry.getRegistry(2001); //access the Restserver RMI registry
//                recieve = (iRECIEVESTUFF) registry.lookup("RestServerRMI");
//            }
//            catch (RemoteException e){
//                e.printStackTrace();
//                System.exit(28);
//            }
//            catch (NotBoundException e){
//                e.printStackTrace();
//                System.exit(29);
//            }
//            try {
//
//                ArrayList<CompletedTask> allusertasks = recieve.getAllUserTasks(uid);
//                for (CompletedTask t:allusertasks) {
//                    new File("/"+String.valueOf(uid)+"/").mkdirs(); //make the base file that we will zip
//                    //get the xml file for this file
//                    byte[] xmlfile = t.getXmlfile();
//                    if(xmlfile != null) {
//                        FileOutputStream fos = new FileOutputStream("/"+t.getTitle()+"/xmlfile.xml");
//                        fos.write(xmlfile);
//                        fos.close();
//                    }
//
//                }
//            }
//            catch(Exception e)
//            {
//                e.printStackTrace();
//                System.exit(28);
//            }
//
//        File tozip = new File("/"+String.valueOf(uid)+"/");
//
//        zipDirectory(tozip,String.valueOf(uid));
//        try{
//            FileInputStream fis = new FileInputStream(String.valueOf(uid)+".zip");
//            return fis;
//        }
//        catch(Exception e){
//            e.printStackTrace();
//            System.exit(31);
//        }
//        return null;
//
//
//    }
//    public iINTERNAL discoverANode()
//    {
//        try
//        {
//            DatagramSocket dgsocket = new DatagramSocket(8887, InetAddress.getByName("0.0.0.0"));
//            dgsocket.setBroadcast(true);
//            //until things get set
//            while(true) {
//                byte[] packetarrived = new byte[15000];
//                DatagramPacket rcvdpckt = new DatagramPacket(packetarrived,packetarrived.length);
//
//                dgsocket.receive(rcvdpckt);
//
//                if(rcvdpckt.getData() != null)
//                {
//                    dgsocket.close();
//                    Packet packet = (Packet) convertFromBytes(rcvdpckt.getData()); //make it back into a packet - hope it's all there!
//                    String ip = packet.getIpAddress();
//                    int port = packet.getPort();
//
//                    Registry registry = LocateRegistry.getRegistry(ip, port);
//                    String[] listOfNodes = registry.list();
//                    Random random = new Random();
//                    if(listOfNodes.length >= 1)
//                    {
//                        int rand = random.nextInt(listOfNodes.length);
//                        try{
//                            return (iINTERNAL) registry.lookup(listOfNodes[rand]);
//
//                        }
//                        catch(Exception e)
//                        {
//                            e.printStackTrace();
//                            System.exit(-26);
//                        }
//                    }
//                    else{
//                        throw new Exception("No nodes found!");
//                    }
//
//                }
//
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            System.exit(-15);
//        }
//        return null;
//    }
//    private Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
//        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
//             ObjectInput in = new ObjectInputStream(bis)) {
//            return in.readObject();
//        }
//    }
//
//    //Function adapted from http://www.journaldev.com/957/java-zip-file-folder-example
//    private void zipDirectory(File dir, String zipDirName) {
//        ArrayList<String> filesListInDir = new ArrayList<String>();
//        try {
//            filesListInDir = populateFilesList(filesListInDir, dir);
//
//            FileOutputStream fos = new FileOutputStream(zipDirName);
//            ZipOutputStream zos = new ZipOutputStream(fos);
//
//            for (String filePath : filesListInDir) {
//                ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length() + 1, filePath.length()));
//                zos.putNextEntry(ze);
//                FileInputStream fis = new FileInputStream(filePath);
//                byte[] buffer = new byte[1024];
//                int len;
//                while ((len = fis.read(buffer)) > 0) {
//                    zos.write(buffer, 0, len);
//                }
//                zos.closeEntry();
//                fis.close();
//            }
//            zos.close();
//            fos.close();
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//            System.exit(30);
//        }
//    }
//
//    //adapted from http://www.journaldev.com/957/java-zip-file-folder-example
//    private ArrayList<String> populateFilesList(ArrayList<String> filesListInDir, File dir) {
//        File[] files = dir.listFiles();
//        for(File file : files){
//            if(file.isFile()) filesListInDir.add(file.getAbsolutePath());
//            else populateFilesList(filesListInDir,file);
//        }
//        return filesListInDir;
//    }



}