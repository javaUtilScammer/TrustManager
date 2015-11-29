import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ClientSimulator extends JFrame {

    private final String serverURL;
    private final String postURL;
    private final String clientName;
    private Configuration config;
    private Gson gson;
    private URL obj;
    private URL objPost;

    public static void main(String args[]) throws IOException {
        new ClientSimulator();
    }

    public ClientSimulator() throws IOException {
        JTextField server = new JTextField("http://localhost:8000/create");
        JTextField client = new JTextField("testClient");
        JTextField validator = new JTextField("testValidation");
        JTextField time = new JTextField("10");
        JTextField score = new JTextField("2.5");
        JTextField scale = new JTextField("5");
        Object[] message = {
            "Server URL", server,
            "Client Name", client,
            "Validation Type", validator,
            "Validation Time", time,
            "Default Score", score,
            "Rating Scale", scale
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Client Simulator", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            System.exit(0);
        }
        serverURL = server.getText();
        obj = new URL(serverURL);
        clientName = client.getText();
        config = new Configuration(client.getText(), validator.getText(), Integer.parseInt(time.getText()), Double.parseDouble(score.getText()), Integer.parseInt(scale.getText()));
        postURL = postConfig();
        objPost = new URL("http://localhost:8000/"+postURL);
        gson = new Gson();
        initComponents();
        setVisible(true);
        setResizable(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account ID", "Username"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Contribution ID", "Account ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Evaluation ID", "Account ID", "Contribution ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jButton1.setText("Create Account");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Create Contribution");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Create Evaluation");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(49, 49, 49)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                        .addComponent(jButton3)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private String postConfig() throws ProtocolException, IOException{
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        Gson gson = new Gson();
        wr.writeBytes(gson.toJson(config));
        wr.flush();
        wr.close();
        int responseCode = connection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        connection.disconnect();
        return response.toString();
    }

    public String post(String message) throws Exception{
        HttpURLConnection connection = (HttpURLConnection) objPost.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(message);
        wr.flush();
        wr.close();
         System.out.println("\nSending 'POST' request to URL : " + serverURL);
         System.out.println("Post parameters : " + message);
        int responseCode = connection.getResponseCode();
        
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        connection.disconnect();
        return response.toString();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JTextField username = new JTextField();
        Object[] message = {
            "Username", username
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Create Account", JOptionPane.OK_CANCEL_OPTION);
        username.grabFocus();
        if (option == JOptionPane.OK_OPTION) {
            AccountTest temp = new AccountTest(username.getText());
            int ind = -1;
            try {
                ind = Integer.parseInt(post(gson.toJson(temp)));
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.addRow(new Object[]{ind, username.getText()});
            } catch (Exception ex) {}
            System.out.println(ind);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        JTextField a_id = new JTextField();
        JTextField state = new JTextField();
        JTextField score = new JTextField();
        Object[] message = {
            "Acount ID", a_id,
            "State", state,
            "Score Validity", score
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Create Contribution", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            ContributionTest temp = new ContributionTest(a_id.getText(),score.getText(),state.getText());
            int ind = -1;
            try {
                ind = Integer.parseInt(post(gson.toJson(temp)));
                DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
                model.addRow(new Object[]{ind, a_id.getText()});
            } catch (Exception ex) {
                Logger.getLogger(ClientSimulator.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(ind);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        JTextField a_id = new JTextField();
        JTextField c_id = new JTextField();
        JTextField rating = new JTextField();
        Object[] message = {
            "Acount ID", a_id,
            "Contribution ID", c_id,
            "Rating", rating
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Create Evaluation", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            EvaluationTest temp = new EvaluationTest(a_id.getText(), c_id.getText(), rating.getText());
            int ind = -1;
            try {
                ind = Integer.parseInt(post(gson.toJson(temp)));
                DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
                model.addRow(new Object[]{ind, a_id.getText(), c_id.getText()});
            } catch (Exception ex) {
                Logger.getLogger(ClientSimulator.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(ind);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    // End of variables declaration//GEN-END:variables
}
