package demoDrawing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;

public class GraphicsTester extends JFrame {
	public GraphicsTester()
	{
		super("Font, Colour, Drawing");
		setVisible(true);
		setSize(480, 250);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.setFont(new Font("SansSerif", Font.BOLD, 12));
		g.setColor(Color.blue);
		g.drawString("ScanSerif, blue",20,50);
		
		g.setFont(new Font("Serif", Font.ITALIC, 14));
		g.setColor(new Color(255, 0, 0));
		g.drawString("Serif, red", 250,50);
		
		g.drawLine(20,60,460,60);
		
		g.setColor(Color.green);
		g.drawRect(20,70,100,50);
		g.fillRect(130,70,100,50);
		
		g.setColor(Color.yellow);
		g.drawRoundRect(240,70,100,50,50,50);
	}
	public static void main(String[] args) {
		GraphicsTester application = new GraphicsTester();
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
