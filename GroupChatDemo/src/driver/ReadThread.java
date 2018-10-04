package driver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Helper method for the GroupChatDemo
 *
 * @author Mason Hernandez
 */
class ReadThread implements Runnable
{
    private MulticastSocket socket;
    private InetAddress group;
    private int port;
    private static final int MAX_LEN = 1000;
    ReadThread(MulticastSocket socket,InetAddress group,int port)
    {
        this.socket = socket;
        this.group = group;
        this.port = port;
    }

    @Override
    public void run()
    {
        while(!GroupChat.isFinished())
        {
            byte[] buffer = new byte[ReadThread.MAX_LEN];
            DatagramPacket datagram = new
                    DatagramPacket(buffer,buffer.length,group,port);
            String message;
            try
            {
                socket.receive(datagram);
                message = new
                        String(buffer,0,datagram.getLength(),"UTF-8");
                if(!message.startsWith(GroupChat.getName())) {
                    System.out.println(message);
                }
            }
            catch(IOException exception)
            {
                System.out.println("Socket closed!");
                System.out.println("Exception: " + exception.getMessage());
            }
        }
    }

    @Override
    public String toString() {
        return "ReadThread{" +
                "socket=" + socket +
                ", group=" + group +
                ", port=" + port +
                '}';
    }
}
