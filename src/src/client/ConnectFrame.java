/**
 * ConnectFrame.java
 * @author Kvita Jakub
 */
package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;
import configuration.ClConf;
import except.BadConnect;
import network.*;
import javax.swing.JOptionPane;


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
 * Vyvola okno pro pripojeni k serveru. Prihlaseni nebo registrace uzivatele.
 * @author Kvita Jakub
 */
public class ConnectFrame extends javax.swing.JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JLabel labelServer;
		private JTextField textFServer;
	private JLabel labelPort;
	private JButton buttonClean;
		private JTextField textFPort;
	private JLabel labelLogin;
		private JTextField textFUsername;
	private JLabel labelPass;
		private JPasswordField textFPass;
		
	private JButton buttonLogin;
	private JButton buttonRegister;
	
	//okno jehoz menu budu menit
	private MainGui g;
	
		
	/**
	 * Vytvori okno pro pripojeni.
	 * @param g MainGui se kterym pracuje, nastavuje mu povoleni tlacitek pro server.
	 */
	public ConnectFrame(MainGui g) {
		super();
		this.g=g;
		initGUI();
	}
	
	public void dispose(){
		//Vrati vse do stavu nepripojeno k serveru
		
		g.setDisconnected();
		
		super.dispose();
	}
	
	
	private void initGUI() {
		try {
			this.setTitle("Connection");
			
			GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				labelServer = new JLabel();
				labelServer.setText("Server:");
			}
			{
				textFServer = new JTextField();
				textFServer.setText(ClConf.getServerName());
			}
			{
				labelPort = new JLabel();
				labelPort.setText("Port:");
			}
			{
				buttonClean = new JButton();
				buttonClean.setText("Clean");
				buttonClean.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						buttonCleanActionPerformed(evt);
					}
				});
			}
			{
				
				textFPort = new JTextField();
				textFPort.setText(ClConf.getPort());
			}
			{
				labelLogin = new JLabel();
				labelLogin.setText("Username:");
			}
			{
				textFUsername = new JTextField();
				textFUsername.setText(ClConf.getUsername());
			}
			{
				labelPass = new JLabel();
				labelPass.setText("Password:");
			}
			{
				textFPass = new JPasswordField();
				textFPass.setText(ClConf.getPass());
			}
			{
				buttonRegister = new JButton();
				buttonRegister.setText("Register");
				buttonRegister.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						buttonRegLogActionPerformed(evt);
					}
				});
			}
			{
				buttonLogin = new JButton();
				buttonLogin.setText("Login");
				buttonLogin.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						buttonRegLogActionPerformed(evt);
					}
				});
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addContainerGap(56, 56)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(labelServer, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(labelPort, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(textFServer, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(textFPort, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(46)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(labelLogin, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(labelPass, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(textFUsername, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(textFPass, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(57)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(buttonLogin, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
				    .addComponent(buttonRegister, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
				    .addComponent(buttonClean, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(46, 46));
			thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup()
				.addContainerGap(45, 45)
				.addGroup(thisLayout.createParallelGroup()
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addComponent(textFUsername, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
				        .addGap(45))
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addComponent(textFServer, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
				        .addGap(45))
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addGroup(thisLayout.createParallelGroup()
				            .addComponent(buttonClean, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
				            .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				                .addComponent(labelLogin, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
				                .addGap(8))
				            .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				                .addComponent(labelServer, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
				                .addGap(24)))
				        .addGap(0, 64, GroupLayout.PREFERRED_SIZE)
				        .addComponent(buttonRegister, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)))
				.addGroup(thisLayout.createParallelGroup()
				    .addGroup(thisLayout.createSequentialGroup()
				        .addComponent(textFPass, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
				        .addGap(0, 0, Short.MAX_VALUE))
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addComponent(labelPass, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
				        .addGap(61))
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addComponent(textFPort, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
				        .addGap(61))
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addComponent(labelPort, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
				        .addGap(0, 19, GroupLayout.PREFERRED_SIZE)
				        .addComponent(buttonLogin, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap(86, 86));
			pack();
			this.setSize(495, 368);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}
	
	
	private void load(){
		
		ClConf.setServerName(textFServer.getText());
		ClConf.setPort(textFPort.getText());
		ClConf.setUsername(textFUsername.getText());
		ClConf.setPass(new String(textFPass.getPassword()));
		
	}
	
	private void buttonRegLogActionPerformed(ActionEvent evt) {
		System.out.println("buttonRegister.actionPerformed, event="+evt);
		
		load();
		
		
		MessageType type;
		if(evt.getSource() == buttonLogin)
			type = MessageType.LOGIN;
		else
			type = MessageType.REGISTER;
		
		int port=0;
		try{
			port=Integer.parseInt(ClConf.getPort());
		}
		catch(java.lang.NumberFormatException e){
			//do pole portu nekdo zadal nejakou blbost takze tam nechame nulu
		}
		
		Data data = new Data();
		data.setType(type);
		data.setstr1(ClConf.getUsername());
		data.setstr2(ClConf.getPass());
		data.setDoc(null);
		
		ClConf.server = new Connection();
		try {
			ClConf.server.connect(ClConf.getServerName(), port);
		} catch (BadConnect e) {
			
			JOptionPane.showMessageDialog(this,"Connection not established. Check server name and port.",
				    "Error",JOptionPane.ERROR_MESSAGE);	
			ClConf.server = null;
			return;
		}
			
		try {
			ClConf.server.send(data);
			data = ClConf.server.receive();
		} catch (BadConnect e) {
			e.printStackTrace();
			
			JOptionPane.showMessageDialog(this,"Bad connection to server. Disconnection recommended.",
				    "Error",JOptionPane.ERROR_MESSAGE);	
			
			return;
		}
		
		//server na REGISTER A LOGIN vraci bud OK nebo ERR
		if(data.getType() == MessageType.ERR){
			
			String err = new String();
			
			//poslal jsem mu LOGIN
			if(type == MessageType.LOGIN)
				err = "Wrong username or password.";
			//poslal jsem mu register
			else
				err = "Username already registered.";
			
			JOptionPane.showMessageDialog(this,err,
				    "Error",JOptionPane.ERROR_MESSAGE);	
			
			return;
		}
		
		//server vratil OK takze jsem prihlasen a muzu s nim pracovat
		g.setConnected();
		
		super.dispose();
	}
	
	private void buttonCleanActionPerformed(ActionEvent evt) {
		System.out.println("buttonClean.actionPerformed, event="+evt);
		
		textFServer.setText("");
		textFPort.setText("");
		textFUsername.setText("");
		textFPass.setText("");
		
		load();
	}

}

//EOF