package ioDemo;

import java.io.*;
public class Redirecting {
	public static void main(String[] args) throws IOException{
		BufferedInputStream in = new BufferedInputStream(
				new FileInputStream("D:\\02ability\\LearnJava\\Everyday\\20190307\\src\\ioDemo\\redirectDemo.java"));
		PrintStream out = new PrintStream(new
				BufferedOutputStream(new FileOutputStream("test.out")));
		System.setIn(in);    System.setOut(out);  System.setErr(out);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s;
		while((s = br.readLine()) != null) System.out.println(s);
		in.close();
		out.close();
		
	}
}
