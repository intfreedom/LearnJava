package xyz.liuwenjun.chapter10_2;
import java.util.concurrent.locks.ReentrantLock;

public class BufferInterruptibly {
	private ReentrantLock lock = new ReentrantLock();
	public void write() {
		lock.lock();
		try {
			long startTime = System.currentTimeMillis();
			System.out.println("start give buff data...");
			for(;;) {//ģ�⴦��ʱ��ܳ�
				if(System.currentTimeMillis()-startTime>Integer.MAX_VALUE)
					break;
			}
			System.out.println("finally over");
		}finally {
			lock.unlock();
		}
	}
	public void read() throws InterruptedException{
		lock.lockInterruptibly();//ע�����������Ӧ�ж�
		try {
			System.out.println("from this buff read data!");
		}finally {
			lock.unlock();
		}
	}

}
