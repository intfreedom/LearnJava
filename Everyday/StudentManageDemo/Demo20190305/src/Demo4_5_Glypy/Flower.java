//:Flower.java
//Calling constructors with "this"

package Demo4_5_Glypy;

public class Flower {
	private int petalCount = 0;
	private String s = new String("null");
	Flower(int petals){
		petalCount = petals;
		System.out.println(
				"Constructor w/ int arg only,petaCount= ");
	}
}
