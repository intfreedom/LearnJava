//:Transmogrify.java
//Dynamically changing the behavior of an object via composition.
package c07.c08;

interface Actor{
	void act();
}

class HappyActor implements Actor{
	public void act() {
		System.out.println("HappyAcotr");
	}
}

class SadActor implements Actor{
	public void act() {
		System.out.println("SadActor");
	}
}

class Stage{
	Actor a = new HappyActor();
	void change() {a = new SadActor();}
	void go() {a.act();}
}

public class Transmogrify {
	public static void main(String[] args) {
		System.out.println("11111111111");
		Stage s = new Stage();
		System.out.println("22222222222");
		s.go();//Prints "HappyActor"
		s.change();
		s.go();//Prints "SadActor"
	}
}
