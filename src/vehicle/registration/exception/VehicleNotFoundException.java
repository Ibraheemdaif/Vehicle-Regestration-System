package vehicle.registration.exception;

/** Thrown when no vehicle exists with the requested plate number. */
public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(String plateNumber) {
        super("No vehicle found with plate: " + plateNumber);
    }
}
