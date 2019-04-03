package xyz.liuwenjun.chapter8_2;

public class FactorialThreadTime {
	
	public static void main(String[] args) {
		System.out.println("main thread starts");
		FactorialThreadT thread = new FactorialThreadT(10);
		thread.start();//将自动进入run()方法
		//让其休眠一段时间，让线程执行；
		try {
			Thread.sleep(1);
		}
		catch(Exception e) {};
		
		System.out.println("main thread ends");
	}

}


class FactorialThreadT extends Thread{
	private int num;
	public FactorialThreadT(int num) {
		this.num=num;
	}
	
	public void run() {
		int i=num;
		int result=1;
		System.out.println("new thread started");
		while(i>0)
		{
			result=result*i;
			i=i-1;
		}
		System.out.println("The factorial of "+num+" is "+result);
		System.out.println("new thread ends.");

	}
}