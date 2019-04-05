package xyz.liuwenjun.chapter8_6;
//��ͬһ��ʵ����Runnable�ӿڹ������ݣ�
public class ShareTargetTester {
	
	public static void main(String[] args) {
		//ֻ��һ��Runnable���͵Ķ���Ϊ��������3�����̣߳�
		TestThread threadobj=new TestThread();
		System.out.println("Starting threads");
		
		new Thread(threadobj, "Thread1").start();
		new Thread(threadobj, "Thread2").start();
		new Thread(threadobj, "Thread3").start();
		
		System.out.println("Threads started, main ends\n");
	}

}

class TestThread implements Runnable{
	//һ�����󴴽���3�����̣߳�
	//�������̹߳�����������˽�г�Աsleeptime
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