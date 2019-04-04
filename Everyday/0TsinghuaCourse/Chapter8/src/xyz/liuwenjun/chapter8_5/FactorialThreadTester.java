package xyz.liuwenjun.chapter8_5;

public class FactorialThreadTester {
	
	public static void main(String[] args) {
		System.out.println("main thread starts");
		FactorialThread t = new FactorialThread(10);
		new Thread(t).start();//将自动进入run()方法
		System.out.println("new thread started, main thread ends");
	}

}

class FactorialThread implements Runnable{
	private int num;
	public FactorialThread(int num) {
		this.num=num;
	}
	
	public void run() {
		int i=num;
		int result=1;
		while(i>0)
		{
			result=result*i;
			i=i-1;
		}
		System.out.println("The factorial of "+num+" is "+result);
		System.out.println("new thread ends.");

	}
}