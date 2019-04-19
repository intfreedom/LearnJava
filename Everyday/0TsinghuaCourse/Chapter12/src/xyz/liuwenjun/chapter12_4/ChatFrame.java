package xyz.liuwenjun.chapter12_4;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class ChatFrame extends JFrame implements ActionListener {
	JTextField tf;
	JTextArea ta;
	JScrollPane sp;
	JButton send;
	JPanel p;
	
	int port;
	String s="";
	String myID;
	Date date;
	ServerSocket server;
	Socket mySocket;
	BufferedReader is;//接收输入；
	PrintWriter os;
	String line;
	
	public ChatFrame(String ID, String remoteID, String IP, int port, boolean isServer) {
		super(ID);
		myID=ID;
		this.port=port;
		ta=new JTextArea();
		ta.setEditable(false);
		sp=new JScrollPane(ta);
		this.setSize(330,400);
		this.setResizable(false);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e) {
			System.out.println("UI error");
		}
		
		this.getContentPane().add(sp, "Centter");
		p=new JPanel();
		this.getContentPane().add(p,"South");
		send=new JButton("Send");
		tf=new JTextField(20);
		tf.requestFocus();
		p.add(tf);
		p.add(send);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		send.addActionListener(this);
		tf.addActionListener(this);
		
		if(isServer) {
			try {
				server=null;
				try {
					server=new ServerSocket(port);
				}catch(Exception e) {
					System.out.println("can not listen to: "+e);
				}
				mySocket=null;
				try {
					mySocket=server.accept();
				}catch(Exception e) {
					System.out.println("Error."+e);
				}
				is = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
				os = new PrintWriter(mySocket.getOutputStream());
			}catch(Exception e) {
				System.out.println("Error: in server client socket "+e);
			}
		}//end of if
		else {
			try {
				mySocket=new Socket(IP, port);
				os=new PrintWriter(mySocket.getOutputStream());
				is=new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			}catch(Exception e) {
				System.out.println("Error: in client socket"+e);
			}
		}
		while(true) {
			try {
				line=is.readLine();
				date=new Date();
				SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String currentTime=formatter.format(date);
				s+=currentTime+""+remoteID+"说：\n"+line+"\n";
				ta.setText(s);
			}catch(Exception e) {
				System.out.println("Error: in receive remote");
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime=formatter.format(date);
		s+=currentTime+" "+myID+"shuo: \n"+tf.getText()+"\n";
		ta.setText(s);
		os.println(tf.getText());
		os.flush();
		tf.setText("");
		tf.requestFocus();
	}
	

}
