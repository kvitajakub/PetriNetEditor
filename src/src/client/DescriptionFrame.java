/**
 * DescriptionFrame.java
 * @author Kvita Jakub
 */
package client;

import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

/**
 * Trida pro zobrazeni okna podrobnosti o siti.
 * @author Kvita Jakub
 */
public class DescriptionFrame extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	
	private JLabel labelUser;
	private JLabel labelName;
	private JLabel labelDesc;
	private JTextField textFVersion;
	private JTextField textFUser;
	private JTextField textFName;
	private JTextPane textPaneDesc;
	private JLabel labelVersion;

	String user;
	String name;
	String version;
	String desc;
	
	/**
	 * Jediny konstruktor, bez jeho parametru nema cenu okno delat.
	 * @param user Uzivatel ktery vlastni sit.
	 * @param name Jmeno site.
	 * @param version Cislo verze.
	 * @param desc  Popis teto verze.
	 */
	public DescriptionFrame(String user,String name,String version,String desc) {
		super();
		if(user != null)
			this.user=user;
		else
			this.user=new String("");
		if(name != null)
			this.name=name;
		else
			this.name=new String("");
		if(version != null)
			this.version=version;
		else
			this.version=new String("");
		if(desc != null)
			this.desc=desc;
		else
			this.desc=new String("");
		
		initGUI();
	}
	
	private void initGUI() {
		try {
			GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			
			this.setTitle("Details");
			
			{
				labelUser = new JLabel();
				labelUser.setText("User:");
			}
			{
				labelName = new JLabel();
				labelName.setText("Name:");
			}
			{
				labelVersion = new JLabel();
				labelVersion.setText("Version:");
			}
			{
				labelDesc = new JLabel();
				labelDesc.setText("Description:");
			}
			{
				textPaneDesc = new JTextPane();
				textPaneDesc.setEditable(false);
				textPaneDesc.setText(desc);
			}
			{
				textFVersion = new JTextField();
				textFVersion.setEditable(false);
				textFVersion.setText(version);
			}
			{
				textFName = new JTextField();
				textFName.setEditable(false);
				textFName.setText(name);
			}
			{
				textFUser = new JTextField();
				textFUser.setEditable(false);
				textFUser.setText(user);
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addContainerGap(34, Short.MAX_VALUE)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(textFUser, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(labelUser, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(textFName, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(labelName, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(textFVersion, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(labelVersion, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(thisLayout.createParallelGroup()
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addComponent(labelDesc, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				        .addGap(105))
				    .addComponent(textPaneDesc, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(35, 35));
			thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup()
				.addContainerGap(27, 27)
				.addGroup(thisLayout.createParallelGroup()
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addComponent(labelUser, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
				        .addGap(34))
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addComponent(labelName, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
				        .addGap(26))
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addComponent(labelVersion, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
				        .addGap(16))
				    .addComponent(labelDesc, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(thisLayout.createParallelGroup()
				    .addGroup(thisLayout.createSequentialGroup()
				        .addComponent(textPaneDesc, GroupLayout.PREFERRED_SIZE, 299, GroupLayout.PREFERRED_SIZE))
				    .addGroup(thisLayout.createSequentialGroup()
				        .addComponent(textFVersion, GroupLayout.PREFERRED_SIZE, 299, GroupLayout.PREFERRED_SIZE))
				    .addGroup(thisLayout.createSequentialGroup()
				        .addComponent(textFName, GroupLayout.PREFERRED_SIZE, 299, GroupLayout.PREFERRED_SIZE))
				    .addGroup(thisLayout.createSequentialGroup()
				        .addComponent(textFUser, GroupLayout.PREFERRED_SIZE, 299, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap(61, Short.MAX_VALUE));
			pack();
			this.setSize(483, 315);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

//EOF