//:WindErro.java

package chapter7_3;

class NoteX{
	public static final int
	MIDDLE_C = 0, C_SHARP = 1, C_FLAT = 2;
}

class InstrumentX{
	public void play(int NoteX) {
		System.out.println("InstrumentX.play()");
	}
}

class WindX extends InstrumentX{
	public void play(NoteX n) {
		System.out.println("WindX.play(NoteX n)");
	}
}

public class WindError {
	public static void tune(InstrumentX i) {
		System.out.println("InstrumentX��ֻ��play(int NoteX)������������");
		i.play(NoteX.MIDDLE_C);
	}
	public static void tune1(WindX j) {
		System.out.println("������������");
		NoteX s = new NoteX();
		j.play(s);
		System.out.println("InstrumentX��ֻ��play(int NoteX)������������");
		j.play(NoteX.MIDDLE_C);
	}
	public static void main(String[] args) {
		WindX flute = new WindX();
		tune(flute);
		tune1(flute);
	}
}
