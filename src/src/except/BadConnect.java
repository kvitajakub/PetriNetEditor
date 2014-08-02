/**
 * BadConnect.java
 * @author Kvita Jakub
 */
package except;

/**
 * Vyjimka pro chyby pri praci s tridou Connection.(Posilani zprav po siti.)
 * @author Kvita Jakub
 */
public class BadConnect extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Jen vola nadrazeny konstruktor.
	 */
	public BadConnect() {
		super();
	}

	/**
	 * Jen vola nadrazeny konstruktor.
	 * @param arg0
	 */
	public BadConnect(String arg0) {
		super(arg0);
	}
	
	/**
	 * Konstruktor.
	 * @param arg0 Zprava.
	 * @param arg1 Duvod.
	 */
	public BadConnect(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Konstruktor.
	 * @param arg0 Duvod.
	 */
	public BadConnect(Throwable arg0) {
		super(arg0);
	}
}

//EOF