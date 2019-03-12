package com.cn.java7_9;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ScrollDemo2 extends JPanel{
	private Dimension size;
	private Vector objects;
	
	private final Color colors[] = {
			Color.red, Color.blue, Color.green, Color.orange,
			Color.cyan, Color.magenta, Color.darkGray, Color.yellow
	};
	private final int color_n = colors.length;
	
	JPanel drawingArea;
	
	public ScrollDemo2() {
		setOpaque(true);
		size = new Dimension(0, 0);
		objects = new Vector();
				
		JLabel instructionsLeft = new JLabel(
				"Click left mouse button to place a circle.");
		JLabel instructionsRight = new JLabel(
				"Click rigth mouse button to clear drawing area.");
		JPanel instructionPanel = new JPanel(new GridLayout(0, 1));
		instructionPanel.add(instructionsLeft);
		instructionPanel.add(instructionsRight);
		
		drawingArea = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				Rectangle rect;
				for(int i = 0; i<objects.size();i++) {
					rect = (Rectangle)objects.elementAt(i);
					g.setColor(colors[(i % color_n)]);
					g.fillOval(rect.x, rect.y, rect.width, rect.height);
					
				}
			}
		};
		drawingArea.setBackground(Color.white);
		drawingArea.addMouseListener(new MyMouseListener());
		
		JScrollPane scroller = new JScrollPane(drawingArea);
		scroller.setPreferredSize(new Dimension(200, 200));
		
		setLayout(new BorderLayout());
		add(instructionPanel, BorderLayout.NORTH);
		add(scroller, BorderLayout.CENTER);
	}
	
	class MyMouseListener extends MouseInputAdapter{
		final int W = 100;
		final int H = 100;
		
		public void mouseReleased(MouseEvent e) {
			boolean changed = false;
			if(SwingUtilities.isRightMouseButton(e)) {
				objects.removeAllElements();
				size.width=0;
				size.height=0;
				changed = true;
			} else {
				int x = e.getX() - W/2;
				int y = e.getY() - H/2;
				if(x < 0)x = 0;
				if(y < 0)y = 0;
				Rectangle rect = new Rectangle(x,  y, W, H);
				objects.addElement(rect);
				drawingArea.scrollRectToVisible(rect);
				
				int this_width = (x + W +2);
				if(this_width > size.width)
				{size.width = this_width; changed=true;}
				
				int this_height = (y + H + 2);
				if(this_height > size.height)
				{size.height = this_height; changed=true;}
				
			}
			if(changed) {
				drawingArea.setPreferredSize(size);
				
				drawingArea.revalidate();
				
			}
			drawingArea.repaint();
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("ScrollDemo2");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new ScrollDemo2());
		frame.pack();
		frame.setVisible(true);
	}
}

