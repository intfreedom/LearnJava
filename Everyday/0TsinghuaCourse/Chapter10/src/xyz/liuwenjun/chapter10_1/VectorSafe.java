package xyz.liuwenjun.chapter10_1;

import java.util.Vector;
//δ����ͬ������֮ǰ�����ܻᷢ�������±�Խ�磻
//�̼߳��ݣ����������̰߳�ȫ�ģ�
//���ǿ���ͨ���ڵ��ö���ȷ��ʹ��ͬ���ֶ�����֤�����ڲ��������п��԰�ȫʹ�ã�

public class VectorSafe {

	private static Vector<Integer> vector= new Vector<Integer>();
	public static void main(String[] args) {
		while(true) {
			for(int i=0;i<10;i++) {
				vector.add(i);
			}
			Thread removeThread = new Thread(new Runnable() {
				//ɾ������Ԫ�ص��߳�
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
