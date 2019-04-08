package xyz.liuwenjun.chapter9_4;

public class Ex8_10 {
	
	public static void main(String[] args) {
		ThreadTest t=new ThreadTest();
		t.setDaemon(true);//启用后台线程；
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

//如果注释掉t.setDaemon(true)语句，则程序永远不会结束；