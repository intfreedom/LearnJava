package java20190304;
class GeneralMethod{
	<Type> void printClassName(Type object) {
		System.out.println(object.getClass().getName());
	}
}
public class Demo3_4{
	public static void main(String[] args) {
		 GeneralMethod gm = new GeneralMethod();
		 gm.printClassName("hello");
		 gm.printClassName(3);
		 gm.printClassName(3.0f);
		 gm.printClassName(3.0);
	}
}
//public class Demo3_4 {
//
//		public static void main(String[] args)
//		{
//			GeneralType<Integer> i = new GeneralType<Integer> (2);
//			GeneralType<Double> d = new GeneralType<Double> (0.33);
//			System.out.println("i.object= " + (Integer)i.getObj());
//		}
//}
//
//class GeneralType<Type>
//{
//	
//	Type object;
//	public GeneralType(Type object) {
//		this.object = object;
//	}
//	public Type getObj() {
//		return object;
//	}
//	}