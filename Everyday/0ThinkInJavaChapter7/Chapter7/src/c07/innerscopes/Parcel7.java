package c07.innerscopes;
//An anonymous inner class that calls the base-class constructor

public class Parcel7 {
	public Wrapping wrap(int x) {
		return new Wrapping(x) {
			public int value() {
				System.out.println("cont1->Contents->value: "+x);
				return super.value()*47;
			}
		};
	}
	
	public static void main(String[] args) {
		Parcel7 p=new Parcel7();
		Wrapping w = p.wrap(10);
		w.value();
		System.out.println("return: "+w.value());
	}
}
