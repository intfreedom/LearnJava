package c076;

public class Parcel {

	class Contents{
		private int i = 11;
		
		public int value() {return i;}
	}
	class Destination{
		private String label;
		Destination(String whereTo){
			System.out.println("herehere  "+whereTo);
			label = whereTo;
		}
		String readLabel() {return label;}
	}
	
	public void ship(String dest) {
		Contents c = new Contents();
		Destination d = new Destination(dest);
	}
	public static void main(String[] args) {
		Parcel p = new Parcel();
		p.ship("Tanzania");
	}
}
