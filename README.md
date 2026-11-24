# Flight Reservation Application

Java desktop app (Swing + SQLite). Design: ENSF 480 Group 14 class / package diagrams.

## Madiha — Search/View Flight & Flight Management

Aligned with the class diagram:

| Design class | Role |
|---|---|
| `SearchFlightGUI` | `<<boundary>>` search/view |
| `AdminGUI` | `<<boundary>>` add / update / remove flights |
| `FlightController` | `<<control>>` |
| `Flight` | `<<entity>>` (`capacity`, `bookedSeats`, `status`, seat methods) |
| `DatabaseManager` | `<<singleton>>` flight CRUD |

### Run

Java 11+ and `lib/sqlite-jdbc-3.46.1.3.jar`.

```bash
javac -cp "lib/sqlite-jdbc-3.46.1.3.jar" Flight.java Promotion.java DatabaseManager.java FlightController.java SearchFlightGUI.java AdminGUI.java

java -cp ".;lib/sqlite-jdbc-3.46.1.3.jar" SearchFlightGUI
java -cp ".;lib/sqlite-jdbc-3.46.1.3.jar" AdminGUI
```

On macOS/Linux use `:` instead of `;` in the classpath.

### Design patterns in this slice

1. **Singleton** — `DatabaseManager`
2. **MVC / layered** — GUI → Controller → DatabaseManager
3. (Team) **Strategy** — `PaymentStrategy`
