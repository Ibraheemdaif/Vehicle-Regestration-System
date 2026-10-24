package vehicle.registration.util;

import vehicle.registration.exception.InvalidInputException;

import java.time.Year;

/**
 * Validation at the boundary: every method checks one rule, throws
 * InvalidInputException on failure and returns the clean value on success.
 */
public final class InputValidator {

    private InputValidator() { }

    public static String validatePlateNumber(String plate) {
        String p = plate == null ? "" : plate.trim().toUpperCase();
        if (p.isEmpty() || !p.matches("[A-Z0-9-]{3,10}")) {
            throw new InvalidInputException("Plate number must be 3-10 alphanumeric characters");
        }
        return p;
    }

    public static String validateOwnerName(String name) {
        String n = name == null ? "" : name.trim();

        if (n.length() < 3 || !n.matches("\\p{L}+(\\s\\p{L}+)*")) {
            throw new InvalidInputException("Owner name must contain only letters");
        }
        return n;
    }

    public static String validateType(String type) {
        String t = type == null ? "" : type.trim();
        for (String valid : new String[]{"Car", "Truck", "Motorcycle"}) {
            if (valid.equalsIgnoreCase(t)) return valid;
        }
        throw new InvalidInputException("Invalid type. Use: Car, Truck, Motorcycle");
    }

    public static int validateYear(String text) {
        int currentYear = Year.now().getValue();
        int year;
        try {
            year = Integer.parseInt(text == null ? "" : text.trim());
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Year must be between 1990 and " + currentYear);
        }
        if (year < 1990 || year > currentYear) {
            throw new InvalidInputException("Year must be between 1990 and " + currentYear);
        }
        return year;
    }


    public static int validateDoors(String text) {
        if (text == null || text.trim().isEmpty()) return 4; // Default value = 4 (if user presses Enter)
        try {
            int doors = Integer.parseInt(text.trim());
            if (doors >= 2 && doors <= 6) return doors;
        } catch (NumberFormatException ignored) { }
        throw new InvalidInputException("Number of doors must be between 2 and 6");
    }

    public static double validateCargo(String text) {
        try {
            double cargo = Double.parseDouble(text == null ? "" : text.trim());
            if (cargo > 0) return cargo;
        } catch (NumberFormatException ignored) { }
        throw new InvalidInputException("Cargo capacity must be a positive number");
    }

    public static String validateEngine(String text) {
        if (text == null || text.trim().isEmpty()) return "Sport"; // Default value = "Sport" (if user presses Enter)
        for (String valid : new String[]{"Sport", "Cruiser", "Off-Road"}) {
            if (valid.equalsIgnoreCase(text.trim())) return valid;
        }
        throw new InvalidInputException("Engine type must be: Sport, Cruiser, Off-Road");
    }
}
