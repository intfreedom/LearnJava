package DemoVariety;

public class DemoTest {
	public static void main(String[] args)
	{
		Shape[] s = new Shape[9];
		int n;
		for(int i = 0;i<s.length;i++) {
			n = (int)(Math.random()*3);
			switch(n) {
			case 0:s[i] = new Circle(); break;
			case 1:s[i] = new Square(); break;
			case 2:s[i] = new Triangle();
			}
		}
		for(int i = 0;i<s.length;i++) s[i].draw();
	}
}

class Shape
{
	void draw()
	{
		
	}
	void erase()
	{
		
	}
}

class Circle extends Shape{
	void draw()
	{
		System.out.println("Circle.draw()");
	}
	void erase()
	{
		System.out.println("Circle.erase()");
	}
}

class Square extends Shape{
	void draw()
	{
		System.out.println("Square.draw()");
	}
	void erase()
	{
		System.out.println("Square.erase()");
	}
}

class Triangle extends Shape{
	void draw() {
		System.out.println("Triangle.draw()");
	}
	void erase()
	{
		System.out.println("Triangle.erase");
	}
}