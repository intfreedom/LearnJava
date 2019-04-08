package xyz.liuwenjun.chapter9_3;

public class Test {
	
	public static void main(String[] args) {
		Tickets t=new Tickets(10);
		new Producer(t).start();
		new Consumer(t).start();		
	}
}

class Producer extends Thread{
	Tickets t=null;
	public Producer(Tickets t) {this.t=t;}
	public void run() {
		while(t.number<t.size)
			t.put();
	}	
}

class Consumer extends Thread{
	Tickets t=null;
	public Consumer(Tickets t) {this.t=t;}
	public void run() {
		while(t.i<t.size)
			t.sell();
	}
}
//当Consumer线程售出票后， available值变为false
class Tickets{
	int number=0;
	int size;
	int i=0;
	boolean available=false;
	public Tickets(int size)
	{
		this.size=size;
	}
	
	public synchronized void put() {
		if(available)//如果还有存票待售，则存票线程等待；
			try {wait();}catch(Exception e) {}
		System.out.println("Producer puts ticket"+(++number));
		available=true;
		notify();//存票后唤醒售票线程开始售票；
	}
	public synchronized void sell() {
		if(!available)//如果没有存票，则售票线程等待
			try {wait();}catch(Exception e) {}
		System.out.println("Consumer buys ticket "+(number));
		available=false;
		notify();//售票后唤醒存票线程开始存票
		if(number==size)
			number=size+1;
		//在售完最后一张票后
		//设置一个结束标志，number>size表示售票结束
	}
}