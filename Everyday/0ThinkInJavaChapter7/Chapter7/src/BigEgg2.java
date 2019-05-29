class Egg2{
	protected class Yolk{
		public Yolk() {
			System.out.println("Egg2.Yolk()");
		}
		public void f() {
			System.out.println("Egg2.Yolk.f()");
		}
	}
	private Yolk y = new Yolk();
	public Egg2() {
		System.out.println("New Egg2()");
	}
	public void insertYolk(Yolk yy) {y=yy;}
	public void g() {y.f();}
}

public class BigEgg2 extends Egg2 {//bigEgg2.Yolk明确地拓展了Egg2.Yolk而且覆盖了它的方法；
	public class Yolk extends Egg2.Yolk{
		public Yolk() {
			System.out.println("BigEgg2.Yolk()");
		}
		public void f() {
			System.out.println("BigEgg2.Yolk().f()");
		}
	}
	public BigEgg2() {
		System.out.println("*****");
		insertYolk(new Yolk());}//insertYolk()允许BigEgg2将它自己的某个Yolk对象上溯造型至
	//Egg2的y句柄，所以当g()调用y.f()的时候，就会使用f()被覆盖版本；
	public static void main(String[] args) {
		System.out.println("111111");
		Egg2 e2 = new BigEgg2();//先调用父类构建器，再执行子类；
		System.out.println("222222");
		e2.g();//
	}
}
