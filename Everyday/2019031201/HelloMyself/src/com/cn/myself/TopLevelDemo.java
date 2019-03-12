package com.cn.myself;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TopLevelDemo {
	public static void main(String s[]) {
		JFrame frame = new JFrame("TopLevelDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel yellowLabel = new JLabel("");
		yellowLabel.setOpaque(true);
		yellowLabel.setBackground(Color.yellow);
		yellowLabel.setPreferredSize(new Dimension(200, 180));
		JMenuBar cyanMenuBar = new JMenuBar();
		cyanMenuBar.setOpaque(true);
		cyanMenuBar.setBackground(Color.cyan);
		cyanMenuBar.setBackground(Color.cyan);
		cyanMenuBar.setPreferredSize(new Dimension(200, 20));
		frame.setJMenuBar(cyanMenuBar);
		frame.getContentPane().add(yellowLabel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
}
