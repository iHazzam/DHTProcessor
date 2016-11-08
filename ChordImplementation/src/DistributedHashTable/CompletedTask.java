package DistributedHashTable;

/**
 * Created by harry on 06/11/2016.
 */
public class CompletedTask {

    private int senderID;
    private byte[] xmlfile;
    private int taskID;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public byte[] getXmlfile() {
        return xmlfile;
    }

    public void setXmlfile(byte[] xmlfile) {
        this.xmlfile = xmlfile;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
}
