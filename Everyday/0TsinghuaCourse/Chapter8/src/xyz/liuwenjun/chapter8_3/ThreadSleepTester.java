package xyz.liuwenjun.chapter8_3;

public class ThreadSleepTester {
	
	public static void main(String[] args) {
		TestThread thread1=new TestThread("thread1");
		TestThread thread2=new TestThread("thread2");
		TestThread thread3=new TestThread("thread3");
		System.out.println("Starting threads");
		thread1.start();
		thread2.start();
		thread3.start();
		System.out.println("Threads started, main ends\n");
	}

}

class TestThread extends Thread{
	private int sleepTime;
	public TestThread(String name) {
		super(name);
		sleepTime=(int)(Math.random()*6000);
	}
	public void run() {
		try {
			System.out.println(
					getName()+"going to sleep for"+sleepTime);
			Thread.sleep(sleepTime);
		}
		catch(InterruptedException exception) {};
		System.out.println(getName()+"finished");
	}
}