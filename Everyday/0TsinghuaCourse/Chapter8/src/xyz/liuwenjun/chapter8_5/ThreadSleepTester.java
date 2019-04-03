package xyz.liuwenjun.chapter8_5;

public class ThreadSleepTester {
	public static void main(String[] args) {
		TestThread thread1=new TestThread();
		TestThread thread2=new TestThread();
		TestThread thread3=new TestThread();
		System.out.println("Starting threads");
		
		new Thread(thread1, "Thread1").start();
		new Thread(thread2, "Thread2").start();
		new Thread(thread3, "Thread3").start();
		
		System.out.println("Threads started, main ends\n");
	}
}
//由于Java的单继承；
//使用Runable方法与chapter8-3的区别，可以继承自其他类；
class TestThread implements Runnable{
	private int sleepTime;
	public TestThread() {
		sleepTime=(int)(Math.random()*6000);
	}
	public void run() {
		try {
			System.out.println(
					Thread.currentThread().getName()+
					"going to sleep for "+sleepTime);
			Thread.sleep(sleepTime);
		}
		catch(InterruptedException exception) {};
		System.out.println(Thread.currentThread().getName()+"finished");
	}
}