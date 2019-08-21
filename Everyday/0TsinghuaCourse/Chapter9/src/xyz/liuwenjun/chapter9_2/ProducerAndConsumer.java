package xyz.liuwenjun.chapter9_2;

//synchronized--�߳�ͬ���ؼ��֣�ʵ�ֻ��⣻
//����ָ����Ҫͬ���Ĵ���λ򷽷���Ҳ���Ǽ�������
//��ʵ����һ�����Ľ��������磺
//Javaʹ�ü��������ƣ�
//ÿ������ֻ��һ���������� ���ö��̶߳ԡ�����������ʵ���̼߳�Ļ��⣻
//���߳�A�����һ������������߳�B����ȴ��߳�A��ɲ��ܻ���������

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
			synchronized(t) {//����
				System.out.println("Producer puts ticket "
						+(++t.number));
				t.available=true;
			}//�ͷŶ���t������
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
			synchronized(t) {//�������t����
				if(t.available==true&&i<=t.number)
					System.out.println("Consumer buys ticket"+(++i));
				if(i==t.number) {
					try {Thread.sleep(1);}catch(Exception e) {}
					t.available=false;
				}
			}//�ͷŶ���t����
		}
		System.out.println("Consumer ends");
	}
}
