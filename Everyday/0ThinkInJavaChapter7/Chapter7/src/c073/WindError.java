package c073;

class NoteX{
	
	public static final int
	MIDDLE_C = 0,C_SHARP = 1,C_FLAT = 2;
	private NoteX() {System.out.println("YesYesYes");};
	public static final NoteX m = new NoteX();
	
}

class InstrumentX{
	public void play(int NoteX) {
		System.out.println("InstrumentX.play()");
	}
	public void play(NoteX n) {
		System.out.println("Just test i.play");
	}
}

class WindX extends InstrumentX{
	public void play(NoteX n) {
		System.out.println("WindX.play(NoteX n)");
	}
}

public class WindError {

	public static void tune(InstrumentX i) {
		i.play(NoteX.MIDDLE_C);
		System.out.println("Look here!!!");
		i.play(NoteX.m);
//		i.play(NoteX n);
	}
	public static void main(String[] args) {
		WindX flute = new WindX();
		tune(flute);
	}
}
