package xyz.liuwenjun.chapter10_2;
import java.util.concurrent.locks.ReentrantLock;

public class BufferInterruptibly {
	private ReentrantLock lock = new ReentrantLock();
	public void write() {
		lock.lock();
		try {
			long startTime = System.currentTimeMillis();
			System.out.println("start give buff data...");
			for(;;) {//模拟处理时间很长
				if(System.currentTimeMillis()-startTime>Integer.MAX_VALUE)
					break;
			}
			System.out.println("finally over");
		}finally {
			lock.unlock();
		}
	}
	public void read() throws InterruptedException{
		lock.lockInterruptibly();//注意这里，可以响应中断
		try {
			System.out.println("from this buff read data!");
		}finally {
			lock.unlock();
		}
	}

}
