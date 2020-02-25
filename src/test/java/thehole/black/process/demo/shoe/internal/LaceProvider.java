package thehole.black.process.demo.shoe.internal;


import thehole.black.process.demo.test.support.ArrayRotator;

public class LaceProvider {

	public static final String RED = "red";
	public static final String GREEN = "green";
	public static final String YELLOW = "yellow";

	private ArrayRotator<Laces> LACES = new ArrayRotator<>(new Laces[] { new Laces(RED, 11L), new Laces(GREEN, 12L), new Laces(YELLOW, 13L) });

	private LaceProvider() {
	}

	private static LaceProvider INSTANCE;

	public static synchronized LaceProvider getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new LaceProvider();
		}
		return INSTANCE;
	}

	public Laces getFreshLaces() {
		return LACES.getNextPhading();
	}

}
