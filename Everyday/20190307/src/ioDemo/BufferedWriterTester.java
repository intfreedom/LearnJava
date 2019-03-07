package ioDemo;
import java.io.*;

public class BufferedWriterTester {
	public static void main(String[] args) throws IOException{
		String fileName = "newHello.txt";
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		out.write("Hello!");
		out.newLine();
		out.write("This is another text file using BufferedWriter, ");
		out.newLine();
		out.write("So I can use a common way to start a newline");
		out.close();
		//file is not in console
		//file is in project file;for example D:\02ability\LearnJava\Everyday\20190307
		System.out.println("why nothing happen");
		
	}
}
