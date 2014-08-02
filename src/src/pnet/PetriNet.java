/** PetriNet.java
 *  @author Kvita Jakub
 */
package pnet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.awt.Graphics;
import java.awt.Color;
import pnet.Arc;
import pnet.Place;
import pnet.Transition;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Attribute;
import except.FalseXML;
import org.dom4j.DocumentException;
import configuration.*;
import except.FalseSimulation;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.nfunk.jep.JEP;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Trida pro udrzovani struktury Petriho site v pameti, pracuje s ni MyPanel.
 * @author Kvita Jakub
 */
public class PetriNet {

	/**
	 * Seznam prvku v siti. Mista a prechody jsou na zacatku, hrany na konci.
	 */
	private ArrayList<NetItem> items   = new ArrayList<NetItem>();
	
	/**
	 * Aktivni prvek site.
	 */
	private NetItem active=null;	
	

	/**
	 * @param x1 X-ova pozice zacatku hrany.
	 * @param y1 Y-ova pozice zacatku hrany.
	 * @param x2 X-ova pozice konce hrany.
	 * @param y2 Y-ova pozice konce hrany.
	 * Metoda se pokusi vytvorit hranu s pocatecnim a koncovym bodem, pokud je v zadanych bodech vhodny objekt a hrana muze existovat.
	 */
	public void addArc(int x1, int y1, int x2, int y2){
		
		NetItem help; //pomocny prvek pro ulozeni k hrane
		
		//pokud neni nic na konci tak koncim
		if(! setAct(x2,y2))
			return;
		
		//ulozim si prvniho
		help=active;
		
		//pokud nic neni na zacatku tak taky koncim
		if(! setAct(x1,y1))
			return;
		
		//jeden musi byt Place a druhy Transition - nemuze to byt jeden a ten samy objekt
		if(! ((help instanceof Place && active instanceof Transition ) ||
			  (help instanceof Transition && active instanceof Place )))
			return;
		
		//udelam novou hranu s odkazy na dane prvky
		active=new Arc(active, help);
		
		//pridam do seznamu na zacatek aby se vykreslovaly nejdriv hrany
		items.add(active);
	}
	
	/**
	 * @param x X-ova pozice noveho prechodu.
	 * @param y Y-ova pozice noveho prechodu.
	 * Prida do site novy prechod pokud na zadanem miste jeste zadny objekt neni.
	 */
	public void addTrans(int x, int y){
		
		//pokud na tom miste neco je tak nic nepridavam a jen nastavim aktivitu
		if(setAct(x,y))
			return;

		//aktivni je novy prvek
		active=new Transition(x,y);
		
		//zaroven ho dam do seznamu
		items.add(0,active);		
	}
	

	/**
	 * @param x X-ova pozice noveho mista.
	 * @param y Y-ova pozice noveho mista.
	 * Prida do site nove misto pokud na zadanem miste neni zadny prvek.
	 */
	public void addPlace(int x, int y){
		
		//pokud na tom miste neco je tak nic nepridavam a jen nastavim aktivitu
		if(setAct(x,y))
			return;

		//aktivni je novy prvek
		active=new Place(x,y);
		
		//zaroven ho dam do seznamu
		items.add(0,active);	
	}
	
	/**
	 * Zrusi aktivitu site. NEsmaze aktivni prvek!
	 */
	public void clearAct(){
		active = null;
	}
	
	/**
	 * Nastavi aktivitu na prvek site na zadanem miste.
	 * @param x X-ova souradnice zadaneho mista.
	 * @param y Y-ova souradnice zadaneho mista.
	 * @return Pokud nastavil aktivitu tak true, pokud na miste nic neni tak false.
	 */
	public boolean setAct(int x, int y){
		
		for(NetItem i : items){
						
			if(i.isThere(x, y)){
				active=i;
				return true;
			}
			
		}
		//nenasel jsem zadny prvek takze zrusim aktivitu a vratim false
		active=null;
		return false;
	}
	
	/**
	 * Nastavi text na aktivnim prvku.
	 * @param str Text, ktery se ma pridat.
	 */
	public void setActText(String str){
		
		if(active != null)
			active.setText(str);
	}
	
	/**
	 * Odstrani aktivni prvek z site.
	 */
	public void deleteAct(){
		//smazu prvek
		items.remove(active);
		
		//hledam zavisle hrany a ukladam si je
		ArrayList<NetItem> bounds= new ArrayList<NetItem>();
		for(NetItem i :items){
			if(i.isBounded(active))
				bounds.add(i);
		}
		//vsechny zavisle hrany smazu taky - ty uz nemaji zadnou zavislost
		for(NetItem i : bounds){
			items.remove(i);
		}
		
		//zrusim aktivniho
		active=null;
	}
	
	/**
	 * Premisti aktivni prvek site na zadane souradnice.
	 * @param x X-ova souradnice nove pozice.
	 * @param y Y-ova souradnice nove pozice.
	 */
	public void relocateAct(int x, int y){
		if(active != null)
			active.relocate(x,y);
	}
	
	/**
	 * Nakresli vsechny prvky site.
	 * @param g
	 */
	public void paintNet(Graphics g){
		
      Graphics2D g2 = (Graphics2D)g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON);
		
		for( NetItem i : items){
			
			if(i==active)
				i.paintItem(g2, NetConf.getColorAct());
			else
				if(i instanceof Arc)
					i.paintItem(g2, Color.BLACK);
				else
					i.paintItem(g2, NetConf.getColorRest());
		}
		
	}
	
	/**
	 * Odsimuluje vsechny mozne kroky site. Pokud je sit spatna tak dela vyjimku.
	 * @throws FalseSimulation Neni mozno sit odsimulovat.
	 */
	public void simulate() throws FalseSimulation{
		
		int MAX_STEPS = 150;
		
		//pokud je simulateStep() true - neco se simuluje - tak pokracuju
		//pri chybe se propusti vyjimka
		for(int i=0;  i<MAX_STEPS && simulateStep()  ;i++);
	}
	
	/**
	 * Odsimuluje jeden krok site. Pokud je sit spatna tak dela vyjimku.
	 * @return Jestli se povedlo udelat nejaky krok simulace.
	 * @throws FalseSimulation Neni mozno sit odsimulovat. Zprava u vyjimky se posila uzivateli, kde je zobrazena takze podrobne popsat co se stalo!
	 */
	public boolean simulateStep() throws FalseSimulation{
		
		//vsechny hrany v siti
		ArrayList<Arc> allArcs = new ArrayList<Arc>();
		//vsechny prechody v siti
		ArrayList<Transition> allTrans = new ArrayList<Transition>();
		
		//rozdelim seznam objektu na vsechny prechody a hrany
		for(NetItem i : items){
			if(i instanceof Arc)
				allArcs.add((Arc)i);
			else if(i instanceof Transition)
				allTrans.add((Transition)i);
		}		
		
		//zamicham je aby nebyly porad ve stejnem poradi
		Collections.shuffle(allArcs);
		Collections.shuffle(allTrans);
		
		//vstupni hrany
		ArrayList<Arc> inputArcs = new ArrayList<Arc>();
		//vystupni hrany
		ArrayList<Arc> outputArcs = new ArrayList<Arc>();
		
		//pro kazdy prechod najdu hrany co vedou do nej a z nej a zkusim nad nim udelat prechod
		for(Transition t : allTrans){
			
			inputArcs = findInArcs(t, allArcs);
			outputArcs = findOutArcs(t, allArcs);
			
			//zkusim udelat prechod
			if(simulateTrans(t, inputArcs, outputArcs))
				return true;		
		}
		
		//nepovedlo se udelat prechod
		return false;
	}
	
	//seznam vsech siti jdoucich do prechodu
	private ArrayList<Arc> findInArcs(Transition t, ArrayList<Arc> allArcs){
		
		ArrayList<Arc> inArc = new ArrayList<Arc>();
		
		for(Arc a : allArcs){
			if(t == a.getTarget())
				inArc.add(a);
		}
		return inArc;
	}
	
	//seznam vsech siti jdoucich z prechodu
	private ArrayList<Arc> findOutArcs(Transition t, ArrayList<Arc> allArcs){
		
		ArrayList<Arc> inArc = new ArrayList<Arc>();
		
		for(Arc a : allArcs){
			if(t == a.getStart())
				inArc.add(a);
		}
		return inArc;
	}
	
	//zkusi udelat simulaci nad zadanym prechodem a vstupnima a vystupnima hranama
	private boolean simulateTrans(Transition t, ArrayList<Arc> inputArcs, ArrayList<Arc> outputArcs) throws FalseSimulation {
		
		//jestli tam je jen jedno svislitko
		if(t.getText().indexOf("|") != t.getText().lastIndexOf("|"))
			throw new FalseSimulation("transition can't contain more than one \"|\" .");
		
		//texty v prechodu
		String condition = null;
		String resVar = null;
		//jejich rozebrani do dvou retezcu
		if(t.getText().indexOf("|")!=-1){
			condition = t.getText().substring(0, t.getText().indexOf("|"));
			resVar =  t.getText().substring(t.getText().indexOf("|")+1);
		}
		else{
			condition = t.getText();
			resVar = new String();
		}
		
		condition = condition.replaceAll("&", "&&");
		
		
		JEP myParser = new JEP();
		myParser.addStandardFunctions();
		//muzou se vytvaret nove promenne ve vyrazu
		myParser.setAllowUndeclared(true);
		
		
		Arc[]  inputArcArray  =  new Arc[inputArcs.size()];
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[]  placeListArray  =  (ArrayList<Integer>[]) new ArrayList[inputArcs.size()];
		@SuppressWarnings("unchecked")
		Iterator<Integer>[]  placeIteratorArray  =  (Iterator<Integer>[]) new Iterator[inputArcs.size()];
		
		int index = -1;
		
		//zpracuju kazdou vstupni hranu a naplnim pole(zasobniky)
		for(Arc a : inputArcs){
			
			index++;
			
			if(!a.getText().matches("([a-z]|[0-9]*)") && !a.getText().matches(""))
				throw new FalseSimulation("One Arc has bad text input.");
			
			
			inputArcArray[index] = a;
			placeListArray[index] = parsePlaceText(a.getStart().getText());
			
			//pokud tam nic neni tak se ani nemusim snazit
			if(placeListArray[index].size() < 1)
				return false;
			
			//pokud hledam promennou tak ji pridam do parsovace a inizializuju iterator
			if(inputArcArray[index].getText().matches("[a-z]")){
				//iterator
				placeIteratorArray[index] = placeListArray[index].iterator();
				//nova promenna s hodnotou
				myParser.addVariable(inputArcArray[index].getText(), placeIteratorArray[index].next());
				
			}
			//pokud je to konstanta tak ji zkusim najit v seznamu a nastavit iterator na ni..pokud ji nenajdu tak koncim protoze nemuzu pokracovat
			else if(inputArcArray[index].getText().matches("[0-9]+")){
				
				placeIteratorArray[index] = placeListArray[index].iterator();
				
				int x = placeIteratorArray[index].next();
				//hledam cislo ktere se rovna konstante
				while(!Integer.toString(x).equals(inputArcArray[index].getText())){
					
					//nenasel jsem - pokud je dalsi tak pokracuju
					if(placeIteratorArray[index].hasNext()){
						 x = placeIteratorArray[index].next();
					}
					//pokud neni dalsi tak nemuzu udelat prechod
					else{
						return false;
					}
					
				}
				//nasel jsem prvek ktery je stejny jako konstanta
				//do parseru nic nepridavam protoze konstantu nepotrebuju
				
			}
			else{
				throw new FalseSimulation("One input Arc has bad text.");
			}
			
		}
		
		//TISK
		System.out.println("Podminka prechodu: "+condition);
		System.out.println("Vysledek: "+resVar);
		for(int i = 0; i< inputArcArray.length;i++){
			if(inputArcArray[i].getText().matches("[a-z]"))
				System.out.println("Variable: "+inputArcArray[i].getText()+" = "+myParser.getVarValue(inputArcArray[i].getText()).toString()+"  "+placeListArray[i]);
			else
				System.out.println("Constant: "+inputArcArray[i].getText()+"  "+placeListArray[i]);
		}
		
		

		//vlozim do parseru podminku
		myParser.parseExpression(condition);
		//dokud je nepruchodna tak menim backtrackingem mozne hodnoty promennych - pokud to nejde tak vratim false ze se prechod neda provest
		
		
		System.out.println("Condition value:  "+myParser.getValue());
		
		
		Integer numb=null;
		//nastavim index pred cyklem na posledni prvek pole
		index = inputArcArray.length-1;
		
		//pokud tam neni zadny text tak to automaticky projde pri navazani vsech promennych
		if(!condition.equals("")){
			//samotny backtracking pres hodnoty promennych
			while(myParser.getValue() != 1.0){
				//muze to byt vic veci - davat si na to pozor
				if(myParser.getValue() != 0.0)
					throw new FalseSimulation("First part of transition is not supported.");
				
				
				//skocim na dalsi promennou
				while(true){
					
					//pokud jsem projel vsechny moznosti tak koncim
					if(index<0)
						return false;
					
					if(inputArcArray[index].getText().matches("[a-z]")){
						//je to promenna tak posunu index
						if(placeIteratorArray[index].hasNext()){
							numb = placeIteratorArray[index].next();
							myParser.addVariable(inputArcArray[index].getText(), numb);
							//pokud jsem dokazal posunout index tak jsem nasel dalsi moznost a koncim vnitrni cyklus
							break;
						}
						else{
							placeIteratorArray[index] = placeListArray[index].iterator();
							numb = placeIteratorArray[index].next();
							myParser.addVariable(inputArcArray[index].getText(), numb);
							index--;
							//musim pokracovat s nizsi promennou
							continue;
						}
					}
					else{
						//je to konstanta takze s ni nemuzu hybat
						index--;
						//zkusim snizit nizsi promennou
						continue;
					}
				}
				
				//nasel jsem dalsi moznost - inicializuju index
				index = inputArcArray.length-1;
				
				//a jedu znova kontrolu jestli ta podminka ted nebude 1
			}
		}
		
		//prechod se da provest
		
		// povoleno prirazovani do promennych
		myParser.setAllowAssignment(true);
		//prehodim parsovany vyraz na druhou pulku prechodu
		myParser.parseExpression(resVar);
		//ohodnotim ho
		//zkontroluju jestli to neni nejaka chyba
		
		if(myParser.getValue() == Double.NaN){
			throw new FalseSimulation("Second part of transition is not supported.");
		}
		
		//odstranuju hodnoty ze vstupnich hran
		for(int i=0; i< placeIteratorArray.length ; i++){
			//zrusim prvek u jednoho mista v pameti
			placeIteratorArray[i].remove();
			//pridam zbyle prvky mista do tohoto mista
			inputArcArray[i].getStart().setText(placeListToStr(placeListArray[i]));
		}
		
		//pridavam hodnoty do vystupnich hran
		for(Arc a : outputArcs){
			
			//pouziju zasobniky protoze jsou volne a potrebuju nejakou promennou
			placeListArray[0] = parsePlaceText(a.getTarget().getText());
			
			if(a.getText().matches("[a-z]")){
				//je to promenna
				//pridam tam hodnotu promenne
				System.out.println(a.getText()+" = ");
				
				//dostanu vyslednou promennou jako double - pretypuju na int a obalim Integerem
				Integer n = null;
				try{
					n = new Integer((int)Double.parseDouble(myParser.getVarValue(a.getText()).toString()));
				}catch(NullPointerException e){
					throw new FalseSimulation("Unbounded variable in second part of transition. Multiple assigments not supported.");
				}
				//pridam ji do seznamu
				placeListArray[0].add(n);
				//nacpu to zpatky do mista
				a.getTarget().setText(placeListToStr(placeListArray[0]));
				
			}
			else if(a.getText().matches("[0-9]+")){
				//je to konstanta
				placeListArray[0].add(Integer.parseInt(a.getText()));
				//nacpu to zpatky do mista
				a.getTarget().setText(placeListToStr(placeListArray[0]));
			}
			else{
				//nic tam neni nebo tam je blbost
				throw new FalseSimulation("One Arc has bad text input.");
			}
		}
		
		return true;
	}
	
	
	//prekovnertuje seznam u mista do textu
	private String placeListToStr(ArrayList<Integer> arcList) {
		
		String ret = new String();
		
		if(arcList.size() == 0)
			return new String("");
		
		for(Integer i : arcList){
			ret += i;
			ret += ",";
		}
		
		return ret.substring(0, ret.length()-1);
	}

	/**
	 * Prozkouma retezec u Mista a vrati hodnoty ktere se v nem nachazeji.
	 * @param s Zkoumany retezec.
	 * @return Seznam cisel.
	 * @throws FalseSimulation 
	 */
	private ArrayList<Integer> parsePlaceText(String s) throws FalseSimulation{
		
		if(!s.matches("^[0-9]+(,[0-9]+)*$") && !s.matches(""))
			throw new FalseSimulation("One of Places contains wrong input.");
		
		ArrayList<Integer> ret = new ArrayList<Integer>();
		
		//hledam cisla
		Pattern p = Pattern.compile("[0-9]+");
		Matcher m = p.matcher(s);
		
		//nasypu vysledky do seznamu
		while(m.find()){
			ret.add(new Integer(m.group()));
		}
		
		return ret;
	}
	
	
	/**
	 * Vytvori z prvku site dokument, ktery je mozno ukladat do XML a posilat streamy.
	 * @return Vytvoreny dokument.
	 */
	public Document toDoc(){
		
		Document xml = DocumentHelper.createDocument();
		Element root = xml.addElement("net");
		
		Element e;
		
		for(NetItem i : items){
			
			if(i instanceof Transition)
				e = root.addElement("Transition");
			else if(i instanceof Place)
				e = root.addElement("Place");
			else
				e = root.addElement("Arc");
			
			i.fillElement(e);
		}

		return xml;
	}

	
	/**
	 * Zahodi sit a vytvori novou podle zadaneho dokumentu.
	 * @param doc Vstupni dokument.
	 * @throws FalseXML XML ma spatnou strukturu, neni to nase xml.
	 * @throws DocumentException Neco u vnitri prace s dokumentem.
	 * TODO spravit tvar funkce aby to nevypadalo tak nechutne a zachytavalo to vsechny problemy
	 */
	@SuppressWarnings("rawtypes")
	public void fromDoc(Document doc) throws FalseXML, DocumentException{
		
		items.clear();
		
		Element root = doc.getRootElement();
		int x1;
		int y1;
		int x2;
		int y2;
		List l;
		Attribute atr;
		Element child;
		String text;
		
		for(Iterator i = root.elementIterator(); i.hasNext();){
			Element elem = (Element) i.next();
			
			if(elem.getQualifiedName().equals("Place")){
				
				l = elem.attributes();
				
				if(l.size() != 2)
					throw new FalseXML();
				
				//atribut 1
				atr = (Attribute) l.get(0);
				if(!atr.getQualifiedName().equals("x"))
					throw new FalseXML();
				
				x1=Integer.parseInt(atr.getValue());
				
				
				atr = (Attribute) l.get(1);
				if(!atr.getQualifiedName().equals("y"))
					throw new FalseXML();
				
				y1=Integer.parseInt(atr.getValue());
				
				//iterator elementu
				l = elem.elements();
				
				if(l.size() != 1)
					throw new FalseXML();
				
				child = (Element) l.get(0);
				
				if(!child.getQualifiedName().equals("text"))
					throw new FalseXML();
				
				text = child.getText();
				
				addPlace(x1,y1);
				setActText(text);
				
			}
			else if(elem.getQualifiedName().equals("Transition")){
				
				l = elem.attributes();
				
				if(l.size() != 2)
					throw new FalseXML();
				
				//atribut 1
				atr = (Attribute) l.get(0);
				if(!atr.getQualifiedName().equals("x"))
					throw new FalseXML();
				
				x1=Integer.parseInt(atr.getValue());
				
				
				atr = (Attribute) l.get(1);
				if(!atr.getQualifiedName().equals("y"))
					throw new FalseXML();
				
				y1=Integer.parseInt(atr.getValue());
				
				//iterator elementu
				l = elem.elements();
				
				if(l.size() != 1)
					throw new FalseXML();
				
				child = (Element) l.get(0);
				
				if(!child.getQualifiedName().equals("text"))
					throw new FalseXML();
				
				text = child.getText();
				
				addTrans(x1,y1);
				setActText(text);
				
			}
			else if(elem.getQualifiedName().equals("Arc")){
				
				l = elem.elements();
				
				if(l.size() != 3)
					throw new FalseXML();
				
				child = (Element) l.get(0);
				
				if(!child.getQualifiedName().equals("start"))
					throw new FalseXML();
				//start
				x1=Integer.parseInt(child.attributeValue("x"));
				y1=Integer.parseInt(child.attributeValue("y"));
				
				child = (Element) l.get(1);
				
				if(!child.getQualifiedName().equals("target"))
					throw new FalseXML();
				//target
				x2=Integer.parseInt(child.attributeValue("x"));
				y2=Integer.parseInt(child.attributeValue("y"));
			
				child = (Element) l.get(2);
				
				if(!child.getQualifiedName().equals("text"))
					throw new FalseXML();
				
				text = child.getText();
				
				addArc(x1,y1,x2,y2);
				setActText(text);
				
			}
			else{
				//neni to ani jedno z mych takze spatne
				throw new FalseXML();
			}
		}
		
	}
}

//EOF