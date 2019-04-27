package c442;

class Tag{
	Tag(int marker){
		System.out.println("Tag("+marker+"");
	}
}
//变量会在调用任何方法之前得到初始化，甚至在构建器调用之前；
class Card{
	Tag t1=new Tag(1);//before constructor
	Card(){
		System.out.println("Card()");
		t3 = new Tag(33);
	}
	Tag t2 = new Tag(2);//after constructor
	void f() {
		System.out.println("f()");
	}
	Tag t3 = new Tag(3);//At end
}

public class OrderOfInitialization {
	public static void main(String[] args) {
		System.out.println("I will see");
		Card t = new Card();
		System.out.println("I will see again");
		t.f();//shows that construction is done
	}

}
