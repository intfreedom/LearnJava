package xyz.liuwenjun.chapter11_2;
import java.net.*;
import java.io.*;

public class URLReader {
	public static void main(String[] args) throws Exception{
		URL cs = new URL("http://www.sina.com/");
		BufferedReader in = new BufferedReader(
				new InputStreamReader(cs.openStream()));
		String inputLine;
		while((inputLine = in.readLine())!=null)
			System.out.println(inputLine);
		in.close();
	}
}


//URL(Uniform Resource Locator)ͳһ��Դ��λ����Internet��ĳһ��Դ�ĵ�ַ
//protocol��resourceName
//���������˿ںţ�