package xyz.liuwenjun.chapter12_1;
//�ȿ�һ����������򿪷�����Server���ٿ�һ�����������һ��client;
//�ٿ�һ�����������һ��client;
import java.io.*;
import java.net.ServerSocket;

public class MultiTalkServer {
	static int clientnum=0;
	public static void main(String[] args)throws IOException {
		ServerSocket serverSocket=null;
		boolean listening=true;
		try {
				serverSocket=new ServerSocket(4700);
			}catch(IOException e) {
				System.out.println("can not listen on port:4700.");
				System.exit(-1);
			}
			while(listening)
			{
				new ServerThread(serverSocket.accept(), clientnum).start();
				clientnum++;
			}
			serverSocket.close();
		}
			
	
	}