package vehicle.registration;

import vehicle.registration.exception.DuplicatePlateException;
import vehicle.registration.exception.InvalidInputException;
import vehicle.registration.exception.VehicleNotFoundException;
import vehicle.registration.model.*;
import vehicle.registration.service.RegistrationService;
import vehicle.registration.util.InputValidator;

import java.time.Year;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Entry point: console menu (Task 6) + seed data (Task 10). */
public class Main {

    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        RegistrationService service = new RegistrationService();
        seedData(service);

        while (true) {
            printMenu();

            int choice;
            try {
                choice = read.nextInt();
                read.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                read.nextLine();
                continue;
            } catch (NoSuchElementException e) {
                break;
            }

            try {
                switch (choice) {
                    case 1: registerVehicle(read, service); break;
                    case 2: searchVehicle(read, service);   break;
                    case 3: updateOwner(read, service);     break;
                    case 4: deleteVehicle(read, service);   break;
                    case 5: printTable(service.getAllVehicles()); break;
                    case 6: filterByType(read, service);    break;
                    case 7: ownerHistory(read, service);    break;
                    case 8: printTable(service.getExpiredRegistrations(Year.now().getValue())); break;
                    case 9: service.printStatistics();    break;
                    case 0:
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number from 0 to 9.");
                }
            } catch (InvalidInputException e) {
                System.out.println("Input Error: " + e.getMessage());
            } catch (DuplicatePlateException | VehicleNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (NoSuchElementException e) {
                System.out.println("Input ended unexpectedly.");
                return;
            }
        }
    }

    // ------------------------------------------------------------------ menu

    private static void printMenu() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("  VEHICLE REGISTRATION SYSTEM v1.0");
        System.out.println("========================================");
        System.out.println("1. Register New Vehicle");
        System.out.println("2. Search Vehicle by Plate");
        System.out.println("3. Update Owner Name");
        System.out.println("4. Delete Vehicle");
        System.out.println("5. List All Vehicles");
        System.out.println("6. Filter by Vehicle Type");
        System.out.println("7. Show Owner History");
        System.out.println("8. Show Expired Registrations");
        System.out.println("9. Statistics Report");
        System.out.println("0. Exit");
        System.out.println("========================================");
        System.out.print("Enter your choice: ");
    }

    // --------------------------------------------------------------- Operations

    private static void registerVehicle(Scanner sc, RegistrationService service) {
        String plate = InputValidator.validatePlateNumber(ask(sc, "Plate number : "));
        String owner = InputValidator.validateOwnerName(ask(sc, "Owner name   : "));
        String type  = InputValidator.validateType(ask(sc, "Type (Car/Truck/Motorcycle): "));
        int year     = InputValidator.validateYear(ask(sc, "Registration year: "));

        Vehicle vehicle;
        switch (type) {
            case "Car":
                int doors = InputValidator.validateDoors(ask(sc, "Number of doors (Enter = 4): "));
                vehicle = new Car(plate, owner, year, "ACTIVE", doors);
                break;
            case "Truck":
                double cargo = InputValidator.validateCargo(ask(sc, "Cargo capacity in tons: "));
                vehicle = new Truck(plate, owner, year, "ACTIVE", cargo);
                break;
            default:
                String engine = InputValidator.validateEngine(
                        ask(sc, "Engine (Sport/Cruiser/Off-Road, Enter = Sport): "));
                vehicle = new Motorcycle(plate, owner, year, "ACTIVE", engine);
        }

        service.registerVehicle(vehicle);
        System.out.println("Vehicle registered successfully:");
        System.out.println("  " + vehicle);
    }

    private static void searchVehicle(Scanner sc, RegistrationService service) {
        String plate = ask(sc, "Plate number to search: ");
        Vehicle v = service.findByPlate(plate);
        System.out.println("  " + v);
        System.out.println("  " + v.getRegistrationLabel());
    }

    private static void updateOwner(Scanner sc, RegistrationService service) {
        String plate = ask(sc, "Plate number: ");
        service.findByPlate(plate);                  // fail early if the plate does not exist
        String newOwner = InputValidator.validateOwnerName(ask(sc, "New owner name: "));
        service.updateOwner(plate, newOwner);
        System.out.println("Owner updated: " + service.findByPlate(plate));
    }

    private static void deleteVehicle(Scanner sc, RegistrationService service) {
        String plate = ask(sc, "Plate number to delete: ");
        service.deleteVehicle(plate);
        System.out.println("Vehicle " + plate.trim().toUpperCase() + " deleted.");
    }

    private static void filterByType(Scanner sc, RegistrationService service) {
        String type = InputValidator.validateType(ask(sc, "Type (Car/Truck/Motorcycle): "));
        printTable(service.filterByType(type));
    }

    private static void ownerHistory(Scanner sc, RegistrationService service) {
        String name = ask(sc, "Owner name (full or partial): ");
        if (name.isEmpty()) {
            throw new InvalidInputException("Owner name must contain only letters");
        }
        printTable(service.getVehiclesByOwner(name));
    }

    // --------------------------------------------------------------- helpers

    private static String ask(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static void printTable(List<Vehicle> vehicles) {
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
            return;
        }
        String line = "-".repeat(88);
        System.out.println(line);
        System.out.printf("%-10s %-12s %-16s %-6s %-8s %s%n",
                "Plate", "Type", "Owner", "Year", "Status", "Details");
        System.out.println(line);
        for (Vehicle v : vehicles) {
            System.out.printf("%-10s %-12s %-16s %-6d %-8s %s%n",
                    v.getPlateNumber(), v.getVehicleType(), v.getOwnerName(),
                    v.getRegistrationYear(), v.getStatus(), v.getRegistrationLabel());
        }
        System.out.println(line);
        System.out.println("Total: " + vehicles.size());
    }

    private static void seedData(RegistrationService service) {
        service.registerVehicle(new Car("CAR-001", "Ahmed Ali", 2019, "EXPIRED", 4));
        service.registerVehicle(new Car("CAR-002", "Sara Kamel", 2023, "ACTIVE", 2));
        service.registerVehicle(new Truck("TRK-001", "Mohamed Said", 2021, "ACTIVE", 10.0));
        service.registerVehicle(new Truck("TRK-002", "Laila Nour", 2017, "EXPIRED", 5.5));
        service.registerVehicle(new Motorcycle("MOT-001", "Omar Fathi", 2022, "ACTIVE", "Sport"));
        service.registerVehicle(new Motorcycle("MOT-002", "Nadia Hamed", 2024, "ACTIVE", "Cruiser"));
    }
}
