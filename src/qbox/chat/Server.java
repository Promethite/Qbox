/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qbox.chat;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author bleha
 */
public class Server extends Thread{
    
    // a unique ID for each connection
    private static int uniqueId;
    // an ArrayList to keep the list of the Client
    private final ArrayList<VlaknoKlienta> al;
    // chat
    private final VypisChatu chat;
    // to zobraz time
    private final SimpleDateFormat sdf;
    // the port number to listen for connection
    private final int port;
    // the boolean that will be turned of to stop the server
    private boolean keepGoing;

    /*
     *  konstruktor
     */
    public Server(int port, VypisChatu chat) {
            // chat
            this.chat = chat;
            // the port
            this.port = port;
            // to zobraz hh:mm:ss
            sdf = new SimpleDateFormat("HH:mm:ss");
            // ArrayList for the Client list
            al = new ArrayList<VlaknoKlienta>();
    }

    @Override
    public void run() {
        keepGoing = true;
        
        /* vytvori socket a ceka na pozadavky na pripojeni */
        try{
            // socket pouzivany serverem
            ServerSocket serverSocket = new ServerSocket(port);

            // zobrazi zpravu, ze se ceka na pripojeni
            chat.printText(String.format("<info>Server čeká na klienty na portu <infoBold>%s <info>.", port));
            
            // nekonecna smycka cekajici na pripojeni
            while(keepGoing) 
            {
                Socket socket = serverSocket.accept();  // potvrdi pripojeni
                // pokud jsem byl vyzvan k ukonceni
                if(!keepGoing){
                    break;
                }
                VlaknoKlienta t = new VlaknoKlienta(socket);  // udela z toho vlakno
                al.add(t);  // ulozi vlakno do arrayListu
                t.start();
            }
            // vyzvani k ukonceni
            try{
                serverSocket.close();
                for(int i = 0; i < al.size(); ++i) {
                    VlaknoKlienta tc = al.get(i);
                    try {
                        tc.sInput.close();
                        tc.sOutput.close();
                        tc.socket.close();
                    }catch(IOException ioE) {
                            // not much I can do
                    }
                }
            }catch(Exception e) {
                chat.printText("<chybaBold>Chyba při zavírání serveru a odpojování klientů: " + e);
            }
        }
        
        // neco se stalo spatne
        catch (IOException e) {
            chat.printText(String.format("<chybaBold>Chyba na novém ServerSocket: %s", e));
        }
    }		
    /*
     * For the GUI to stop the server
     */
    public void vypni() {
        keepGoing = false;
        
        // connect to myself as Client to exit statement
        // Socket socket = serverSocket.accept();
        try {
            Socket socket = new Socket("localhost", port);
            chat.printText("<chybaBold>Server byl vypnut.");
        }catch(Exception e) {
            // neni nic, co bych s tim mohl udelat
        }
    }
    
    /*
     *  posle zpravu vsem klientum
     */
    public synchronized void broadcast(ChatMessage message) {
        
        // zobraz zpravu do GUI
        if(chat != null){
            chat.printZprava(message);
        }
        
        //prochazima pozpatku kvuli moznemu odebrani klientu
        for(int i = al.size(); --i >= 0;) {
            VlaknoKlienta ct = al.get(i);
            
            // zkus napsat klientovy, pokud se nepovede, tak ho odstran ze seznamu
            if(!ct.napisZpravu(message)) {
                al.remove(i);
                chat.printText(String.format("<nameBold>\"%s\" <chyba>dropped (connection lost)", ct.username));
            }
        }
    }

    // pro klienty, kteri se radne odpojili pomoci zpravy
    synchronized void remove(int id) {
        // projde seznam, dokud nenajde ID
        for(int i = 0; i < al.size(); ++i) {
            VlaknoKlienta ct = al.get(i);
            // ID nalezeno
            if(ct.id == id) {
                al.remove(i);
                return;
            }
        }
    }


    /** jedna instance vlakna pobezi pro kazdeho klienta */
    class VlaknoKlienta extends Thread {
        
        // socket pro komunikaci
        Socket socket;
        ObjectInputStream sInput;
        ObjectOutputStream sOutput;
        
        // moje unikatni id (pro jednodusi odpojeni)
        int id;
        
        // jmeno klienta
        String username;
        
        // the only type of message a will receive
        ChatMessage cm;
        
        // cas pripojeni
        String date;

        // konstruktor
        VlaknoKlienta(Socket socket) {
            // unikatni id
            id = ++uniqueId;
            this.socket = socket;
            /* vytvareni datovych proudu */
            try{
                // create output first
                sOutput = new ObjectOutputStream(socket.getOutputStream());
                sInput  = new ObjectInputStream(socket.getInputStream());
                
                // read the username
                ChatMessage first = (ChatMessage) sInput.readObject();
                username = validateUsername(first);
                
                
                chat.printText(String.format("<nameBold>\"%s\" <info>se právě připojil ", username));
            }catch (IOException e) {
                chat.printText("<chybaBold>Chyba při vytváření nového vstupního/výstupního proudu: " + e);
                return;
            }

            // have to catch ClassNotFoundException
            // but I read a String, I am sure it will work
            catch (ClassNotFoundException e) {
                // nedela nic
            }
            date = new Date().toString() + "\n";
        }
        
        private String validateUsername(ChatMessage zprava){
                boolean equals = false;
                for(VlaknoKlienta vk:al){
                    if(vk.getUsername().equals(zprava.getOdesilatel())){
                        equals = true;
                    }
                }
                if(equals){
                    remove(id);
                    ukonciSpojeni();
                    chat.printText("<chybaBold>Někdo se pokusil připojit pod použitým jménem.");
                    return "chybny klient";
                }else{
                    return zprava.getOdesilatel();
                }
        }
        
        public String getUsername(){
            return username;
        }

        // to co pobezi porad
        @Override
        public void run() {
            // zajisti beh do odhlaseni
            boolean keepGoing = true;
            while(keepGoing) {
                
                // read a String (which is an object)
                try {
                    cm = (ChatMessage) sInput.readObject();
                }catch (IOException e) {
                    chat.printText(String.format("<nameBold>\"%s\" <chyba>Exception reading Streams: <chybaBold>%s", username, e));
                    break;				
                }catch(ClassNotFoundException e2) {
                    break;
                }
                
                // the messaage part of the ChatMessage
                String message = cm.getMessage();

                // Switch on the type of message receive
                switch(cm.getType()) {
                    case ChatMessage.MESSAGE:
                        cm.setOdesilatel(username);
                        broadcast(cm);
                    break;
                    case ChatMessage.LOGOUT:
                        chat.printText(String.format("<nameBold>\"%s\" <chyba>disconnected with a LOGOUT message", username));
                        keepGoing = false;
                    break;
                    case ChatMessage.WHOISIN:
                        napisZpravu(new ChatMessage(1, "List of the users connected at " + sdf.format(new Date()) + "\n"));
                        // scan al the users connected
                        for(int i = 0; i < al.size(); ++i) {
                            VlaknoKlienta ct = al.get(i);
                            napisZpravu(new ChatMessage(1, (i+1) + ") " + ct.username + " since " + ct.date));
                        }
                    break;
                }
            }
            
            // odstrani klienta ze seznamu pripojenych klientu
            remove(id);
            ukonciSpojeni();
        }

        // zkus zavrit vse
        private void ukonciSpojeni() {
            // zkus ukoncit spojeni
            try {
                if(sOutput != null) sOutput.close();
            }catch(Exception e) {}
            
            try {
                if(sInput != null) sInput.close();
            }catch(Exception e) {}
            
            try {
                if(socket != null) socket.close();
            }catch (Exception e) {}
        }

        /*
         * Napise String do klientova output streamu
         */
        private boolean napisZpravu(ChatMessage zprava) {
            // pokud je klient pripojen, posli mu zpravu
            if(!socket.isConnected()) {
                ukonciSpojeni();
                return false;
            }
            // napis zpravu do proudu
            try {
                sOutput.writeObject(zprava);
            }
            // pokud se objevi chyba, informuj uzivatele
            catch(IOException e) {
                chat.printText(String.format("<ChybaBold>Chyba při odesílání zprávy uživateli <userBold>\"%s\": <chybaBold>%s", username, e));
            }
            return true;
        }
    }
    
    

}