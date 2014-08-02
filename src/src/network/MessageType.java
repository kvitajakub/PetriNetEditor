/**
 * MessageType.java
 * @author Kvita Jakub
 */
package network;

/**
 * Typy zprav ktere si muze klient a server zasilat.
 * Vysledky serveru se zasilaji se stejnym typem jako prijimana zprava.
 * Potvrzeni jsou OK a ERR.
 * @author Kvita Jakub
 */
public enum MessageType {
	
	/**Prihlaseni uzivatele k uctu na serveru.  */
	LOGIN,
	/**Registrace noveho uctu na serveru a prihlaseni se k nemu. */
	REGISTER,
	/** Potvrzeni ze strany serveru. */
	OK,
	/** Potvrzeni ze strany serveru. */
	ERR,
	/** Vypis vsechn siti a uzivatelu na serveru. se str1 umi vyhledavat */
	NETLIST,
	/** Otevre lokalne sit ze serveru. */
	OPEN,
	/** Smaze sit na serveru. */
	DELETE,
	/** Ulozeni site na serveru.  */
	SAVE,
	/** Simulace vsech kroku site. */
	SIMULATE,
	/** Krok simulace site */
	SIMULATE_STEP,
	/** Ukonceni spojeni se serverem */
	DISCONNECT
}

//EOF