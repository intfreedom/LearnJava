//: Lunch.java

class Soup{
	private Soup() {}
	public static Soup makeSoup() {
		return new Soup();
	}
	private static Soup ps1 = new Soup();
	public static Soup access() {
		return ps1;
	}
	public void f() {}
}

class Sandwich{
	void f() {new Lunch();}
}

public class Lunch {
	void test() {
		Soup priv2 = Soup.makeSoup();
		Sandwich f1 = new Sandwich();
		Soup.access().f();
	}
}
