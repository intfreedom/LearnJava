package xyz.liuwenjun.chapter9_2;

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
		while((t.number)<t.size) {
			synchronized(t) {
				System.out.println("Producer puts ticket"
						+(++t.number));
				t.available=true;
			}
		}
		System.out.println("Producer ends!");
	}
	
}

class Consumer extends Thread{
	Tickets t=null;
	int i=0;
	public Consumer(Tickets t) {this.t=t;}
	public void run() {
		while(i<t.size) {
			synchronized(t) {//申请对象t的锁；
				if(t.available==true&&i<=t.number)
					System.out.println("Consumer buys ticket "+(++i));
				if(i==t.number) {
					try {Thread.sleep(1);}catch(Exception e) {}
					t.available=false;
				}
			}//释放对象t的锁；
		}
		System.out.println("Consumer ends");
	}
}

class Tickets{
	int number=0;
	int size;
	boolean available=false;
	public Tickets(int size)
	{
		this.size=size;
	}
}