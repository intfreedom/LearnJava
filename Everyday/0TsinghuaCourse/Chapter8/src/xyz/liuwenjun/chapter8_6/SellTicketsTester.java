package xyz.liuwenjun.chapter8_6;
//�������߳�ģ��3����Ʊ�ڵ���Ʊ��Ϊ��
//���3���̣߳�Ӧ�ù���200��Ʊ��ÿ���̵߳��õ���ͬһ��SellTickets�����е�
//run()���������ʵ���ͬһ�������еı���(tickets);
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

//chapter8 �̼߳�����ݹ���