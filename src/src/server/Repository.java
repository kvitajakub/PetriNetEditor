/**
 * Repository.java
 * @author Kvita Jakub
 */
package server;

import java.util.Date;
import java.util.Iterator;
import java.io.*;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.Element;
import configuration.SeConf;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileFilter;

/**
 * Trida implementujici metody pro praci se sitema ulozenyma na serveru.
 * @author Kvita Jakub
 */
public class Repository {

	private DateFormat dateFormat;
	private String rootpath;
	private File root;
	private Document nets;
	private SeConf config;
	
	
	/**
	 * Inicializace formatu data.
	 * @param config Konfiguracni objekt pro praci s seznamem uzivatelu.
	 */
	public Repository(SeConf config) {
		super();
		dateFormat = new SimpleDateFormat("yyyy-mm-dd  hh_mm_ss");
		this.config = config;
		
		//cesta kde budou vsechny adresare a soubory
    	String path = MainListen.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    	try {
			rootpath = URLDecoder.decode(path, "UTF-8");
			rootpath = rootpath.substring(0,rootpath.lastIndexOf("/"));
			rootpath = rootpath.substring(0,rootpath.lastIndexOf("/")+1);
			rootpath = rootpath + "examples/server/";
			root = new File(rootpath);
			if(!root.exists() || !root.isDirectory()){
				 root.mkdir();
			}		
			
		} catch (IOException e) {
			e.printStackTrace();
			rootpath = null;
		}
    	
	}

	/**
	 * Aktualizuje strukturu vsech uzivatelu a jejich siti i s verzemi.
	 * TODO opravit at to neni takove osklive
	 */
	public void initNets(){
		
		nets = DocumentHelper.createDocument();
		Element root = nets.addElement("root");
		//pro kazdeho uzivatele
		String user = null;
		File userdir = null;
		Element userelem = null;
		//pro kazdou sit
		File[] usernets;
		Element netelem = null;
		//pro kazdou verzi
		File[] netversions;
		String name;
		Element versionelem;
		File verdesc;
		
		Iterator<String> i = config.userList.iterator();
		while(i.hasNext()){
			 user=i.next();
			 i.next(); //preskocim heslo
			
			//promenne pro noveho uzivatele
			userdir = new File(rootpath,user);
			userelem = root.addElement("user");
				userelem.addAttribute("login", user);
			
			//pokud nema adresar tak ho vytvorim a koncim
			if(!userdir.exists()){
				userdir.mkdir();
				continue;
			}
			
			//pro kadou jeho sit - kazdy adresar v jejich adresari
			usernets = userdir.listFiles(new FileFilter(){
				public boolean accept(File pathname){
					if(pathname.isDirectory())
						return true;
					return false;
				}
			});
			
			for(File net : usernets){
				
				//pridam sit do struktury
				netelem = userelem.addElement("net");
					netelem.addAttribute("name", net.getName());
				
				//pro kazdou verzi - jen soubory xml
				netversions = net.listFiles(new FileFilter(){
					public boolean accept(File pathname){
						if(pathname.isFile() && pathname.getName().endsWith(".xml"))
							return true;
						return false;
					}
				});
				
				for(File version : netversions){
				
					//oriznu ze jmena ".xml"
					name = version.getName().substring(0, version.getName().length()-4);
					
					versionelem = netelem.addElement("version");
						versionelem.addAttribute("time", name);
					
					verdesc = new File(net,name+"-description.txt");
					if(verdesc.exists() && verdesc.isFile()){
						//prectu soubor a nacpu ho jako atribut
						versionelem.addAttribute("description", readFile(verdesc));
					}
					else{
						versionelem.addAttribute("description", "");
					}
					
				}
				
			}	
		}
	}
	
	/**
	 * Aktualizuje strukturu siti jen sitema ktere v nazvu obsahuji zadany retezec.
	 * @param str Retezec ktery musi obsahovat nazev.
	 */
	public void searchNets(String str){
		
		nets = DocumentHelper.createDocument();
		Element root = nets.addElement("root");
		//pro kazdeho uzivatele
		String user = null;
		File userdir = null;
		Element userelem = null;
		//pro kazdou sit
		File[] usernets;
		Element netelem = null;
		//pro kazdou verzi
		File[] netversions;
		String name;
		Element versionelem;
		File verdesc;
		
		Iterator<String> i = config.userList.iterator();
		while(i.hasNext()){
			 user=i.next();
			 i.next(); //preskocim heslo
			
			//promenne pro noveho uzivatele
			userdir = new File(rootpath,user);
			userelem = root.addElement("user");
				userelem.addAttribute("login", user);
			
			//pokud nema adresar tak ho vytvorim a koncim
			if(!userdir.exists()){
				userdir.mkdir();
				continue;
			}
			
			//pro kadou jeho sit - kazdy adresar v jejich adresari
			usernets = userdir.listFiles(new FileFilter(){
				public boolean accept(File pathname){
					if(pathname.isDirectory())
						return true;
					return false;
				}
			});
			
			for(File net : usernets){
				
				//pokud tam neni vyhledavany string tak to cele preskocim
				if(net.getName().indexOf(str) == -1)
					continue;
				
				//pridam sit do struktury
				netelem = userelem.addElement("net");
					netelem.addAttribute("name", net.getName());
				
				//pro kazdou verzi - jen soubory xml
				netversions = net.listFiles(new FileFilter(){
					public boolean accept(File pathname){
						if(pathname.isFile() && pathname.getName().endsWith(".xml"))
							return true;
						return false;
					}
				});
				
				for(File version : netversions){
				
					//oriznu ze jmena ".xml"
					name = version.getName().substring(0, version.getName().length()-4);
					
					versionelem = netelem.addElement("version");
						versionelem.addAttribute("time", name);
					
					verdesc = new File(net,name+"-description.txt");
					if(verdesc.exists() && verdesc.isFile()){
						//prectu soubor a nacpu ho jako atribut
						versionelem.addAttribute("description", readFile(verdesc));
					}
					else{
						versionelem.addAttribute("description", "");
					}
					
				}
				
			}	
		}
		
	}
	
	//precte cely soubor do stringu
	private String readFile(File f){
		
		BufferedReader reader=null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String ret = new String("");
		String s;
		
		try {
			while((s = reader.readLine()) != null){
				ret+= s;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return ret;
	}
	
	/**
	 * Ulozi sit do uloziste.
	 * @param name Jmeno site.
	 * @param desc Popis site.
	 * @param net Samotna sit.
	 * @throws IOException Nepovedlo se ulozit.
	 */
	public void saveNet(String name, String desc, Document net) throws IOException{
		
		//config.getUser();
		//cesta k adresari se siti
		File netfile = new File(new File(root,config.getUser()), name);
		
		if(!netfile.exists() || !netfile.isDirectory())
			netfile.mkdirs();
		
		//jmeno nove verze podle casu
		String vname = versionName();
		
		//soubor kde bude sit
		File vfile = new File(netfile,vname+".xml");
		File vdesc = new File(netfile,vname+"-description.txt");
		
		System.out.println(vfile.getName());
		System.out.println(vdesc.getName());
		
		//vytvorim nove soubory
		vfile.createNewFile();
		vdesc.createNewFile();
		
		//zapisu sit
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer=null;
		writer = new XMLWriter(new FileWriter(vfile), format );
		writer.write( net );
		writer.close();
		//zapisu popis
		Writer out = new OutputStreamWriter(new FileOutputStream(vdesc));
		out.write(desc);
		out.close();

	}
	
	/**
	 * Ulozi sit ktera byla simulovana uzivatelem. Jmena se generuji automaticky.
	 * @param net Simulovana sit.
	 * @throws IOException Nepovedlo se ulozit.
	 */
	public void saveSimulated(Document net) throws IOException{
		
		//jmeno podle casu
		String name = versionName();
		
		File netfile = new File(new File(root,"SIMULATED"), config.getUser());
		
		netfile.mkdirs();
		
		//soubor kde bude sit
		File vfile = new File(netfile,name+".xml");
		File vdesc = new File(netfile,name+"-description.txt");
		
		//vytvorim nove soubory
		vfile.createNewFile();
		vdesc.createNewFile();
		
		//zapisu sit
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer=null;
		writer = new XMLWriter(new FileWriter(vfile), format );
		writer.write( net );
		writer.close();
		//zapisu popis
		Writer out = new OutputStreamWriter(new FileOutputStream(vdesc));
		
		String desc = new String("Simulated by user: "+config.getUser());
		out.write(desc);
		
		out.close();
		
	}
	
	/**
	 * Otevre sit z uloziste.
	 * @param user Jmeno vlastnika.
	 * @param name Jmeno site.
	 * @param version Jmeno verze.
	 * @return Otevrena sit.
	 * @throws IOException Neco se nepovedlo.
	 */
	public Document openNet(String user, String name, String version) throws IOException{
		
		File vfile = new File(new File(new File(root,user),name),version+".xml");
		
		if(!vfile.exists())
			throw new IOException();
		
	    SAXReader xmlReader = new SAXReader();
	    Document doc;
		
	    try {
			doc = xmlReader.read(vfile);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new IOException();
		}
		
	    return doc;
	}
	
	/**
	 * Smaze sit z uloziste.
	 * @param user Jmeno vlastnika.
	 * @param name Jmeno site.
	 * @param version Jmeno verze.
	 * @return Jestli se to povedlo nebo ne.
	 */
	public boolean deleteNet(String user, String name, String version){
		
		
		if(!user.equals(config.getUser())){
			//nemuze mazat cizi site
			return false;
		}
		
		File nfile = new File(new File(root,user),name);
		
		//pokud tam nic neni tak je smazana
		if(!nfile.exists())
			return true;
		
		File vfile = new File(nfile,version+".xml");
		File descfile = new File(nfile,version+"-description.txt");
		
		
		System.out.println("NET : "+vfile.getName());
		
		
		if(vfile.exists()){
			if(!vfile.delete())
				return false;
		}
		
		System.out.println("DES : "+descfile.getName());
		if(descfile.exists()){
			if(!descfile.delete())
				return false;
		}

		//pokud je nadrazeny adresar site prazdny tak ho smazu
		if(nfile.list().length==0){
			System.out.println("DIR : "+nfile.getName());
			nfile.delete();
		}
			
		return true;
	}
	
	/**
	 * Vrati aktualni cas ve vhodnem formatu jako jmeno verze.
	 * @return Aktualni cas.
	 */
	private String versionName(){
		dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH_mm_ss");
		return dateFormat.format(new Date());
	}
	
	/**
	 * Vraci strukturu uzivatelu, jejich siti a verzi v ulozisti.
	 * @return Strukturovany Dokument.
	 */
	public Document getNets() {
		return nets;
	}
}

//EOF