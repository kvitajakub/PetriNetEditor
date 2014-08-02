/** FalseXML.java
 *  @author Kvita Jakub
 */
package except;

/**
 * Trida implementujici vyjimku, ktera se vyvola pri parsovani xml, ktere neni pro nas program.
 * @author Kvita Jakub
 */
public class FalseXML extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Jen vola nadrazeny konstruktor.
	 */
	public FalseXML() {
		super();
	}

	/**
	 * Jen vola nadrazeny konstruktor.
	 * @param arg0
	 */
	public FalseXML(String arg0) {
		super(arg0);
	}
	
	/**
	 * Konstruktor.
	 * @param message Zprava.
	 * @param cause Duvod.
	 */
	public FalseXML(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Konstruktor.
	 * @param cause Duvod.
	 */
	public FalseXML(Throwable cause) {
		super(cause);
	}
}

//EOF