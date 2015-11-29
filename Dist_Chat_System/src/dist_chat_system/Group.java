/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dist_chat_system;

/**
 *
 * @author Fakheraldin
 */
import java.util.TreeMap;

/**
 *
 * @author Fakheraldin
 */
public class Group
{
    private String name;
    private TreeMap < String,Client > clients; 

    public Group() 
    {
        clients = new TreeMap < String,Client >();
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void addClient(Client newClient) {
        this.clients.put(newClient.getName(), newClient);
    }
    
    public void removeClient(Client trmvClient) {
        this.clients.remove(trmvClient.getName());
    }

    public TreeMap<String, Client> getClients() {
        return this.clients;
    }

    public void setClients(TreeMap<String, Client> clients) {
        this.clients = clients;
    }
}
