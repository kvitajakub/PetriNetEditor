/** Transition.java
 *  @author Kvita Jakub
 */
package pnet;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import org.dom4j.Element;
import configuration.*;

/**
 * Trida pro prechody v siti.
 * @author Kvita Jakub
 */
public class Transition extends Point implements NetItem{

	private static final long serialVersionUID = 1L;
	
	private String text="";

	private int rectWidth=NetConf.getSizeOfRectX();
	private int rectHeight=NetConf.getSizeOfRectY();
	
	
	/**
	 * Vytvori novy prechod na pozici zadane parametry. Nic neosetruje, vse se musi predzpracovat ve volajici funkci.
	 * @param x X-ova souradnice pozice.
	 * @param y Y-ova souradnice pozice.
	 */
	public Transition(int x, int y) {
		super(x, y);
	}

	
	public boolean isThere(int x, int y){
		
		if( x>this.x-(rectWidth/2) && x<this.x+(rectWidth/2) && 
			y>this.y-(rectHeight/2) && y<this.y+(rectHeight/2))
			return true;
		else
			return false;
	}
	
	/**
	 * Vrati sirku obdelniku v zadane grafice. s Textem, ktery je uvnitr aby se mohl nafukovat.
	 * @param g Zadana grafika.
	 * @return Vysledna sirka
	 */
	public int RectWidth(Graphics g){
		
        String first="";
        String last="";
        
        int index = text.lastIndexOf('|', text.length());
        
        if(index != -1){
        	first = text.substring(0, index);
        	last  = text.substring(index+1, text.length());
        }
        else{
        	first = text;
        }
        
        return width(g,first,last);
	}
	
	private int width(Graphics g,String first,String last){
      
        Font font = new Font(NetConf.getFont(), Font.PLAIN, NetConf.getSizeOfText());
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
		
		if(NetConf.getSizeOfRectX() < metrics.stringWidth(text))
			return metrics.stringWidth(text)+3;
		else
			return NetConf.getSizeOfRectX();
		
	}
	
	/**
	 * Vrati vysku obdelnika i s textem. Protoze se muze menit font tak se musi zvetsovat.
	 * @param g Zadana grafika.
	 * @return Vysledna vyska.
	 */
	public int RectHeight(Graphics g){
        String first="";
        String last="";
        
        int index = text.lastIndexOf('|', text.length());
        
        if(index != -1){
        	first = text.substring(0, index);
        	last  = text.substring(index+1, text.length());
        }
        else{
        	first = text;
        }
        
        return height(g,first,last);
	}
	
	private int height(Graphics g,String first, String last){
        Font font = new Font(NetConf.getFont(), Font.PLAIN, NetConf.getSizeOfText());
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
		
        if(first.equals("") && last.equals(""))
        	return NetConf.getSizeOfRectY();
        
		if(NetConf.getSizeOfRectY()/2 < metrics.getHeight())
			return metrics.getHeight()*2+3;
		else
			return NetConf.getSizeOfRectY();
	}
	
	
	
	public void paintItem(Graphics g, Color c){
		
		rectWidth = RectWidth(g);
		rectHeight = RectHeight(g);
		
		//obdelnik
        g.setColor(c);
        g.fillRect(this.x-rectWidth/2,this.y-rectHeight/2,rectWidth,rectHeight);
        g.setColor(Color.BLACK);
        g.drawRect(this.x-rectWidth/2,this.y-rectHeight/2,rectWidth,rectHeight);
        g.drawLine(this.x+6-rectWidth/2,this.y,this.x-6+rectWidth/2,this.y);
        
        //text
        Font font = new Font(NetConf.getFont(), Font.PLAIN, NetConf.getSizeOfText());
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        
        String textfirst="";
        String textlast="";
        
        int index = text.lastIndexOf('|', text.length());
        
        if(index != -1){
        	textfirst = text.substring(0, index);
        	textlast  = text.substring(index+1, text.length());
        }
        else{
        	textfirst = text;
        }
        
        int x = this.x-metrics.stringWidth(textfirst)/2;    
        int y = this.y-rectHeight/6;
        
        if(!textfirst.equals(""))
        	g.drawString(textfirst, x, y);
        
        x = this.x-metrics.stringWidth(textlast)/2;
        y = this.y+2*rectHeight/6;
        
        if(!textlast.equals(""))
        	g.drawString(textlast, x, y);
        
	}
	
	public boolean isBounded(NetItem i){
		return false;
	}
	
	public void relocate(int x,int y){
		this.x=x;
		this.y=y;
	}
	
	public String getText(){
		return text;
	}
	
	public void setText(String x){
		text=x;
	}

	public void fillElement(Element e) {
		
		e.addAttribute("x",Integer.toString(this.x));
		e.addAttribute("y",Integer.toString(this.y));
		e.addElement("text")
			.addText(text);
	}
}

//EOF