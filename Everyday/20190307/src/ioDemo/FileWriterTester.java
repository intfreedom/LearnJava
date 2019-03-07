package ioDemo;

import java.io.*;

class FileWriterTester {
	public static void main(String[] args){
		String fileName = "Hello.txt";
		try {
			FileWriter writer = new FileWriter(fileName);
			writer.write("Hello!\n");
			writer.write("This is my first text file, \n");
			writer.write("You can see how this is done.\n");
			writer.write("input a chinese.\n");
			writer.close();
		}
		catch(IOException iox) {System.out.println("Problem writing" + fileName);}
	}
}
