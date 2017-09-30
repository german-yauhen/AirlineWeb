package by.htp.hermanovich.airline.entities;

/**
 * Description: This class describes all applications tickets.
 *
 * Created by Yauheni Hermanovich on 20.07.2017.
 */
public class Ticket extends BaseEntity {
    private int id;
    private String ticketNumber;
    private User user;
    private Flight flight;
    private Luggage luggage;
    private float totalPrice;

    public Ticket() {
    }

    public Ticket(int id, String ticketNumber, User user, Flight flight, Luggage luggage, float totalPrice) {
        this.id = id;
        this.ticketNumber = ticketNumber;
        this.user = user;
        this.flight = flight;
        this.luggage = luggage;
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Luggage getLuggage() {
        return luggage;
    }

    public void setLuggage(Luggage luggage) {
        this.luggage = luggage;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        if (id != ticket.id) return false;
        if (Float.compare(ticket.totalPrice, totalPrice) != 0) return false;
        if (ticketNumber != null ? !ticketNumber.equals(ticket.ticketNumber) : ticket.ticketNumber != null)
            return false;
        if (user != null ? !user.equals(ticket.user) : ticket.user != null) return false;
        if (flight != null ? !flight.equals(ticket.flight) : ticket.flight != null) return false;
        return luggage != null ? luggage.equals(ticket.luggage) : ticket.luggage == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (ticketNumber != null ? ticketNumber.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (flight != null ? flight.hashCode() : 0);
        result = 31 * result + (luggage != null ? luggage.hashCode() : 0);
        result = 31 * result + (totalPrice != +0.0f ? Float.floatToIntBits(totalPrice) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketNumber='" + ticketNumber + '\'' +
                ", user=" + user +
                ", flight=" + flight +
                ", luggage=" + luggage +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
