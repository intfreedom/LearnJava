package xyz.liuwenjun.chapter12_4;

public class ChatClientFrame {
	public static void main(String[] args) {
		ChatFrame cclient=new ChatFrame("Dog", "Cat", "127.0.0.1", 2009, false);
	}

}
//各自用一个Java虚拟机启动，一个客户端，一个Server端；
//先启动Server端，再启动Client端；