package c07.innerscopes;
//Using "instance initialization" to perform
//construction on an anonymous inner class;

public class Parcel9 {
	public Destination dest(final String dest, final float price) {
		return new Destination() {
			private int cost;
			//Instance initialization for each object;
			{
				cost = Math.round(price);
				if(cost>100)
					System.out.println("over budget!");
			}
			private String label = dest;
			public String readLabel() {return label;}
		};
	}
	
	public static void main(String[] args) {
		Parcel9 p = new Parcel9();
		Destination d = p.dest("Tanzania", 101.395F);
	}
}
