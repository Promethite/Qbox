/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qbox.chat;

import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

/**
 *
 * @author bleha
 */
public class MojePripojeni {
    
    private final ArrayList<Klient> klienti;
    private final JTabbedPane chatPane;
    
    public MojePripojeni(JTabbedPane chatPane){
        klienti = new ArrayList<>();
        this.chatPane = chatPane;
    }
    
    public void addKlient(Klient klient){
        klienti.add(klient);
    }
    
    public Klient getKlient(int index){
        return klienti.get(index);
    }
    
    public void removeKlient(int index){
        JScrollPane scroll = (JScrollPane) chatPane.getComponentAt(index);
        for(int i = 0; i<klienti.size(); i++){            
            if(klienti.get(i).getTextPane().getParent() == scroll.getComponent(0)){
                chatPane.remove(index);
                klienti.remove(i);
            }
        }
    }
    
    public void novyKlient(String adresa, String nickname, String heslo){
        JTextPane chat = null;
        if(chatPane.getTabCount() == 1 && chatPane.getTitleAt(0).equals("Žádný server")){
            JScrollPane scroll = (JScrollPane) chatPane.getComponentAt(0);
            chat = (JTextPane) scroll.getViewport().getComponent(0); 
            chatPane.setTitleAt(0, nickname);
        }else{
            chat = new JTextPane();
            chat.setEditable(false);
            JScrollPane scroll = new JScrollPane(chat);        
            chatPane.addTab(nickname, scroll);
        }
        Klient klient = new Klient(adresa, 1500, nickname, new VypisChatu(chat));
        klienti.add(klient);
        klient.start();
    }
    
    public void posliZpravu(String zprava){
        JScrollPane scroll = (JScrollPane) chatPane.getSelectedComponent();
        for(Klient klient : klienti){            
            if(klient.getTextPane().getParent() == scroll.getComponent(0)){
                if(zprava.equals("reconnect")){
                    klient.start();
                    return;
                }else if(zprava.equals("exit")){
                    removeKlient(chatPane.getSelectedIndex());
                    return;
                }else{
                    klient.sendMessage(new ChatMessage(1, zprava));
                    return;
                }
            }
        }
    }
    
}
