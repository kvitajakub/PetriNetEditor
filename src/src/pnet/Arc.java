/** Arc.java
 *  @author Kvita Jakub
 */
package pnet;

import pnet.Place;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.*;
import org.dom4j.Element;
import configuration.*;

/**
 * Trida pro hranu v siti.
 * @author Kvita Jakub
 */
public class Arc implements NetItem{
	
	private String text="";
	private NetItem start;
	private NetItem target;
	
	/**
	 * Vytvori novou hranu z objekty na konci. Nic neosetruje, vse se musi zaridit na miste volani.
	 * @param start  Pocatecni objekt(misto nebo prechod). Musi existovat!
	 * @param target Koncovy objekt(misto nebo prechod). Musi existovat!
	 */
	public Arc(NetItem start, NetItem target){
		this.start=start;
		this.target=target;
	}
	
	
	public boolean isThere(int x, int y){
		//vzorec pocita vzdalenost bodu od usecky dane souradnicemi pocatecniho a ciloveho objektu
		//rozptyl od usecky muze byt az maxdist pixelu
		double maxdist=5.0;
		
		int x1=((Point)start).x;
		int x2=((Point)target).x;
		int y1=((Point)start).y;
		int y2=((Point)target).y;
		
		
		int xcenter=(x1+x2)/2;
		int xdeviation=Math.abs(xcenter-x1);
		int ycenter=(y1+y2)/2;
		int ydeviation=Math.abs(ycenter-y1);
		
		if(xdeviation < 4)
			xdeviation = 4;
		if(ydeviation < 4)
			ydeviation = 4;
		
		//pokud je v obdelniku danem useckou(uhlopricka)
		//a pokud je vydalenost bodu od usecky mensi nez maxdist
		if( x >= (xcenter-xdeviation) &&
			x <= (xcenter+xdeviation) &&
			y >= (ycenter-ydeviation) &&
			y <= (ycenter+ydeviation) &&
			Line2D.ptLineDist(x1, y1, x2, y2, x, y)<maxdist)
			
			return true;
			
		return false;
	}
	
	public void paintItem(Graphics g, Color c){
		
		g.setColor(c);			
		
		int a1=((Point)start).x;   //pocatecni bod
		int a2=((Point)start).y;   //pocatecni bod
		int b1=((Point)target).x;  //koncovy bod
		int b2=((Point)target).y;  //koncovy bod
		
		Point p1;  //pocatecni bod
		Point p2;  // koncovy bod 
		
		RectangularShape object;
		
		//udelam si objekt
		if(start instanceof Place)
			object=new Ellipse2D.Double(a1-((Place)start).CircleWidth(g)/2, a2-NetConf.getSizeOfCircle()/2, ((Place)start).CircleWidth(g), NetConf.getSizeOfCircle());
		else
			object=new Rectangle2D.Double(a1-((Transition)start).RectWidth(g)/2,a2-((Transition)start).RectHeight(g)/2,((Transition)start).RectWidth(g),((Transition)start).RectHeight(g));
		
		//pocitam prusecik
		p1=getIntersect((Point)target,(Point)start,object);
		
		//udelam druhy objekt
		if(target instanceof Place)
			object=new Ellipse2D.Double(b1-((Place)target).CircleWidth(g)/2, b2-NetConf.getSizeOfCircle()/2, ((Place)target).CircleWidth(g), NetConf.getSizeOfCircle());
		else
			object=new Rectangle2D.Double(b1-((Transition)target).RectWidth(g)/2, b2-((Transition)target).RectHeight(g)/2,((Transition)target).RectWidth(g),((Transition)target).RectHeight(g));
		
		//pocitam druhy prusecik
		p2=getIntersect((Point)start,(Point)target,object);
		
	//mam spocitane souradnice p1, p2
		
		drawArrow(g,p1.x,p1.y,p2.x,p2.y);
		
		drawString(g,c,text,(int)(p1.x+p2.x)/2, (int)(p1.y+p2.y)/2);
		
	}
	
	//vola se z paintItem()
	private Point getIntersect(Point p1, Point p2, RectangularShape object){
		//Pocitani bodu ktery na usecce protina nejaky objekt
		//  pracuje s parametrickou rovnici usecky (parametr t od 0 do 1)
		//  pocita numerickou metodou pulenim intervalu
		//  objekt je v bode p2
		//POZOR neni obecne reseni
		
		double t=0.5;    //parametr rovnice usecky/primky
		double cycle=0.5;   //pro cyklus puleni intervalu
		
		double x=0;
		double y=0;
		
		Point ret = new Point();  // vysledny bod
				
		while(cycle>0.001){
			
			cycle/=2;
			
			x=p1.x+t*(p2.x-p1.x);
			y=p1.y+t*(p2.y-p1.y);
			
			if(!object.intersects(x, y, 0.01, 0.01)){
				t+=cycle;
			}
			else{
				t-=cycle;
			}
		}
		
		ret.x=(int)x;
		ret.y=(int)y;
		
		return ret;
	}
	
	
	//namaluje sipku - vola se z paintItem
	//kostra prevzata ze STACK OVERFLOW
   private void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
    	
        Graphics2D g = (Graphics2D) g1.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        
        if(NetConf.isArrowFilled()){
            //vykresli sip
            g.fillPolygon(new int[] {len, len-NetConf.getArrowSize(), len-NetConf.getArrowSize(), len},
                          new int[] {0  , -NetConf.getArrowSize()   , NetConf.getArrowSize()    , 0  }, 4);
        }
        else{
	        //vykresli sipku
	        g.drawLine(len, 0, len-NetConf.getArrowSize(), -NetConf.getArrowSize());
	        g.drawLine(len, 0, len-NetConf.getArrowSize(), NetConf.getArrowSize());
        }
    }
	
    //vykresli text na souradnici
   	private void drawString(Graphics g,Color c, String str,int x,int y){
    	
   		
   		if(str == null || str.equals(""))
   			return;
   		
   		
        Font font = new Font(NetConf.getFont(), Font.PLAIN, NetConf.getSizeOfText());
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();

        g.setColor(Color.BLACK);
        
        int rx1 = x-3-metrics.stringWidth(str)/2;
        int ry1 = y-2-(2*metrics.getHeight()/3);
        int width = metrics.stringWidth(str)+6;
        int height = metrics.getHeight();
        
        g.drawRect(rx1, ry1, width, height);
        
        if(c==NetConf.getColorAct())
        	g.setColor(c);
        else
        	g.setColor(NetConf.getColorRest());
       
        g.fillRect(rx1+1, ry1+1, width-1, height-1);
        
        g.setColor(Color.BLACK);
    	g.drawString(text,x-metrics.stringWidth(str)/2, y);
    	
    }
    
	public boolean isBounded(NetItem i){
		if(i == start || i == target)
			return true;
		return false;
	}
	
	public String getText(){
		return text;
	}
	
	public void setText(String x){
		text=x;
	}

	/**
	 * Hrany se hybat nemohou, metoda je prazdna.
	 */
	public void relocate(int x,int y){
	}

	
	public void fillElement(Element e) {
		
	Element s = e.addElement("start");
		s.addAttribute("x",Integer.toString(((Point)this.start).x));
		s.addAttribute("y",Integer.toString(((Point)this.start).y));

		s = e.addElement("target");
		s.addAttribute("x",Integer.toString(((Point)this.target).x));
		s.addAttribute("y",Integer.toString(((Point)this.target).y));
			
		e.addElement("text")
			.addText(text);
	}	
	
	/**
	 * Vrati pocatecni objekt hrany.
	 * @return Vraceny objekt.
	 */
	public NetItem getStart() {
		return start;
	}

	
	/**
	 * Vrati koncovy objekt hrany.
	 * @return raceny objekt.
	 */
	public NetItem getTarget() {
		return target;
	}
}

//EOF