package demoCardLayout;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JPanel;

public class CardLayoutDemo {
	JPanel cards;
	final static String BUTTONPANEL = "JPanel with JButtons";
	final static String TEXTPANEL = "JPanel with JTextField";
	
	public void addComponentToPane(Container pane) {
		JPanel comboBoxPane = new JPanel();
		String comboBoxItems[] = {BUTTONPANEL, TEXTPANEL};
		JComboBox cb = new JComboBox(comboBoxItems);
		cb.setEditable(false);
		cb.addItemListener(this);
		comboBoxPane.add(cb);
		
		JPanel card1 = new JPanel();
		card1.add(new JButton("Button1"));
		card1.add(new JButton("Button2"));
		card1.add(new JButton("Button3"));
		
		JPanel card2 = new JPanel();
		card2.add(new JTextField("TextField", 20));
		
		cards = new JPanel(new CardLayout());
		cards.add(card1, BU)
	}
	

}
