package com.aws.codestar.projecttemplates.exception;

/**
 * An exception thrown when incompatible values where found in a column.
 */
public class IncompatibleColumnValuesException extends Exception {

	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 6534022568654752970L;

	/**
	 * Default Constructor
	 */
	public IncompatibleColumnValuesException() {
		super();
	}

	/**
	 * Constructor method with error message
	 *
	 * @param message is the error message.
	 */
	public IncompatibleColumnValuesException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified cause and a detail message of
	 * what caused it.
	 *
	 * @param cause is the cause of the error.
	 */
	public IncompatibleColumnValuesException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 *
	 * @param message is the detailed error message.
	 * @param cause   is the detailed cause of the failure.
	 */
	public IncompatibleColumnValuesException(String message, Throwable cause) {
		super(message, cause);
	}
}
