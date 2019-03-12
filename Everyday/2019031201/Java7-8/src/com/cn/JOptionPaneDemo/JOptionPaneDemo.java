package com.cn.JOptionPaneDemo;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class JOptionPaneDemo extends JFrame implements ActionListener {
	public JOptionPaneDemo() {
		super("Simple");
		Container c = getContentPane();
		JButton button = new JButton("Quit");
		button.addActionListener(this);
		c.setLayout(new FlowLayout());
		c.add(button);
	}
	
	public void actionPerformed(ActionEvent e) {
		int select = JOptionPane.showConfirmDialog(this, "sure to do it?",
				"sure", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(select==JOptionPane.OK_OPTION)
			System.exit(0);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JOptionPaneDemo();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(200, 100);
		frame.setVisible(true);
	}
}
