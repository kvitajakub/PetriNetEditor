/**
 * ServeClient.java
 * @author Kvita Jakub
 */
package server;

import java.io.IOException;
import java.net.Socket;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Document;
import network.Connection;
import network.Data;
import network.MessageType;
import except.BadConnect;
import except.FalseSimulation;
import except.FalseXML;
import configuration.SeConf;
import pnet.PetriNet;

/**
 * Trida implementujici vlakno obsluhy jednoho klienta.
 * @author Kvita Jakub
 */
public class ServeClient implements Runnable {

	private Connection client;
	private Repository rep;
	private SeConf config = null;
	
	/**
	 * Konstruktor dostane socket na kterem je uzivatel a pokusi se vytvorit spojeni. 
	 * @param sock Socket uzivatele.
	 * @throws BadConnect Nepovedlo se vytvorit spojeni.
	 */
	public ServeClient(Socket sock) throws BadConnect {
		super();
		
		client = new Connection();
		client.connect(sock);
		
		config = new SeConf();
		rep = new Repository(config);
		
		config.printRegisteredUsers();
	}

	public void run() {
		
		System.out.println("Spoustim vlakno obsluhovani klienta.");
		
		Element root;
		
		Data data = new Data();
		data.setType(MessageType.OK);
		
		//pokud naposledy neprisel disconnect tak porad cekam na dalsi pozadavky
		looping:
		while(data.getType() != MessageType.DISCONNECT){
		
			//prijmu zpravu od klienta
			try {
				data = client.receive();
			} catch (BadConnect e) {
				e.printStackTrace();
				return;
			}
			
			//zpracuju podle typu
			switch(data.getType()){
			
				case LOGIN:
					//nastavim aktivitu na uzivatele ktery mi prisel
					config.setUser(data.getstr1());
					config.setPassword(data.getstr2());
					
					//pokud je registrovany tak OK jinak ERR
					if(config.isActiveRegistered())
						try {
							client.send(new Data(MessageType.OK,null,null,null));
						} catch (BadConnect e) {
							e.printStackTrace();
							break looping;
						}
					else
						try {
							client.send(new Data(MessageType.ERR,null,null,null));
						} catch (BadConnect e) {
							e.printStackTrace();
							break looping;
						}
					
				break;	
				case REGISTER:
					//nastavim aktivitu na uzivatele ktery mi prisel
					config.setUser(data.getstr1());
					config.setPassword(data.getstr2());
					
					//pokud neni registrovany a jmeno je volne
					if(!config.isActiveRegistered() && config.isNameFree()){
						config.addActiveToRegister();
						try {
							client.send(new Data(MessageType.OK,data.getstr1(),data.getstr2(),null));
						} catch (BadConnect e) {
							e.printStackTrace();
							break looping;
						}
					}
					else{
						try {
							client.send(new Data(MessageType.ERR,data.getstr1(),data.getstr2(),null));
						} catch (BadConnect e) {
							e.printStackTrace();
							break looping;
						}
					}
					
				break;
				case DISCONNECT:
					
					//na odpojeni se neodpovida, akorat skoncim pri dalsi smycce cyklu
					
				break;
				case NETLIST:
					
					//inicializuju pametovou strukturu vsech siti a poslu ji klientovi
					if(data.getstr1()==null)
						rep.initNets();
					else
						rep.searchNets(data.getstr1());
					
					try {
						
						client.send(new Data(MessageType.OK,null,null,rep.getNets()));
					} catch (BadConnect e) {
						e.printStackTrace();
						break looping;
					}
					
				break;
				case OPEN:
					
					//otevru sit jakou uzivatel chce a poslu mu ji
					
					root = data.getDoc().getRootElement();
					try {
						
						Document net = rep.openNet(root.attributeValue("user"), root.attributeValue("name"), root.attributeValue("version"));
						client.send(new Data(MessageType.OK,null,null,net));
						
					} catch (IOException e2) {
						e2.printStackTrace();
						try {
							client.send(new Data(MessageType.ERR,null,null,null));
						} catch (BadConnect e) {
							e.printStackTrace();
							break looping;
						}
						
					} catch (BadConnect e) {
						e.printStackTrace();
						break looping;
					}
					
				break;
				case DELETE:
					
					//smazu sit
					root = data.getDoc().getRootElement();
					try {
						//pokud se povede smazat tak OK
						if(rep.deleteNet(root.attributeValue("user"), root.attributeValue("name"), root.attributeValue("version"))){
							client.send(new Data(MessageType.OK,null,null,null));
						}
						else{
							client.send(new Data(MessageType.ERR,null,null,null));
						}
						
					} catch (BadConnect e) {
						e.printStackTrace();
						break looping;
					}
					
				break;
				case SAVE:
					//ulozi se sit se jmenem a popisem - pokud uz nejaka takova byla tak se vytvori nova verze								
					try {
						//ulozim sit
						rep.saveNet(data.getstr1(),data.getstr2(),data.getDoc());
						//povedlo se tak poslu ok
						client.send(new Data(MessageType.OK,null,null,null));
					} catch (BadConnect e) {
						e.printStackTrace();
						break looping;
					} catch (IOException e) {
						//nepovedlo se ulozit tak poslu err
						e.printStackTrace();
						try {
							client.send(new Data(MessageType.ERR,null,null,null));
						} catch (BadConnect e1) {
							e1.printStackTrace();
						}
					}
					
				break;
				case SIMULATE:
					//simulace site			
					
					//zkusim sit zpracovat
					try {
						PetriNet net = new PetriNet();
						net.fromDoc(data.getDoc());
						net.simulate();
						rep.saveSimulated(data.getDoc());
						
						//vse se povedlo tak poslu OK
						client.send(new Data(MessageType.OK,null,null,net.toDoc()));
						
					} catch (FalseXML e2) {
						e2.printStackTrace();
						
						try {
							client.send(new Data(MessageType.ERR,"Sit se nepovedlo na serveru nacist.",null,null));
						} catch (BadConnect e) {
							e.printStackTrace();
							break looping;
						}
						
						break;
					} catch (DocumentException e2) {
						e2.printStackTrace();
						
						try {
							client.send(new Data(MessageType.ERR,"Sit se nepovedlo na serveru nacist.",null,null));
						} catch (BadConnect e) {
							e.printStackTrace();
							break looping;
						}
						
						break;
					} catch (FalseSimulation e) {
						//simulace se nepovedla - poslu zpravu z vyjimky klientovi
						
						try {
							client.send(new Data(MessageType.ERR,e.getMessage(),null,null));
						} catch (BadConnect e2) {
							e2.printStackTrace();
							break looping;
						}
						
						break;
					} catch (IOException e) {
						e.printStackTrace();
						
						try {
							client.send(new Data(MessageType.ERR,"Sit se na serveru nepovedlo ulozit jako simulovanou.",null,null));
						} catch (BadConnect e2) {
							e2.printStackTrace();
							break looping;
						}
						
						break;
					} catch (BadConnect e) {
						e.printStackTrace();
						break looping;
					}
					
				break;
				case SIMULATE_STEP:
					//vicemene to same jako predchozi blok ale vola se jina funkce
					
					//zkusim sit zpracovat
					try {
						PetriNet net = new PetriNet();
						net.fromDoc(data.getDoc());
						net.simulateStep();
						rep.saveSimulated(data.getDoc());
						
						//vse zatim proslo takze OK odpoved
						client.send(new Data(MessageType.OK,null,null,net.toDoc()));
						
					} catch (FalseXML e2) {
						e2.printStackTrace();
						
						try {
							client.send(new Data(MessageType.ERR,"Sit se nepovedlo na serveru nacist.",null,null));
						} catch (BadConnect e) {
							e.printStackTrace();
							break looping;
						}
						
						break;
					} catch (DocumentException e2) {
						e2.printStackTrace();
						
						try {
							client.send(new Data(MessageType.ERR,"Sit se nepovedlo na serveru nacist.",null,null));
						} catch (BadConnect e) {
							e.printStackTrace();
							break looping;
						}
						
						break;
					} catch (FalseSimulation e) {
						//simulace se nepovedla - poslu zpravu z vyjimky klientovi
						
						try {
							client.send(new Data(MessageType.ERR,e.getMessage(),null,null));
						} catch (BadConnect e2) {
							e2.printStackTrace();
							break looping;
						}
						
						break;
					} catch (IOException e) {
						e.printStackTrace();
						
						try {
							client.send(new Data(MessageType.ERR,"Sit se na serveru nepovedlo ulozit jako simulovanou.",null,null));
						} catch (BadConnect e2) {
							e2.printStackTrace();
							break looping;
						}
						
						break;
					} catch (BadConnect e) {
						e.printStackTrace();
						break looping;
					}
					
				break;
				default:
					//kdyby nahodou nekdo poslal neco co neznam
					try {
						client.send(new Data(MessageType.ERR,null,null,null));
					} catch (BadConnect e) {
						e.printStackTrace();
						break looping;
					}
					
				break;
			}
		}
		
		//uzavreni klienta
		client.close();
		
		config.saveUsers();
		
		System.out.println("Koncim vlakno obsluhovani klienta");
	}
}

//EOF