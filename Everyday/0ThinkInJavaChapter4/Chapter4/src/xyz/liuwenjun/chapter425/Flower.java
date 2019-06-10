//:Flower.java
//Calling constructors with "this"
package xyz.liuwenjun.chapter425;

public class Flower {
	private int petalCount=0;
	private String s = new String("null");
	Flower(int petals){
		petalCount = petals;
		System.out.println("Constructor w/ int arg only, petalCount="+petalCount);
		
	}
	Flower(String ss){
		System.out.println("Constructor w/ String arg only, s= "+ss);
		System.out.println(this.s);
		System.out.println(this.petalCount);
		s=ss;
	}
	Flower(String s, int petals){
		this(petals);
		Flower f1 = new Flower(petals);//以上两句的效果相同；
		//print();构建器可以调用其他方法，其他方法内部不能调用构建器；
		//Flower(petals);为何不能用此
		this.s=s;
		System.out.println("String & int args");
	}
	Flower(){
		this("hi", 47);
		System.out.println("default constructor(no args)");
	}
	void print() {
		System.out.println("petalCount = "+petalCount+" s= "+s);
	}
	public static void main(String[] args) {
		Flower x = new Flower();
		System.out.println(222);
//		x.print();
		Flower y = new Flower("Yes, Yes");
	}
}
