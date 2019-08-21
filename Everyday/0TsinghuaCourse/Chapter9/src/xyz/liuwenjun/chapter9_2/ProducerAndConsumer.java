package xyz.liuwenjun.chapter9_2;

//synchronized--线程同步关键字，实现互斥；
//用于指定需要同步的代码段或方法，也就是监视区；
//可实现与一个锁的交互；例如：
//Java使用监视器机制；
//每个对象只有一个“锁”， 利用多线程对“锁”的争夺实现线程间的互斥；
//当线程A获得了一个对象的锁后，线程B必须等待线程A完成才能获得这个锁；

public class ProducerAndConsumer {
	
	public static void main(String[] args) {
		Tickets t = new Tickets(10);
//		new Consumer(t).start();//开始卖票线程；
//		new Producer(t).start();//开始存票线程；
//		这两行顺序调整的话，会有很大变化；
		new Producer(t).start();//开始存票线程；
		new Consumer(t).start();//开始卖票线程；
	}

}

class Tickets{
	int number=0;
	int size;
	boolean available=false;
	public Tickets(int size) {
		this.size=size;
	}
	
}

class Producer extends Thread{
	Tickets t=null;
	public Producer(Tickets t) {
		this.t=t;
	}
	public void run() {
		while(t.number<t.size) {
			synchronized(t) {//申请
				System.out.println("Producer puts ticket "
						+(++t.number));
				t.available=true;
			}//释放对象t的锁；
		}
	}
}

class Consumer extends Thread{
	Tickets t=null;
	int i=0;
	public Consumer(Tickets t) {
		this.t=t;
	}
	
	public void run() {
		while(i<t.size) {
			synchronized(t) {//申请对象t的锁
				if(t.available==true&&i<=t.number)
					System.out.println("Consumer buys ticket"+(++i));
				if(i==t.number) {
					try {Thread.sleep(1);}catch(Exception e) {}
					t.available=false;
				}
			}//释放对象t的锁
		}
		System.out.println("Consumer ends");
	}
}
