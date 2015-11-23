/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat_system_server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

class ClientHandler extends Thread
{
    private Socket client;
    
    // constructor
    public ClientHandler(Socket client) 
    {
        this.client = client;
    }
    
    @Override
    public void run() {
        try 
        {
            //Create IO Streams
            DataOutputStream dos = new DataOutputStream(client.getOutputStream());
            DataInputStream dis = new DataInputStream(client.getInputStream());
            //Perform IO Operations
            while (true) 
            {
                //get the input msg 
                String msgInput = dis.readUTF();
                //parse input msg into the three main fields
                String [] clientId = msgInput.split(",");
                String name = clientId[0];
                String iP = clientId[1];
                String portNo = clientId[2];
 
                Client currentClient;
                
                if(GlobalVariables.clients.containsKey(name))
                {
                    currentClient = GlobalVariables.clients.get(name);
                    currentClient.setIp(iP);
                    currentClient.setPortNo(portNo);
                    currentClient.setStatus(true);
                }
                else
                {
                    GlobalVariables.clients.put(name, new Client(name,iP,portNo,true));
                    currentClient = GlobalVariables.clients.get(name);
                }
                
                //creating the response for the customer
                StringBuilder stringBuilder = new StringBuilder("");
                for(Map.Entry<String,Client> entry : GlobalVariables.clients.entrySet()) 
                {
                    //Putting each user in the application in a line with attributes separated by ","
                    Client cValue = entry.getValue();
                    if(!(cValue.getName().equals(currentClient.getName())))
                    {    
                        stringBuilder.append(cValue.getName()+","+cValue.getIp()+","+cValue.getPortNo()+","+Boolean.toString(cValue.isStatus())+"\r\n");
                    }
                }
                //another new line so now we can enter us groups' names
                stringBuilder.append("\r\n");
                //loop on current user's groups
                for(Map.Entry<String,Group> entry : currentClient.getGroups().entrySet()) 
                {
                    //each group is entered in a line
                    Group gValue = entry.getValue();
                    stringBuilder.append(gValue.getName()+"\r\n");
                }
                //indicate end of message by adding new line
                stringBuilder.append("\r\n");
                System.out.println(stringBuilder.toString());
                dos.writeUTF(stringBuilder.toString());
                break;
            }
            
            //release resources
            dis.close();
            dos.close();
            client.close();
        } 
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
        }
    }   
}  

public class Chat_System_Server 
{  
    public static void main(String[] args) throws IOException 
    {
        //Create main server socket
        ServerSocket server = new ServerSocket(1234);
        while(true)
        {
            //listen for clients
            Socket client = server.accept();
            //create new thread to serve each client
            (new ClientHandler(client)).start();
        }
    }    
}
