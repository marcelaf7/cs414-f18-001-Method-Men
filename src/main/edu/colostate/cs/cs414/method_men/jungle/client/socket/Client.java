package edu.colostate.cs.cs414.method_men.jungle.client.socket;

import edu.colostate.cs.cs414.method_men.jungle.client.gui.GUI;

import javax.swing.*;
import java.net.InetAddress;
import java.net.Socket;


public class Client implements Runnable{

    private Socket socket;

    private Client(InetAddress address, int port) throws Exception{
        this.socket = new Socket(address,port);
    }

    public void run(){
        if(Thread.currentThread().getName().equals("Receive")){
            try{
                ClientReceive receive = new ClientReceive(this.socket);
                receive.receive();
            }catch(Exception e){}
        }
        if(Thread.currentThread().getName().equals("ClientSend")){
            try{
                ClientSend send = new ClientSend(this.socket);
                send.send();
            }catch(Exception e){}
        }
    }

    public static void main(String[] args) throws Exception{
        Client client = null;
        InetAddress localAddress = InetAddress.getByName("127.0.0.1");
        int localPort = 2000;
        boolean localConnected = false;

        try {
            client = new Client(localAddress, localPort);
            localConnected = true;
            System.out.println("Connected to server at " + localAddress.toString() + ":" + localPort);
        } catch (Exception e) {
            System.out.println("Failed to connect to server at " + localAddress.toString() + ":" + localPort);
        }

        InetAddress piAddress = InetAddress.getByName("jungle.marcelfiore.com");
        int piPort = 2000;
        try {
            if (!localConnected) {
                client = new Client(piAddress, piPort);
                System.out.println("Connected to server at " + piAddress + ":" + piPort);
            }
        } catch (Exception e) {
            System.out.println("Failed to connect to server at " + piAddress.toString() + ":" + piPort);
        }

        if (client == null) {
            System.err.println("No server found");
            System.exit(1);
        }

        GUI g = new GUI(client.socket);
        g.startGUI();
        g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
