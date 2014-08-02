/**
 * Connection.java
 * @author Kvita Jakub
 */
package network;

import network.Data;
import except.*;
import java.net.*;
import java.io.*;

/**
 * Nadstavba programu pro posilani zprav pres sockety. Posila zpravu ve tvaru objektu Data.
 * @author Kvita Jakub
 */
public class Connection {

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket sock;
	
	/**
	 * Pouze vytvori objekt, nikam se nepripojuje.
	 */
	public Connection(){
	}

	/**
	 * Prijme socket za svuj a vytvori si na nem streamy pro posilani objektu.
	 * @param sock Uz predtim vytvorene spojeni.
	 * @throws BadConnect Indikuje ze se nepovedlo vytvorit spojeni.
	 */
	public void connect(Socket sock) throws BadConnect{
		this.sock=sock;
		
		try {
			out = new ObjectOutputStream(sock.getOutputStream());
			in = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			throw new BadConnect("Nejde vytvorit streamy.(1)");
		}
	}
	
	/**
	 * Pokusi se vytvorit socket z parametru a vytvorit si na nem streamy pro posilani objektu.
	 * @param name  Jmeno serveru.
	 * @param port  Port na kterem server nasloucha.
	 * @throws BadConnect Nepovedlo se pripojit nebo vztvorit streamy.
	 */
	public void connect(String name, int port) throws BadConnect{
		
		try {
			sock = new Socket(name, port);
		} catch (UnknownHostException e) {
			throw new BadConnect("Spatne vytvoreni socketu.(1)");
		} catch (IOException e) {
			throw new BadConnect("Spatne vytvoreni socketu.(2)");
		}
		
		try {
			out = new ObjectOutputStream(sock.getOutputStream());
			in = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			throw new BadConnect("Nejde vytvorit streamy.(2)");
		}
	}
	
	/**
	 * Uzavreni streamu a socketu.
	 */
	public void close(){
		try {
			out.close();
			in.close();
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Posle objekt dat.
	 * @param data Posilany objekt.
	 * @throws BadConnect  Nepovedlo se poslat, prekryva vyjimku IOExeption.
	 */
	public void send(Data data) throws BadConnect{
		
		try {
			out.writeObject(data);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BadConnect("Zprava neodeslana.");
		}

		System.out.println("");
		System.out.println("     Type : "+data.getType());
		System.out.println("<<-- Login: "+data.getstr1()+"  "+data.getstr2());
		if(data.getDoc()!=null)
			System.out.println("     Data : "+data.getDoc().asXML());
		else
			System.out.println("     Data : "+null);
	}
	
	/**
	 * Prijme data ze sveho socketu.
	 * @return Prijata data.
	 * @throws BadConnect Nepovedlo se prijmout.
	 */
	public Data receive() throws BadConnect{
		
		Data data = null;
		
		try {
			data = (Data)in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
			throw new BadConnect("Zprava spatne prijata.(1)");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new BadConnect("Zprava spatne prijata.(2)");
		}
		
		System.out.println("");
		System.out.println("     Type : "+data.getType());
		System.out.println("-->> Login: "+data.getstr1()+"  "+data.getstr2());
		if(data.getDoc()!=null)
			System.out.println("     Data : "+data.getDoc().asXML());
		else
			System.out.println("     Data : "+null);
		
		return data;
	}
	
}

//EOF