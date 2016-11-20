package qbox.chat;

import java.awt.Color;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Ondřej Bleha
 */
public class VypisChatu {
        
    private final JTextPane chat;
    private final StyledDocument doc;
    // to zobraz time
    private final SimpleDateFormat formatCasu;
    
    //barvicky
    Color infoBarva = new Color(0,47,93);
    Color lightBlue = new Color(66,96,128);
    Color chybaBarva = new Color(203,17,22);
    Color textBarva = Color.BLACK;
    
    //styly
    SimpleAttributeSet normal;
    SimpleAttributeSet vitej;
    SimpleAttributeSet chyba;
    SimpleAttributeSet chybaBold;
    SimpleAttributeSet info;
    SimpleAttributeSet infoBold;
    SimpleAttributeSet nameBold;

    
        
    
    public VypisChatu(JTextPane chat){
        this.chat = chat;
        DefaultCaret caret = (DefaultCaret)chat.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.doc = (StyledDocument) this.chat.getDocument();
        formatCasu = new SimpleDateFormat("HH:mm:ss");
        
        
        
        //styly
        normal = new SimpleAttributeSet();
            StyleConstants.setFontFamily(normal, "SansSerif");
            StyleConstants.setFontSize(normal, 12);
        vitej = new SimpleAttributeSet(normal);
            StyleConstants.setForeground(vitej, new Color(7,67,24));
        chyba = new SimpleAttributeSet(normal);
            StyleConstants.setForeground(chyba, chybaBarva);
        chybaBold = new SimpleAttributeSet(chyba);
            StyleConstants.setBold(chybaBold, true);
        info = new SimpleAttributeSet(normal);
            StyleConstants.setForeground(info, new Color(77,111,141));
        infoBold = new SimpleAttributeSet(info);
            StyleConstants.setBold(infoBold, true);
        nameBold = new SimpleAttributeSet(normal);
            StyleConstants.setForeground(nameBold, new Color(0,47,93));
            StyleConstants.setBold(nameBold, true);
            //chat.insertIcon(new ImageIcon ("/home/bleha/Dokumenty/grafika/inkscape/Qbox/icon16px.png" ));
        
    }
    
    private String aktualniCas(){
        //chat.insertIcon(new ImageIcon ("/home/bleha/Dokumenty/grafika/inkscape/Qbox/icon16px.png" ));
        return formatCasu.format(new Date());
    }
    
    public void clear(){
        chat.setText("");
    }
    
    private void printCas(){
        print(String.format("<%s> ", aktualniCas()), info);
    }
    
    public void printZprava(ChatMessage msg) {
        if(chat != null){
            printCas();
            print(String.format("\"%s\": " , msg.getOdesilatel()), nameBold);
            //print(msg.getMessage()+"\n", text);
            printFormatovanaZprava(msg.getMessage()+"\n");
        }
    }
    
    public void printText(String s){
        if(chat != null){
            printCas();
            printFormatovanyText(s+"\n");
        }
    }
    
    private void print(String text, SimpleAttributeSet styl){
        try {
            doc.insertString(doc.getLength(), text, styl);
        } catch (BadLocationException ex) {
            System.out.println("nepodarilo se vypsat text!");
        }
    }
    
    public JTextPane getTextPane(){
        return chat;
    }
    
    private void printFormatovanaZprava(String s){
        SimpleAttributeSet styl = new SimpleAttributeSet(normal);
        ArrayList<String> tagy = rozdelNaTagy(s);
        
        for (String prvek : tagy) {
            switch(prvek){
                case "<b>":
                    StyleConstants.setBold(styl, true);
                    break;
                case "</b>":
                    StyleConstants.setBold(styl, false);
                    break;
                case "<i>":
                    StyleConstants.setItalic(styl, true);
                    break;
                case "</i>":
                    StyleConstants.setItalic(styl, false);
                    break;
                case "<u>":
                    StyleConstants.setUnderline(styl, true);
                    break;
                case "</u>":
                    StyleConstants.setUnderline(styl, false);
                    break;
                case "</color>":
                    StyleConstants.setForeground(styl, Color.BLACK);
                    break;
                default:
                    if(prvek.startsWith("<color=")){
                        try{
                            String color = prvek.substring(prvek.indexOf('=')+1, prvek.length()-1);
                            StyleConstants.setForeground(styl, Color.decode(color));
                        }catch(Exception e){}
                    }else{
                        print(prvek, styl);
                    }
                    break;
            }
        }
    }
    
    private void printFormatovanyText(String s){
        SimpleAttributeSet styl = new SimpleAttributeSet(normal);
        ArrayList<String> tagy = rozdelNaTagy(s);
        
        for (String prvek : tagy) {
            switch(prvek){
                case "<text>":
                    styl = normal;
                    break;
                case "<vitej>":
                    styl = vitej;
                    break;
                case "<chyba>":
                    styl = chyba;
                    break;
                case "<chybaBold>":
                    styl = chybaBold;
                    break;
                case "<info>":
                    styl = info;
                    break;
                case "<infoBold>":
                    styl = infoBold;
                    break;
                case "<nameBold>":
                    styl = nameBold;
                    break;
                case "</color>":
                    StyleConstants.setForeground(styl, Color.BLACK);
                    break;
                default:
                    if(prvek.startsWith("<color=")){
                        try{
                            String color = prvek.substring(prvek.indexOf('=')+1, prvek.length()-1);
                            StyleConstants.setForeground(styl, Color.decode(color));
                        }catch(Exception e){}
                    }else{
                        print(prvek, styl);
                    }
                    break;
            }
        }
    }
    
    private ArrayList<String> rozdelNaTagy(String s){
        ArrayList<String> tagy = new ArrayList();
        StringBuilder tag = new StringBuilder();
        
        for (int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            switch(c){
                case '<':
                    if(tag.length() != 0){
                        tagy.add(tag.toString());
                        tag.delete(0, tag.length());
                    }
                    tag.append(c);
                    break;
                case '>':
                    tag.append(c);
                    tagy.add(tag.toString());
                    tag.delete(0, tag.length());
                    break;
                default:
                    tag.append(c);
                    break;
            }
        }
        tagy.add(tag.toString());
        tag.delete(0, tag.length());
        
        return tagy;
    }
    
}
