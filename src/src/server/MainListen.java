/**
 * MainListen.java
 * @author Kvita Jakub
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import except.BadConnect;
import configuration.SeConf;

/**
 * Trida ktera zpracuje naslouchani serveru na portu a vytvareni vlaken.
 * @author Kvita Jakub
 */
public class MainListen {

	/**
	 * Main aplikace serveru.
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("Spoustim server na portu: "+SeConf.getListeningPort());
		
		new MainListen().listen();
	}
	
	ServerSocket sersoc = null;
	
	/**
	 * Konstruktor. Prida metody ktere se provedou pri ukonceni procesu.
	 */
	public MainListen(){
		
		Runtime.getRuntime().addShutdownHook(new CleanUp(this));
	}
	
	
	private class CleanUp extends Thread{
		private MainListen server;
		
		public CleanUp(MainListen server){
			super();
			this.server=server;
		}
		public void run(){
			server.doACleanExit();
		}
	}
	
	/**
	 * Metoda ktera se provede na konci vlakna. Ukonci naslouchajici port.
	 */
	public void doACleanExit(){
		System.out.println("Server se ukoncuje.");
		try {
			sersoc.close();
		} catch (IOException e) {
		}
	}
	
	/**
	 * Tady probiha samotne naslouchani a prijimani klientu v nekonecnem cyklu.
	 */
	public void listen(){
		
		try {
			sersoc = new ServerSocket(SeConf.getListeningPort());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		while(true){
			
			try {
				//prijme dalsiho zajemce a udela pro nej nove vlakno
				(new Thread(new ServeClient(sersoc.accept()))).start();
				
			} catch (BadConnect e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}

//EOF