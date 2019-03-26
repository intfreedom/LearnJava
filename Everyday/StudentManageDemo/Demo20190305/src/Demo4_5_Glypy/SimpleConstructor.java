//:SimpleConstructor.java
//Demonstration of a simple constructor
package Demo4_5_Glypy;

class Rock {
	Rock(){
		System.out.println("Creating Book");
	}
}

public class SimpleConstructor {
	public static void main(String[] args) {
		for(int i = 0; i < 10; i++)
			new Rock();
	}
} ///:~
