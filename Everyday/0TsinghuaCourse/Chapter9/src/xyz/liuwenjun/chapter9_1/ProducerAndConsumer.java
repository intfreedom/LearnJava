package xyz.liuwenjun.chapter9_1;

public class ProducerAndConsumer {
	
	public static void main(String[] args) {
		Tickets t = new Tickets(10);
//		new Consumer(t).start();//��ʼ��Ʊ�̣߳�
//		new Producer(t).start();//��ʼ��Ʊ�̣߳�
//		������˳������Ļ������кܴ�仯��
		new Producer(t).start();//��ʼ��Ʊ�̣߳�
		new Consumer(t).start();//��ʼ��Ʊ�̣߳�
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
			System.out.println("Producer puts ticket "+(++t.number));
			t.available=true;
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
			if(t.available==true&&i<=t.number)
				System.out.println("Consumer buys ticket"+(++i));
			if(i==t.number)
				t.available=false;
		}
	}
	
	
}