/** NetItem.java
 *  @author Kvita Jakub
 */
package pnet;

import java.awt.*;
import org.dom4j.Element;

/**
 * @author Kvita Jakub
 *	Rozhrani vsech objektu v Petriho sitich {@link PetriNet}
 */
public interface NetItem {

	/**
	 * Zjisti jestli je objekt na zadanych souradnicich.
	 * @param x X-ova souradnice pozice.
	 * @param y Y-ova souradnice pozice.
	 * @return True nebo false.
	 */
	public boolean isThere(int x, int y);
	
	/**
	 * Nastavi text v objektu na novou hodnotu.
	 * @param str Nova hodnota textu v objektu.
	 */
	public void setText(String str);
	
	/**
	 * Vrati text, ktery je ulozen v objektu
	 * @return Vraceny text.
	 */
	public String getText();
	
	/**
	 * Nakresli objekt se vsim vsudy.
	 * @param g Kam se bude kreslit.
	 * @param c Jakou barvou se bude kreslit.
	 */
	public void paintItem(Graphics g, Color c);
	
	/**
	 * Jestli je tento objekt navazany na objekt predany jako parametr. Funguje pouze u hran, ktere jsou navazane
	 * na mista a prechody.
	 * @param i Objekt, na ktery ma byt tento objekt navazany.
	 * @return True nebo false.
	 */
	public boolean isBounded(NetItem i);
	
	/**
	 * Premisti objekt na novou pozici.
	 * @param x X-ova souradnice nove pozice.
	 * @param y Y-ova souradnice nove pozice.
	 */
	public void relocate(int x, int y);
	
	/**
	 * Naplni Element hodnotami o tomto objektu, pro prevod site do objektu Document a xml.
	 * @param e
	 */
	public void fillElement(Element e);
}
//EOF