# Vehicle Registration System (Java console app)

## Compile & run (needs JDK 11+)
```
cd VehicleRegistration
mkdir out
javac -encoding UTF-8 -d out $(find src -name "*.java")      # Linux / macOS
java  -cp out vehicle.registration.Main

# quick checks for Task 2 + Task 3
java -cp out vehicle.registration.SelfTest
```
Windows (PowerShell):
```
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out vehicle.registration.Main
```
Or just open the `src` folder in IntelliJ / Eclipse / VS Code and run `Main`.
