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
    
    private void add(String name)
    {
        jList1.setModel(dm);
        dm.addElement(name);     
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
        refreshButton = new javax.swing.JButton();
        clearClientButton = new javax.swing.JButton();
        deleteClientButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        clearClientButton.setText("Clear");
        clearClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearClientButtonActionPerformed(evt);
            }
        });

        deleteClientButton.setText("Delete");
        deleteClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteClientButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                        .addComponent(deleteClientButton)
                        .addGap(43, 43, 43)
                        .addComponent(clearClientButton, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(refreshButton)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(deleteClientButton)
                            .addComponent(clearClientButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(refreshButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(118, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        dm.clear();
        jList1.setModel(dm);
        for(Map.Entry<String,Client> entry : GlobalVariables.clients.entrySet()) 
        {
            Client client = entry.getValue();
            add(client.getName());
        }
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void clearClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearClientButtonActionPerformed

        dm.clear();
        jList1.setModel(dm);
    }//GEN-LAST:event_clearClientButtonActionPerformed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        // TODO add your handling code here:
        String selected = jList1.getSelectedValue();
    }//GEN-LAST:event_jList1ValueChanged

    private void deleteClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteClientButtonActionPerformed
        int indexDelete;
        String selectedUser = jList1.getSelectedValue();
        indexDelete = jList1.getSelectedIndex();
        delete(indexDelete);
        updateAllUsers_delete(selectedUser);
    }//GEN-LAST:event_deleteClientButtonActionPerformed

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
    
    public static void updateAllUsers_delete(String userName)
    {
        long trmvId = 0;
        Client trmvClient = GlobalVariables.clients.get(userName);
        for(Map.Entry<String,Group> entry : trmvClient.getGroups().entrySet()) 
        {
            Group gRValue = entry.getValue();   
            gRValue.removeClient(trmvClient);
        }
        GlobalVariables.clients.remove(userName);
        for(Map.Entry<Long,ClientHandler> entry : GlobalVariables.threads.entrySet()) 
        {
            ClientHandler cHValue = entry.getValue();   
            if((cHValue.getCurrentClient().getName()).equals(userName))
            {
                trmvId = cHValue.getId();
                break;
            }
        }
        
        ClientHandler trmvThread = GlobalVariables.threads.get(trmvId);
        GlobalVariables.threads.remove(trmvId);
        trmvThread.setThread_close(true);
        
        for(Map.Entry<Long,ClientHandler> entry : GlobalVariables.threads.entrySet()) 
        {
            ClientHandler cHValue = entry.getValue();   
            cHValue.setUpdate_flag(true);
            cHValue.setUpdate_msg("delete"+","+userName);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            public void run() {
                gui mainGui = new gui();
                mainGui.setVisible(true);
            }
        });
        
        //Create main server socket
        ServerSocket server = new ServerSocket(1234);
        while(true)
        {
            //update GUI with the clients list
            
            //listen for clients
            Socket client = server.accept();
            //create new thread to serve each client
            ClientHandler incomming = new ClientHandler(client);
            GlobalVariables.threads.put(incomming.getId(),incomming);
            incomming.start();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearClientButton;
    private javax.swing.JButton deleteClientButton;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton refreshButton;
    // End of variables declaration//GEN-END:variables
}
