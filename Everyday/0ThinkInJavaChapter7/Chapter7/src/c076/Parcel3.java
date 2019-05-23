package c076;

abstract class Contents{
	abstract public int value();
}

interface Destination{
	String readLabel();
}

public class Parcel3 {

	private class PContents extends Contents{
		private int i = 11;
		public int value() {
			System.out.println("PContents value i: "+i);
			return i;
			}
		
	}
	protected class PDestination implements Destination{
		private String label;
		private PDestination(String whereTo) {
			label = whereTo;
			System.out.println("PDestination: "+label);
		}
		public String readLabel() {return label;}
	}
	
	public Destination dest(String s) {
		return new PDestination(s);
	}
	
	public Contents cont() {
		return new PContents();
	}
}

class Test{
	public static void main(String[] args) {
		System.out.println("1******");
		Parcel3 p = new Parcel3();
		System.out.println("2******");
		Contents c = p.cont();
		System.out.println("3******");
		Destination d = p.dest("Tanzania");
	}
}