package utility.exception;

public class UnknownPokemonException extends RuntimeException {
	private static final long serialVersionUID = -619525725941006101L;

	public UnknownPokemonException(String message) {
		super(message);
	}
}
