package c07.innerscopes;
// A method that returns an anonymous inner class

public class Parcel6 {
	public Contents cont() {
		System.out.println("cont: ");
		return new Contents() {
			private int i=11;
			public int value() {
				System.out.println("cont->Contents->value: "+i);
				return i;
				}
		};
	}
	//this anonymous inner class replace code,as follows;
	public Contents cont1() {
		System.out.println("cont1: ");
		class MyContents implements Contents {
			private int i=12;
			public int value() {
				System.out.println("cont1->Contents->value: "+i);
				return i;
			}
		}
		return new MyContents();
	}
	
	public static void main(String[] args) {
		Parcel6 p = new Parcel6();
		Contents c = p.cont();
		c.value();
		Parcel6 p1 = new Parcel6();
		Contents c1 = p1.cont1();//notice c1 return new MyContents();
		c1.value();
		//System.out.println("cont->Contents->value: "+c.value());
	}
}
