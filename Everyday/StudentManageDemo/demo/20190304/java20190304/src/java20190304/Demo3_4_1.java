package java20190304;

public class Demo3_4_1 {
	public static void main(String[] args) {
		ShowType st = new ShowType();
		GeneralType<Integer> i = new GeneralType<Integer>(2);
		GeneralType<String> s = new GeneralType<String>("hello");
		st.show(i);
		st.show(s);
	}
}

class GeneralType<Type>{
	Type object;
	public GeneralType(Type object) {
		this.object = object;
	}
	
	public Type getObj() {
		return object;
	}
	
}

class ShowType{
	public void show(GeneralType<?> o) {
		System.out.println(o.getObj().getClass().getName());
	}
}