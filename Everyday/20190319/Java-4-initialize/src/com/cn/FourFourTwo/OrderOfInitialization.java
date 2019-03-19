package com.cn.FourFourTwo;
class Tag{
	Tag(int marker){
		System.out.println("Tag("+marker+")");
	}
}

class Card{
	Tag t1 = new Tag(1);
	Card(){
		System.out.println("Card()");
		t3 = new Tag(33);
	}
	Tag t2 =new Tag(2);
	void f() {
		System.out.println("f()");
	}
	Tag t3 = new Tag(3);//若没有初始化t3，line12（t3 = ···）会报错
}
public class OrderOfInitialization {
	public static void main(String[] args) {
		Card t = new Card();
		t.f();
	}
}
