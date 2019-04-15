package xyz.liuwenjun.chapter12_2;
//先运行Server,再运行Client
public class QuoteServer {
	
	public static void main(String args[])throws java.io.IOException{
		new QuoteServerThread().start();
	}

}
