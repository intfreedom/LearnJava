package c07.innerscopes;
//An anonymous inner class that performs

public class Parcel8 {
	//Argument must be final to use inside
	//anonymous inner class;
	public Destination dest(final String dest) {
		System.out.println("testtesttest");
		return new Destination(){
			private String label = dest;
			public String readLabel() {
				System.out.println("String: "+label);
				return label;
				}
			
		};
	}
	
	public static void main(String[] args) {
		Parcel8 p = new Parcel8();
		Destination d = p.dest("Tanzania");
		System.out.println("VSVSVStesttesttest");
		d.readLabel();
	}
}
//testtesttest
//VSVSVStesttesttest
//String: Tanzania
//结果表明，Parcel6中，调用，不用使用c1.MyContents.value()这般调用；
//因为，句柄d已然返回到匿名类；再Parcel6.java中，也就是c1已经返回到notice c1 return new MyContents();
