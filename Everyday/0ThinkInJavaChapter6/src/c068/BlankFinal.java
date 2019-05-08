package c068;
class Poppet{
	Poppet(int i){
		System.out.println(i);
		}
}

public class BlankFinal {

	final int i=0;
	final int j;
	final Poppet p;
	BlankFinal(){
		j=1;
		p=new Poppet(1);
	}
	BlankFinal(int x){
		j = x;
		p = new Poppet(2);
	}
	
	public static void main(String[] args) {
		BlankFinal bf = new BlankFinal();
		BlankFinal bf1 = new BlankFinal(3);
	}
}
