/**
 * AboutFrame.java
 * @author Kvita Jakub
 */
package client;

import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;

/**
 * Trida pro vyvolani okna About.
 * @author Kvita Jakub
 */
public class AboutFrame extends javax.swing.JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static final long serialVersionUID = 1L;
	
	private JTextPane txtpneditorASimulator;

	/**
	 * Vytvoreni a inicializace okna pomoci funkce {@link #initGUI}
	 */
	public AboutFrame() {
		super();
		setTitle("O programu");
		initGUI();
	}
	
	//TODO dopsat okno aby to vypadalo hezky
	private void initGUI() {
		try {
			GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				txtpneditorASimulator = new JTextPane();
				txtpneditorASimulator.setEditable(false);
				txtpneditorASimulator.setContentType("text/html");
				txtpneditorASimulator.setText("<font face=\"tahoma\"size=4>\r\n<p style=\"text-align: center;\" >\r\n<b>Petri net simulator and editor</b>\r\n<br>\r\nv1.0\r\n<br><br><br>\r\n<b>Authors</b>\r\n<br>\r\n<i>Jakub Kvita\r\n<br>\r\n&\r\n<br>\r\nOndrej Cienciala\r\n</i>\r\n<br><br>\r\nwish you good luck\r\n<br>\r\nwith this application!\r\n<font size=3>\r\n<br><br><br>\r\nIf problem occurs, contact:<br>\r\nxkvita01@stud.fit.vutbr.cz<br>\r\nxcienc02@stud.fit.vutbr.cz\r\n</p>\r\n</font>");
			}
				thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
					.addComponent(txtpneditorASimulator, 0, 262, Short.MAX_VALUE));
				thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup()
				.addComponent(txtpneditorASimulator, 0, 384, Short.MAX_VALUE));
			pack();
			setSize(400, 300);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}

}

//EOF