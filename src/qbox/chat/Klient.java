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
import java.net.Socket;
import java.text.SimpleDateFormat;
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
public class Klient {

    // pro komunikaci
    private ObjectInputStream sInput;		// pro cteni ze socketu
    private ObjectOutputStream sOutput;		// pro psani do socketu
    private Socket socket;

    // graficke komponenty
    private final VypisChatu chat;

    // server, port a prihlasovaci jmeno
    private final String server;
    private final String nickname;
    private final int port;

    /*
     * konstruktor
     */
    public Klient(String server, int port, String nickname, VypisChatu chat) {
        this.server = server;
        this.port = port;
        this.nickname = nickname;
        this.chat = chat;
    }

    /*
     * To start the dialog
     */
    public boolean start() {
        chat.printText(String.format("<info>Pokouším se připojit k serveru <infoBold>%s", server));
        
        // pokusi se propojit k serveru
        try {
            socket = new Socket(server, port);
        }
        
        // pokud spojeni selze
        catch(Exception ec) {
            chat.printText("<chybaBold>Chyba při připojování k serveru: " + ec);
            return false;
        }

        chat.printText(String.format("<info>Spojení navázáno <infoBold>%s:%s", socket.getInetAddress(), socket.getPort()));

        /* vytváření datových proudů */
        try{
            sInput  = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        }catch (IOException eIO) {
            chat.printText("<chybaBold>Chyba při vytváření datových proudů: " + eIO);
            return false;
        }

        // vytvori vlakno pro naslouchani serveru
        new ListenFromServer().start();
        
        // posle nase uzivatelske jmeno serveru, toto je zaroven jedina zprava
        // kterou posilame jako String. Ostatni zpravy budou ChatMessage objekty
        try{
            sOutput.writeObject(new ChatMessage(-1, "Q4566ZU6TGGTR", nickname));
        }catch (IOException eIO) {
            chat.printText("<chybaBold>Chyba při přihlašování : " + eIO);
            disconnect();
            return false;
        }
        
        // uspech -> informujeme volajiciho metody o uspesnosti
        return true;
    }
    
    public JTextPane getTextPane(){
        return chat.getTextPane();
    }

    /*
     * posle zpravu serveru
     */
    public void sendMessage(ChatMessage msg) {
        msg.setOdesilatel(this.nickname);
        try {
            sOutput.writeObject(msg);
        }catch(IOException e) {
            chat.printText("<chybaBold>Chyba při odesílání zprávy serveru: " + e);
        }
    }

    /*
     * pokud se neco nepovede
     * zavre datove proudy a odpoji se
     */
    private void disconnect() {
        try {
            if(sInput != null) sInput.close();
        }catch(Exception e) {} // nic moc jineho nelze delat
        
        try {
            if(sOutput != null) sOutput.close();
        }catch(Exception e) {} // nic moc jineho nelze delat
        
        try{
            if(socket != null) socket.close();
        }catch(Exception e) {} // nic moc jineho nelze delat

        // informuje GUI
        if(chat != null){
            //chat.connectionFailed();
        }

    }

    /*
     * trida, ktera ceka na zpravu od serveru a posila ji dal jako parametr funkce zobraz
     */
    class ListenFromServer extends Thread {

        @Override
        public void run() {
            while(true) {
                try {
                    ChatMessage msg = (ChatMessage) sInput.readObject();
                    // if console mode print the message and add back the prompt
                    if(chat != null) {
                        chat.printZprava(msg);
                    }
                }catch(IOException e) {
                    chat.printText("<chybaBold>Server ukončil spojení: " + e);
                    if(chat != null){
                        //chat.connectionFailed();
                    }
                    break;
                }
                
                // can't happen with a String object but need the catch anyhow
                catch(ClassNotFoundException e2) {
                }
            }
        }
    }
}
