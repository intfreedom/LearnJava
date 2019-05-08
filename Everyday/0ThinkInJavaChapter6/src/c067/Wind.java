package c067;
import java.util.*;

class Instrument{
	public void play() {
		System.out.println("Yes!!!");
	}
	static void tune(Instrument i) {
		i.play();
	}
}

class Wind extends Instrument {

	public static void main(String[] args) {
		Wind flute = new Wind();
		Instrument.tune(flute);
	}
}
