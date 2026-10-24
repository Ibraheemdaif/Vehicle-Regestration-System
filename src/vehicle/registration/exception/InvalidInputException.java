package vehicle.registration.exception;

/** Thrown by InputValidator when user input breaks a validation rule. */
public class InvalidInputException extends IllegalArgumentException {
    public InvalidInputException(String message) {
        super(message);
    }
}
