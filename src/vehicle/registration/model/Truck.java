package vehicle.registration.model;

import java.util.Locale;

public class Truck extends Vehicle {

    private final double cargoCapacityTons;

    public Truck(String plateNumber, String ownerName, int registrationYear,
                 String status, double cargoCapacityTons) {
        super(plateNumber, ownerName, "Truck", registrationYear, status);
        this.cargoCapacityTons = cargoCapacityTons;
    }

    public double getCargoCapacityTons() { return cargoCapacityTons; }

    @Override
    public String getRegistrationLabel() {
        return String.format(Locale.US, "Commercial Truck — Cargo: %.1f tons", cargoCapacityTons);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(Locale.US, " | Cargo: %.1f tons", cargoCapacityTons);
    }
}
