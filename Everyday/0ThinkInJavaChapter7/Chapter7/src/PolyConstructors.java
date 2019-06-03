//:PloyConstructors.java
//Constructors and polymorphism don't produce what you might expect.

abstract class Glyph{
	abstract void draw();
	Glyph(){
		System.out.println("Glypy() before draw()");
		draw();
		System.out.println("Glypy() after draw()");
	}
}

class RoundGlyph extends Glyph{
	int radius = 1;
	RoundGlyph(int r){
		System.out.println("**********");
		radius = r;
		System.out.println("RoundGlyph.RoundGlyph(), radius = "+radius);
	}
	void draw() {
		System.out.println("RoundGlyph.draw(), radius = "+radius);
	}
}

public class PolyConstructors {
	public static void main(String[] args) {
		new RoundGlyph(5);
	}

}
