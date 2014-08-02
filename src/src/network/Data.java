/**
 * Data.java
 * @author Kvita Jakub
 */
package network;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import java.io.*;

/**
 * Trida pro objekty posilane v tomto programu pres sit.
 * @author Kvita Jakub
 */
public class Data implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private MessageType type;
	private String str1;
	private String str2;
	private String docstr;
	
	/**
	 * Vytvori objekt bez parametru.
	 */
	public Data(){
		super();
		type = null;
		str1 = null;
		str2 = null;
		docstr = null;
	}
	
	/**
	 * Vytvori objekt se zadanymi vlastnostmi.
	 * @param type  Typ zpravy kterou zasilame.(potreba vzdy.)
	 * @param s1 Viceucelovy retezec - login uzivatele, jmeno site.
	 * @param s2 Viceucelovy retezec - heslo uzivatele, popis site.
	 * @param doc	Dokument kde muze byt obsazena Petriho sit ktere se zprava tyka nebo struktura souboru.
	 */
	public Data(MessageType type, String s1, String s2, Document doc) {
		super();
		this.type = type;
		this.str1 = s1;
		this.str2 = s2;
		if(doc != null)
			this.docstr = doc.asXML();
		else
			this.docstr = null;
	}

	/**
	 * @return Hodnota pole type.
	 */
	public MessageType getType() {
		return type;
	}
	/**
	 * @param type Nastavi hodnotu pole type.
	 */
	public void setType(MessageType type) {
		this.type = type;
	}
	/**
	 * @return Hodnota pole str1.
	 */
	public String getstr1() {
		return str1;
	}
	/**
	 * @param s Nastavi pole str1
	 */
	public void setstr1(String s) {
		this.str1 = s;
	}
	/**
	 * @return Hodnota pole str2.
	 */
	public String getstr2() {
		return str2;
	}
	/**
	 * @param s Nastavi hodnotu pole str2.
	 */
	public void setstr2(String s) {
		this.str2 = s;
	}
	/**
	 * @return Hodnota pole doc.
	 */
	public Document getDoc() {
		
		if(this.docstr == null)
			return null;
		else{
		try {
				return DocumentHelper.parseText(docstr);
			} catch (DocumentException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	/**
	 * @param doc Nastavi hodnotu pole doc.
	 */
	public void setDoc(Document doc) {
		if(doc == null)
			this.docstr = null;
		else
			this.docstr = doc.asXML();
	}
}

//EOF