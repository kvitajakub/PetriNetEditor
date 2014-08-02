/**
 * MyPanel.java
 * @author Kvita Jakub
 */
package client;

import javax.swing.*;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.io.SAXReader;
import org.dom4j.DocumentException;

import client.MEv;

import java.awt.*;
import java.awt.event.*;
import pnet.PetriNet;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import except.FalseXML;
import configuration.*;

/**
 * Trida pro zobrazeni panelu ve kterem je sit.
 * @author Kvita Jakub
 */
public class MyPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private MainGui frame;
	private PetriNet net=new PetriNet();
	private File file=null; //odkud nebo kam ukladam sit
	private String servername=null;

	private int x;
	private int y;
	
	/**
	 * Vytvoreni objektu, vytvoreni posluchacu udalosti pro mys.
	 * @param frame Okno ve kterem se bude panel zobrazovat.
	 */
	public MyPanel(MainGui frame) {
		super();
		this.frame=frame;
		
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                processMouse(MEv.PRESSED,e);
            }
            public void mouseReleased(MouseEvent e) {
	                processMouse(MEv.RELEASED,e);
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter(){
        	public void mouseDragged(MouseEvent e) {
	            	processMouse(MEv.DRAGGED,e);
        	}
        });
        
	}
	
	private void processMouse(MEv type, MouseEvent e){
		//zpracuje pohyb mysi v panelu
		// type - vyctovy typ jaka udalost se s mysi stala
		// e - cely event, hlavne kvuli pozici		
		switch(type){
			case PRESSED:
					switch(frame.button){
						case ARC:
								x=e.getX();
								y=e.getY();
								net.setAct(e.getX(),e.getY());
							break;
						case TRANS:
							net.addTrans(e.getX(), e.getY());
							break;
						case PLACE:
								net.addPlace(e.getX(), e.getY());
							break;
						case DELETE:
								net.setAct(e.getX(),e.getY());
								net.deleteAct();
							break;
						case SELECT:
						case INSERT:
						default:
								net.setAct(e.getX(),e.getY());
							break;
					}
				break;
			case DRAGGED:
					switch(frame.button){
						case ARC:
						case DELETE:
							
							break;
						case TRANS:
						case PLACE:
						case SELECT:
						case INSERT:
						default:
								net.relocateAct(e.getX(), e.getY());
							break;
					}
				break;
			case RELEASED:
					switch(frame.button){
						case ARC:
							net.addArc(x, y, e.getX(), e.getY());
							break;
						case TRANS:
		
							break;
						case PLACE:
		
							break;
						case DELETE:
		
							break;
						case SELECT:
						case INSERT:
						default:
		
							break;
					}
				break;
		}
		
		//na konci vsechno prekreslim
		repaint();
	}
	
	/**
	 * Nastavi text v aktivnim prvku.
	 * @param str Text na ktery se ma hodnota zmenit.
	 */
	public void setActiveText(String str){
		net.setActText(str);
		repaint();
	}
	
	
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);       
        
        this.setBackground(NetConf.getColorBack());
        net.paintNet(g);
    }  
	
	
    /**
     * Konvertuje sit do Document.
     * @return Document ktery obsahuje sit.
     */
	public Document toDoc(){	
		return net.toDoc();
	}
	
	/**
	 * Nahraje sit z dokumentu.
	 * @param doc
	 * @throws DocumentException 
	 * @throws FalseXML 
	 */
	public void fromDoc(Document doc) throws FalseXML, DocumentException{
		net.fromDoc(doc);
	}
    
	/**
	 * Soubor ve kterem je sit ulozena pomoci Save.
	 * @return Cilovy soubor.
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * Jmeno site ktere ma na serveru.
	 * @return Jmeno site.
	 */
	public String getServername() {
		return servername;
	}
	
	/**
	 * Nastavime jake chceme aby sit mela jmeno na sereru.
	 * @param servername Jmeno site.
	 */
	public void setServername(String servername) {
		this.servername = servername;
	}

	/**
	 * Nastavi novy soubor kde je sit.
	 * @param file Novy soubor.
	 */
	public void setFile(File file) {
		this.file = file;
	}
	
	/**
	 * Ulozi sit do souboru {@link #file}
	 */
	public void save(){
		
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer=null;
		try {
			
			Document xml = toDoc();
			writer = new XMLWriter(new FileWriter(file), format );
			writer.write( xml );
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Nahraje sit ze souboru file do sebe.
	 * @throws FalseXML  Pri parsovani spatneho xml.
	 */
	public void load() throws FalseXML{
		
		//pokud nemam soubor tak se ani o nic nesnazim
		if(file == null)
			return;
		
	    SAXReader xmlReader = new SAXReader();
	    Document doc;
	    try {
			doc = xmlReader.read(file);
			net.fromDoc(doc);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new FalseXML();
		}
	}
	
}

//EOF