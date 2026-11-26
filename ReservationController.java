import java.util.List;

public class ReservationController {
    private DatabaseManager db;

    public ReservationController() {
        this.db = DatabaseManager.getInstance();
    }

    //public Reservation createReservation(Customer c, Flight f, int seats)

    public void cancelReservation(int reservationID) {
        Reservation reservation = db.getReservation(reservationID);
        reservation.cancel();
        db.updateReservation(reservation);
    }

    public void modifyReservation(int reservationID, int newSeats) {
        Reservation reservation = db.getReservation(reservationID);
        Flight flight = reservation.getFlight();

        if (reservation == null || newSeats <= 0) {
            return;
        }

        //have to set seat numbers 
        
        int seatDifference = newSeats - reservation.getNumSeats();

        if (seatDifference > 0) {
            if (flight.getAvailableSeats() < seatDifference) {
                return;
            }

            for (int i = 0; i < seatDifference; i++) {
                if (flight.reserveSeat() == false) {
                    return;
                }
            }
        } else if (seatDifference < 0) {
            for (int i = 0; i < Math.abs(seatDifference); i++) {
                flight.releaseSeat();
            }
        }
        //still need to do seatNumber list

        reservation.setNumSeats(newSeats);

        db.updateFlight(flight);
        db.updateReservation(reservation);
    }

    public List<Reservation> getCustomerReservations(int customerID) {
        return db.getReservationsByCustomer(customerID);
    }

    public String generateConfirmation(Reservation r) {
        return """
        =====
        RESERVATION CONFIRMATION
        =====

        Reservation confirmed for %d seats on flight %s
        Seat numbers: %s
        Reservation ID: %d

        """.formatted(r.getNumSeats(), r.getFlight().getFlightNumber(), r.getSeatNumbers(), r.getReservationID());
    }
    
}
