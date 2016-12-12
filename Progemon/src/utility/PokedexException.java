package utility;

public class PokedexException extends RuntimeException {

	public PokedexException() {
		super();
	}

	public PokedexException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public PokedexException(String arg0) {
		super(arg0);
	}

	public PokedexException(Throwable arg0) {
		super(arg0);
	}

}
