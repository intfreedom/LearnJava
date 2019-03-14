package com.cn.liuwenjun;

class Value{
	int i;
}
public class Equivalence {
	public static void main(String[] args) {
		Integer n1 = new Integer(47);
		Integer n2 = new Integer(47);
		System.out.println(n1 == n2);
		System.out.println(n1 != n2);
		System.out.println(n1.equals(n2));
		System.out.println("*****************");
		Value v1 = new Value();
		Value v2 = new Value();
		v1.i = v2.i = 100;
		System.out.println(v1.equals(v2));
		System.out.println(test(11));
		System.out.println(test2(100));
	}
	
	static int test(int testval) {
		int result = 0;
		if(testval > 10)
			result = -1;
		else if(testval < 10)
			result = +1;
		else
			return result;
		
		return result;
	}
	
	static int test2(int testval) {
		if(testval > 100)
			return -1;
		if(testval < 100)
			return +1;
		return 0;
	}

}

