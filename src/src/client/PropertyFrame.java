/**
 * PropertyFrame.java
 * @author Kvita Jakub
 */
package client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerNumberModel;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import configuration.NetConf;
import java.awt.GraphicsEnvironment;

/**
 * Trida pro vyvolani a praci s oknem pro nastaveni a konfiguraci.
 * @author Kvita Jakub
 */
public class PropertyFrame extends javax.swing.JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	private int sizeOfCircle = NetConf.getSizeOfCircle();
	private int sizeOfRectX  = NetConf.getSizeOfRectX();
	private int sizeOfRectY  = NetConf.getSizeOfRectY();
	private int arrowSize    = NetConf.getArrowSize();
	
	private Color colorAct  = NetConf.getColorAct();
	private Color colorRest = NetConf.getColorRest();
	private Color colorBack = NetConf.getColorBack();
	
	private String font    = NetConf.getFont();
	private int sizeOfText = NetConf.getSizeOfText();
	
	private String appType = NetConf.getAppType();
	
	
	private static final long serialVersionUID = 1L;
	private JLabel labelAppearance;
	private JButton buttonOK;
	private JSpinner spinSizePlace;
	private JComboBox comboFont;
	private JSpinner spinTextSize;
	private JLabel labelTextSize;
	private JLabel labelFontName;
	private JSpinner spinSizeArrow;
	private JLabel labelArrow;
	private JButton buttonDefault;
	private JSpinner spinSizeTrans;
	private JLabel labelSizeTrans;
	private JLabel labelSizePlace;
	private JComboBox comboAppearance;

	private JFrame frame;
	
	
	/**
	 * Inicializace a vytvoreni okna pomoci metody {@link #initGUI()}
	 * @param frame Okno hlavni obrazovky, ktere se ma prekreslovat.
	 */
	public PropertyFrame(MainGui frame) {
		super();
		this.frame=frame;
		initGUI();
	}
	
	/**
	 * Dispose ktery predtim ulozi zmenena data do konfigurace.
	 * Nemuze to byt prekryty dispose() protoze u krizku se nic neuklada.
	 */
	public void mydispose(){
		
		dispose();
		
		//ulozim zmeny do konfigurace se kterou pracuje program
		NetConf.setAppType(appType);
		NetConf.setArrowSize(arrowSize);
		NetConf.setColorAct(colorAct);
		NetConf.setColorBack(colorBack);
		NetConf.setColorRest(colorRest);
		NetConf.setFont(font);
		NetConf.setSizeOfCircle(sizeOfCircle);
		NetConf.setSizeOfRectX(sizeOfRectX);
		NetConf.setSizeOfRectY(sizeOfRectY);
		NetConf.setSizeOfText(sizeOfText);
		
		frame.repaint();
	}
	
	
	private void initGUI() {
		try {
			
			this.setTitle("Properties");
			
			
			GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				ComboBoxModel comboAppModel = 
						new DefaultComboBoxModel(NetConf.getAppTypes());
				comboAppearance = new JComboBox();
				comboAppearance.setModel(comboAppModel);
				comboAppearance.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						comboAppearanceActionPerformed(evt);
					}
				});
				comboAppModel.setSelectedItem(appType);
			}
			{
				labelAppearance = new JLabel();
				labelAppearance.setText("Appearance");
			}
			{
				labelFontName = new JLabel();
				labelFontName.setText("Font");
			}
			{
				ComboBoxModel comboFontModel = 
						new DefaultComboBoxModel(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
				comboFont = new JComboBox();
				comboFont.setModel(comboFontModel);
				comboFont.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						comboFontActionPerformed(evt);
					}
				});
				comboFontModel.setSelectedItem(font);
			}
			{
				SpinnerNumberModel spinTextSizeModel = 
						new SpinnerNumberModel(sizeOfText,1,50,1);
				spinTextSize = new JSpinner();
				spinTextSize.setModel(spinTextSizeModel);
				spinTextSize.addPropertyChangeListener(new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						spinTextSizePropertyChange(evt);
					}
				});
			}
			{
				labelTextSize = new JLabel();
				labelTextSize.setText("Size");
			}
			{
				labelArrow = new JLabel();
				labelArrow.setText("Arrow Size");
			}
			{
				SpinnerNumberModel spinSizeArrowModel = 
						new SpinnerNumberModel(arrowSize,1,50,1);
				spinSizeArrow = new JSpinner();
				spinSizeArrow.setModel(spinSizeArrowModel);
				spinSizeArrow.addPropertyChangeListener(new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						spinSizeArrowPropertyChange(evt);
					}
				});
				spinSizeArrow.addContainerListener(new ContainerAdapter() {
				});
			}
			{
				buttonOK = new JButton();
				buttonOK.setText("OK");
				buttonOK.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						buttonOKActionPerformed(evt);
					}
				});
			}
			{
				buttonDefault = new JButton();
				buttonDefault.setText("Default");
				buttonDefault.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						buttonDefaultActionPerformed(evt);
					}
				});
			}
			{
				labelSizeTrans = new JLabel();
				labelSizeTrans.setText("Transition size");
			}
			{
				SpinnerNumberModel spinSizeTransModel = 
						new SpinnerNumberModel(sizeOfRectX,50,600,10);
				spinSizeTrans = new JSpinner();
				spinSizeTrans.setModel(spinSizeTransModel);
				spinSizeTrans.addPropertyChangeListener(new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						spinSizeTransPropertyChange(evt);
					}
				});
			}
			{
				labelSizePlace = new JLabel();
				labelSizePlace.setText("Place size");
			}
			{
				SpinnerNumberModel spinSizePlaceModel = 
						new SpinnerNumberModel(sizeOfCircle,25,500,10);
				spinSizePlace = new JSpinner();
				spinSizePlace.setModel(spinSizePlaceModel);
				spinSizePlace.getEditor().addPropertyChangeListener(new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						editorPropertyChange(evt);
					}
				});
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addContainerGap(25, 25)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(comboAppearance, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(labelAppearance, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(spinSizePlace, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
				    .addComponent(labelSizePlace, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(spinSizeTrans, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
				    .addComponent(labelSizeTrans, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(spinSizeArrow, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
				    .addComponent(labelArrow, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(comboFont, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
				    .addComponent(labelFontName, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(17)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(spinTextSize, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
				    .addComponent(labelTextSize, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(34)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(buttonOK, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(buttonDefault, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(28, 28));
			thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup()
				.addContainerGap(38, 38)
				.addGroup(thisLayout.createParallelGroup()
				    .addComponent(comboFont, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
				    .addComponent(comboAppearance, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
				    .addGroup(thisLayout.createSequentialGroup()
				        .addGap(25)
				        .addGroup(thisLayout.createParallelGroup()
				            .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				                .addComponent(buttonDefault, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
				                .addGap(60))
				            .addGroup(thisLayout.createSequentialGroup()
				                .addGap(35)
				                .addGroup(thisLayout.createParallelGroup()
				                    .addComponent(spinSizeArrow, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
				                    .addComponent(spinSizeTrans, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
				                    .addComponent(spinSizePlace, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
				                    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				                        .addPreferredGap(spinSizeArrow, spinTextSize, LayoutStyle.ComponentPlacement.INDENT)
				                        .addComponent(spinTextSize, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)))))))
				.addGap(27)
				.addGroup(thisLayout.createParallelGroup()
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addComponent(labelFontName, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
				        .addGap(0, 50, Short.MAX_VALUE))
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addGap(0, 0, Short.MAX_VALUE)
				        .addComponent(labelTextSize, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
				        .addGap(0, 7, GroupLayout.PREFERRED_SIZE)
				        .addComponent(buttonOK, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE))
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addComponent(labelArrow, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
				        .addGap(0, 41, Short.MAX_VALUE))
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addComponent(labelSizeTrans, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
				        .addGap(0, 22, Short.MAX_VALUE))
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addComponent(labelSizePlace, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
				        .addGap(0, 47, Short.MAX_VALUE))
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addComponent(labelAppearance, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
				        .addGap(0, 34, Short.MAX_VALUE)))
				.addContainerGap(64, 64));
			pack();
			this.setSize(423, 380);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}
	
	private void comboAppearanceActionPerformed(ActionEvent evt) {
		System.out.println("comboAppearance.actionPerformed, event="+evt);
		
		String str = (String)comboAppearance.getSelectedItem();
		
		if(str.equals("Red")){
			colorAct = Color.ORANGE;
			colorRest = new java.awt.Color(255,60,60);
			colorBack = new java.awt.Color(255,250,250);
			appType = "Red";
		}
		else if(str.equals("Blue")){
			colorRest = new java.awt.Color(110,110,255);
			colorAct = new java.awt.Color(230,230,40);
			colorBack = new java.awt.Color(235,235,255);
			appType = "Blue";
		}
		else if(str.equals("Green")){
			colorAct = new java.awt.Color(230,60,230);
			colorRest = new java.awt.Color(70,235,70);
			colorBack = new java.awt.Color(235,255,235);
			appType = "Green";
		}		
	}
	
	private void buttonOKActionPerformed(ActionEvent evt) {
		System.out.println("buttonOK.actionPerformed, event="+evt);
		
		mydispose();
	}
	
	private void editorPropertyChange(PropertyChangeEvent evt) {
		System.out.println("spinSizePlace.getEditor().propertyChange, event="+evt);
		
		SpinnerNumberModel model = (SpinnerNumberModel)spinSizePlace.getModel();
		
		sizeOfCircle = model.getNumber().intValue();
	}
	
	private void spinSizeTransPropertyChange(PropertyChangeEvent evt) {
		System.out.println("spinSizeTrans.propertyChange, event="+evt);
		
		SpinnerNumberModel model = (SpinnerNumberModel)spinSizeTrans.getModel();
		
		sizeOfRectX = model.getNumber().intValue();
		sizeOfRectY = model.getNumber().intValue()/2;
		
	}
	
	private void buttonDefaultActionPerformed(ActionEvent evt) {
		System.out.println("buttonDefault.actionPerformed, event="+evt);
		
		sizeOfCircle = 75;
		sizeOfRectX  = 100;
		sizeOfRectY  = 50;
		arrowSize    = 10;
		
		colorAct  = Color.ORANGE;
		colorRest = new java.awt.Color(255,60,60);
		colorBack = new java.awt.Color(255,250,250);
		
		font    = "Arial";
		sizeOfText = 12;
		
		appType = "Red";
		
		this.repaint();
	}	
	
	public void repaint(long time, int x, int y, int width, int height) {
        super.repaint(time,x,y,width,height);       
        
        comboAppearance.getModel().setSelectedItem(appType);
        spinSizePlace.getModel().setValue(sizeOfCircle);
        spinSizeTrans.getModel().setValue(sizeOfRectX);
        spinSizeArrow.getModel().setValue(arrowSize);
        spinTextSize.getModel().setValue(sizeOfText);
        comboFont.getModel().setSelectedItem(font);
    }
	
	private void spinSizeArrowPropertyChange(PropertyChangeEvent evt) {
		System.out.println("spinSizeArrow.propertyChange, event="+evt);
		
		
		arrowSize = ((SpinnerNumberModel)spinSizeArrow.getModel()).getNumber().intValue();
	}
	
	private void spinTextSizePropertyChange(PropertyChangeEvent evt) {
		System.out.println("spinTextSize.propertyChange, event="+evt);
		
		
		sizeOfText = ((SpinnerNumberModel)spinTextSize.getModel()).getNumber().intValue();
	}
	
	private void comboFontActionPerformed(ActionEvent evt) {
		System.out.println("comboFont.actionPerformed, event="+evt);
	
		
		font = ((ComboBoxModel)comboFont.getModel()).getSelectedItem().toString();
				
	}
}

//EOF