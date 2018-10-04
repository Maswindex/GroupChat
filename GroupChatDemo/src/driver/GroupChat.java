package driver;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * The initial demo class for testing using ports to message
 *
 *
 * @author Mason Hernandez
 * @author Jacob Landowski
 * @version 1.0
 */
public class GroupChat
{
    private static final String TERMINATE = "Exit";
    private static String name;
    private static volatile boolean finished = false;
    private static final String MULTICAST = "255.0.0.0";
    private static final int PORT = 1234;

    /**
     * Main function for this demo class
     *
     * @param args [MULTICAST, PORT]
     */
    public static void main(String[] args)
    {
        try
        {
            InetAddress group = InetAddress.getByName(MULTICAST);
//            int port = Integer.parseInt(args[1]);

            Scanner userIn = new Scanner(System.in);
            System.out.print("Enter your name: ");
            name = userIn.nextLine();
            MulticastSocket socket = new MulticastSocket(PORT);

            // Since we are deploying
            socket.setTimeToLive(0);

            //this on localhost only (For a subnet set it as 1)
            socket.joinGroup(group);
            Thread userThread = new Thread(new ReadThread(socket,group,PORT));

            // Spawn a thread for reading messages
            userThread.start();

            // sent to the current group
            System.out.println("Start typing messages...\n");
            while(true)
            {
                String message;
                message = userIn.nextLine();
                if(message.equalsIgnoreCase(GroupChat.TERMINATE))
                {
                    finished = true;
                    socket.leaveGroup(group);
                    socket.close();
                    break;
                }
                message = name + ": " + message;
                byte[] buffer = message.getBytes();
                DatagramPacket datagram = new
                        DatagramPacket(buffer,buffer.length,group,PORT);
                socket.send(datagram);
            }
        }
        catch(SocketException se)
        {
            System.out.println("Error creating socket");
            se.printStackTrace();
        }
        catch(IOException ie)
        {
            System.out.println("Error reading/writing from/to socket");
                    ie.printStackTrace();
        }
    }

    /**
     * Retrieves name
     *
     * @return string name
     */
    public static String getName()
    {
        return name;
    }

    /**
     * Retrieves finished boolean
     *
     * @return boolean finished
     */
    public static boolean isFinished()
    {
        return finished;
    }
}