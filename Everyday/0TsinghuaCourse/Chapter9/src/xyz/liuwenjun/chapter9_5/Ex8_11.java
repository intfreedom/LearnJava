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
	boolean flag0=false;//0�����־������true�ѱ�����, false 0���򱻷��£�
	boolean flag1=false;
	boolean flag2=false;
}

class Player0 extends Thread{//0����Ϸ�ߵ���
	private Balls ball;
	public Player0(Balls b) {this.ball=b;}
	public void run() {
		while(true) {
			while(ball.flag1==true) {
				System.out.println("I'm Player0, I'm waiting flag1");
			};//���1�����ѱ����ߣ���ȴ���
			ball.flag1=true;//����1����
			while(ball.flag0==true) {
				System.out.println("I'm Player0, I'm waiting rigth side ball");
			};//���0�����ѱ����ߣ���ȴ���
			if(ball.flag1=true&&ball.flag0==false){
				ball.flag0=true;//����0����
				System.out.println("Player0 has got two balls!");
				ball.flag1=false;//����1����
				ball.flag0=false;//����0����
				try {sleep(1);}catch(Exception e) {};//���º���Ϣ1ms;
			}
		}
	}
}

class Player1 extends Thread{//1����Ϸ�ߵ���
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

class Player2 extends Thread{//2����Ϸ�ߣ�
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
//����պ���3���˶��õ������ֱߵ��򣬶��ȴ����ֱߵ�����˭�����ܷ��֣�
//����3���̶߳���������ֹ���ĵȴ����У���͹�����������
//ִ�н�������ɴκ��������������������Ϣ(��ΪI'm waiting...)�����κ��˶�������ͬʱӵ���������