package vehicle.registration.exception;

/** Thrown when we try to register a plate number that already exists. */
public class DuplicatePlateException extends RuntimeException {
    public DuplicatePlateException(String plateNumber) {
        super("Plate number already registered: " + plateNumber);
    }
}
