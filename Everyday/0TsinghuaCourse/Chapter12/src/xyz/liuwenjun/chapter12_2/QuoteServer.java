package xyz.liuwenjun.chapter12_2;
//������Server,������Client
public class QuoteServer {
	
	public static void main(String args[])throws java.io.IOException{
		new QuoteServerThread().start();
	}

}
