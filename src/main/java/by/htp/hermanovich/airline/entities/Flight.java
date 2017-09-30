package by.htp.hermanovich.airline.entities;

import java.sql.Date;

/**
 * Description: This class describes all applications flights.
 *
 * Created by Yauheni Hermanovich on 19.07.2017.
 */
public class Flight extends BaseEntity {
    private int id;
    private Aircraft aircraft;
    private String flightNumber;
    private Airport departureAirport;
    private Airport arrivalAirport;
    private Date sheduledDeparture;
    private Date sheduledArrival;
    private float pricePerSeat;

    public Flight(){
    }

    public Flight(int id, Aircraft aircraft, String flightNumber, Airport departureAirport, Airport arrivalAirport, Date sheduledDeparture, Date sheduledArrival, float pricePerSeat) {
        this.id = id;
        this.aircraft = aircraft;
        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.sheduledDeparture = sheduledDeparture;
        this.sheduledArrival = sheduledArrival;
        this.pricePerSeat = pricePerSeat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public Date getSheduledDeparture() {
        return sheduledDeparture;
    }

    public void setSheduledDeparture(Date sheduledDeparture) {
        this.sheduledDeparture = sheduledDeparture;
    }

    public Date getSheduledArrival() {
        return sheduledArrival;
    }

    public void setSheduledArrival(Date sheduledArrival) {
        this.sheduledArrival = sheduledArrival;
    }

    public float getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(float pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flight flight = (Flight) o;

        if (id != flight.id) return false;
        if (Float.compare(flight.pricePerSeat, pricePerSeat) != 0) return false;
        if (aircraft != null ? !aircraft.equals(flight.aircraft) : flight.aircraft != null) return false;
        if (flightNumber != null ? !flightNumber.equals(flight.flightNumber) : flight.flightNumber != null)
            return false;
        if (departureAirport != null ? !departureAirport.equals(flight.departureAirport) : flight.departureAirport != null)
            return false;
        if (arrivalAirport != null ? !arrivalAirport.equals(flight.arrivalAirport) : flight.arrivalAirport != null)
            return false;
        if (sheduledDeparture != null ? !sheduledDeparture.equals(flight.sheduledDeparture) : flight.sheduledDeparture != null)
            return false;
        return sheduledArrival != null ? sheduledArrival.equals(flight.sheduledArrival) : flight.sheduledArrival == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (aircraft != null ? aircraft.hashCode() : 0);
        result = 31 * result + (flightNumber != null ? flightNumber.hashCode() : 0);
        result = 31 * result + (departureAirport != null ? departureAirport.hashCode() : 0);
        result = 31 * result + (arrivalAirport != null ? arrivalAirport.hashCode() : 0);
        result = 31 * result + (sheduledDeparture != null ? sheduledDeparture.hashCode() : 0);
        result = 31 * result + (sheduledArrival != null ? sheduledArrival.hashCode() : 0);
        result = 31 * result + (pricePerSeat != +0.0f ? Float.floatToIntBits(pricePerSeat) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Route " + flightNumber + " [" + departureAirport + " == " + arrivalAirport + "] " + String.valueOf(sheduledDeparture) + "***" + pricePerSeat;
    }
}
