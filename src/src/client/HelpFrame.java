/**
 * HelpFrame.java
 * @author Kvita Jakub
 */
package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;


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
 * Trida pro vyvolani okna Help.
 * @author Kvita Jakub
 */
public class HelpFrame extends javax.swing.JFrame {
	
	private static final long serialVersionUID = 1L;
	
	static String HELPHTML="Editor a simulator vysokourovnovych Petriho Siti. Aplikace vytvorena v roce 2012 jako projekt do predmetu IJA (FIT VUTBR)." +
			"       Autori:   Jakub Kvita a Ondrej Cienciala";
	
	
	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JButton buttonOk;
	private JTextPane jTextPane4;
	private JTextPane jTextPane3;
	private JTextPane jTextPane2;
	private JTextPane jTextPane1;
	private JTabbedPane jTabbedPane1;

	/**
	 * Vytvoreni a inicializace okna pomoci funkce {@link #initGUI}
	 */
	public HelpFrame() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			
			this.setTitle("Help");
			
			GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				buttonOk = new JButton();
				buttonOk.setText("OK");
				buttonOk.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						buttonOkActionPerformed(evt);
					}
				});
			}
			{
				jTabbedPane1 = new JTabbedPane();
				{
					jTextPane1 = new JTextPane();
					jTabbedPane1.addTab("Program control", null, jTextPane1, null);
					jTextPane1.setText("Program is divided into two separate parts - a server and a client.\r\n\r\nClient alows you creation and editing Petri nets and it is also their local store.\r\n\r\nServer is used as repository of Petri nets and allows simulate them by registered users.\r\n\r\nMore on Client and Server tab.");
					jTextPane1.setPreferredSize(new java.awt.Dimension(375, 232));
					jTextPane1.setEditable(false);
				}
				{
					jTextPane2 = new JTextPane();
					jTabbedPane1.addTab("Client", null, jTextPane2, null);
					jTextPane2.setText("The client is designed for work with unlimited number of Petri nets simultaneously.\r\n\r\nElements of Petri net may be entered and edited using buttons in the right side of window.\r\n\r\nIt is possible to save and load Petri nets to and from local computer. Also, the color scheme and font settings can be changed during work with the program.");
					jTextPane2.setEditable(false);
				}
				{
					jTextPane3 = new JTextPane();
					jTabbedPane1.addTab("Server", null, jTextPane3, null);
					jTextPane3.setText("The most important server functions are simulation of Petri nets, Petri nets store and user authentication. \r\n\r\nVersion control is possible for Petri nets. Informations of user activity are also stored in the server. \r\n\r\nServer runs on port 22222.");
					jTextPane3.setEditable(false);
				}
				{
					jTextPane4 = new JTextPane();
					jTabbedPane1.addTab("Petri nets", null, jTextPane4, null);
					jTextPane4.setText("Petri net is used for graphical modeling of discrete distributed systems.\r\n\r\nIt consists of three elements - places, transitions and arcs. The arc always leads from a place to a transition or from a transition to a place.\r\n\r\nIn the place, there can be tokens, which are modeled as integers in this program. Operations as addition and subtraction can be performed with tokens which are determined in transitions.\r\n\r\nOn the arc, there can be either variable or constant that defines the value in the following place.");
					jTextPane4.setEditable(false);
				}
			}
					thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
						.addComponent(jTabbedPane1, 0, 259, Short.MAX_VALUE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(buttonOk, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(11, 11));
					thisLayout.setHorizontalGroup(thisLayout.createParallelGroup()
						.addComponent(jTabbedPane1, GroupLayout.Alignment.LEADING, 0, 381, Short.MAX_VALUE)
						.addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
						    .addGap(151)
						    .addComponent(buttonOk, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
						    .addContainerGap(158, Short.MAX_VALUE)));
			pack();
			this.setSize(387, 337);
			this.setResizable(false);
			getContentPane().setBackground(new java.awt.Color(255,255,255));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void buttonOkActionPerformed(ActionEvent evt) {
		System.out.println("buttonOk.actionPerformed, event="+evt);
		this.dispose();
	}
}

//EOF