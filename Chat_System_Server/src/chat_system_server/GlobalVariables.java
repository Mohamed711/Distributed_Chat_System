//This class is as the database of the server
package chat_system_server;

import java.util.TreeMap;


public class GlobalVariables
{
    public static TreeMap<String,Client> clients = new TreeMap<String,Client>();
    public static TreeMap<String,Group> groups = new TreeMap<String,Group>();
}
