/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dist_chat_system;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

/**
 *
 * @author Fakheraldin
 */
public class ClientHandler extends Thread
{
    private Socket clientSocket;
    private String update_msg;
    private boolean update_flag;
    private Client currentClient;
    private  boolean thread_close;
    private gui mainGui;
    // constructor
    public ClientHandler(Socket client, gui mainGui) 
    {
        this.mainGui=mainGui;
        this.clientSocket = client;
        update_flag = false;
        update_msg = new String("");
        thread_close = false;
    }
    
    @Override
    public void run() {
        try 
        {
            //Create IO Streams
            DataOutputStream dos = new DataOutputStream(this.clientSocket.getOutputStream());
            DataInputStream dis = new DataInputStream(this.clientSocket.getInputStream());
            //Perform IO Operations
            while (true) 
            {
                //get the input msg 
                String msgInput;
                while(dis.available()== 0)
                {
                    if(update_flag)
                    {
                        System.out.println(update_msg);
                        dos.writeUTF(update_msg);
                        update_flag = false;
                    } 
                    if(thread_close)
                    {
                        break;
                    }
                }
                if(thread_close)
                {
                    break;
                }
                msgInput = dis.readUTF();
                //parse input msg into different fields
                String [] msgFields = msgInput.split(",");
                String header = msgFields[0];
                if(header.equals("sign_in"))
                {
                    String name = msgFields[1];
                    String iP = msgFields[2];
                    String portNo = msgFields[3];

                    boolean onFlag=false;
                    if(GlobalVariables.clients.containsKey(name))
                    {
                        this.currentClient = GlobalVariables.clients.get(name);
                        //check whether the client is online or not
                        if(this.currentClient.isStatus())//online
                        {
                            onFlag = true;
                            System.out.println("you are online");
                            dos.writeUTF("you are online");
                        }
                        else
                        {
                            this.currentClient.setIp(iP);
                            this.currentClient.setPortNo(portNo);
                            this.currentClient.setStatus(true);
                        }
                    }
                    else
                    {
                        GlobalVariables.clients.put(name, new Client(name,iP,portNo,true));
                        this.currentClient = GlobalVariables.clients.get(name);
                        mainGui.add(name);
                    }
                    if(!onFlag)
                    {
                        //creating the response for the customer in case of not online client
                        StringBuilder stringBuilder = new StringBuilder("");
                        for(Map.Entry<String,Client> entry : GlobalVariables.clients.entrySet()) 
                        {
                            //Putting each user in the application in a line with attributes separated by ","
                            Client cValue = entry.getValue();
                            if(!(cValue.getName().equals(this.currentClient.getName())))
                            {    
                                stringBuilder.append(cValue.getName()+","+cValue.getIp()+","+cValue.getPortNo()+","+Boolean.toString(cValue.isStatus())+"\r\n");
                            }
                        }
                        //if there is no clients or only the current client
                        if(GlobalVariables.clients.size()<=1)
                        {
                            stringBuilder.append("empty"+"\r\n");
                        }
                        //another new line so now we can enter us groups' names
                        stringBuilder.append("\r\n");
                        //loop on current user's groups
                        for(Map.Entry<String,Group> entry : GlobalVariables.groups.entrySet()) 
                        {
                            //each group is entered in a line
                            Group gValue = entry.getValue();
                            if(this.currentClient.getGroups().containsKey(gValue.getName()))
                            {
                                stringBuilder.append(gValue.getName()+","+"true"+"\r\n");
                            }
                            else
                            {
                                stringBuilder.append(gValue.getName()+","+"false"+"\r\n");
                            }
                        }
                        if(GlobalVariables.groups.isEmpty())
                        {
                            stringBuilder.append("empty"+"\r\n");
                        }
                        //indicate end of message by adding new line
                        stringBuilder.append("\r\n");
                        System.out.println(stringBuilder.toString());
                        dos.writeUTF(stringBuilder.toString());
                        gui.updateAllUsers_signIn(this.getId(), name, portNo, iP);
                    }
                }
                else if(header.equals("create_grp"))
                {
                    String groupName = msgFields[1];
                    if (GlobalVariables.groups.containsKey(groupName))
                    {
                        System.out.println("group name exists");
                        dos.writeUTF("group name exists");        
                    }
                    else
                    {
                        Group newGroup = new Group();
                        newGroup.setName(groupName);
                        GlobalVariables.groups.put(groupName, newGroup);
                        newGroup.addClient(this.currentClient);
                        this.currentClient.addGroup(newGroup);
                        System.out.println("created_successfully");
                        dos.writeUTF("created_successfully");
                        gui.updateAllUsers_createGroup(groupName, this.getId());
                    }  
                }
                else if(header.equals("enroll_grp"))
                {
                   String grpName = msgFields[1];
                   this.currentClient.getGroups().put(grpName, GlobalVariables.groups.get(grpName));
                   GlobalVariables.groups.get(grpName).addClient(this.currentClient);
                }
                else if(header.equals("unenroll_grp"))
                {
                    String grpName = msgFields[1];
                    this.currentClient.getGroups().remove(grpName);
                    GlobalVariables.groups.get(grpName).removeClient(this.currentClient);
                }
                else if(header.equals("grp_msg"))
                {
                    String grpName = msgFields[1];
                    String msg = msgFields[2];
                    
                    gui.updateAllUsers_groupMsg(grpName, msg, this.getId(), this.currentClient.getName());
                }
                else if(header.equals("open_grp"))
                {
                    String grpName = msgFields[1];
                    Group currentGroup = GlobalVariables.groups.get(grpName);
                    StringBuilder s = new StringBuilder("");
                    for(Map.Entry<String,Client> entry : currentGroup.getClients().entrySet()) 
                    {
                        Client cValue = entry.getValue();   
                        s.append(cValue.getName()+",");
                        String sOut = s.toString();
                        sOut = sOut.substring(0, sOut.length()-1);
                        System.out.println(sOut);
                        dos.writeUTF(sOut);
                    }           
                }
                else if(header.equals("sign_out"))
                {
                   this.currentClient.setStatus(false);
                   gui.updateAllUsers_signOut(this.getId(),this.currentClient.getName());
                   break;
                }
            }
            //release resource
            dis.close();
            dos.close();
            this.clientSocket.close();
        } 
        catch (Exception e) 
        {
            this.currentClient.setStatus(false);
            System.out.println("kharag el client :D");
            System.out.println(e.getMessage());
        }
    }  

    public String getUpdate_msg() 
    {
        return update_msg;
    }

    public void setUpdate_msg(String update_msg) 
    {
        this.update_msg = update_msg;
    }

    public boolean isUpdate_flag() 
    {
        return update_flag;
    }

    public void setUpdate_flag(boolean update_flag) 
    {
        this.update_flag = update_flag;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public Client getCurrentClient() {
        return currentClient;
    }

    public void setCurrentClient(Client currentClient) {
        this.currentClient = currentClient;
    }

    public boolean isThread_close() {
        return thread_close;
    }

    public void setThread_close(boolean thread_close) {
        this.thread_close = thread_close;
    }
    
}
