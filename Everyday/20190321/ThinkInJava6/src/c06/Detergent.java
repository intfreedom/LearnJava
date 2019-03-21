package c06;
class Cleanser{
	private String s = new String("Cleanser");
	public void append(String a) {s+=a;}
	public void dilute() {append(" dilute()");}
	public void apply() {append(" apply()");}
	public void scrub() {append(" scrub()");}
	public void print() {System.out.println(s);}
	public static void main(String[] args) {
		Cleanser x = new Cleanser();
		x.dilute(); x.apply(); x.scrub();
		x.print();
	}
}

public class Detergent extends Cleanser {
	public void scrub() {
		append(" Detergent.scrub()");
		super.scrub();
	}
	public void foam() {append(" foam()");}
	public static void main(String[] args) {
		Detergent x = new Detergent();
		x.dilute();
		x.apply();
		x.scrub();
		x.foam();
		x.print();
		System.out.println("Testing base class: ");
		Cleanser.main(args);
	}

}
