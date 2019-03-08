package swingDemo;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SwingApplet extends JApplet {
	public void init() {
		Container contentPane=getContentPane();
		contentPane.setLayout(new GridLayout(2,1));
		JButton button=new JButton("Click me");
		final JLabel label=new JLabel();
		contentPane.add(button);
		contentPane.add(label);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String information = JOptionPane.showInputDialog("Please input a String");
				label.setText(information);
			}
		});
	}

}
