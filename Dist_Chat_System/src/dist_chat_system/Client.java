/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dist_chat_system;

import java.util.TreeMap;

/**
 *
 * @author Fakheraldin
 */
public class Client
{
    private String name;
    private String ip;
    private String portNo;
    private boolean status;
    private TreeMap < String,Group > groups;

    public Client(String cName,String cIp, String cPortno, boolean cStatus) 
    {
        this.name = cName;
        this.ip = cIp;
        this.portNo = cPortno;
        this.status = cStatus;
        this.groups = new TreeMap< String,Group >(); 
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPortNo() {
        return portNo;
    }

    public void setPortNo(String portNo) {
        this.portNo = portNo;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean cStatus) {
        this.status = cStatus;
    }

    public TreeMap<String, Group> getGroups() {
        return groups;
    }

    public void setGroups(TreeMap<String, Group> groups) {
        this.groups = groups;
    }
    
    public void addGroup(Group newGroup) {
        this.groups.put(newGroup.getName(), newGroup);
    }
}