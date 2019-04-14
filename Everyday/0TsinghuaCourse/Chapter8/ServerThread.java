package xyz.liuwenjun.chapter12_1;
import java.io.*;
import java.net.*;

public class ServerThread extends Thread{
	Socket socket = null;
	int clientnum;
	public ServerThread(Socket socket, int num) {
		this.socket=socket;
		clientnum=num+1;
		
	}
	public void run() {
		try {
			String line;
			BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter os = new PrintWriter(socket.getOutputStream());
			BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Client: "+clientnum+":"+is.readLine());
			line = sin.readLine();
			while(!line.equals("bye"))
			{
				os.println(line);
				os.flush();
				System.out.println("Server: "+line);
				System.out.println("Client: "+clientnum+":"+is.readLine());
				line=sin.readLine();
			}
			os.close();
			is.close();
			socket.close();
		}catch(Exception e) {
			System.out.println("Error: "+e);
		}
	}

}
