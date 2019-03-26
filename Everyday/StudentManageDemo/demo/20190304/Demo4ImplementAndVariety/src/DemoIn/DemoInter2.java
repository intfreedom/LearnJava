package DemoIn;


public class DemoInter2 {
	public static void main(String[] args)
	{
		Shape2D var1, var2;
		var1 = new Rectangle(5,6);
		System.out.println("Area of var1 = "+var1.area());
		var2 = new Circle1(2.0);
		System.out.println("Area of var2 = "+var2.area());
	}
}

class Circle1 implements Shape2D
{
	double radius;
	public Circle1(double r)
	{
		radius = r;
	}
	public double area()
	{
		return(pi*radius*radius);
	}
}

class Rectangle implements Shape2D
{
	int width,height;
	public Rectangle(int w, int h)
	{
		width=w;
		height=h;
	}
	public double area()
	{
		return(width *height);
	}
}
interface Shape2D{
	final double pi=3.14;
	public abstract double area();
}

