package DistributedHashTable;

/**
 * Created by harry on 03/11/2016.
 */
public class DHTTask {
    private byte[] file;
    private String filename;
    private String filetype;
    private TaskDefinition taskToComplete;
    private int senderID;

    public DHTTask(String filename, String filetype, TaskDefinition tasktodo, byte[] file, int senderID)
    {
        this.file = file;
        this.senderID = senderID;
        this.filename = filename;
        this.filetype = filetype;
        taskToComplete = tasktodo;
    }

    public byte[] getFile() {
        return file;
    }

    public String getFilename() {
        return filename;
    }

    public String getFiletype() {
        return filetype;
    }

    public TaskDefinition getTaskToComplete() {
        return taskToComplete;
    }

    public int getSenderID() {
        return senderID;
    }

}

