package xyz.liuwenjun.chapter8_6;
//用三个线程模仿3个售票口的售票行为；
//这个3个线程，应该共享200张票；每个线程调用的是同一个SellTickets对象中的
//run()方法，访问的是同一个对象中的变量(tickets);
public class SellTicketsTester {

	public static void main(String[] args) {
		SellTickets t = new SellTickets();
		new Thread(t).start();
		new Thread(t).start();
		new Thread(t).start();
	}
}

class SellTickets implements Runnable
{
	private int tickets=200;
	public void run() {
		while(tickets>0) {
			System.out.println(Thread.currentThread().getName()+
					" is selling ticket "+tickets--);
		}
	}
}

//chapter8 线程间的数据共享；