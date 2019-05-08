package c068;
class SmallBrain{}
final class Dinosaur{
	int i=7;
	int j=1;
	SmallBrain x = new SmallBrain();
	void f() {
		System.out.println("i= "+i+" j= "+j);
	}
}
public class Jurassic {

	public static void  main(String[] args) {
		Dinosaur  n = new Dinosaur();
		n.f();
		n.i=40;
		n.j++;
		n.f();
	}
}
