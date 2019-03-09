package demoSwing;
import java.awt.event.*;
import javax.swing.*;

public class ExtendMouseAdapter extends MouseAdapter{
	JFrame f;
	public ExtendMouseAdapter() {
		f=new JFrame();
		f.setSize(300, 150);
		f.show();
		f.addMouseListener(this);
		f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	public void mouseClicked(MouseEvent e) {
		f.setTitle("Point"+e.getX()+", "+e.getY());
	}
	public static void main(String[] args) {
		new ExtendMouseAdapter();
	}
}
