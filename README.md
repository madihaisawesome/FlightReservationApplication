# Flight Reservation Application

ENSF 480 Term Project — Group 14

A Java Swing desktop app for browsing flights, making reservations, simulating payments, and managing flight data. Data is stored locally with SQLite.

## Team
- Youssef Ibrahim
- Jeeya Somaiya
- Madiha Khan
- Kaitlyn Dhamo

## Roles

The app is intended for three user types:

| Role | Purpose |
|---|---|
| **Customer** | Search flights, book, pay, manage own reservations, receive promotions |
| **Flight Agent** | Help customers: search flights, manage profiles and reservations |
| **System Administrator** | Maintain flights (add, update, remove) and related system data |

## Requirements
- Java 11 or newer
- SQLite JDBC driver in `lib/sqlite-jdbc-3.46.1.3.jar`

## Build & run

From the project root:

**Windows**
```bash
javac -cp "lib/sqlite-jdbc-3.46.1.3.jar" *.java
java -cp ".;lib/sqlite-jdbc-3.46.1.3.jar" SearchFlightGUI
java -cp ".;lib/sqlite-jdbc-3.46.1.3.jar" AdminGUI
```

**macOS / Linux**
```bash
javac -cp "lib/sqlite-jdbc-3.46.1.3.jar" *.java
java -cp ".:lib/sqlite-jdbc-3.46.1.3.jar" SearchFlightGUI
java -cp ".:lib/sqlite-jdbc-3.46.1.3.jar" AdminGUI
```

> Note: A single `MainGUI` login/role menu is planned. Until then, launch feature screens directly (e.g. `SearchFlightGUI`, `AdminGUI`).

On first run, SQLite creates `flight_reservation.db` and seeds sample flights.

## Architecture

Layered design matching the class / package diagrams:

- **Presentation** — Swing GUIs (`SearchFlightGUI`, `AdminGUI`, `BookingGUI`, `PaymentGUI`, …)
- **Business logic** — Controllers and entities (`FlightController`, `ReservationController`, `PaymentController`, …)
- **Data** — `DatabaseManager` (Singleton) + SQLite

### Design patterns
- **Singleton** — `DatabaseManager`
- **Strategy** — `PaymentStrategy` (`CreditCardPayment`, `DebitCardPayment`)
- **Factory / Observer** — planned for users and promotions (see design document)

## Project status

| Feature | Status |
|---|---|
| Search / view flights | Implemented |
| Flight management (admin) | Implemented |
| Booking management | In progress |
| Customer management | In progress |
| Payment simulation | In progress |
| Monthly promotion news | In progress |
| Login / main menu | Not started |

## Design document
See `ENSF_480_Term_Project_Group_14_Design_Document` for use cases, diagrams, and class details.
