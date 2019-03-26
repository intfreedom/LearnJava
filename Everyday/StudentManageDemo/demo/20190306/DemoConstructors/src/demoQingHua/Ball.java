package demoQingHua;

public class Ball {
	private Point center;
	private double radius;
	private String colour;
	public Ball() {}
	public Ball(double xValue,double yValue, double r) {
		center = new Point(xValue, yValue);
		radius = r;
		
	}
	public Ball(double xValue, double yValue, double r, String c) {
		this(xValue, yValue, r);
		colour = c;
	}
	public String toString() {
		return "A ball with center " + center.toString() + ", radius "
	+ Double.toString(radius) + ",colour " + colour;
	}
}
