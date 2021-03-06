/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qbox;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.text.DefaultCaret;
import qbox.chat.ChatMessage;
import qbox.chat.Klient;
import qbox.chat.MojePripojeni;
import qbox.chat.Server;
import qbox.chat.VypisChatu;

/**
 *
 * @author bleha
 */
public class Okno extends javax.swing.JFrame {

    private final TimeUpdater timeUpdater;
    private final MojePripojeni mojePripojeni;
    private final PripojitOkno pripojitOkno;
    
    private Server server;
    private Klient client;
    private Klient client2;
    
    /**
     * Creates new form Okno
     */
    public Okno() {
        initComponents();
        
        //nastaveni time updateru
        this.timeUpdater = new TimeUpdater(time);
        
        //nastavi mojePripojeni
        this.mojePripojeni = new MojePripojeni(chatPane);
        
        //nastavi pripojitOkno
        this.pripojitOkno = new PripojitOkno(mojePripojeni);
        
        DefaultCaret caret = (DefaultCaret)chat.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        //tray icon
        trayicon();
    }
    
    private void trayicon() {
		// pokud je tray ikon podporovana
		if (SystemTray.isSupported()) {
			// PopUp menu pro tray ikonu
			PopupMenu popup = new PopupMenu();
	
			// menuItem pro PopUpmenu
			final MenuItem minimize = new MenuItem("Minimalizovat");
			// action listener pro MenuItem
			minimize.addActionListener(new ActionListener() {
                                @Override
				public void actionPerformed(ActionEvent arg0) {
					// volame metodu pro zmenu visiblity
					setvisible();
					// nastavime text na MenuItem dle visiblity
					if (isVisible() == false) {
						minimize.setLabel("Otevřít");
					} else {
						minimize.setLabel("Minimalizovat");
					}
				}
			});
			// MenuItem pro ukonceni programu
			MenuItem exit = new MenuItem("Ukončit");
			// action listener pro konec programu
			exit.addActionListener(new ActionListener() {
                                @Override
				public void actionPerformed(ActionEvent arg0) {
					System.exit(0);
				}
			});
			// pridame menuItems do PopUp menu
			popup.add(minimize);
			popup.add(exit);
			
			//vytvorime novou systemTray
			SystemTray tray = SystemTray.getSystemTray();
			// a obrazek pro tray ikony jako Image
			Image image = Toolkit.getDefaultToolkit().getImage("/icons/icon32px.png");
			// vytvorime novou tray ikonu
			TrayIcon trayIcon = new TrayIcon(image, "Qbox", popup);
			
                        Dimension size = trayIcon.getSize();
                        trayIcon.setImage(new ImageIcon(getClass().getResource("/icons/icon32px.png")).getImage().getScaledInstance(size.width-2, size.height-2, 0));
                        
                        try {
				// a pridame ji do SystemTray
				tray.add(trayIcon);
			} catch (AWTException e) {
			}
                        
			// Mouse Listener pro TrayIcon
			trayIcon.addMouseListener(new MouseListener() {
				// po kliknuti
                                @Override
				public void mouseClicked(MouseEvent arg0) {
					// pokud klidneme levym tlacitkem, zmeni se hodnota Visible
					if (arg0.getButton() == 1)
						setvisible();
				}
	
                                @Override
				public void mouseEntered(MouseEvent arg0) {
				}
	
                                @Override
				public void mouseExited(MouseEvent arg0) {
				}
	
                                @Override
				public void mousePressed(MouseEvent arg0) {
				}
	
                                @Override
				public void mouseReleased(MouseEvent arg0) {
				}
	
			});
		} else {
		}
	
	}
    
        private void setvisible() {
		// nastavi opacnou Visiblitu nez je aktualni
		if (this.isVisible() == true) {
			this.setVisible(false);
		} else {
			this.setVisible(true);
		}
	}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jRadioButton1 = new javax.swing.JRadioButton();
        jToolBar1 = new javax.swing.JToolBar();
        turnServerOn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        message = new javax.swing.JTextField();
        emotikon = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        serverPane = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        serverChat = new javax.swing.JTextPane();
        serverIn = new javax.swing.JTextField();
        chatPane = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        chat = new javax.swing.JTextPane();
        time = new javax.swing.JLabel();
        connectionStatus = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuConnect = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        menuIdentity = new javax.swing.JMenuItem();
        menuNastaveni = new javax.swing.JMenuItem();

        jRadioButton1.setText("jRadioButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        turnServerOn.setText("Zapni server");
        turnServerOn.setFocusable(false);
        turnServerOn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        turnServerOn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        turnServerOn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                turnServerOnActionPerformed(evt);
            }
        });
        jToolBar1.add(turnServerOn);

        message.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                messageKeyPressed(evt);
            }
        });

        emotikon.setText(":)");
        emotikon.setBorderPainted(false);

        jSplitPane1.setDividerLocation(260);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setContinuousLayout(true);

        jScrollPane1.setViewportView(serverChat);

        serverIn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                serverInKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(serverIn, javax.swing.GroupLayout.DEFAULT_SIZE, 783, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(serverIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        serverPane.addTab("Žádný server", new javax.swing.ImageIcon(getClass().getResource("/icons/icon16px.png")), jPanel2); // NOI18N

        jSplitPane1.setTopComponent(serverPane);

        chatPane.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        chat.setEditable(false);
        jScrollPane2.setViewportView(chat);

        chatPane.addTab("Žádný server", jScrollPane2);
        jScrollPane2.getAccessibleContext().setAccessibleName("");

        jSplitPane1.setRightComponent(chatPane);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(message)
                .addGap(0, 0, 0)
                .addComponent(emotikon))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(message, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emotikon))
                .addGap(0, 0, 0))
        );

        time.setText("17:24");

        connectionStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/minus_16px.png"))); // NOI18N
        connectionStatus.setText("Disconnected");

        jMenu1.setText("Připojení");

        menuConnect.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        menuConnect.setText("Připojit");
        menuConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuConnectActionPerformed(evt);
            }
        });
        jMenu1.add(menuConnect);

        menuBar.add(jMenu1);

        jMenu2.setText("Permise");
        menuBar.add(jMenu2);

        jMenu3.setText("Nástroje");
        menuBar.add(jMenu3);

        jMenu4.setText("Nastavení");

        menuIdentity.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        menuIdentity.setText("Identity");
        jMenu4.add(menuIdentity);

        menuNastaveni.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK));
        menuNastaveni.setText("Nastavení");
        jMenu4.add(menuNastaveni);

        menuBar.add(jMenu4);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(connectionStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(time)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(time)
                    .addComponent(connectionStatus)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void turnServerOnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_turnServerOnActionPerformed
        if(server != null){
            server.vypni();
            server = null;
            return;
        }
        serverPane.setTitleAt(0, "Server");
        server = new Server(1500, new VypisChatu(serverChat));
        server.start();
    }//GEN-LAST:event_turnServerOnActionPerformed

    private void messageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_messageKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER && !message.getText().equals("")){
            mojePripojeni.posliZpravu(message.getText());
            message.setText("");            
        }
    }//GEN-LAST:event_messageKeyPressed

    private void menuConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuConnectActionPerformed
        pripojitOkno.setVisible(true);
    }//GEN-LAST:event_menuConnectActionPerformed

    private void serverInKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_serverInKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER && server!=null && !serverIn.getText().equals("")){
            server.broadcast(new ChatMessage(1, serverIn.getText(), "Server"));
            serverIn.setText("");
        }
    }//GEN-LAST:event_serverInKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane chat;
    private javax.swing.JTabbedPane chatPane;
    private javax.swing.JLabel connectionStatus;
    private javax.swing.JButton emotikon;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem menuConnect;
    private javax.swing.JMenuItem menuIdentity;
    private javax.swing.JMenuItem menuNastaveni;
    private javax.swing.JTextField message;
    private javax.swing.JTextPane serverChat;
    private javax.swing.JTextField serverIn;
    private javax.swing.JTabbedPane serverPane;
    private javax.swing.JLabel time;
    private javax.swing.JButton turnServerOn;
    // End of variables declaration//GEN-END:variables

}