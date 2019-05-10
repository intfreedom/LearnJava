package c076;

public class Parcel2 {

	class Contents{
		private int i = 11;
		public Contents() {
			System.out.println("Class Contents  ");
		}
		public int value() {return i;}
	}
	class Destination{
		private String label;
		Destination(String whereTo){
			System.out.println("Class Destination  "+whereTo);
			label = whereTo;
		}
		String readLabel() {return label;}
	}
	public Destination to(String s) {
		return new Destination(s);
	}
	public Contents cont() {
		return new Contents();
	}
	public void ship(String dest) {
		Contents c = cont();
		Destination d = to(dest);
	}
	
	public static void main(String[] args) {
		Parcel2 p = new Parcel2();
		System.out.println("111111");
		p.ship("Tanzania");
		System.out.println("222222");
		Parcel2 q = new Parcel2();
		
		Parcel2.Contents c = q.cont();
		System.out.println("333333");
		Parcel2.Destination  d = q.to("Borneo");
	}
	
}
