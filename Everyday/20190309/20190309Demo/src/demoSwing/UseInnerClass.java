package demoSwing;
import java.awt.event.*;
import javax.swing.*;

public class UseInnerClass {
	JFrame f;
	public UseInnerClass() {
		f=new JFrame();
		f.setSize(300, 150);
		f.show();
		f.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				f.setTitle("Point"+e.getX()+", "+e.getY());
			}
		});
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String[] args) {new UseInnerClass();}
}
