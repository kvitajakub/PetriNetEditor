/**
 * SaveAsServer.java
 * @author Kvita Jakub
 */
package client;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import network.MessageType;
import org.dom4j.Document;
import configuration.ClConf;
import network.*;
import client.MyPanel;
import except.BadConnect;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Trida pro zobrazeni okna ulozeni site na server.
 * @author Kvita Jakub
 */
public class SaveAsServer extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	
	private JLabel labelName;
	private JTextField textName;
	private JLabel labelDesc;
	private JButton buttonSave;

	private MyPanel panel;
	private MyPanelHeadline head;
	
	private JTextArea textArea;

	/**
	 * Konstruktor.
	 * @param pane Dostanu z neho aktivni panel ktery se bude ukladat a hlavicku, kterou budu mozna prepisovat.
	 */
	public SaveAsServer(JTabbedPane pane) {
		super();		
		this.panel = (MyPanel)pane.getSelectedComponent();
		this.head = (MyPanelHeadline)pane.getTabComponentAt(pane.getSelectedIndex());
		initGUI();
	}
	
	private void initGUI() {
		try {
			
			GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			
			this.setTitle("Net name and description.");
			
			{
				labelName = new JLabel();
				labelName.setText("Name:");
			}
			{
				textName = new JTextField();
				if(panel.getServername() != null)
					textName.setText(panel.getServername());
			}
			{
				labelDesc = new JLabel();
				labelDesc.setText("Description:");
			}
			{
				buttonSave = new JButton();
				buttonSave.setText("Save");
				buttonSave.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						buttonSaveActionPerformed(evt);
					}
				});
			}
			{
				textArea = new JTextArea();
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addContainerGap()
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(textName, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
				    .addComponent(labelName, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(16)
				.addGroup(thisLayout.createParallelGroup()
				    .addComponent(textArea, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addGap(12)
				        .addComponent(labelDesc, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				        .addGap(106)))
				.addGap(18)
				.addComponent(buttonSave, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
				.addContainerGap(18, Short.MAX_VALUE));
			thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup()
				.addGap(7)
				.addGroup(thisLayout.createParallelGroup()
				    .addComponent(labelDesc, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addComponent(labelName, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
				        .addGap(20)))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(thisLayout.createParallelGroup()
				    .addGroup(thisLayout.createSequentialGroup()
				        .addComponent(textName, GroupLayout.PREFERRED_SIZE, 382, GroupLayout.PREFERRED_SIZE)
				        .addGap(0, 0, Short.MAX_VALUE))
				    .addComponent(textArea, GroupLayout.Alignment.LEADING, 0, 382, Short.MAX_VALUE)
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addGap(150)
				        .addComponent(buttonSave, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
				        .addGap(0, 141, Short.MAX_VALUE)))
				.addContainerGap(23, 23));
			pack();
			this.setSize(503, 298);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}
	
	
	//SAVE - konecne zacnu neco delat
	private void buttonSaveActionPerformed(ActionEvent evt) {
		System.out.println("buttonSave.actionPerformed, event="+evt);
		
		String sname = textName.getText();
		String desc = textArea.getText();
		
		if(sname == null || sname.equals("")){
			JOptionPane.showMessageDialog(this,"Name can't be empty.",
		    "Warning",JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		Document net = panel.toDoc();
		Data data = new Data(MessageType.SAVE,sname,desc,net);
		
		try {
			ClConf.server.send(data);
			data = ClConf.server.receive();			
		} catch (BadConnect e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"Bad communication with server. Diconnect recommended.",
				    "Error",JOptionPane.ERROR_MESSAGE);
			this.dispose();
		}
	
		if(data.getType() == MessageType.ERR){
			JOptionPane.showMessageDialog(this,"Net wasn't saved. Try it with different name.",
				    "Warning",JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		panel.setServername(sname);
		if(panel.getFile() == null){
			head.setName(sname);
			head.repaint();
		}
		
		this.dispose();
	}

}

//EOF