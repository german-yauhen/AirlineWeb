package by.htp.hermanovich.airline.entities;

/**
 * Description: This class describes all applications aircrafts. Aircraft can be added to the database by administrator.
 *
 * Created by Yauheni Hermanovich on 13.07.2017.
 */
public class Aircraft extends BaseEntity {
    private String aircraftCode;
    private String model;

    public Aircraft() {
    }

    public Aircraft(String aircraftCode, String model) {
        this.aircraftCode = aircraftCode;
        this.model = model;
    }

    public String getAircraftCode() {
        return aircraftCode;
    }

    public void setAircraftCode(String aircraftCode) {
        this.aircraftCode = aircraftCode;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Aircraft aircraft = (Aircraft) o;

        if (aircraftCode != null ? !aircraftCode.equals(aircraft.aircraftCode) : aircraft.aircraftCode != null)
            return false;
        return model != null ? model.equals(aircraft.model) : aircraft.model == null;
    }

    @Override
    public int hashCode() {
        int result = aircraftCode != null ? aircraftCode.hashCode() : 0;
        result = 31 * result + (model != null ? model.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return model + " [" + aircraftCode + "]";
    }
}
