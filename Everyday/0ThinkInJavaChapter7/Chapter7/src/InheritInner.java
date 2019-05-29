class WithInner{
	class Inner{
		
	}
}

public class InheritInner extends WithInner.Inner{
	//!InheritInner(){}//Won't compile
	InheritInner(WithInner wi){
		wi.super();
		System.out.println("yesyesyes");
	}
	public static void main(String[] args) {
		System.out.println("1111111");
		WithInner wi = new WithInner();
		System.out.println("2222222");
		InheritInner ii = new InheritInner(wi);
	}
}
