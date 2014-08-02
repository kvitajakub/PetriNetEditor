/**
 * Conf.java
 * @author Kvita Jakub
 */
package configuration;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import except.FalseXML;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * Trida udrzujici hodnoty nastaveni programu. Vse je uchovano ve statickych promennych.
 * @author Kvita Jakub
 */
public class NetConf {

	//vnitrni konfigurace
	private static final String confFileName = "net.config.xml";
	private static File confFile = null;
	
	
	private static int sizeOfCircle = 75;   // 25 - 500
	private static int sizeOfRectX  = 100;   // 50 - 600
	private static int sizeOfRectY  = 50;   // 25 - 300
	private static int arrowSize    = 10;    //  2 -  50
	
	private static boolean arrowFilled=true;
	
	private static Color colorAct  = Color.ORANGE;
	private static Color colorRest = Color.RED;
	private static Color colorBack = Color.WHITE;
	private static final String[] appTypes = new String[] {"Red","Green","Blue"};
	private static String font    = "Arial";
	private static int sizeOfText = 12;
	
	private static String appType = "Red";
	
	/**
	 * Pokusi se najit soubor s nastavenim, hleda v adresari u binarnich souboru soubor podle confFileName.
	 * Spousti funkci {@link #loadConf()} a {@link #saveConf()}
	 */
	public static void load(){
			
    	String path = NetConf.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    	String decodedPath = null;
    	
    	try {
			decodedPath = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
    	if(decodedPath != null){
    		//pokud jsem to nacetl
    		decodedPath=decodedPath.substring(0,decodedPath.lastIndexOf("/"));
    		decodedPath=decodedPath.substring(0,decodedPath.lastIndexOf("/")+1);
    		decodedPath+="examples/";
    		
    		confFile = new File(decodedPath+confFileName);
    	}
    	
    	//pokud mam existujici soubor s nastavenim tak ho nactu
    	if(confFile != null){
        	if(confFile.exists()){
        		try {
					NetConf.loadConf();
				} catch (FalseXML e1) {
					e1.printStackTrace();
					confFile.delete();
	        		try {
	    				confFile.createNewFile();
	    				NetConf.saveConf();
	    				
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    				confFile=null;
	    			}
				}
        	}
    		else{
        		//zkusim ho vytvorit a ulozit do nej defaultni konfiguraci
        		try {
    				confFile.createNewFile();
    				NetConf.saveConf();
    				
    			} catch (IOException e) {
    				e.printStackTrace();
    				confFile=null;
    			}
    		}
    	}

	}

	private static void saveConf(){
		//pokud nemam kam tak neukladam
		if(confFile == null)
			return;
		
		Document xml = DocumentHelper.createDocument();
		Element root = xml.addElement("configuration");
		
		root.addElement("sizeOfCircle")
			.addText(Integer.toString(sizeOfCircle));
		root.addElement("sizeOfRectX")
			.addText(Integer.toString(sizeOfRectX));
		root.addElement("sizeOfRectY")
			.addText(Integer.toString(sizeOfRectY));
		root.addElement("arrowSize")
			.addText(Integer.toString(arrowSize));
		root.addElement("colorAct")
			.addText(Integer.toString(colorAct.getRGB()));
		root.addElement("colorRest")
			.addText(Integer.toString(colorRest.getRGB()));
		root.addElement("colorBack")
			.addText(Integer.toString(colorBack.getRGB()));
		root.addElement("font")
			.addText(font);
		root.addElement("sizeOfText")
			.addText(Integer.toString(sizeOfText));
		root.addElement("appType")
			.addText(appType);
		
		//vypisu vytvoreny dokument
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer=null;
		try {
			writer = new XMLWriter(new FileWriter(confFile), format );
			writer.write( xml );
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	@SuppressWarnings("rawtypes")
	private static void loadConf() throws FalseXML{
		
		if(confFile == null)
			return;
		
	    SAXReader xmlReader = new SAXReader();
	    Document doc;
	    try {
			doc = xmlReader.read(confFile);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new FalseXML();
		}
	    
	    Element root = doc.getRootElement();
	    Element elem=null;
	    
	    for(Iterator i = root.elementIterator();i.hasNext();){
	    	elem =(Element) i.next();
	    	
	    	if(elem.getName().equals("sizeOfCircle"))
	    		sizeOfCircle=Integer.parseInt(elem.getText());
	    	else if(elem.getName().equals("sizeOfRectX"))
	    		sizeOfRectX=Integer.parseInt(elem.getText());
	    	else if(elem.getName().equals("sizeOfRectY"))
	    		sizeOfRectY=Integer.parseInt(elem.getText());
	    	else if(elem.getName().equals("arrowSize"))
	    		arrowSize=Integer.parseInt(elem.getText());
	    	else if(elem.getName().equals("colorAct"))
	    		colorAct=new Color(Integer.parseInt(elem.getText()));
	    	else if(elem.getName().equals("colorRest"))
	    		colorRest=new Color(Integer.parseInt(elem.getText()));
	    	else if(elem.getName().equals("colorBack"))
	    		colorBack=new Color(Integer.parseInt(elem.getText()));
	    	else if(elem.getName().equals("font"))
	    		font=elem.getText();
	    	else if(elem.getName().equals("sizeOfText"))
	    		sizeOfText=Integer.parseInt(elem.getText());
	    	else if(elem.getName().equals("appType"))
	    		appType=elem.getText();
	    	else
	    		throw new FalseXML();
	    }
		
	}
	
	/**
	 * Vrati typ vzhledu, ktery se pouziva.
	 * @return Promenna appType.
	 */
	public static String getAppType() {
		return appType;
	}
	
	/**
	 * Vrati pole typu, ktere jsou pro tuto aplikaci dostupne.
	 * @return Pole typu.
	 */
	public static String[] getAppTypes() {
		return appTypes;
	}
	
	/**
	 * Vrati velikost vykreslene sipky.
	 * @return Promenna arrowSize.
	 */
	public static int getArrowSize() {
		return arrowSize;
	}
	
	/**
	 * vrati baru aktivniho prvku.
	 * @return Promenna colorAct.
	 */
	public static Color getColorAct() {
		return colorAct;
	}
	
	/**
	 * Vrati barvu pozadi.
	 * @return Promenna colorBack.
	 */
	public static Color getColorBack() {
		return colorBack;
	}
	
	/**
	 * Vrati barvu ostatnich prvku ktere nejsou aktivni.
	 * @return Promenna colorRest.
	 */
	public static Color getColorRest() {
		return colorRest;
	}
	
	/**
	 * Vrati font, kterym se pise text.
	 * @return Promenna font.
	 */
	public static String getFont() {
		return font;
	}
	
	/**
	 * Vrati minimalni velikost Mist.
	 * @return Promenna sizeOfCircle.
	 */
	public static int getSizeOfCircle() {
		return sizeOfCircle;
	}
	
	/**
	 * Vrati minimalni sirku Prechodu.
	 * @return Promenna sizeOfRectX.
	 */
	public static int getSizeOfRectX() {
		return sizeOfRectX;
	}
	
	/**
	 * Vrati minimalni vysku Prechodu.
	 * @return Promenna sizeOfRectY.
	 */
	public static int getSizeOfRectY() {
		return sizeOfRectY;
	}
	
	/**
	 * Vrati velikost textu kterou se pise.
	 * @return Promena sizeOfText.
	 */
	public static int getSizeOfText() {
		return sizeOfText;
	}
	
	/**
	 * Vrati jestli je sipka vyplnena nebo prazdna.
	 * @return Promenna arrowFilled.
	 */
	public static boolean isArrowFilled() {
		return arrowFilled;
	}

	/**
	 * Nastavi novy typ vzhledu.
	 * @param appType Nova hodnota promenne appType.
	 */
	public static void setAppType(String appType) {
		NetConf.appType = appType;
		saveConf();
	}
	
	/**
	 * Nastavi jestli je sipka vyplnena.
	 * @param arrowFilled Nova hodnota promenne arrowFilled.
	 */
	public static void setArrowFilled(boolean arrowFilled) {
		NetConf.arrowFilled = arrowFilled;
		saveConf();
	}
	
	/**
	 * Nastavi novou velikost sipky.
	 * @param arrowSize Nova hodnota promenne arrowSize.
	 */
	public static void setArrowSize(int arrowSize) {
		NetConf.arrowSize = arrowSize;
		saveConf();
	}
	
	/**
	 * Nastavi novou barvu aktivniho objektu.
	 * @param colorAct Nova hodnota promenne colorAct.
	 */
	public static void setColorAct(Color colorAct) {
		NetConf.colorAct = colorAct;
		saveConf();
	}
	
	/**
	 * Nastavi novou barvu pozadi.
	 * @param colorBack Nova hodnota promenne colorBack.
	 */
	public static void setColorBack(Color colorBack) {
		NetConf.colorBack = colorBack;
		saveConf();
	}
	
	/**
	 * Nastavi novou barvu zbytku prvku.
	 * @param colorRest Nova hodnota promenne colorRest.
	 */
	public static void setColorRest(Color colorRest) {
		NetConf.colorRest = colorRest;
		saveConf();
	}
	
	/**
	 * Nastavi novy font textu.
	 * @param font Nova hodnota promenne font.
	 */
	public static void setFont(String font) {
		NetConf.font = font;
		saveConf();
	}
	
	/**
	 * Nastavi minimalni velikost Mist.
	 * @param sizeOfCircle Nova hodnota promenne sizeOfCircle.
	 */
	public static void setSizeOfCircle(int sizeOfCircle) {
		NetConf.sizeOfCircle = sizeOfCircle;
		saveConf();
	}
	
	/**
	 * Nastavi minimalni sirku Prechodu.
	 * @param sizeOfRectX Nova hodnota promenne sizeOfRectX.
	 */
	public static void setSizeOfRectX(int sizeOfRectX) {
		NetConf.sizeOfRectX = sizeOfRectX;
		saveConf();
	}
	
	/**
	 * Nastavi minimalni vysku Prechodu.
	 * @param sizeOfRectY Nova hodnota promenne sizeOfRectY.
	 */
	public static void setSizeOfRectY(int sizeOfRectY) {
		NetConf.sizeOfRectY = sizeOfRectY;
		saveConf();
	}
	
	/**
	 * Nastavi novou velikost textu.
	 * @param sizeOfText Nova hodnota promenne sizeOfText.
	 */
	public static void setSizeOfText(int sizeOfText) {
		NetConf.sizeOfText = sizeOfText;
		saveConf();
	}
}

//EOF