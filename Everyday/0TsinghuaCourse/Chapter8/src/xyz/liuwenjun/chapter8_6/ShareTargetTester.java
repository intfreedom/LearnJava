package xyz.liuwenjun.chapter8_6;
//用同一个实现了Runnable接口共享数据；
public class ShareTargetTester {
	
	public static void main(String[] args) {
		//只用一个Runnable类型的对象为参数创建3个新线程；
		TestThread threadobj=new TestThread();
		System.out.println("Starting threads");
		
		new Thread(threadobj, "Thread1").start();
		new Thread(threadobj, "Thread2").start();
		new Thread(threadobj, "Thread3").start();
		
		System.out.println("Threads started, main ends\n");
	}

}

class TestThread implements Runnable{
	//一个对象创建的3个新线程；
	//这三个线程共享这个对象的私有成员sleeptime
	private int sleepTime;
	public TestThread() {
		sleepTime=(int)(Math.random()*6000);
	}
	public void run() {
		try {
			System.out.println(
					Thread.currentThread().getName()+" going to sleep for "+
			sleepTime);
			Thread.sleep(sleepTime);
		}
		catch(InterruptedException exception) {};
		System.out.println(Thread.currentThread().getName()+" finished");
	}
}