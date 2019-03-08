package swingDemo;

public class Parcel2 {
	class Contents {
		private int i = 11;
		public int value() {return i;}
	}
	class Destination{
		private String label;
		Destination(String whereTo){
			label = whereTo;
			System.out.println("21");
		}
		String readLabel() {return label;}
	}
	public Destination to(String s){return new Destination(s);}
	public Contents cont() {return new Contents();}
	
	public void ship(String dest) {
		Contents c = cont();
		Destination d = to(dest);
		System.out.println(d.readLabel());
	}
	public static void main(String[] args) {
		Parcel2 p = new Parcel2();
		p.ship("Tanzania2");
		Parcel2 q = new Parcel2();
		Parcel2.Contents c = q.cont();
		Parcel2.Destination d = q.to("Borneo");
	}

}
