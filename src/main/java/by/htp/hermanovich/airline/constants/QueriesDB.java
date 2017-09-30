package by.htp.hermanovich.airline.constants;

/**
 * Description: This class contains the sql-queries to the current database. The queries are formatted in constant view.
 *
 * Created by Yauheni Hermanovich on 11.07.2017.
 */
public class QueriesDB {

    /*Queries to work with users database table.*/
    public static final String CHECK_AUTHORIZATION = "SELECT login, password FROM users WHERE login = ? AND password = ?";
    public static final String GET_USER_BY_LOGIN = "SELECT * FROM users WHERE login = ?";
    public static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    public static final String ADD_USER_WITHOUT_USERTYPE = "INSERT INTO users (firstname, surname, document_number, login, password)" +
                                                            "VALUES (?, ?, ?, ?, ?)";
    public static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?";
    public static final String UPDATE_USER_BY_ID = "UPDATE users SET firstname = ?, surname = ?, document_number = ?," +
                                                    "login = ?, password = ?, user_type = ?  WHERE id = ?";
    public static final String GET_ALL_USERS = "SELECT * FROM users";

    /*Queries to work with airports database table.*/
    public static final String ADD_AIRPORT = "INSERT INTO airports (airport_code, airport_name, city) VALUES (?, ?, ?)";
    public static final String UPDATE_AIRPORT = "UPDATE airports SET airport_name = ?, city = ? WHERE airport_code = ?";
    public static final String DELETE_AIRPORT_BY_CODE = "DELETE FROM airports WHERE airport_code = ?";
    public static final String GET_AIRPORT_BY_CODE = "SELECT * FROM airports WHERE airport_code = ?";
    public static final String GET_ALL_AIRPORTS = "SELECT * FROM airports";

    /*Queries to work with luggage database table.*/
    public static final String GET_LUGGAGE_BY_ID = "SELECT * FROM luggage WHERE id = ?";
    public static final String ADD_LUGGAGE = "INSERT INTO luggage (luggage_type, price) VALUES (?, ?)";
    public static final String UPDATE_LUGGAGE = "UPDATE luggage SET luggage_type = ?, price = ? WHERE id = ?";
    public static final String GET_LUGGAGE_BY_TYPE = "SELECT * FROM luggage WHERE luggage_type = ?";
    public static final String GET_ALL_LUGGAGE_TYPES = "SELECT * FROM luggage";
    public static final String DELETE_LUGGAGE_BY_ID = "DELETE FROM luggage WHERE id = ?";

    /*Queries to work with aircraft database table.*/
    public static final String ADD_AIRCRAFT = "INSERT INTO aircrafts (aircraft_code, model) VALUES (?, ?)";
    public static final String UPDATE_AIRCRAFT = "UPDATE aircrafts SET model = ? WHERE aircraft_code = ?";
    public static final String GET_ALL_AIRCRAFTS = "SELECT * FROM aircrafts";
    public static final String GET_AIRCRAFT_BY_CODE = "SELECT * FROM aircrafts WHERE aircraft_code = ?";
    public static final String DELETE_AIRCRAFT_BY_CODE = "DELETE FROM aircrafts WHERE aircraft_code = ?";

    /*Queries to work with flight database table.*/
    public static final String GET_FLIGHT_BY_ID = "SELECT * FROM flights WHERE id = ?";
    public static final String ADD_FLIGHT = "INSERT INTO flights (aircrafts_aircraft_code, flight_number, departure_airport" +
                                            ", arrival_airport, sheduled_departure_date, sheduled_arrival_date, price_per_seat)" +
                                            "VALUES(?, ?, ?, ?, ?, ?, ?)";
    public static final String GET_FLIGHTS_BY_DEP_ARR_DATE = "SELECT * FROM flights WHERE departure_airport = ? AND arrival_airport = ? AND sheduled_departure_date = ?";
    public static final String GET_FLIGHTS_BY_DEP_ARR = "SELECT * FROM flights WHERE departure_airport = ? AND arrival_airport = ?";
    public static final String GET_FLIGHTS_BY_DEP_DATE = "SELECT * FROM flights WHERE departure_airport = ? AND sheduled_departure_date = ?";

    /*Queries to work with tickets database table.*/
    public static final String ADD_TICKET = "INSERT INTO tickets (ticket_number, users_id, flights_id, luggage_id, total_price)" +
        "VALUES(?, ?, ?, ?, ?)";
    public static final String GET_ALL_TICKETS_BY_USER = "SELECT ticket_number, firstname, surname, document_number, login, user_type, " +
            "aircraft_code, model, flight_number, " +
            "dep.airport_code AS dep_airport_code, dep.airport_name AS dep_airport_name, dep.city AS dep_airport_city, " +
            "arr.airport_code AS arr_airport_code, arr.airport_name AS arr_airport_name, arr.city AS arr_airport_city, " +
            "sheduled_departure_date, sheduled_arrival_date, price_per_seat, luggage_type, price, total_price " +
            "FROM users JOIN tickets ON users.id = tickets.users_id " +
            "JOIN flights ON flights.id = tickets.flights_id " +
            "JOIN luggage ON luggage.id = tickets.luggage_id " +
            "JOIN aircrafts ON aircrafts.aircraft_code = aircrafts_aircraft_code " +
            "JOIN airports AS dep ON dep.airport_code = departure_airport " +
            "JOIN airports AS arr ON arr.airport_code = arrival_airport " +
            "WHERE users_id = ?";
    public static final String GET_TICKET_BY_NUMBER = "SELECT * FROM tickets WHERE ticket_number = ?";
    public static final String DELETE_TICKET_BY_NUMBER = "DELETE FROM tickets WHERE ticket_number = ?";
}
