package vehicle.registration;

import vehicle.registration.model.*;

/** Quick checks for Task 2 (polymorphism) and Task 3 (equals/hashCode). */
public class SelfTest {
    public static void main(String[] args) {
        // Task 2
        Vehicle v1 = new Car("ABC-001", "Sara Ahmed", 2022, "ACTIVE", 4);
        Vehicle v2 = new Truck("TRK-099", "Ali Hassan", 2020, "ACTIVE", 8.5);
        Vehicle v3 = new Motorcycle("MOT-555", "Omar Said", 2023, "ACTIVE", "Sport");
        System.out.println(v1.getRegistrationLabel());
        System.out.println(v2.getRegistrationLabel());
        System.out.println(v3.getRegistrationLabel());
        System.out.println(v1);
        System.out.println(v2);
        System.out.println(v3);

        // Task 3
        Vehicle a = new Car("abc-001", "Ali", 2021, "ACTIVE", 4);
        Vehicle b = new Car("ABC-001", "Sara", 2022, "ACTIVE", 2);
        System.out.println(a.equals(b));                       // true
        System.out.println(a.hashCode() == b.hashCode());      // true
    }
}
