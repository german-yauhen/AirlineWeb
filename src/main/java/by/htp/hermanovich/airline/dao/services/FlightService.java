package by.htp.hermanovich.airline.dao.services;

import by.htp.hermanovich.airline.constants.MessageConstants;
import by.htp.hermanovich.airline.constants.Parameters;
import by.htp.hermanovich.airline.dao.implementations.FlightDAO;
import by.htp.hermanovich.airline.entities.Aircraft;
import by.htp.hermanovich.airline.entities.Flight;
import by.htp.hermanovich.airline.exceptions.DAOException;
import by.htp.hermanovich.airline.utils.ConnectorDB;
import by.htp.hermanovich.airline.entities.Airport;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

/**
 * Description: This class describes actions on the flight object.
 *
 * Created by Yauheni Hermanovich on 19.07.2017.
 */
public class FlightService {
    private final static Logger logger = Logger.getLogger(FlightService.class);

    private volatile static FlightService instance;

    public FlightService() {
    }

    /**
     * Singleton realization with "Double Checked Locking & Volatile" principle for high performance and thread safety.
     *
     * @return      - an instance of the class.
     */
    public static FlightService getInstance() {
        if (instance == null) {
            synchronized (FlightService.class) {
                if (instance == null) {
                    return instance = new FlightService();
                }
            }
        }
        return instance;
    }

    /**
     * This method registers new flight of the airline company.
     * This method implements work with transaction support.
     *
     * @param flight   - a new flight object will be registered.
     * @throws SQLException
     */
    public void addFlightToDB(Flight flight) throws SQLException {
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            FlightDAO.getInstance().add(flight, connection);
            connection.commit();
            logger.info(MessageConstants.TRANSACTION_SUCCEEDED);
        } catch (SQLException | DAOException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error(MessageConstants.TRANSACTION_FAILED);
            throw new SQLException(e);
        } finally {
            ConnectorDB.closeConnection(connection);
        }
    }

    /**
     * This method receives a new entity of flight from database using the id. The workflow of this method
     * consists of the following steps:
     *          - the method invokes a DAO method and receives a map with parameters that are returned from database table;
     *          - the method invokes method <i>buildFlightFromMap(...map)</i> and receives an entity of flight.
     *
     * @param id            - flight id;
     * @return              - an entity of flight.
     * @throws SQLException
     */
    public Flight getFlightById(int id) throws SQLException {
        Flight flight = null;
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            HashMap<String, String> flightInfoMap = FlightDAO.getInstance().getFlightInfoById(id, connection);
            connection.commit();
            logger.info(MessageConstants.TRANSACTION_SUCCEEDED);
            flight = buildFlightFromMap(flightInfoMap);
            flight.setId(Integer.parseInt(flightInfoMap.get(Parameters.FLIGHT_ID)));
        } catch (SQLException | DAOException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error(MessageConstants.TRANSACTION_FAILED);
            throw new SQLException(e);
        } finally {
            ConnectorDB.closeConnection(connection);
        }
        return flight;
    }

    /**
     * This method create a new entity of flight from a <i>map</i> of parameters.
     * The map is passed the the method as a parameter. The method invokes and directs
     * the control to other service classes of this module:
     *      - AircraftService
     *      - AirportService
     *      - AirportService
     *
     * @param flightInfoMap     - a map with parameters.
     * @return                              - an entity of flight.
     * @throws SQLException
     */
    public Flight buildFlightFromMap(HashMap<String, String> flightInfoMap) throws SQLException {
        Flight flight = new Flight();
        try {
            Aircraft aircraftForFlight = AircraftService.getInstance().getAircraftFromDB(flightInfoMap.get(Parameters.AIRCRAFT_FOR_FLIGHT));
            Airport departureForFlight = AirportService.getInstance().getAirportFromDB(flightInfoMap.get(Parameters.DEPARTURE_FOR_FLIGHT));
            Airport arrivalForFlight = AirportService.getInstance().getAirportFromDB(flightInfoMap.get(Parameters.ARRIVAL_FOR_FLIGHT));
            if (aircraftForFlight != null && departureForFlight != null && arrivalForFlight != null) {
                flight.setAircraft(aircraftForFlight);
                flight.setFlightNumber(flightInfoMap.get(Parameters.FLIGHT_NUMBER_FOR_FLIGHT));
                flight.setDepartureAirport(departureForFlight);
                flight.setArrivalAirport(arrivalForFlight);
                flight.setSheduledDeparture(Date.valueOf(flightInfoMap.get(Parameters.DATE_OF_FLIGHT)));
                flight.setSheduledArrival(Date.valueOf(flightInfoMap.get(Parameters.DATE_OF_FLIGHT)));
                flight.setPricePerSeat(Float.parseFloat(flightInfoMap.get(Parameters.PRICE_PER_SEAT)));
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.DATABASE_ACCESS_ERROR);
            throw new SQLException(MessageConstants.DATABASE_ACCESS_ERROR, e);
        }
        return flight;
    }

    /**
     * This method identifies a search context. The method determines the context according to the
     * parameters in the <i>searchConditions</i> map with corresponding values. The search context has
     * three search options following bellow:
     *          - the <i>searchConditions</i> has departure, arrival and date keys;
     *          - the <i>searchConditions</i> has departure and arrival keys;
     *          - the <i>searchConditions</i> has departure and date keys.
     * Each option means calling to the corresponding method of the flight service module
     *
     * @return                      - a result list of flights.
     * @param searchConditions      - the <i>searchConditions</i> map with corresponding values.
     */
    public List<Flight> identifySearchContext(HashMap<String, String> searchConditions) throws SQLException {
        List<Flight> flightsFromDB = null;
        try {
            if (searchConditions.containsKey(Parameters.DEPARTURE_FOR_FLIGHT)
                    && searchConditions.containsKey(Parameters.ARRIVAL_FOR_FLIGHT)
                    && searchConditions.containsKey(Parameters.DATE_OF_FLIGHT)) {
                flightsFromDB = getFlightsByDepArrDateFromDB(searchConditions);
            } else if (searchConditions.containsKey(Parameters.DEPARTURE_FOR_FLIGHT)
                    && searchConditions.containsKey(Parameters.ARRIVAL_FOR_FLIGHT)) {
                flightsFromDB = getFlightsByDepArrFromDB(searchConditions);
            } else if (searchConditions.containsKey(Parameters.DEPARTURE_FOR_FLIGHT)
                    && searchConditions.containsKey(Parameters.DATE_OF_FLIGHT)) {
                flightsFromDB = getFlightsByDepDateFromDB(searchConditions);
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.DATABASE_ACCESS_ERROR);
            throw new SQLException(MessageConstants.DATABASE_ACCESS_ERROR, e);
        }
        return flightsFromDB;
    }

    /**
     * This method describes actions to find the flights by the departure airport and the date of the flight.
     *
     * @param searchConditions      - the <i>searchConditions</i> map with corresponding values.
     * @return                      - a result list of flights.
     * @throws SQLException
     */
    private List<Flight> getFlightsByDepDateFromDB(HashMap<String, String> searchConditions) throws SQLException {
        List<Flight> flightsFromDB = null;
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            Airport depAirportForSearch = AirportService.getInstance().getAirportFromDB(searchConditions.get(Parameters.DEPARTURE_FOR_FLIGHT));
            Date dateForSearch = Date.valueOf(searchConditions.get(Parameters.DATE_OF_FLIGHT));
            flightsFromDB = FlightDAO.getInstance().getFlightsByDepDate(depAirportForSearch, dateForSearch, connection);
            connection.commit();
            logger.info(MessageConstants.TRANSACTION_SUCCEEDED);
        } catch (SQLException | DAOException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error(MessageConstants.TRANSACTION_FAILED);
        } finally {
            ConnectorDB.closeConnection(connection);
        }
        return flightsFromDB;
    }

    /**
     * This method describes actions to find the flights by the departure airport, arrival airport of the flight.
     *
     * @param searchConditions      - the <i>searchConditions</i> map with corresponding values.
     * @return                      - a result list of flights.
     * @throws SQLException
     */
    private List<Flight> getFlightsByDepArrFromDB(HashMap<String, String> searchConditions) throws SQLException {
        List<Flight> flightsFromDB = null;
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            Airport depAirportForSearch = AirportService.getInstance().getAirportFromDB(searchConditions.get(Parameters.DEPARTURE_FOR_FLIGHT));
            Airport arrAirportForSearch = AirportService.getInstance().getAirportFromDB(searchConditions.get(Parameters.ARRIVAL_FOR_FLIGHT));
            flightsFromDB = FlightDAO.getInstance().getFlightsByDepArr(depAirportForSearch, arrAirportForSearch, connection);
            connection.commit();
            logger.info(MessageConstants.TRANSACTION_SUCCEEDED);
        } catch (SQLException | DAOException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error(MessageConstants.TRANSACTION_FAILED);
        } finally {
            ConnectorDB.closeConnection(connection);
        }
        return flightsFromDB;
    }

    /**
     * This method describes actions to find the flights by the departure airport, arrival airport and the date of the flight.
     *
     * @param searchConditions      - the <i>searchConditions</i> map with corresponding values.
     * @return                      - a result list of flights.
     * @throws SQLException
     */
    private List<Flight> getFlightsByDepArrDateFromDB(HashMap<String, String> searchConditions) throws SQLException {
        List<Flight> flightsFromDB = null;
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            Airport depAirportForSearch = AirportService.getInstance().getAirportFromDB(searchConditions.get(Parameters.DEPARTURE_FOR_FLIGHT));
            Airport arrAirportForSearch = AirportService.getInstance().getAirportFromDB(searchConditions.get(Parameters.ARRIVAL_FOR_FLIGHT));
            Date dateForSearch = Date.valueOf(searchConditions.get(Parameters.DATE_OF_FLIGHT));
            flightsFromDB = FlightDAO.getInstance().getFlightsByDepArrDate(depAirportForSearch, arrAirportForSearch, dateForSearch, connection);
            connection.commit();
            logger.info(MessageConstants.TRANSACTION_SUCCEEDED);
        } catch (SQLException | DAOException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error(MessageConstants.TRANSACTION_FAILED);
        } finally {
            ConnectorDB.closeConnection(connection);
        }
        return flightsFromDB;
    }
}