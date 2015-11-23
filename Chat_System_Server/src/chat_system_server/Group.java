/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat_system_server;

import java.util.TreeMap;

/**
 *
 * @author Fakheraldin
 */
public class Group
{
    private String name;
    private TreeMap < String,Client > clients; 

    public Group() {
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
