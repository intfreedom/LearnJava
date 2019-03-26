package java20190304;

public class Demo3_6 extends Custom{
	Spoon sp;Fork frk;Knife kn;
	DinnerPlate pl;
	public Demo3_6(int i) {
		super(i+1);
		sp = new Spoon(i+2);
		frk = new Fork(i+3);
		kn = new Knife(i+4);
		pl = new DinnerPlate(i+5);
		System.out.println("PlaceSetting constructor");
	}
	public static void main(String[] args) {
		Demo3_6 demo = new Demo3_6(9);
	}
}

class Plate{
	public Plate(int i) {
		System.out.println("Plate constructor");
	}
}

class DinnerPlate extends Plate{
	public DinnerPlate(int i) {
		super(i);
		System.out.println("DinnerPlate constructor");
	}
}

class Utensil{
	Utensil(int i){
		System.out.println("Utensil constructor");
	}
}

class Spoon extends Utensil{
	public Spoon(int i) {
		super(i);
		System.out.println("Spoon constructor");
	}
}
class Fork extends Utensil{
	public Fork(int i) {
		super(i);
		System.out.println("Fork constructor");
	}
}
class Knife extends Utensil{
	public Knife(int i) {
		super(i);
		System.out.println("Knife constructor");
	}
}

class Custom{
	public Custom(int i) {
		System.out.println("Custom constructor");
	}
}


//class Line
//{
//	private Demo3_6 p1,p2;
//	Line(Point)
//}