package DistributedHashTable;

import java.util.Scanner;

/**
 * Created by messenge on 07/11/2016.
 */
public class launchNewNode {
    public static void main(String[] args) {


                Scanner scanner = new Scanner(System.in);
                System.out.println("Please enter the name of the new node that you are starting");
                String nodename = scanner.next();
                System.out.println("Please enter the rest server IP");
                String restServerIP = scanner.next();
                ChordNode newnode = new ChordNode(nodename, false, restServerIP);

            }

}
