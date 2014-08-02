/** MainGui.java
 *  @author Kvita Jakub
 */
package client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import client.Buttons;
import client.HelpFrame;
import client.MyPanel;
import client.PropertyFrame;
import except.BadConnect;
import except.FalseXML;
import configuration.*;
import client.ConnectFrame;
import client.ManageNets;
import java.lang.Thread;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import network.*;
import client.SaveAsServer;

/** 
 * Trida obsahuje main pro klientskou aplikaci, vytvari hlavni okno aplikace a pracuje s dalsimi castmi programu.
 * @author Kvita Jakub
 */
public class MainGui extends javax.swing.JFrame {
	
	private static final long serialVersionUID = 1L;

	{
		//Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Detekce modu prace s aktivni siti.
	 */
	public Buttons button=Buttons.FALSE;
	
	private JMenuBar menuBar;
		private JMenu menuFile;
			private JMenuItem menuItemNew;
			private JMenuItem menuItemOpen;
			private JMenuItem menuItemSave;
			private JMenuItem menuItemSaveas;
			private JMenuItem menuItemClose;
			private JSeparator menuSepFile;
			private JMenuItem menuItemProp;
			private JSeparator menuSep2File;
			private JMenuItem menuItemExit;
		private JMenu menuServer;	
			private JMenuItem menuItemSimStep;
			private JMenuItem menuItemSim;
			private JSeparator menuSepServer1;
			private JMenuItem menuItemManageSer;
			private JCheckBoxMenuItem menucheckboxDisconnect;
			private JMenuItem menuItemSaveSer;
			private JSeparator menuSepServer2;
			private JCheckBoxMenuItem menucheckboxConnect;
		private JMenu menuHelp;
			private JMenuItem menuItemHelp;
			private JSeparator menuSepHelp;
			private JMenuItem menuItemAbout;
	
	private JTabbedPane tabPane;
		private MyPanel panel;
	
	private JPanel jPanelButtons;
		private JButton buttonArc;
		private JButton buttonPlace;
		private JButton buttonTrans;
		private JButton buttonSelect;
		private JButton buttonDelete;
		private JTextField textField;
		private JButton textButton;	
	
	private int newfiles = 0;
	
	/** Main pro zacatek prace programu klienta
	 * @param args 
	 */
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainGui inst = new MainGui();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	/**
	 * Vnitrni trida pro uklid na konci programu.
	 * @author Kuba
	 */
	private class CleanUp extends Thread{
		private MainGui gui;
		
		public CleanUp(MainGui g){
			super();
			this.gui=g;
		}
		public void run(){
			gui.doACleanExit();
		}
	}
	
	/**
	 * Metoda pro korektni ukonceni programu. Ulozeni nastaveni, ukonceni spojeni se serverem atp.
	 * Pridana v konstruktoru do Runtime.
	 */
	protected void doACleanExit() {
		
		//ulozim si ktere lokalni ulozene site mam otevrene
		ArrayList<String> openFiles = new ArrayList<String>(); 
		File file;
		for(int i=0;i<tabPane.getTabCount();i++){
			file = ((MyPanel)tabPane.getComponentAt(i)).getFile();
			if(file != null){
				openFiles.add(file.getPath());
			}
		}
		ClConf.setOpenFiles(openFiles);
		
		//pokud jsem nekam pripojeny tak mu poslu ze se odpojuju
		if(ClConf.server != null){
			try {
				ClConf.server.send(new Data(MessageType.DISCONNECT,null,null,null));
			} catch (BadConnect e) {
				e.printStackTrace();
			}
			ClConf.server.close();
			ClConf.server = null;
		}

	}
	
	/**
	 * Nastaveni povoleni tlacitek menu ze nejsem pripojeny k serveru.
	 */
	public void setConnected(){
		//Connect
		menucheckboxConnect.setEnabled(false);
		menucheckboxConnect.setSelected(true);
		//Disconnect
		menucheckboxDisconnect.setEnabled(true);
		menucheckboxDisconnect.setSelected(false);
		
		menuItemSimStep.setEnabled(true);
		menuItemSim.setEnabled(true);
		menuItemManageSer.setEnabled(true);
		menuItemSaveSer.setEnabled(true);
	}
	
	/**
	 * Nastaveni tlacitek menu ze jsem pripojeny k serveru.
	 */
	public void setDisconnected(){
		//Connect
		menucheckboxConnect.setEnabled(true);
		menucheckboxConnect.setSelected(false);
		//Disconnect
		menucheckboxDisconnect.setEnabled(false);
		menucheckboxDisconnect.setSelected(true);
		
		menuItemSimStep.setEnabled(false);
		menuItemSim.setEnabled(false);
		menuItemManageSer.setEnabled(false);
		menuItemSaveSer.setEnabled(false);
	}
	
	/**
	 * Konstruktor teto tridy. Nejprve inicializuje nastaveni. Pak prida vlakno ktere zpracuje ukonceni programu
	 *  a nakonec inicializuje samotne hlavni okno programu.
	 */
	public MainGui() {
		super();
		
		//nacteni konfigurace klienta ze souboru
		ClConf.load();
		//nacteni konfigurace siti ze souboru
		NetConf.load();
		
		//prida vlakno pro korektni ukonceni programu
		Runtime.getRuntime().addShutdownHook(new CleanUp(this));
		
		//inicializuje program
		initGUI();
	}
	
	/**
	 * Zakladni inicializate okna a jeho soucasti
	 */
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			this.setTitle("Petri Nets");
			{
				jPanelButtons = new JPanel();
				getContentPane().add(jPanelButtons, BorderLayout.EAST);
				GroupLayout jPanelButtonsLayout = new GroupLayout((JComponent)jPanelButtons);
				jPanelButtons.setLayout(jPanelButtonsLayout);
				jPanelButtons.setPreferredSize(new java.awt.Dimension(156, 562));
				{
					buttonArc = new JButton();
					buttonArc.setText("Arc");
					buttonArc.setSize(100, 45);
					buttonArc.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							buttonArcActionPerformed(evt);
						}
					});
				}
				{
					buttonPlace = new JButton();
					buttonPlace.setText("Place");
					buttonPlace.setSize(100, 45);
					buttonPlace.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							buttonPlaceActionPerformed(evt);
						}
					});
				}
				{
					textField = new JTextField();
					textField.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							textButtonActionPerformed(evt);
						}
					});
				}
				{
					textButton = new JButton();
					textButton.setText("Insert Data");
					textButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							textButtonActionPerformed(evt);
						}
					});
				}
				{
					buttonTrans = new JButton();
					buttonTrans.setText("Transition");
					buttonTrans.setSize(100, 45);
					buttonTrans.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							buttonTransActionPerformed(evt);
						}
					});
				}
				{
					buttonSelect = new JButton();
					buttonSelect.setText("Select");
					buttonSelect.setSize(100, 45);
					buttonSelect.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							buttonSelectActionPerformed(evt);
						}
					});
				}
				{
					buttonDelete = new JButton();
					buttonDelete.setText("Delete");
					buttonDelete.setSize(100, 45);
					buttonDelete.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							buttonDeleteActionPerformed(evt);
						}
					});
				}
				jPanelButtonsLayout.setHorizontalGroup(jPanelButtonsLayout.createSequentialGroup()
					.addContainerGap(19, 19)
					.addGroup(jPanelButtonsLayout.createParallelGroup()
					    .addComponent(buttonDelete, GroupLayout.Alignment.LEADING, 0, 105, Short.MAX_VALUE)
					    .addComponent(buttonTrans, GroupLayout.Alignment.LEADING, 0, 105, Short.MAX_VALUE)
					    .addComponent(buttonArc, GroupLayout.Alignment.LEADING, 0, 105, Short.MAX_VALUE)
					    .addComponent(buttonPlace, GroupLayout.Alignment.LEADING, 0, 105, Short.MAX_VALUE)
					    .addComponent(buttonSelect, GroupLayout.Alignment.LEADING, 0, 105, Short.MAX_VALUE)
					    .addGroup(jPanelButtonsLayout.createSequentialGroup()
					        .addComponent(textField, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
					        .addGap(0, 0, Short.MAX_VALUE))
					    .addGroup(jPanelButtonsLayout.createSequentialGroup()
					        .addComponent(textButton, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
					        .addGap(0, 0, Short.MAX_VALUE)))
					.addContainerGap(32, 32));
				jPanelButtonsLayout.setVerticalGroup(jPanelButtonsLayout.createSequentialGroup()
					.addContainerGap(50, 50)
					.addComponent(buttonArc, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addGap(30)
					.addComponent(buttonPlace, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addGap(30)
					.addComponent(buttonTrans, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addGap(30)
					.addComponent(buttonSelect, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addGap(50)
					.addComponent(buttonDelete, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addGap(70)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(textButton, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(175, Short.MAX_VALUE));
			}
			{
				tabPane = new JTabbedPane();
				getContentPane().add(tabPane, BorderLayout.CENTER);
				tabPane.setPreferredSize(new java.awt.Dimension(828, 635));
				tabPane.setBackground(new java.awt.Color(255,255,255));
				{
					
					//pokud nejsou od posledne otevrene nejake soubory tak otevru jeden novy
					if(ClConf.getOpenFiles() == null){
						newfiles++;
						panel = new MyPanel(this);
						tabPane.addTab("new "+Integer.toString(newfiles), null, panel, null);
						tabPane.setTabComponentAt(tabPane.getSelectedIndex(), new MyPanelHeadline("new "+Integer.toString(newfiles),tabPane));
						panel.setBackground(NetConf.getColorBack());
					}
					else{
						//jinak projdu vsechny jmena a oteviram tyto soubory
						File file;
						for(Iterator<String> i = ClConf.getOpenFiles().iterator();i.hasNext();){
							String name = (String)i.next();
							
							file = new File(name);
							if(file.exists()){			
								panel = new MyPanel(this);
								tabPane.addTab("new", null, panel, null);
								panel.setFile(file);
								panel.load();
								tabPane.setSelectedIndex(tabPane.getTabCount()-1);
								tabPane.setTabComponentAt(tabPane.getSelectedIndex(), new MyPanelHeadline(file.getName(),tabPane));
								panel.setBackground(NetConf.getColorBack());
							}
						}
					}
				}
			}
			{
				menuBar = new JMenuBar();
				setJMenuBar(menuBar);
				{
					menuFile = new JMenu();
					menuBar.add(menuFile);
					menuFile.setText("File");
					{
						menuItemNew = new JMenuItem();
						menuFile.add(menuItemNew);
						menuItemNew.setText("New");
						menuItemNew.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								menuItemNewActionPerformed(evt);
							}
						});
					}
					{
						menuItemOpen = new JMenuItem();
						menuFile.add(menuItemOpen);
						menuItemOpen.setText("Open");
						menuItemOpen.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								menuItemOpenActionPerformed(evt);
							}
						});
					}
					{
						menuItemSave = new JMenuItem();
						menuFile.add(menuItemSave);
						menuItemSave.setText("Save");
						menuItemSave.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								menuItemSaveActionPerformed(evt);
							}
						});
					}
					{
						menuItemSaveas = new JMenuItem();
						menuFile.add(menuItemSaveas);
						menuItemSaveas.setText("Save As");
						menuItemSaveas.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								menuItemSaveasActionPerformed(evt);
							}
						});
					}
					{
						menuItemClose = new JMenuItem();
						menuFile.add(menuItemClose);
						menuItemClose.setText("Close");
						menuItemClose.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								menuItemCloseActionPerformed(evt);
							}
						});
					}
					{
						menuSepFile = new JSeparator();
						menuFile.add(menuSepFile);
					}
					{
						menuItemProp = new JMenuItem();
						menuFile.add(menuItemProp);
						menuItemProp.setText("Properties");
						menuItemProp.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								menuItemPropActionPerformed(evt);
							}
						});
					}
					{
						menuSep2File = new JSeparator();
						menuFile.add(menuSep2File);
					}
					{
						menuItemExit = new JMenuItem();
						menuFile.add(menuItemExit);
						menuItemExit.setText("Exit");
						menuItemExit.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								menuItemExitActionPerformed(evt);
							}
						});
					}
				}
				{
					menuServer = new JMenu();
					menuBar.add(menuServer);
					menuServer.setText("Server");
					{
						menuItemManageSer = new JMenuItem();
						menuServer.add(menuItemManageSer);
						menuItemManageSer.setText("Manage Nets");
						menuItemManageSer.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								menuItemManageSerActionPerformed(evt);
							}
						});
						menuItemManageSer.setEnabled(false);
					}
					{
						menuItemSaveSer = new JMenuItem();
						menuServer.add(menuItemSaveSer);
						menuItemSaveSer.setText("Save");
						menuItemSaveSer.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								menuItemSaveSerActionPerformed(evt);
							}
						});
						menuItemSaveSer.setEnabled(false);
					}
					{
						menuSepServer1 = new JSeparator();
						menuServer.add(menuSepServer1);
					}
					{
						menuItemSim = new JMenuItem();
						menuServer.add(menuItemSim);
						menuItemSim.setText("Simulate");
						menuItemSim.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								menuItemSimActionPerformed(evt);
							}
						});
						menuItemSim.setEnabled(false);
					}
					{
						menuItemSimStep = new JMenuItem();
						menuServer.add(menuItemSimStep);
						menuItemSimStep.setText("Simulate Step");
						menuItemSimStep.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								menuItemSimStepActionPerformed(evt);
							}
						});
						menuItemSimStep.setEnabled(false);
					}
					{
						menuSepServer2 = new JSeparator();
						menuServer.add(menuSepServer2);
					}
					{
						menucheckboxConnect = new JCheckBoxMenuItem();
						menuServer.add(menucheckboxConnect);
						menucheckboxConnect.setText("Connect");
						menucheckboxConnect.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								menucheckboxConnectActionPerformed(evt);
							}
						});
					}
					{
						menucheckboxDisconnect = new JCheckBoxMenuItem();
						menuServer.add(menucheckboxDisconnect);
						menucheckboxDisconnect.setText("Disconnect");
						menucheckboxDisconnect.setEnabled(false);
						menucheckboxDisconnect.setSelected(true);
						menucheckboxDisconnect.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								menucheckboxDisconnectActionPerformed(evt);
							}
						});
					}
				}
				{
					menuHelp = new JMenu();
					menuBar.add(menuHelp);
					menuHelp.setText("Help");
					{
						menuItemHelp = new JMenuItem();
						menuHelp.add(menuItemHelp);
						menuItemHelp.setText("Help");
						menuItemHelp.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								menuItemHelpActionPerformed(evt);
							}
						});
					}
					{
						menuSepHelp = new JSeparator();
						menuHelp.add(menuSepHelp);
					}
					{
						menuItemAbout = new JMenuItem();
						menuHelp.add(menuItemAbout);
						menuItemAbout.setText("About");
						menuItemAbout.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								menuItemAboutActionPerformed(evt);
							}
						});
					}
				}
			}
			pack();
			setSize(1000, 700);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//POSLUCHACI UDALOSTI***************************************************************************
	
	/**
	 * Metoda pro aktivni cinnost posluchace. Tlacitko Help z Menu.
	 * Vyvola nove okno z helpem.
	 * @param evt 
	 */
	private void menuItemHelpActionPerformed(ActionEvent evt) {
		System.out.println("menuItemHelp.actionPerformed, event="+evt);
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				HelpFrame inst = new HelpFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
		
	}
	
	/**
	 * Metoda pro aktivni cinnost posluchace. Tlacitko About z Menu.
	 * Vyvola nove okno o programu.
	 * @param evt
	 */
	private void menuItemAboutActionPerformed(ActionEvent evt) {
		System.out.println("menuItemAbout.actionPerformed, event="+evt);
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				AboutFrame inst = new AboutFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
		
	}
	
	/**
	 * Metoda pro aktivni cinnost posluchace. Tlacitko New z Menu.
	 * Otevre novy panel site, inicializuje ho.
	 * @param evt
	 */
	private void menuItemNewActionPerformed(ActionEvent evt) {
		System.out.println("menuItemNew.actionPerformed, event="+evt);
		
		//zrusim aktivitu tlacitek
		button=Buttons.FALSE;
		
		//vytvori novy tab
		newfiles++;
		panel = new MyPanel(this);
		tabPane.addTab(null, panel);
		//prepne aktivitu na vytvoreny tab
		tabPane.setSelectedIndex(tabPane.getTabCount()-1);
		//nastavim headline
		tabPane.setTabComponentAt(tabPane.getSelectedIndex(), new MyPanelHeadline("new "+Integer.toString(newfiles),tabPane));
		panel.setBackground(NetConf.getColorBack());
	}
	
	/**
	 * Metoda pro aktivni cinnost posluchace. Tlacitko Open z Menu.
	 * Vyvola vyberove okno pro soubor a ten potom otevre jako novou sit.
	 * @param evt
	 */
	private void menuItemOpenActionPerformed(ActionEvent evt) {
		System.out.println("menuItemOpen.actionPerformed, event="+evt);
		
		JFileChooser fileChooser = new JFileChooser(ClConf.getRoot());
		
		int retval=fileChooser.showOpenDialog(this);
		
		if(retval == JFileChooser.APPROVE_OPTION){
			File file = fileChooser.getSelectedFile();
			
			//novy panel
			panel = new MyPanel(this);
			tabPane.addTab(null, panel);
			panel.setBackground(NetConf.getColorBack());
			
			//prepne aktivitu na vytvoreny tab
			tabPane.setSelectedIndex(tabPane.getTabCount()-1);
			//nastavim headline
			tabPane.setTabComponentAt(tabPane.getSelectedIndex(), new MyPanelHeadline(file.getName(),tabPane));
			//zrusim aktivitu tlacitek
			button=Buttons.FALSE;
			
			//ulozim soubor do site a nahraju ho
			panel.setFile(file);
			try{
				panel.load();
			} catch (FalseXML e) {
				JOptionPane.showMessageDialog(this,
					    "False xml of Petri net on input. Rewrite file on save.",
					    "Warning",
					    JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}
	
	/**
	 * Metoda pro aktivni cinnost posluchace. Tlacitko Save z Menu.
	 * Ulozi aktivni sit do souboru pridruzenemu tabu nebo vyvola metodu {@link #saveNetAs()}.
	 * @param evt
	 */
	private void menuItemSaveActionPerformed(ActionEvent evt) {
		System.out.println("menuItemSave.actionPerformed, event="+evt);
		
		//pokud uz mam slinkovany  tab se souborem tak ho tam ulozim jinak musim vybrat
		if(((MyPanel)tabPane.getSelectedComponent()).getFile() != null)
			((MyPanel)tabPane.getSelectedComponent()).save();
		else
			saveNetAs();
		
	}
	
	/**
	 * Metoda pro aktivni cinnost posluchace. Tlacitko Save As z Menu.
	 * Spousti metodu {@link #saveNetAs()}.
	 * @param evt
	 */
	private void menuItemSaveasActionPerformed(ActionEvent evt) {
		System.out.println("menuItemSaveas.actionPerformed, event="+evt);
		
		saveNetAs();
	}
	
	/**
	 * Metoda pro aktivni cinnost posluchace. Tlacitko Help z Menu
	 * Vyvola okno vyberu souboru kam pak ulozi aktivni sit. Osetruje existujici soubory.
	 */
	private void saveNetAs(){
		
		@SuppressWarnings("serial")
		JFileChooser fileChooser = new JFileChooser(ClConf.getRoot()){

			public void approveSelection(){
				//pokud soubor existuje tak se zeptam jestli ho chce prepsat
				if(getSelectedFile().exists()){
					int i = JOptionPane.showConfirmDialog(this,"File exists, rewrite?", "Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
					switch(i){
						case JOptionPane.YES_OPTION:
							super.approveSelection();
							return;
						case JOptionPane.NO_OPTION:
							return;
						case JOptionPane.CANCEL_OPTION:
							cancelSelection();
							return;
					}
				}
				super.approveSelection();
			}
		};
		
		int retval=fileChooser.showSaveDialog(this);
		
		if(retval == JOptionPane.YES_OPTION){
				
			//zmenim cilovy soubor
			((MyPanel)tabPane.getSelectedComponent()).setFile(fileChooser.getSelectedFile());
			//ulozim sit
			((MyPanel)tabPane.getSelectedComponent()).save();
			//prepsani hlavicky aktivniho tabu
			((MyPanelHeadline)tabPane.getTabComponentAt(tabPane.getSelectedIndex())).setName(fileChooser.getSelectedFile().getName());
		}		
	}
	
	/**
	 * Metoda pro aktivni cinnost posluchace. Tlacitko Close z Menu.
	 * Zavre aktivni tab. Neuklada sit v tomto tabu, nijak s ni nepracuje.
	 * @param evt
	 */
	private void menuItemCloseActionPerformed(ActionEvent evt) {
		System.out.println("menuItemClose.actionPerformed, event="+evt);
		//smaze aktivni tab
		
		if(tabPane.getSelectedIndex()!=-1){
			tabPane.remove(tabPane.getSelectedIndex());
		}
		
	}
	
	/**
	 * Metoda pro aktivni cinnost posluchace. Tlacitko Properties z Menu.
	 * Otevre okno {@link PropertyFrame} pro graficke nastaveni programu.
	 * @param evt
	 */
	private void menuItemPropActionPerformed(ActionEvent evt) {
		System.out.println("menuItemProp.actionPerformed, event="+evt);
		
		PropertyFrame inst = new PropertyFrame(this);
		inst.setLocationRelativeTo(this);
		inst.setVisible(true);
		
		repaint();
	}
	
	/**
	 * Metoda pro aktivni cinnost posluchace. Tlacitko Exit z Menu.
	 * Uvolni hlavni okno programu takze skonci cely program.
	 * @param evt
	 */
	private void menuItemExitActionPerformed(ActionEvent evt) {
		System.out.println("menuItemExit.actionPerformed, event="+evt);		

		this.dispose();
	}
	
	/**
	 * Metoda pro aktivni cinnost posluchace. Tlacitko Arc.
	 * Spusti mod malovani hran ve vsech sitich.
	 * @param evt
	 */
	private void buttonArcActionPerformed(ActionEvent evt) {
		System.out.println("buttonArc.actionPerformed, event="+evt);
		
		button=Buttons.ARC;
	}
	
	/**
	 * Metoda pro aktivni cinnost posluchace. Tlacitko Place.
	 * Spusti mod malovani mist ve vsech sitich.
	 * @param evt
	 */
	private void buttonPlaceActionPerformed(ActionEvent evt) {
		System.out.println("buttonPlace.actionPerformed, event="+evt);
		
		button=Buttons.PLACE;
	}
	
	/**
	 * Metoda pro aktivni cinnost posluchace. Tlacitko Transition.
	 * Spusti mod malovani prechodu ve vsech sitich.
	 * @param evt
	 */
	private void buttonTransActionPerformed(ActionEvent evt) {
		System.out.println("buttonTrans.actionPerformed, event="+evt);

		button=Buttons.TRANS;
	}
	
	/**
	 * Metoda pro aktivni cinnost posluchace. Tlacitko Delete
	 * Spusti mod mazani aktivnich prvku ve vsech sitich.
	 * @param evt
	 */
	private void buttonDeleteActionPerformed(ActionEvent evt) {
		System.out.println("buttonDelete.actionPerformed, event="+evt);

		button=Buttons.DELETE;
	}
	
	/**
	 * Metoda pro aktivni cinnost posluchace. Tlacitko Insert
	 * Vlozi do aktivniho prvku v siti v aktivnim tabu hodnotu ktera je ve chlivku {@link textField} a smaze ho.
	 * @param evt
	 */
	private void textButtonActionPerformed(ActionEvent evt) {
		System.out.println("textButton.actionPerformed, event="+evt);
		
		((MyPanel)tabPane.getSelectedComponent()).setActiveText(textField.getText());
		
		textField.setText("");
	}
	
	/**
	 * Metoda pro aktivni cinnost posluchace. Tlacitko Move.
	 * Zapne mod vybirani(aktivovani) prvku v siti podle toho kam klikne mys.
	 * @param evt
	 */
	private void buttonSelectActionPerformed(ActionEvent evt) {
		System.out.println("buttonSelect.actionPerformed, event="+evt);
		
		
		button=Buttons.SELECT;
	}
	
	/**
	 * Metoda pro aktivni cinnost posluchace. Tlacitko Connect z Menu.
	 * @param evt
	 */
	private void menucheckboxConnectActionPerformed(ActionEvent evt) {
		System.out.println("menucheckboxConnect.actionPerformed, event="+evt);
		
		
		menucheckboxConnect.setEnabled(false);
		menucheckboxConnect.setSelected(false);
		
		ConnectFrame conn = new ConnectFrame(this);
		conn.setLocationRelativeTo(this);
		conn.setVisible(true);
	}
	
	// DISCONNECT
	private void menucheckboxDisconnectActionPerformed(ActionEvent evt) {
		System.out.println("menucheckboxDisconnect.actionPerformed, event="+evt);
		
		try {
			ClConf.server.send(new Data(MessageType.DISCONNECT, null, null,null));
		} catch (BadConnect e) {
			e.printStackTrace();
		}
		
		ClConf.server.close();
		ClConf.server = null;
		
		//nastavim tlacitka na nepripojeno
		setDisconnected();
	}
	
	//MANAGE NETS
	private void menuItemManageSerActionPerformed(ActionEvent evt) {
		System.out.println("menuItemManageSer.actionPerformed, event="+evt);
		
		ManageNets inst = new ManageNets(this, tabPane);
		inst.setLocationRelativeTo(this);
		inst.setVisible(true);
		
	}
	
	//Save to server
	private void menuItemSaveSerActionPerformed(ActionEvent evt) {
		System.out.println("menuItemSaveSer.actionPerformed, event="+evt);
		
		SaveAsServer inst = new SaveAsServer(tabPane);
		inst.setLocationRelativeTo(this);
		inst.setVisible(true);
	}
	
	//Simulate
	private void menuItemSimActionPerformed(ActionEvent evt) {
		System.out.println("menuItemSim.actionPerformed, event="+evt);
		
		//sit v aktivnim tabu
		Document net = ((MyPanel)tabPane.getSelectedComponent()).toDoc();
		//data pro server
		Data data = new Data(MessageType.SIMULATE,null,null,net);
		
		try {
			ClConf.server.send(data);
			data=ClConf.server.receive();
		} catch (BadConnect e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"Bad connection to server. Disconnection recommended.",
				    "Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(data.getType()==MessageType.ERR){
			JOptionPane.showMessageDialog(this,data.getstr1(),
				    "Warning",JOptionPane.WARNING_MESSAGE);
			return;
		}
		else{
			try {
				((MyPanel)tabPane.getSelectedComponent()).fromDoc(data.getDoc());
			} catch (FalseXML e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this,"Returned net is badly formatted.:-(",
					    "Error",JOptionPane.ERROR_MESSAGE);
			} catch (DocumentException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this,"Returned net is badly formatted.:-(",
					    "Error",JOptionPane.ERROR_MESSAGE);
			}
		}
		this.repaint();
	}
	
	//Simulate Step
	private void menuItemSimStepActionPerformed(ActionEvent evt) {
		System.out.println("menuItemSimStep.actionPerformed, event="+evt);
		
		//sit v aktivnim tabu
		Document net = ((MyPanel)tabPane.getSelectedComponent()).toDoc();
		//data pro server
		Data data = new Data(MessageType.SIMULATE_STEP,null,null,net);
		
		try {
			ClConf.server.send(data);
			data=ClConf.server.receive();
		} catch (BadConnect e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"Bad connection to server. Disconnection recommended.",
				    "Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(data.getType()==MessageType.ERR){
			JOptionPane.showMessageDialog(this,data.getstr1(),
				    "Warning",JOptionPane.WARNING_MESSAGE);
			return;
		}
		else{
			try {
				((MyPanel)tabPane.getSelectedComponent()).fromDoc(data.getDoc());
			} catch (FalseXML e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this,"Returned net is badly formatted.:-(",
					    "Error",JOptionPane.ERROR_MESSAGE);
			} catch (DocumentException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this,"Returned net is badly formatted.:-(",
					    "Error",JOptionPane.ERROR_MESSAGE);
			}
		}
		
		this.repaint();
	}

}

//EOF