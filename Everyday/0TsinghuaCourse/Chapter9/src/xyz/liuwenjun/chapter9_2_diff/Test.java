package xyz.liuwenjun.chapter9_2_diff;

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
		System.out.println("Producer puts ticket"+(++number));
		available=true;
	}
	public synchronized void sell() {
		if(available==true&&i<=number)
			System.out.println("Consumer buys ticket"+(++i));
		if(i==number)
			available=false;
	}
}