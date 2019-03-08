package swingDemo;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class SwingApplication {
	public static void main(String[] args) {
		JFrame f = new JFrame("Simple Swing Application");
		Container contentPane = f.getContentPane();
		contentPane.setLayout(new GridLayout(1,2));//one row, two columns
		JButton button = new  JButton("Click me");
		final JLabel label = new JLabel();
		contentPane.add(button);
		contentPane.add(label);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String information=JOptionPane.showInputDialog("Please input a String");
				label.setText(information);
			}
		});
		f.setSize(200,100);
		f.show();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
