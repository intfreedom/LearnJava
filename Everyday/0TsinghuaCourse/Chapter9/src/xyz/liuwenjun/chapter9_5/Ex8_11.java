package xyz.liuwenjun.chapter9_5;

public class Ex8_11 {

	public static void main(String[] args) {
		Balls ball = new Balls();
		Player0 p0 = new Player0(ball);
		Player1 p1 = new Player1(ball);
		Player2 p2 = new Player2(ball);
		p0.start();
		p1.start();
		p2.start();
		
	}
}

class Balls{
	boolean flag0=false;//0号球标志变量，true已被拿走, false 0号球被放下；
	boolean flag1=false;
	boolean flag2=false;
}

class Player0 extends Thread{//0号游戏者的类
	private Balls ball;
	public Player0(Balls b) {this.ball=b;}
	public void run() {
		while(true) {
			while(ball.flag1==true) {
				System.out.println("I'm Player0, I'm waiting flag1");
			};//如果1号球已被拿走，则等待；
			ball.flag1=true;//拿起1号球；
			while(ball.flag0==true) {
				System.out.println("I'm Player0, I'm waiting rigth side ball");
			};//如果0号球已被拿走，则等待；
			if(ball.flag1=true&&ball.flag0==false){
				ball.flag0=true;//拿起0号球；
				System.out.println("Player0 has got two balls!");
				ball.flag1=false;//放下1号球；
				ball.flag0=false;//放下0号球；
				try {sleep(1);}catch(Exception e) {};//放下后休息1ms;
			}
		}
	}
}

class Player1 extends Thread{//1号游戏者的类
	private Balls ball;
	public Player1(Balls b) {this.ball=b;}
	public void run() {
		while(true) {
			while(ball.flag0==true) {
				System.out.println("I'm Player1, I'm waiting flag0");
			};
			ball.flag0=true;
			while(ball.flag2==true) {
				System.out.println("I'm Player1, I'm waiting rigth side ball");
			};
			if(ball.flag0=true&&ball.flag2==false){
				ball.flag2=true;
				System.out.println("Player1 has got two balls!");
				ball.flag0=false;
				ball.flag2=false;
				try {sleep(1);}catch(Exception e) {};
			}
		}
	}
}

class Player2 extends Thread{//2号游戏者；
	private Balls ball;
	public Player2(Balls b) {this.ball=b;}
	public void run() {
		while(true) {
			while(ball.flag2==true) {
				System.out.println("I'm Player2, I'm waiting flag2");
			};
			ball.flag2=true;
			while(ball.flag1==true) {
				System.out.println("I'm Player2, I'm waiting rigth side ball");
			};
			if(ball.flag2=true&&ball.flag1==false){
				ball.flag1=true;
				System.out.println("Player2 has got two balls!");
				ball.flag1=false;
				ball.flag2=false;
				try {sleep(1);}catch(Exception e) {};
			}
		}
	}
}
//如果刚好有3个人都拿到了左手边的球，都等待右手边的球，则谁都不能放手，
//则这3个线程都将陷入无止尽的等待当中，这就构成了死锁；
//执行结果，若干次后将陷入死锁，不再输出信息(改为I'm waiting...)，即任何人都不能再同时拥有两侧的球；