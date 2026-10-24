package vehicle.registration.service;

import vehicle.registration.exception.DuplicatePlateException;
import vehicle.registration.exception.VehicleNotFoundException;
import vehicle.registration.model.Vehicle;

import java.util.*;
import java.util.stream.Collectors;

/** All business logic: storage (Task 5), stream queries (Task 7), statistics (Task 8). */
public class RegistrationService {

    private final List<Vehicle> vehicleList = new ArrayList<>();
    private final Map<String, Vehicle> plateIndex = new HashMap<>();
    private final Set<String> registeredPlates = new HashSet<>();

    private String normalize(String plate) {
        return plate == null ? "" : plate.trim().toUpperCase();
    }

    // ===================== CRUD with collections =====================

    public void registerVehicle(Vehicle v) {
        String key = normalize(v.getPlateNumber());
        if (registeredPlates.contains(key)) {
            throw new DuplicatePlateException(key);
        }
        vehicleList.add(v);
        plateIndex.put(key, v);
        registeredPlates.add(key);
    }

    public Vehicle findByPlate(String plate) {
        String key = normalize(plate);
        Vehicle v = plateIndex.get(key);
        if (v == null) {
            throw new VehicleNotFoundException(key);
        }
        return v;
    }

    public void deleteVehicle(String plate) {
        Vehicle v = findByPlate(plate);
        String key = normalize(plate);
        vehicleList.remove(v);
        plateIndex.remove(key);
        registeredPlates.remove(key);
    }

    public void updateOwner(String plate, String newOwner) {
        findByPlate(plate).setOwnerName(newOwner);
    }

    public void updateStatus(String plate, String newStatus) {
        findByPlate(plate).setStatus(newStatus);
    }

    public List<Vehicle> getAllVehicles() {
        return Collections.unmodifiableList(vehicleList);
    }

    // ===================== streams (no loops) =====================

    public List<Vehicle> filterByType(String type) {
        return vehicleList.stream()
                .filter(v -> v.getVehicleType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public List<Vehicle> getVehiclesByOwner(String ownerName) {
        return vehicleList.stream()
                .filter(v -> v.getOwnerName().toLowerCase().contains(ownerName.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Vehicle> getExpiredRegistrations(int currentYear) {
        return vehicleList.stream()
                .filter(v -> (currentYear - v.getRegistrationYear()) > 5)
                .sorted(Comparator.comparingInt(Vehicle::getRegistrationYear))
                .collect(Collectors.toList());
    }

    public List<Vehicle> getSortedByYear(boolean ascending) {
        Comparator<Vehicle> comp = Comparator.comparingInt(Vehicle::getRegistrationYear);
        if (!ascending) comp = comp.reversed();
        return vehicleList.stream().sorted(comp).collect(Collectors.toList());
    }

    // ===================== statistics =====================

    public void printStatistics() {
        if (vehicleList.isEmpty()) {
            System.out.println("No vehicles registered yet - nothing to report.");
            return;
        }

        IntSummaryStatistics stats = vehicleList.stream()
                .mapToInt(Vehicle::getRegistrationYear)
                .summaryStatistics();

        Map<String, Long> byType = vehicleList.stream()
                .collect(Collectors.groupingBy(Vehicle::getVehicleType, Collectors.counting()));

        Map<Boolean, Long> byStatus = vehicleList.stream()
                .collect(Collectors.partitioningBy(
                        v -> v.getStatus().equalsIgnoreCase("ACTIVE"),
                        Collectors.counting()));

        System.out.println("========== REGISTRATION STATISTICS ==========");
        System.out.printf("Total Vehicles : %d%n", stats.getCount());
        System.out.printf("Average Year   : %d%n", Math.round(stats.getAverage()));
        System.out.printf("Newest Vehicle : %d%n", stats.getMax());
        System.out.printf("Oldest Vehicle : %d%n", stats.getMin());
        System.out.println("----------------------------------------------");
        System.out.println("Vehicles by Type:");
        for (String type : List.of("Car", "Truck", "Motorcycle")) {
            System.out.printf("  %-11s : %d%n", type, byType.getOrDefault(type, 0L));
        }
        System.out.println("----------------------------------------------");
        System.out.printf("ACTIVE vehicles : %d%n", byStatus.get(true));
        System.out.printf("EXPIRED vehicles: %d%n", byStatus.get(false));
        System.out.println("==============================================");
    }
}
