/**
 * Conf.java
 * @author Kvita Jakub
 */
package configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import except.FalseXML;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import client.MainGui;
import network.*;

/**
 * Trida udrzujici hodnoty nastaveni programu. Vse je uchovano ve statickych promennych.
 * @author Kvita Jakub
 */
public class ClConf {

	//vnitrni konfigurace
	private static final String confFileName = "client.config.xml";
	private static File confFile = null;
	
	//vnejsi konfigurace
	
	private static File root=null;
	
	/**
	 * Server ke kteremu je aktualne uzivatel pripojen.
	 */
	public static Connection server = null;
	
	private static ArrayList<String> openFiles = null;
	
	private static String serverName = new String("");
	private static String port = new String("");
	private static String username = new String("");
	private static String pass = new String("");
	
	/**
	 * Pokusi se najit soubor s nastavenim, hleda v adresari u binarnich souboru soubor podle confFileName.
	 * Spousti funkci {@link #loadConf()} a {@link #saveConf()}
	 */
	public static void load(){
			
    	String path = MainGui.class.getProtectionDomain().getCodeSource().getLocation().getPath();
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
    		root=new File(decodedPath+"examples/client");
    		
    		if(!root.exists())
    			root.mkdirs();
    		
    		confFile = new File(root,confFileName);
    	}
    	
    	//pokud mam existujici soubor s nastavenim tak ho nactu
    	if(confFile != null){
        	if(confFile.exists()){
        		try {
					ClConf.loadConf();
				} catch (FalseXML e1) {
					e1.printStackTrace();
					confFile.delete();
	        		try {
	    				confFile.createNewFile();
	    				ClConf.saveConf();
	    				
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
    				ClConf.saveConf();
    				
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
		Element elem;
		
		root.addElement("serverName")
			.addText(serverName);
		root.addElement("port")
			.addText(port);
		root.addElement("username")
			.addText(username);
		root.addElement("pass")
			.addText(pass);
		elem=root.addElement("openFiles");
		
		if(openFiles != null){
			for(String str : openFiles){
				elem.addElement("file")
				 .addText(str);
			}
		}
		
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
	    	
	    	if(elem.getName().equals("serverName"))
	    		serverName=elem.getText();
	    	else if(elem.getName().equals("port"))
	    		port=elem.getText();
	    	else if(elem.getName().equals("username"))
	    		username=elem.getText();
	    	else if(elem.getName().equals("pass"))
	    		pass=elem.getText();
	    	else if(elem.getName().equals("openFiles")){
	    		
	    		Element elem2;
	    		Iterator i2 = elem.elementIterator();
	    		if(i2.hasNext())
	    			openFiles = new ArrayList<String>();
	    		else
	    			openFiles = null;
	    		while(i2.hasNext()){
	    			elem2 = (Element) i2.next();
	    			
	    			openFiles.add(elem2.getText());
	    		}	    		
	    	}
	    	else
	    		throw new FalseXML();
	    }
		
	}
	
	/**
	 * Vrati promennou kde jsou vsechny soubory pro klienta.
	 * @return Promenna root.
	 */
	public static File getRoot() {
		return root;
	}
	
	/**
	 * Vrati retezec ktery se pouziva u jmena serveru.
	 * @return Promenna serverName.
	 */
	public static String getServerName() {
		return serverName;
	}
	
	/**
	 * Nastavi jmeno serveru.
	 * @param serverName Nova hodnota.
	 */
	public static void setServerName(String serverName) {
		ClConf.serverName = serverName;
		saveConf();
	}
	
	/**
	 * Vrati jako retezec cislo portu na kterem je server.
	 * @return Promenna port.
	 */
	public static String getPort() {
		return port;
	}
	
	/**
	 * Nastavi cislo portu na kterem je server.
	 * @param port Nova hodnota.
	 */
	public static void setPort(String port) {
		ClConf.port = port;
		saveConf();
	}
	
	/**
	 * Vrati uzivatelske jmeno, ktere klient pouziva na serveru.
	 * @return Promenna username.
	 */
	public static String getUsername() {
		return username;
	}
	
	/**
	 * Nastavi nove uzivatelske jmeno klienta na serveru.
	 * @param username Nova hodnota.
	 */
	public static void setUsername(String username) {
		ClConf.username = username;
		saveConf();
	}
	
	/**
	 * Vrati heslo ktere pouziva klient pro ucet na serveru.
	 * @return Promenna pass.
	 */
	public static String getPass() {
		return pass;
	}
	
	/**
	 * Nstavi nove heslo se kterym se bude pracovat.
	 * @param pass Nova hodnota.
	 */
	public static void setPass(String pass) {
		ClConf.pass = pass;
		saveConf();
	}
	
	/**
	 * Vrati seznam nazvu otevrenych souboru.
	 * @return Vrazeny seznam.
	 */
	public static ArrayList<String> getOpenFiles() {
		return openFiles;
	}
	
	/**
	 * Nastavi novy seznam otevrenych souboru.
	 * @param openFiles Nova hodnota.
	 */
	public static void setOpenFiles(ArrayList<String> openFiles) {
		ClConf.openFiles = openFiles;
		saveConf();
	}
}

//EOF