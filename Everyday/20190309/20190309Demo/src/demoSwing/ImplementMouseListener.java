package demoSwing;

import java.awt.event.*;
import javax.swing.*;

public class ImplementMouseListener implements MouseListener{
	JFrame f;
	public ImplementMouseListener() {
		f=new JFrame();
		f.setSize(300,150);
		f.show();
		f.addMouseListener(this);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {
		f.setTitle("Point is "+e.getX()+", "+e.getY());
	}
	public static void main(String[] args) {
		new ImplementMouseListener();
	}
}
