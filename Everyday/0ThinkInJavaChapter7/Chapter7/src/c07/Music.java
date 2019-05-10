package c07;

class Note{
	private int value;
	private Note(int val) {
		value = val;
		System.out.println(value);
	}
	public static final Note 
		middleC = new Note(0),
		cSharp = new Note(1),
		cFlat = new Note(2);
//	public static final Note cFlat = new Note(2);
}

class Instrument{
	public void play(Note n) {
		System.out.println("Instrument.play()");
	}
}

class Wind extends Instrument{
	public void play(Note n) {
		System.out.println("Wind.play()");
	}
	//如下就会产生过载；
//	public void play(int Note) {
//		System.out.println("Wind.play()");
//	}//结果：Instrument.play()
}
 

public class Music {

	public static void tune(Instrument i) {
		System.out.println("whowhowho");
		i.play(Note.middleC);
		System.out.println("***********");
	}
	public static void main(String[] args) {
		System.out.println("111111111111");
		Wind flute = new Wind();
		System.out.println("222222222222");
		tune(flute);
		System.out.println("Just this can too");
		flute.play(Note.middleC);
		System.out.println("3333333333333");
	}
}
