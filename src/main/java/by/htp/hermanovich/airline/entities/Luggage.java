package by.htp.hermanovich.airline.entities;

/**
 * Description: This class describes luggage type of luggage available for selection.
 *
 * Created by Yauheni Hermanovich on 13.07.2017.
 */
public class Luggage extends BaseEntity {
    private int id;
    private String luggageType;
    private float price;

    public Luggage() {
    }

    public Luggage(int id, String luggageType, float price) {
        this.id = id;
        this.luggageType = luggageType;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLuggageType() {
        return luggageType;
    }

    public void setLuggageType(String luggageType) {
        this.luggageType = luggageType;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Luggage luggage = (Luggage) o;

        if (id != luggage.id) return false;
        if (Float.compare(luggage.price, price) != 0) return false;
        return luggageType != null ? luggageType.equals(luggage.luggageType) : luggage.luggageType == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (luggageType != null ? luggageType.hashCode() : 0);
        result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
        return result;
    }

    @Override
    public String toString() {
        return luggageType;
    }
}
