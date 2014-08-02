/**
 * ManageNets.java
 * @author Kvita Jakub
 */
package client;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;
import javax.swing.JTabbedPane;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import java.util.List;
import configuration.ClConf;
import configuration.NetConf;
import except.BadConnect;
import except.FalseXML;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import client.DescriptionFrame;
import network.Data;
import network.MessageType;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
/**
 * Trida pro zobrazeni siti na serveru a dalsi praci s nimi.
 * @author Kvita Jakub
 */
public class ManageNets extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	
	private JTextField serchField;
	private JButton buttonSearch;
	private JComboBox comboUser;
	private JComboBox comboNet;
	private JButton buttonDetails;
	private JButton buttonOpen;
	private JButton buttonDelete;
	private JComboBox comboVersion;
	
	private MainGui frame;
	private JTabbedPane pane;
	Element serverroot;
	
	String[] names;
	String[][] nets;
	String[][][] versions;
	String[][][] descriptions;
		
	
	/**
	 * Konstruktor.
	 * @param frame Hlavni okno - potreba pri vytvareni novych panelu.
	 * @param pane TabbedPane at mam kde vytvaret panely.
	 */
	public ManageNets(MainGui frame,JTabbedPane pane) {
		super();
		this.frame=frame;
		this.pane=pane;
		
		if(ClConf.server == null)
			return;
		
		
		try {
			init();
			initGUI();
		} catch (BadConnect e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"RBad connection to server. Disconnection recommended.",
				    "Connection Error",JOptionPane.ERROR_MESSAGE);	
			return;
		}
		
		//zmenim uzivatele na aktivniho
		comboUser.setSelectedItem(ClConf.getUsername());
		int index = comboUser.getSelectedIndex();
		
		comboNet.setModel(new DefaultComboBoxModel(nets[index]));
		
		try{
			comboVersion.setModel(new DefaultComboBoxModel(versions[index][0]));
		} catch(ArrayIndexOutOfBoundsException e){
			comboVersion.setModel(new DefaultComboBoxModel(new String[] {}));
		}
		
		this.repaint();
	}
	
	
	private void init() throws BadConnect{
		
		Data data = new Data(MessageType.NETLIST,null,null,null);
			ClConf.server.send(data);
			data = ClConf.server.receive();
		//mam dokument se vsim co je na serveru
		serverroot = data.getDoc().getRootElement();
		
		parseDoc();
	}
	
	
	//rozebere dokument serveru na slozky vhodne k zpracovani v tomto okne
	@SuppressWarnings("unchecked")
	private void parseDoc(){
		//seznam uzivatelu
		List<Element> u = serverroot.elements();
		//seznam siti
		List<Element> n;
		//seznam verzi
		List<Element> v;
		//n.size();
		
		names = new String[u.size()];
		nets = new String[u.size()][];
		versions = new String[u.size()][][];
		descriptions = new String [u.size()][][];
		
		int i=0;
		int j=0;
		int k=0;
		for(Element e : u){
			
			names[i]= e.attribute("login").getValue();
			
			n = e.elements();
			
			nets[i] = new String[n.size()];
			versions[i] = new String[n.size()][];
			descriptions[i] = new String[n.size()][];
			
			j=0;
			for(Element f : n){
				
				nets[i][j]=f.attribute("name").getText();
			
				v = f.elements();
				
				versions[i][j] = new String[v.size()];
				descriptions[i][j] = new String[v.size()];
				
				//s verzema jdu odzadu aby byly nejnovejsi nejdrive
				k=v.size()-1;
				for(Element g : v){
				
					versions[i][j][k]=convVerToPrint(g.attribute("time").getText());
					descriptions[i][j][k]=g.attribute("description").getText();
					
					k--;
				}
				j++;
			}
			i++;
		}
	}
	
	//konverze jmen verzi kvuli tomu ze ':' nemuze byt ve jmenu souboru
	private String convVerToPrint(String s){
		return s.replace('_',':');
	}
	
	private String convVerToSave(String s){
		return s.replace(':','_');
	}
	
	private void initGUI() {
		try {
			GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			
			this.setTitle("Manage nets on server");
			
			
			{
				serchField = new JTextField();
			}
			{
				buttonSearch = new JButton();
				buttonSearch.setText("Search");
				buttonSearch.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						buttonSearchActionPerformed(evt);
					}
				});
			}
			{
				ComboBoxModel comboUserModel = 
						new DefaultComboBoxModel(names);
				comboUser = new JComboBox();
				comboUser.setModel(comboUserModel);
				comboUser.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						comboUserActionPerformed(evt);
					}
				});
			}
			{
				ComboBoxModel comboNetModel = 
						new DefaultComboBoxModel(new String[]{});
				comboNet = new JComboBox();
				comboNet.setModel(comboNetModel);
				comboNet.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						comboNetActionPerformed(evt);
					}
				});
			}
			{
				ComboBoxModel comboVersionModel = 
						new DefaultComboBoxModel(new String[]{});
				comboVersion = new JComboBox();
				comboVersion.setModel(comboVersionModel);
				comboVersion.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						comboVersionActionPerformed(evt);
					}
				});
			}
			{
				buttonDetails = new JButton();
				buttonDetails.setText("Details");
				buttonDetails.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						buttonDetailsActionPerformed(evt);
					}
				});
			}
			{
				buttonDelete = new JButton();
				buttonDelete.setText("Delete");
				buttonDelete.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						buttonDeleteActionPerformed(evt);
					}
				});
			}
			{
				buttonOpen = new JButton();
				buttonOpen.setText("Open");
				buttonOpen.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						buttonOpenActionPerformed(evt);
					}
				});
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addContainerGap()
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(buttonSearch, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
				    .addComponent(serchField, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
				.addGap(39)
				.addComponent(comboUser, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
				.addGap(19)
				.addComponent(comboNet, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
				.addGap(23)
				.addComponent(comboVersion, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
				.addGap(41)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(buttonDelete, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
				    .addComponent(buttonOpen, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
				    .addComponent(buttonDetails, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(19, 19));
			thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup()
				.addContainerGap()
				.addGroup(thisLayout.createParallelGroup()
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addComponent(serchField, 0, 350, Short.MAX_VALUE)
				        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				        .addComponent(buttonSearch, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
				        .addGap(0, 13, GroupLayout.PREFERRED_SIZE))
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addComponent(comboUser, GroupLayout.PREFERRED_SIZE, 315, GroupLayout.PREFERRED_SIZE)
				        .addGap(10)
				        .addComponent(buttonOpen, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
				        .addGap(0, 13, Short.MAX_VALUE))
				    .addGroup(thisLayout.createSequentialGroup()
				        .addGap(45)
				        .addGroup(thisLayout.createParallelGroup()
				            .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				                .addComponent(buttonDelete, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
				                .addGap(42)
				                .addComponent(buttonDetails, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
				                .addGap(0, 152, Short.MAX_VALUE))
				            .addGroup(thisLayout.createSequentialGroup()
				                .addPreferredGap(buttonDelete, comboNet, LayoutStyle.ComponentPlacement.INDENT)
				                .addGroup(thisLayout.createParallelGroup()
				                    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				                        .addComponent(comboNet, 0, 311, Short.MAX_VALUE)
				                        .addGap(0, 71, GroupLayout.PREFERRED_SIZE))
				                    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				                        .addGap(78)
				                        .addComponent(comboVersion, 0, 304, Short.MAX_VALUE)))))))
				.addContainerGap());
			pack();
			this.setSize(479, 351);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}
	
	//zmenil se USER
	private void comboUserActionPerformed(ActionEvent evt) {
		System.out.println("comboUser.actionPerformed, event="+evt);
		
		int index = comboUser.getSelectedIndex();
		
		comboNet.setModel(new DefaultComboBoxModel(nets[index]));
		
		try{
			comboVersion.setModel(new DefaultComboBoxModel(versions[index][0]));
		} catch(ArrayIndexOutOfBoundsException e){
			comboVersion.setModel(new DefaultComboBoxModel(new String[] {}));
		}
		
		this.repaint();
	}
	
	//zmenila se NET
	private void comboNetActionPerformed(ActionEvent evt) {
		System.out.println("comboNet.actionPerformed, event="+evt);
		
		int i1 = comboUser.getSelectedIndex();
		int i2 = comboNet.getSelectedIndex();
		
		comboVersion.setModel(new DefaultComboBoxModel(versions[i1][i2]));
		
		this.repaint();
	}
	
	//zmenila se VERSION
	private void comboVersionActionPerformed(ActionEvent evt) {
		System.out.println("comboVersion.actionPerformed, event="+evt);
		//nic nemusim delat
	}
	
	//tlacitko DETAILS
	private void buttonDetailsActionPerformed(ActionEvent evt) {
		System.out.println("buttonDetails.actionPerformed, event="+evt);
		
		int i1 = comboUser.getSelectedIndex();
		int i2 = comboNet.getSelectedIndex();
		int i3 = comboVersion.getSelectedIndex();
		
		String user = null;
		String name = null;
		String version = null;
		String desc = null;
		
		//pokud mam nejakeho uzivatele
		if(i1 != -1){
			user = names[i1];
			if(i2 != -1){
				name = nets[i1][i2];
				if(i3 != -1){
					version = versions[i1][i2][i3];
					desc= descriptions[i1][i2][i3];
				}
			}
		}
		
		DescriptionFrame inst = new DescriptionFrame(user,name,version,desc);
		inst.setLocationRelativeTo(this);
		inst.setVisible(true);	
	}
	
	//tlacitko OPEN
	private void buttonOpenActionPerformed(ActionEvent evt) {
		System.out.println("buttonOpen.actionPerformed, event="+evt);
		
		
		int i1 = comboUser.getSelectedIndex();
		int i2 = comboNet.getSelectedIndex();
		int i3 = comboVersion.getSelectedIndex();
		
		String user = null;
		String name = null;
		String version = null;
		
		if(i1 != -1 && i2 != -1 && i3 != -1){
			user = names[i1];
			name = nets[i1][i2];
			version = convVerToSave(versions[i1][i2][i3]);
		}
		else{
			JOptionPane.showMessageDialog(this,"Choose user, net and version.",
				    "Information",JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("open");
			root.addAttribute("user", user);
			root.addAttribute("name", name);
			root.addAttribute("version",version);
		
		Data data = new Data(MessageType.OPEN,null,null,doc);
		
		try {
			ClConf.server.send(data);
			data=ClConf.server.receive();
		} catch (BadConnect e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"Bad connection to server. Disconnection recommended.",
				    "Error",JOptionPane.ERROR_MESSAGE);
			this.dispose();
		}
		
		if(data.getType() == MessageType.ERR){
			JOptionPane.showMessageDialog(this,"Net wasn't transmitted.",
				    "Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		
		
		//vytvorim novou sit v novem panelu
		//novy panel
		MyPanel jpanel = new MyPanel(frame);
		
		try {
			jpanel.fromDoc(data.getDoc());
			
		} catch (FalseXML e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"Net is badly formatted.",
				    "Error",JOptionPane.ERROR_MESSAGE);
			return;
		} catch (DocumentException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"Net is badly formatted.",
				    "Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		jpanel.setBackground(NetConf.getColorBack());
		jpanel.setServername(name);
		//pridame tab do paneu
		pane.addTab(null, jpanel);
		//prepne aktivitu na vytvoreny tab
		pane.setSelectedIndex(pane.getTabCount()-1);
		//nastavim headline
		pane.setTabComponentAt(pane.getSelectedIndex(), new MyPanelHeadline(name,pane));
		
		pane.repaint();
		this.dispose();
	}
	
	//tlacitko DELETE
	private void buttonDeleteActionPerformed(ActionEvent evt) {
		System.out.println("buttonDelete.actionPerformed, event="+evt);
		
		int i1 = comboUser.getSelectedIndex();
		int i2 = comboNet.getSelectedIndex();
		int i3 = comboVersion.getSelectedIndex();
		
		String user = null;
		String name = null;
		String version = null;
		
		if(i1 != -1 && i2 != -1 && i3 != -1){
			user = names[i1];
			name = nets[i1][i2];
			version = convVerToSave(versions[i1][i2][i3]);
		}
		else{
			JOptionPane.showMessageDialog(this,"Choose user, net and version.",
				    "Information",JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("delete");
			root.addAttribute("user", user);
			root.addAttribute("name", name);
			root.addAttribute("version",version);
		
		Data data = new Data(MessageType.DELETE,null,null,doc);
		
		try {
			ClConf.server.send(data);
			data=ClConf.server.receive();
		} catch (BadConnect e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"Bad connection to server. Disconnection recommended.",
				    "Error",JOptionPane.ERROR_MESSAGE);
			this.dispose();
		}
		
		if(data.getType() == MessageType.ERR){
			JOptionPane.showMessageDialog(this,"Net wasn't deleted.",
				    "Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		
		try {
			init();
		} catch (BadConnect e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"Bad connection to server. Disconnection recommended.",
				    "Error",JOptionPane.ERROR_MESSAGE);	
			this.dispose();
			return;
		}
		
		//zmenim uzivatele na toho co tam byl
		comboUser.setSelectedItem(user);
		int index = comboUser.getSelectedIndex();
		
		comboNet.setModel(new DefaultComboBoxModel(nets[index]));
		comboNet.setSelectedItem(name);
		
		int ind2 = comboNet.getSelectedIndex();
		try{
			comboVersion.setModel(new DefaultComboBoxModel(versions[index][ind2]));
		} catch(ArrayIndexOutOfBoundsException e){
			comboVersion.setModel(new DefaultComboBoxModel(new String[] {}));
		}
		this.repaint();
		
	}
	
	//tlacitko SEARCH
	private void buttonSearchActionPerformed(ActionEvent evt) {
		System.out.println("buttonSearch.actionPerformed, event="+evt);

		String s = serchField.getText();
	
		Data data = new Data(MessageType.NETLIST,s,null,null);
		try {
			ClConf.server.send(data);
			data = ClConf.server.receive();
		} catch (BadConnect e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"Bad connection to server. Disconnection recommended.",
				    "Error",JOptionPane.ERROR_MESSAGE);	
			this.dispose();
			return;
		}
		
		//mam dokument se vsim co je na serveru
		serverroot = data.getDoc().getRootElement();
		//zpracuju ho
		parseDoc();
		
		//refreshnu comba
		//zmenim uzivatele na toho co tam byl
		comboUser.setSelectedItem(ClConf.getUsername());
		int index = comboUser.getSelectedIndex();
		
		comboNet.setModel(new DefaultComboBoxModel(nets[index]));
		
		int ind2 = comboNet.getSelectedIndex();
		try{
			comboVersion.setModel(new DefaultComboBoxModel(versions[index][ind2]));
		} catch(ArrayIndexOutOfBoundsException e){
			comboVersion.setModel(new DefaultComboBoxModel(new String[] {}));
		}
		this.repaint();
	}

}

//EOF