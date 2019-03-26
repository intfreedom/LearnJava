package DemoInterface;

class Circle implements Shape2D{
	double radius;
	String color;
	public Circle(double r) {
		radius=r;
	}
	public double area() {
		return(pi*radius*radius);
	}
	public void setColor(String str) {
		color=str;
		System.out.println("color="+color);
	}
}

public class DemoTest{
	public static void main(String[] args) {
		Circle cir;
		cir=new Circle(2.0);
		cir.setColor("blue");
		System.out.println("Area = " + cir.area());
	}
}

interface Shape2D{
	final double pi=3.14;
	public abstract double area();
}