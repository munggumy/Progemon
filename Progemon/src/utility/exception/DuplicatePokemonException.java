package utility.exception;

public class DuplicatePokemonException extends RuntimeException {

	private static final long serialVersionUID = 4228528572114717980L;

	public DuplicatePokemonException(String message) {
		super(message);
	}

}
