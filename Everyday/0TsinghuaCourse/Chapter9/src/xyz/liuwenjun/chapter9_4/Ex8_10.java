package xyz.liuwenjun.chapter9_4;

public class Ex8_10 {
	
	public static void main(String[] args) {
		ThreadTest t=new ThreadTest();
		t.setDaemon(true);//���ú�̨�̣߳�
		t.start();
	}
}

class ThreadTest extends Thread{
	public void run() {
		while(true) {
			System.out.println("do it, do it ,just");
		}
		}
}

//���ע�͵�t.setDaemon(true)��䣬�������Զ���������