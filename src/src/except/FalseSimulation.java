/**
 * FalseSimulation.java
 * @author Kvita Jakub
 */
package except;

/**
 * Trida pro vyjimku pri simulaci kdy texty v objektech jsou nevhodne pro simulaci.
 * @author Kvita Jakub
 */
public class FalseSimulation extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor.
	 */
	public FalseSimulation() {
		super();
	}

	/**
	 * Konstruktor.
	 * @param message Zprava.
	 * @param cause Duvod.
	 */
	public FalseSimulation(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Konstruktor.
	 * @param cause Duvod.
	 */
	public FalseSimulation(Throwable cause) {
		super(cause);
	}

	/**
	 * Konstruktor.
	 * @param arg0 Zprava.
	 */
	public FalseSimulation(String arg0) {
		super(arg0);
	}
}

//EOF