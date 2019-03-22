//:Wind.java

import java.util.*;

class Instrument{
	public void play() {}
	static void tune(Instrument i) {
		i.play();
	}
}


class Wind extends Instrument{
	public static void main(String[] args) {
		Wind flute = new Wind();
		Instrument.tune(flute);
	}
}
