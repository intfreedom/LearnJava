package com.cn.liuwenjun;

public class AutoInc {
	public static void main(String[] args) {
		int i = 1;
		prt("i: " + i);
		prt("++i: " + ++i);
		prt("i++: " + i++);
		prt("i: " + i);
		prt("--i: " + --i);
		prt("i--: " + i--);
		prt("i: " + i);
	}
	
	static void prt(String s) {
		System.out.println(s);
	}
}
