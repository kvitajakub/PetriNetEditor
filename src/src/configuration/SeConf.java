/**
 * SeConf.java
 * @author Kvita Jakub
 */
package configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.io.File;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.io.IOException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.Element;
import org.dom4j.Attribute;
import server.MainListen;
import except.FalseXML;

/**
 * Konfigurace serveru pro komunikaci s klientem.
 * @author Kvita Jakub
 */
public class SeConf {

	//naslouchajici port
	private static int listeningPort=22222;
	//jmeno souboru s loginy a hesly uzivatelu
	private static String userFileName = new String("users.xml");
	//odkaz na soubor s uzivateli
	private File userFile = null;
	
	/**
	 * Seznam registrovanych uzivatelu - tvar LOGIN HESLO LOGIN HESLO...
	 */
	public ArrayList<String> userList;
	
	//uzivatel pod kterym pracuju
	private String user = null;
	private String password = null;
	
	/**
	 * Nacte uzivatele ze souboru userFileName
	 */
	public SeConf(){
		super();
		
		userList = new ArrayList<String>();
		
		try {
			loadUsers();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//nacte seznam uzivatelu a hesel do pameti ze souboru
	private void loadUsers() throws IOException{
		
		//cesta k rootu
    	String path = MainListen.class.getProtectionDomain().getCodeSource().getLocation().getPath();
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
    		decodedPath+="examples/server/";
    		userFile = new File(decodedPath+userFileName);
    	}
    	else{
    		//nejaka chyba
    		throw new IOException("Chyba pri nacitani souboru users.xml");
    	}
    	
    	if(userFile.exists()){
    		//nactu ho
    		
    		System.out.println("Nacitam userfile.");
    		
    		try {
				loadusersXML();
			} catch (FalseXML e) {
				userList = new ArrayList<String>();
				userFile.delete();
				saveUsers();
			}
    		
    	}
    	else{
    		System.out.println("Vytvarim novy userfile s uzivatelem SIMULATED");
    		
    		userList.add("SIMULATED");
    		userList.add("");
    		
    		saveUsers();
    	}
	}
	
	@SuppressWarnings("rawtypes")
	private void loadusersXML() throws FalseXML{
		
		//TODO zpracovani xml - osetrit chybna xml
		
		if(userFile == null)
			return;
		
	    SAXReader xmlReader = new SAXReader();
	    Document doc;
	    try {
			doc = xmlReader.read(userFile);
		} catch (DocumentException e) {
			throw new FalseXML();
		} catch (Exception c){
			throw new FalseXML();
		}
	    
	    
	    Element root = doc.getRootElement();
	    Element user;
	    Attribute atb;
	    
	    for(Iterator i = root.elementIterator();i.hasNext();){
	    	user = (Element)i.next();
	    	
	    	if(user.getQualifiedName().equals("user")){
	    		
	    		try{
	    			atb = user.attribute(0);
	    		} catch (IndexOutOfBoundsException x){
	    			x.printStackTrace();
	    			throw new FalseXML();
	    		}
	    		
	    		if(!atb.getQualifiedName().equals("login")){
	    			throw new FalseXML();
	    		}
	    		else{
	    			userList.add(atb.getText());
	    		}
	    		
	    		try{
	    			atb = user.attribute(1);
	    		} catch (IndexOutOfBoundsException x){
	    			x.printStackTrace();
	    			throw new FalseXML();
	    		}
	    		
	    		if(!atb.getQualifiedName().equals("pass")){
	    			throw new FalseXML();
	    		}
	    		else{
	    			userList.add(atb.getText());
	    		}
	    	}
	    	else{
	    		throw new FalseXML();
	    	}
	    }
		
	}
	
	/**
	 * Ulozi registrovane uzivatele do souboru.
	 */
	public void saveUsers(){
		
		if(userFile == null)
			return;
		
		if(!userFile.exists())
			try {
				userFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		
		Document xml = DocumentHelper.createDocument();
		Element root = xml.addElement("users");
		Element user;
		
		
		for(Iterator<String> i = userList.iterator();i.hasNext();){
			user = root.addElement("user");
			user.addAttribute("login", i.next());
			user.addAttribute("pass", i.next());
		}
		
		//vypisu vytvoreny dokument
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer=null;
		try {
			writer = new XMLWriter(new FileWriter(userFile), format );
			writer.write( xml );
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Zjisti jestli je uzivatel registrovany u nas.
	 * @return  True nebo false.
	 */
	public boolean isActiveRegistered(){
		
		if(user.equals("SIMULATED"))
			return false;
		
		
		for(Iterator<String> i = userList.iterator();i.hasNext();){
			
			if(i.next().equals(user)){
				//pokud je stejny login tak musim jeste zkontrolovat heslo
				if(i.next().equals(password)){
					//jsou oba stejne takze jsem nasel
					return true;
				}
			}
			else{
				//pokud neni stejny login tak preskocim jmeno
				i.next();
			}
		}
		
		//nenasel jsem
		return false;
	}
	
	/**
	 * Zjisti jestli je jmeno uzivatele volne pro registraci
	 * @return True nebo false.
	 */
	public boolean isNameFree(){
		
		if(user.equals("SIMULATED"))
			return false;
		
		for(Iterator<String> i = userList.iterator();i.hasNext();){
			
			if(i.next().equals(user))
					return false;
			else
				//preskocim heslo
				i.next();

		}	
		//nenasel jsem
		return true;
	}
	
	/**
	 * Prida aktivniho uzivatele k registrovanym
	 */
	public void addActiveToRegister(){
		userList.add(user);
		userList.add(password);
	}
	
	
	/**
	 * Vytiskne registrovane uzivatele.
	 */
	public void printRegisteredUsers(){
		
		System.out.println("Registrovani uzivatele:");
		
		for(Iterator<String> i = userList.iterator();i.hasNext();){
			
			System.out.println(i.next()+"   "+i.next());
		}
		
		System.out.println("----------");
		
	}

	/**
	 * Vrati jmeno uzivatele ktery je prihlaseny.
	 * @return Promenna user.
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * Nastavi jmeno pripojeneho uzivatele.
	 * @param user Nova hodnota.
	 */
	public void setUser(String user) {
		this.user = user;
	}
	
	/**
	 * Vrati heslo pripojeneho uzivatele.
	 * @return Promenna password.
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Nastavi nove heslo pripojeneho uzivatele.
	 * @param password Nova hodnota.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Vrati port na kterem server nasloucha.
	 * @return Promenna listeningPort.
	 */
	public static int getListeningPort() {
		return listeningPort;
	}
}

//EOF