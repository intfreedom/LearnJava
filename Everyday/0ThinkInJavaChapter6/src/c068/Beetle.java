package c068;

class Insect{
	int i=9;
	int j;
	Insect(){
		prt("i = "+i+", j= "+j);
		j=39;
	}
	static int x1= prt("static Insect.x1 initialized");
	static int prt(String s) {
		System.out.println(s);
		System.out.println("prt_Insect.x1");
		return 47;
		
	}
}

public class Beetle extends Insect {

	int k=prt("Beetle.k initialized");
	Beetle(){
		prt("k = "+k);
		prt("j = "+j);
	}
	
	static int x2 = 
			prt("static Beetle.x2 initialized");
	static int prt(String s) {
		System.out.println(s);
		System.out.println("prt_Beetle.x2");
		return 63;
	}
	Beetle b = new Beetle();
	public static void main(String[] args) {
		System.out.println("StartMain1");
		prt("Beetle constructor");
		System.out.println("StartMain2");
//		Beetle b = new Beetle();换到main之外会有何变化；就不会调用基础类的构建器以及
		//自己的构建器； 
	}
}
