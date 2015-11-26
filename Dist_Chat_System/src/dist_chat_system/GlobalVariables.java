/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dist_chat_system;
import java.util.TreeMap;


public class GlobalVariables
{
    public static TreeMap<String,Client> clients = new TreeMap<String,Client>();
    public static TreeMap<String,Group> groups = new TreeMap<String,Group>();
    public static TreeMap<Long,ClientHandler> threads = new TreeMap<Long,ClientHandler>();
}
