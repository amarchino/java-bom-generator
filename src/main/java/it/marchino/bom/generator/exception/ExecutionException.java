package it.marchino.bom.generator.exception;

public class ExecutionException extends RuntimeException {

	private static final long serialVersionUID = -8927522141956485316L;

	public ExecutionException() {
		super();
	}

	public ExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExecutionException(String message) {
		super(message);
	}

	public ExecutionException(Throwable cause) {
		super(cause);
	}

}
