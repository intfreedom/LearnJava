//:Music3.java
//An extensible program
package chapter7_2;
import java.util.*;

class Instrument3{
	public void play() {
		System.out.println("Instrument3.play()");
	}
}

class Wind3 extends Instrument3{
	public void play() {
		System.out.println("Wind3.play()");
	}
	public String what() {return "Wind3";}
}

class Percussion3 extends Instrument3{
	public void play() {
		System.out.println("Percussion3.play()");
	}
	public String what() {return "Percussion3";}
	public void adjust() {}
}

class Stringed3 extends Instrument3{
	public void play() {
		System.out.println("Stringed3.play()");
	}
	public String what() {return "Stringed3";}
	public void adjust() {}
}

class Brass3 extends Wind3{
	public void play() {
		System.out.println("Brass3.play()");
	}
	public void adjust() {
		System.out.println("Brass3.adjust()");
	}
}

class Woodwind3 extends Wind3{
	public void play() {
		System.out.println("Woodwind3.play()");
	}
	public String what() {return "Woodwind3";}
	
}

public class Music3 {
	static void tune(Instrument3 i) {
		i.play();
	}
	static void tuneAll(Instrument3[] e) {
		for(int i = 0; i < e.length; i++) {
			tune(e[i]);
		}
	}
	public static void main(String[] args) {
		Instrument3[] orchestra = new Instrument3[5];
		int i = 0;
		orchestra[i++] = new Wind3();
		orchestra[i++] = new Percussion3();
		orchestra[i++] = new Stringed3();
		orchestra[i++] = new Brass3();
		orchestra[i] = new Woodwind3();
		tuneAll(orchestra);
		System.out.println("不同的调用方式有何区别");
		Instrument3 orchestra1 = new Wind3();
		Instrument3 orchestra2 = new Percussion3();
		Instrument3 orchestra3 = new Stringed3();
		Instrument3 orchestra4 = new Brass3();
		Instrument3 orchestra5 = new Woodwind3();
		tune(orchestra1);
		tune(orchestra2);
		tune(orchestra3);
		tune(orchestra4);
		tune(orchestra5);
	}
	
}
