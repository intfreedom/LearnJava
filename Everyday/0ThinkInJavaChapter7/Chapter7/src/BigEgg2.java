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

public class BigEgg2 extends Egg2 {//bigEgg2.Yolk��ȷ����չ��Egg2.Yolk���Ҹ��������ķ�����
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
		insertYolk(new Yolk());}//insertYolk()����BigEgg2�����Լ���ĳ��Yolk��������������
	//Egg2��y��������Ե�g()����y.f()��ʱ�򣬾ͻ�ʹ��f()�����ǰ汾��
	public static void main(String[] args) {
		System.out.println("111111");
		Egg2 e2 = new BigEgg2();//�ȵ��ø��๹��������ִ�����ࣻ
		System.out.println("222222");
		e2.g();//
	}
}
