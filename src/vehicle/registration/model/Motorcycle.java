package vehicle.registration.model;

public class Motorcycle extends Vehicle {

    private final String engineType;

    public Motorcycle(String plateNumber, String ownerName, int registrationYear,
                      String status, String engineType) {
        super(plateNumber, ownerName, "Motorcycle", registrationYear, status);
        this.engineType = engineType;
    }

    public String getEngineType() { return engineType; }

    @Override
    public String getRegistrationLabel() {
        return "Motorcycle — Engine: " + engineType;
    }

    @Override
    public String toString() {
        return super.toString() + " | Engine: " + engineType;
    }
}
