package xyz.liuwenjun.chapter10_1;

import java.util.Vector;
//未加上同步机制之前，可能会发生数组下标越界；
//线程兼容：对象本身不是线程安全的，
//但是可以通过在调用端正确地使用同步手段来保证对象在并发环境中可以安全使用；

public class VectorSafe {

	private static Vector<Integer> vector= new Vector<Integer>();
	public static void main(String[] args) {
		while(true) {
			for(int i=0;i<10;i++) {
				vector.add(i);
			}
			Thread removeThread = new Thread(new Runnable() {
				//删除向量元素的线程
				public void run() {
					synchronized(vector) {
						for(int i=0;i<vector.size();i++) {
							vector.remove(i);
						}
					}
				}
			});
			
			Thread printThread = new Thread(new Runnable() {
				public void run() {
					synchronized(vector) {
						for(int i=0;i<vector.size();i++) {
							System.out.println((vector.get(i)));
						}
					}
				}
			});
			
			removeThread.start();
			printThread.start();
			while(Thread.activeCount()>20);
		}
	}
}
