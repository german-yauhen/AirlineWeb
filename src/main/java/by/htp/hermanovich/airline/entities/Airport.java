package by.htp.hermanovich.airline.entities;

/**
 * Description: This class describes all applications airports. Airport can be added to the database by administrator.
 *
 * Created by Yauheni Hermanovich on 13.07.2017.
 */
public class Airport extends BaseEntity {
    private String airportCode;
    private String airportName;
    private String city;

    public Airport() {
    }

    public Airport(String airportCode, String airportName, String city) {
        this.airportCode = airportCode;
        this.airportName = airportName;
        this.city = city;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Airport airport = (Airport) o;

        if (airportCode != null ? !airportCode.equals(airport.airportCode) : airport.airportCode != null) return false;
        if (airportName != null ? !airportName.equals(airport.airportName) : airport.airportName != null) return false;
        return city != null ? city.equals(airport.city) : airport.city == null;
    }

    @Override
    public int hashCode() {
        int result = airportCode != null ? airportCode.hashCode() : 0;
        result = 31 * result + (airportName != null ? airportName.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return city + "[" + airportCode + "]";
    }
}
