/** Place.java
 *  @author Kvita Jakub
 */
package pnet;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;

import org.dom4j.Element;
import configuration.*;

/**
 * Trida pro mista v siti.
 * @author Kvita Jakub
 */
public class Place extends Point implements NetItem{
	
	private static final long serialVersionUID = 1L;
	
	private String text="";
	
	private int circSize=NetConf.getSizeOfCircle();
	
	/**
	 * Vytvori nove misto na pozici zadane parametry. Nic neosetruje, vse se musi predzpracovat ve volajici funkci.
	 * @param x X-ova souradnice pozice.
	 * @param y Y-ova souradnice pozice.
	 */
	public Place(int x, int y) {
		super(x, y);
	}
	
	//pokud ten objekt je na zadanych souradnicich
	public boolean isThere(int x,int y){
		
		RectangularShape object=new Ellipse2D.Double(this.x-circSize/2, this.y-NetConf.getSizeOfCircle()/2, circSize, NetConf.getSizeOfCircle());
		
		if(object.intersects(x, y, 0.01, 0.01))
			return true;
		else
			return false;
	}
	
	/**
	 * Spocita sirku kruhu v dane grafice i s textem ktery je vevnitr aby se kruh mohl nafukovat.
	 * @param g Zadana grafika
	 * @return Sirka v pixelech.
	 */
	public int CircleWidth(Graphics g){
        Font font = new Font(NetConf.getFont(), Font.PLAIN, NetConf.getSizeOfText());
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
		
		if(NetConf.getSizeOfCircle() < metrics.stringWidth(text))
			return metrics.stringWidth(text)+3;
		else
			return NetConf.getSizeOfCircle();
	}
	
	//nakresli objekt
	public void paintItem(Graphics g, Color c){
		
		circSize=CircleWidth(g);
			
		//kruh
    	g.setColor(c);
    	g.fillOval(this.x-circSize/2, this.y-NetConf.getSizeOfCircle()/2, circSize, NetConf.getSizeOfCircle());
    	g.setColor(Color.BLACK);
    	g.drawOval(this.x-circSize/2, this.y-NetConf.getSizeOfCircle()/2, circSize, NetConf.getSizeOfCircle());
		
    	//text
        Font font = new Font(NetConf.getFont(), Font.PLAIN, NetConf.getSizeOfText());
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
    	
    	g.drawString(text, this.x-metrics.stringWidth(text)/2, this.y+NetConf.getSizeOfText()/3);
	}
	
	
	//jesli je objekt vazany na neco jineho
	public boolean isBounded(NetItem i){
		return false;
	}
	
	//premisteni objektu
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