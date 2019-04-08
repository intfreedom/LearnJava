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
//��Consumer�߳��۳�Ʊ�� availableֵ��Ϊfalse
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
		if(available)//������д�Ʊ���ۣ����Ʊ�̵߳ȴ���
			try {wait();}catch(Exception e) {}
		System.out.println("Producer puts ticket"+(++number));
		available=true;
		notify();//��Ʊ������Ʊ�߳̿�ʼ��Ʊ��
	}
	public synchronized void sell() {
		if(!available)//���û�д�Ʊ������Ʊ�̵߳ȴ�
			try {wait();}catch(Exception e) {}
		System.out.println("Consumer buys ticket "+(number));
		available=false;
		notify();//��Ʊ���Ѵ�Ʊ�߳̿�ʼ��Ʊ
		if(number==size)
			number=size+1;
		//���������һ��Ʊ��
		//����һ��������־��number>size��ʾ��Ʊ����
	}
}