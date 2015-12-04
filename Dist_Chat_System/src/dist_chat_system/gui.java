package dist_chat_system;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import javax.swing.DefaultListModel;

public class gui extends javax.swing.JFrame 
{
    
    //Model
    DefaultListModel dm = new DefaultListModel();
    
    public gui() 
    {
        initComponents();
    }
    
    public void add(String name)
    {
        jList1.setModel(dm);
        dm.addElement(name);     
    }
    
    public void updateUserStatus(boolean newStatus, String userName)
    {
        jList1.setModel(dm);       
        if (newStatus == true)
        {
            String userStatus = userName + "      online";
            String oldUserStatus = userName + "      offline";
            int index = dm.indexOf(oldUserStatus);
            dm.remove(index);
            dm.add(index, userStatus);
        }
        else
        {   String oldUserStatus = userName + "      online";
            String userStatus = userName + "      offline";
            int index = dm.indexOf(oldUserStatus);
            dm.remove(index);
            dm.add(index, userStatus);
        }
    }
    
    private void delete(int index)
    { 
        jList1.setModel(dm);
        dm.remove(index);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<String>();
        deleteClientButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        deleteClientButton.setText("Delete");
        deleteClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteClientButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Users List");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(deleteClientButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(106, 106, 106)
                        .addComponent(jLabel1)))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deleteClientButton)
                .addGap(26, 26, 26))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        // TODO add your handling code here:
        String selected = jList1.getSelectedValue();
    }//GEN-LAST:event_jList1ValueChanged

            
    private void deleteClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteClientButtonActionPerformed
        int indexDelete = jList1.getSelectedIndex();
        if(indexDelete != -1) // to handle case of non selected element
        {
            String selectedUser = jList1.getSelectedValue();
            String[] splitted = selectedUser.split("\\s+");
            selectedUser = splitted[0];
            delete(indexDelete);
            updateAllUsers_delete(selectedUser);
        }
    }//GEN-LAST:event_deleteClientButtonActionPerformed
    
    public static void updateAllUsers_signIn(Long id, String userName, String portNo, String Ip)
    {
        for(Map.Entry<Long,ClientHandler> entry : GlobalVariables.threads.entrySet()) 
        {
            ClientHandler cHValue = entry.getValue();   
            if(cHValue.getId()!=id)
            {
                cHValue.setUpdate_flag(true);
                cHValue.setUpdate_msg("status"+","+userName+","+Ip+","+portNo+","+"true");
            }
        }
    }
    
    public static void updateAllUsers_signOut(Long id,String userName)
    {
        for(Map.Entry<Long,ClientHandler> entry : GlobalVariables.threads.entrySet()) 
        {
            ClientHandler cHValue = entry.getValue();   
            if(cHValue.getId()!=id)
            {
                cHValue.setUpdate_flag(true);
                cHValue.setUpdate_msg("status"+","+userName+","+"false");
            }
        }
        GlobalVariables.threads.remove(id);
    }
    
    public static void updateAllUsers_groupMsg(String groupName, String msg, long userId, String userName)
    {
        for(Map.Entry<Long,ClientHandler> entry : GlobalVariables.threads.entrySet()) 
        {
            ClientHandler cHValue = entry.getValue();   
            
            if(cHValue.getId()!= userId)
            {
                if(cHValue.getCurrentClient().getGroups().containsKey(groupName))
                {
                    cHValue.setUpdate_flag(true);
                    cHValue.setUpdate_msg("grp_msg"+","+groupName+","+userName+","+msg);
                }
            }
        }
    }
    
    public static void updateAllUsers_createGroup(String groupName,Long userId)
    {
        for(Map.Entry<Long,ClientHandler> entry : GlobalVariables.threads.entrySet()) 
        {
            ClientHandler cHValue = entry.getValue();   
            
            if(cHValue.getId()!= userId)
            {               
                cHValue.setUpdate_flag(true);
                cHValue.setUpdate_msg("create_grp"+","+groupName);               
            }
        }
    }
    
    public static void updateAllUsers_deleteGroup(String groupName)
    {
        for(Map.Entry<Long,ClientHandler> entry : GlobalVariables.threads.entrySet()) 
        {
            ClientHandler cHValue = entry.getValue();                  
            cHValue.setUpdate_flag(true);
            cHValue.setUpdate_msg("delete_grp"+","+groupName);               
        }
    }
    
    public static void updateAllUsers_delete(String userName)
    {
        long trmvId = 0;
        Client trmvClient = GlobalVariables.clients.get(userName);
        for(Map.Entry<String,Group> entry : trmvClient.getGroups().entrySet()) 
        {
            Group gRValue = entry.getValue();   
            gRValue.removeClient(trmvClient);
            //if the user was the last member in the group remove this group
            if(gRValue.getClients().size()==0)
            {
                GlobalVariables.groups.remove(gRValue.getName());
                updateAllUsers_deleteGroup(gRValue.getName());
            }
        }
        for(Map.Entry<Long,ClientHandler> entry : GlobalVariables.threads.entrySet()) 
        {
            ClientHandler cHValue = entry.getValue();   
            if((cHValue.getCurrentClient().getName()).equals(userName))
            {
                trmvId = cHValue.getId();
                break;
            }
        }
        for(Map.Entry<Long,ClientHandler> entry : GlobalVariables.threads.entrySet()) 
        {
            ClientHandler cHValue = entry.getValue();   
            cHValue.setUpdate_flag(true);
            cHValue.setUpdate_msg("delete"+","+userName);
        }
        GlobalVariables.clients.remove(userName);
        ClientHandler trmvThread = GlobalVariables.threads.get(trmvId);
        GlobalVariables.threads.remove(trmvId);
        if(trmvThread!=null)
        {
            trmvThread.setThread_close(true);
    
        }
    }
    
    public static void main(String args[]) throws IOException 
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        gui mainGui = new gui();
        mainGui.setVisible(true);
        
        ////////// dummy clients our names
        GlobalVariables.clients.put("Fakhr",new Client("Fakhr","","",false));
        mainGui.add("Fakhr      offline");
        GlobalVariables.clients.put("Hesham",new Client("Hesham","","",false));
        mainGui.add("Hesham      offline");
        GlobalVariables.clients.put("Farida",new Client("Farida","","",false));
        mainGui.add("Farida      offline");
        GlobalVariables.clients.put("Arwa",new Client("Arwa","","",false));
        mainGui.add("Arwa      offline");
        GlobalVariables.clients.put("Amr",new Client("Amr","","",false));
        mainGui.add("Amr      offline");
        GlobalVariables.clients.put("Abbas",new Client("Abbas","","",false));
        mainGui.add("Abbas      offline");
        GlobalVariables.groups.put("MainGroup",new Group("MainGroup"));
        for(Map.Entry<String,Client> entry : GlobalVariables.clients.entrySet()) 
        {
            Client cValue = entry.getValue();   
            GlobalVariables.groups.get("MainGroup").addClient(cValue);
            cValue.addGroup(GlobalVariables.groups.get("MainGroup"));
        }
        //////////
        //Create main server socket
        ServerSocket server = new ServerSocket(1234);
        while(true)
        {
            //update GUI with the clients list
            
            //listen for clients
            Socket client = server.accept();
            //create new thread to serve each client
            ClientHandler incomming = new ClientHandler(client,mainGui);
            GlobalVariables.threads.put(incomming.getId(),incomming);
            incomming.start();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteClientButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
