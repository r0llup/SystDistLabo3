/**
 * EchecsApplicationClient
 *
 * Copyright (C) 2012 Sh1fT
 *
 * This file is part of EchecsApplicationClient.
 *
 * EchecsApplicationClient is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * EchecsApplicationClient is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchecsApplicationClient; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */

package echecsapplicationclient;

import handlers.TableRowTransferHandler;
import interfaces.EchecsSessionBeanRemote;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import models.CustomTableModel;
import pieces.Bishop;
import pieces.Empty;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import renderers.CustomTableCellRenderer;

/**
 * Manage an {@link EchecsApplicationClient}
 * @author Sh1fT
 */
public class EchecsApplicationClient extends JFrame {
    @EJB(name="EchecsSessionBean")
    private static EchecsSessionBeanRemote echecsSessionBean;
    @Resource(mappedName="jms/myQueue")
    private static Queue myQueue;
    @Resource(mappedName="jms/myQueueFactory")
    private static ConnectionFactory myQueueFactory;
    private CustomTableModel customTableModel;
    private PlayerNameDialog playerNameDialog;
    private Integer playerId;
    private String playerName;
    private String playerColor;
    private Integer whoStart;

    /**
     * Create a new {@link EchecsApplicationClient} instance
     */
    public EchecsApplicationClient() {
        this.initComponents();
        this.setCustomTableModel(new CustomTableModel(this, 8, 8));
        this.setPlayerNameDialog(new PlayerNameDialog(this, true));
        this.setPlayerId(null);
        this.setPlayerName(null);
        this.setPlayerColor(null);
        this.setWhoStart(null);
        this.echecsjTable.setModel(this.getCustomTableModel());
        this.echecsjTable.setTransferHandler(new TableRowTransferHandler(
                this.echecsjTable));
        this.echecsjTable.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                e.consume();
                JComponent c = (JComponent) e.getSource();
                TransferHandler handler = c.getTransferHandler();
                handler.exportAsDrag(c, e, TransferHandler.MOVE);
            }

            public void mouseMoved(MouseEvent e) {}
        });
        this.initChessboard(64, 64);
    }

    /**
     * Initialize the Session Bean
     * @param beanName 
     */
    public static void initSessionBean(String beanName) {
        try {
            Context c = new InitialContext();
            echecsSessionBean = (EchecsSessionBeanRemote) c.lookup(beanName);
        } catch (NamingException ex) {
            System.out.println("Error: " + ex.getLocalizedMessage());
            System.exit(1);
        }
    }

    /**
     * Initialize the Chessboard
     * @param rowSize
     * @param columnSize 
     */
    public void initChessboard(Integer rowSize, Integer columnSize) {
        for (Integer r=0; r < this.echecsjTable.getRowCount(); r++) {
            this.echecsjTable.setRowHeight(r, rowSize);
            for (Integer c=0; c < this.echecsjTable.getColumnCount(); c++) {
                this.echecsjTable.getColumnModel().getColumn(c).setMinWidth(columnSize);
                this.echecsjTable.getColumnModel().getColumn(c).setMaxWidth(columnSize);
                this.echecsjTable.getColumnModel().getColumn(c).setCellRenderer(
                        new CustomTableCellRenderer());
                if ((r >= 0) && (r < 2))
                    this.getCustomTableModel().setValueAt(this.initPieces(r, c, "b"), r, c);
                else if ((r >= 6) && (r < 8))
                    this.getCustomTableModel().setValueAt(this.initPieces(r, c, "w"), r, c);
                else
                    this.getCustomTableModel().setValueAt(new Empty(), r, c);
            }
        }
    }

    /**
     * Initialize the Pieces
     * @param row
     * @param column
     * @param color
     * @return 
     */
    public Piece initPieces(Integer row, Integer column, String color) {
        ImageIcon iconP = new ImageIcon(this.getClass().getResource("/icons/" +
                color + "p.png"), color + "p");
        switch (row) {
            case 0:
                return this.initPieces(column, color);
            case 1:
                return new Pawn(color, iconP);
            case 6:
                return new Pawn(color, iconP);
            case 7:
                return this.initPieces(column, color);
            default:
                break;
        }
        return null;
    }

    /**
     * Initialize the Pieces
     * @param column
     * @param color
     * @return 
     */
    public Piece initPieces(Integer column, String color) {
        ImageIcon iconB = new ImageIcon(this.getClass().getResource("/icons/" +
                color + "b.png"), color + "b");
        ImageIcon iconK = new ImageIcon(this.getClass().getResource("/icons/" +
                color + "k.png"), color + "k");
        ImageIcon iconN = new ImageIcon(this.getClass().getResource("/icons/" +
                color + "n.png"), color + "n");
        ImageIcon iconQ = new ImageIcon(this.getClass().getResource("/icons/" +
                color + "q.png"), color + "q");
        ImageIcon iconR = new ImageIcon(this.getClass().getResource("/icons/" +
                color + "r.png"), color + "r");
        switch (column) {
            case 0:
                return new Rook(color, iconR);
            case 1:
                return new Knight(color, iconN);
            case 2:
                return new Bishop(color, iconB);
            case 3:
                return new Queen(color, iconQ);
            case 4:
                return new King(color, iconK);
            case 5:
                return new Bishop(color, iconB);
            case 6:
                return new Knight(color, iconN);
            case 7:
                return new Rook(color, iconR);
            default:
                break;
        }
        return null;
    }

    /**
     * Create JMS Message for JMS MyQueue
     * @param session
     * @param messageData
     * @return
     * @throws JMSException 
     */
    public Message createJMSMessageForjmsMyQueue(Session session,
            Object messageData) throws JMSException {
        TextMessage tm = session.createTextMessage();
        tm.setText(messageData.toString());
        return tm;
    }

    /**
     * Send JMS Message to MyQueue
     * @param messageData
     * @throws JMSException 
     */
    public void sendJMSMessageToMyQueue(Object messageData) throws JMSException {
        Connection connection = null;
        Session session = null;
        try {
            connection = this.getMyQueueFactory().createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(this.getMyQueue());
            messageProducer.send(this.createJMSMessageForjmsMyQueue(session, messageData));
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    Logger.getLogger(this.getClass().getName()).log(
                            Level.WARNING, "Cannot close session", e);
                }
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Receive JMS Message to MyQueue
     * @return
     * @throws JMSException 
     */
    public String receiveJMSMessageToMyQueue() throws JMSException {
        Connection connection = null;
        Session session = null;
        TextMessage m = null;
        connection = this.getMyQueueFactory().createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.start();
        MessageConsumer consumer = session.createConsumer(this.getMyQueue());
        m = (TextMessage) consumer.receive();
        return m.getText();
    }

    public static EchecsSessionBeanRemote getEchecsSessionBean() {
        return echecsSessionBean;
    }

    public static void setEchecsSessionBean(EchecsSessionBeanRemote echecsSessionBean) {
        EchecsApplicationClient.echecsSessionBean = echecsSessionBean;
    }

    public static Queue getMyQueue() {
        return myQueue;
    }

    public static void setMyQueue(Queue myQueue) {
        EchecsApplicationClient.myQueue = myQueue;
    }

    public static ConnectionFactory getMyQueueFactory() {
        return myQueueFactory;
    }

    public static void setMyQueueFactory(ConnectionFactory myQueueFactory) {
        EchecsApplicationClient.myQueueFactory = myQueueFactory;
    }

    public CustomTableModel getCustomTableModel() {
        return customTableModel;
    }

    public void setCustomTableModel(CustomTableModel customTableModel) {
        this.customTableModel = customTableModel;
    }

    public PlayerNameDialog getPlayerNameDialog() {
        return playerNameDialog;
    }

    public void setPlayerNameDialog(PlayerNameDialog playerNameDialog) {
        this.playerNameDialog = playerNameDialog;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public Integer getWhoStart() {
        return whoStart;
    }

    public void setWhoStart(Integer whoStart) {
        this.whoStart = whoStart;
    }

    /**
     * Update the Title
     */
    public void updateTitle() {
       this.setTitle("EchecsApplicationClient ~ " + this.getPlayerName() + " ~ " +
            (this.getPlayerColor().equals("b") ? "Black" : "White") + " ~ " +
            (this.getWhoStart().equals(this.getPlayerId()) ? "You start!" : "You're next!"));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        echecsjScrollPane = new javax.swing.JScrollPane();
        echecsjTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("EchecsApplicationClient");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        echecsjScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chessboard", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 0, 11), java.awt.Color.darkGray)); // NOI18N

        echecsjTable.setDoubleBuffered(true);
        echecsjTable.setDragEnabled(true);
        echecsjTable.setDropMode(javax.swing.DropMode.INSERT_ROWS);
        echecsjTable.setIntercellSpacing(new java.awt.Dimension(0, 0));
        echecsjTable.setRowSelectionAllowed(false);
        echecsjScrollPane.setViewportView(echecsjTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(echecsjScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(echecsjScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
                .addContainerGap())
        );

        echecsjScrollPane.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
       this.getPlayerNameDialog().setVisible(true);
       this.setPlayerId(this.getEchecsSessionBean().retrievePlayerId(
               new Random().nextInt(200000000)/100000000+1));
       this.setPlayerName(this.getPlayerNameDialog().getPlayerName());
       this.setPlayerColor(new String[]{"b", "w"}[this.getPlayerId()-1]);
       this.getEchecsSessionBean().initGame(
               this.getPlayerId(), this.getPlayerName());
       this.setWhoStart(this.getEchecsSessionBean().whoIsPlaying());
       if (this.getWhoStart().equals(this.getPlayerId())) {
           System.out.println("player " + this.getPlayerName() +
                   " démarre la partie");
           this.getEchecsSessionBean().startGame();
       }
       System.out.println("playerId: " + this.getPlayerId());
       System.out.println("playerName: " + this.getPlayerName());
       System.out.println("playerColor: " + this.getPlayerColor());
       System.out.println("whoStart: " + this.getWhoStart());
       this.updateTitle();
    }//GEN-LAST:event_formWindowOpened

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.out.println("Error: " + ex.getLocalizedMessage());
            System.exit(1);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                EchecsApplicationClient eac = new EchecsApplicationClient();
                eac.initSessionBean("beans.EchecsSessionBean");
                eac.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane echecsjScrollPane;
    private javax.swing.JTable echecsjTable;
    // End of variables declaration//GEN-END:variables
}