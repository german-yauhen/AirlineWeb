package by.htp.hermanovich.airline.dao.implementations;

import by.htp.hermanovich.airline.constants.Parameters;
import by.htp.hermanovich.airline.constants.MessageConstants;
import by.htp.hermanovich.airline.constants.QueriesDB;
import by.htp.hermanovich.airline.dao.ImplFlightDAO;
import by.htp.hermanovich.airline.entities.Airport;
import by.htp.hermanovich.airline.entities.Flight;
import by.htp.hermanovich.airline.exceptions.DAOException;
import by.htp.hermanovich.airline.utils.ConnectorDB;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description: This class contains implementation of interface methods which works with <i>flights</i> database table.
 *
 * Created by Yauheni Hermanovich on 19.07.2017.
 */
public class FlightDAO implements ImplFlightDAO {
    private static final Logger logger = Logger.getLogger(FlightDAO.class);

    private volatile static FlightDAO instance;

    private FlightDAO() {
    }

    /**
     * Singleton realization with "Double Checked Locking & Volatile" principle for high performance and thread safety.
     *
     * @return      - an instance of the class.
     */
    public static FlightDAO getInstance() {
        if (instance == null) {
            synchronized (FlightDAO.class) {
                if (instance == null) {
                    instance = new FlightDAO();
                }
            }
        }
        return instance;
    }

    /**
     * This method creates and inserts an entity in a database table.
     *
     * @param flight     - the current flight which has been created.
     * @param connection - the current connection to a database. Transmitted from the service module to provide transactions.
     */
    @Override
    public void add(Flight flight, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(QueriesDB.ADD_FLIGHT);
            statement.setString(1, flight.getAircraft().getAircraftCode());
            statement.setString(2, flight.getFlightNumber());
            statement.setString(3, flight.getDepartureAirport().getAirportCode());
            statement.setString(4, flight.getArrivalAirport().getAirportCode());
            statement.setDate(5, flight.getSheduledDeparture());
            statement.setDate(6, flight.getSheduledArrival());
            statement.setFloat(7, flight.getPricePerSeat());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeStatement(statement);
        }
    }

    /**
     * This method creates an information about flight represented in <i>map</i> view.
     *
     * @param id                - flight id;
     * @param connection        - the current connection to a database. Transmitted from the service module to provide transactions;
     * @return                  - a <i>map</i> of parameters for building the flight object.
     * @throws DAOException
     */
    public HashMap<String, String> getFlightInfoById(int id, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        HashMap<String, String> flightInfoMap = new HashMap<>();
        try {
            statement = connection.prepareStatement(QueriesDB.GET_FLIGHT_BY_ID);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                flightInfoMap.put(Parameters.FLIGHT_ID, String.valueOf(resultSet.getInt(Parameters.ID)));
                flightInfoMap.put(Parameters.AIRCRAFT_FOR_FLIGHT, resultSet.getString(Parameters.AIRCRAFTS_AIRCRAFT_CODE_DB));
                flightInfoMap.put(Parameters.FLIGHT_NUMBER_FOR_FLIGHT, resultSet.getString(Parameters.FLIGHT_NUMBER_DB));
                flightInfoMap.put(Parameters.DEPARTURE_FOR_FLIGHT, resultSet.getString(Parameters.DEPARTURE_AIRPORT_DB));
                flightInfoMap.put(Parameters.ARRIVAL_FOR_FLIGHT, resultSet.getString(Parameters.ARRIVAL_AIRPORT_DB));
                flightInfoMap.put(Parameters.DATE_OF_FLIGHT, resultSet.getString(Parameters.SHEDULED_DEPARTURE_DB));
                flightInfoMap.put(Parameters.PRICE_PER_SEAT, resultSet.getString(Parameters.PRICE_PER_SEAT_DB));
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeResultSet(resultSet);
            ConnectorDB.closeStatement(statement);
        }
        return flightInfoMap;
    }

    /**
     * This method describes actions to find the flights by the departure airport, arrival airport and the date of the flight.
     *
     * @param depAirportForSearch   - departure airport for the search context;
     * @param arrAirportForSearch   - arrival airport for the search context;
     * @param dateForSearch         - date of flight for the search context;
     * @param connection            - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return                      - a list of flights that is fulfilled of the condition.
     * @throws DAOException
     */
    @Override
    public List<Flight> getFlightsByDepArrDate(Airport depAirportForSearch, Airport arrAirportForSearch, Date dateForSearch, Connection connection) throws DAOException {
        List<Flight> flightsFromDB = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(QueriesDB.GET_FLIGHTS_BY_DEP_ARR_DATE);
            statement.setString(1, depAirportForSearch.getAirportCode());
            statement.setString(2, arrAirportForSearch.getAirportCode());
            statement.setDate(3, dateForSearch);
            resultSet = statement.executeQuery();
            flightsFromDB = new ArrayList<Flight>();
            while (resultSet.next()) {
                Flight flight = new Flight();
                flight.setId(resultSet.getInt(Parameters.ID));
                flight.setFlightNumber(resultSet.getString(Parameters.FLIGHT_NUMBER_DB));
                flight.setDepartureAirport(depAirportForSearch);
                flight.setArrivalAirport(arrAirportForSearch);
                flight.setSheduledDeparture(dateForSearch);
                flight.setSheduledArrival(dateForSearch);
                flight.setAircraft(AircraftDAO.getInstance().getByCode(resultSet.getString(Parameters.AIRCRAFTS_AIRCRAFT_CODE_DB), connection));
                flight.setPricePerSeat(resultSet.getFloat(Parameters.PRICE_PER_SEAT_DB));
                flightsFromDB.add(flight);
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeResultSet(resultSet);
            ConnectorDB.closeStatement(statement);
        }
        return flightsFromDB;
    }

    /**
     * This method describes actions to find the flights by the departure and arrival airports of the flight.
     *
     * @param depAirportForSearch   - departure airport for the search context;
     * @param arrAirportForSearch   - arrival airport for the search context;
     * @param connection            - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return                      - a list of flights that is fulfilled of the condition.
     * @throws DAOException
     */
    @Override
    public List<Flight> getFlightsByDepArr(Airport depAirportForSearch, Airport arrAirportForSearch, Connection connection) throws DAOException {
        List<Flight> flightsFromDB = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(QueriesDB.GET_FLIGHTS_BY_DEP_ARR);
            statement.setString(1, depAirportForSearch.getAirportCode());
            statement.setString(2, arrAirportForSearch.getAirportCode());
            resultSet = statement.executeQuery();
            flightsFromDB = new ArrayList<Flight>();
            while (resultSet.next()) {
                Flight flight = new Flight();
                flight.setId(resultSet.getInt(Parameters.ID));
                flight.setFlightNumber(resultSet.getString(Parameters.FLIGHT_NUMBER_DB));
                flight.setDepartureAirport(depAirportForSearch);
                flight.setArrivalAirport(arrAirportForSearch);
                flight.setSheduledDeparture(Date.valueOf(resultSet.getString(Parameters.SHEDULED_DEPARTURE_DB)));
                flight.setSheduledArrival(Date.valueOf(resultSet.getString(Parameters.SHEDULED_ARRIVAL_DB)));
                flight.setAircraft(AircraftDAO.getInstance().getByCode(resultSet.getString(Parameters.AIRCRAFTS_AIRCRAFT_CODE_DB), connection));
                flight.setPricePerSeat(resultSet.getFloat(Parameters.PRICE_PER_SEAT_DB));
                flightsFromDB.add(flight);
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeResultSet(resultSet);
            ConnectorDB.closeStatement(statement);
        }
        return flightsFromDB;
    }

    /**
     * This method describes actions to find the flights by the departure airport and the date of the flight.
     *
     * @param depAirportForSearch   - departure airport for the search context;
     * @param dateForSearch         - date of flight for the search context;
     * @param connection            - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return                      - a list of flights that is fulfilled of the condition.
     * @throws DAOException
     */
    @Override
    public List<Flight> getFlightsByDepDate(Airport depAirportForSearch, Date dateForSearch, Connection connection) throws DAOException {
        List<Flight> flightsFromDB = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(QueriesDB.GET_FLIGHTS_BY_DEP_DATE);
            statement.setString(1, depAirportForSearch.getAirportCode());
            statement.setDate(2, dateForSearch);
            resultSet = statement.executeQuery();
            flightsFromDB = new ArrayList<Flight>();
            while (resultSet.next()) {
                Flight flight = new Flight();
                flight.setId(resultSet.getInt(Parameters.ID));
                flight.setFlightNumber(resultSet.getString(Parameters.FLIGHT_NUMBER_DB));
                flight.setDepartureAirport(depAirportForSearch);
                flight.setArrivalAirport(AirportDAO.getInstance().getByCode(resultSet.getString(Parameters.ARRIVAL_AIRPORT_DB), connection));
                flight.setSheduledDeparture(dateForSearch);
                flight.setSheduledArrival(dateForSearch);
                flight.setAircraft(AircraftDAO.getInstance().getByCode(resultSet.getString(Parameters.AIRCRAFTS_AIRCRAFT_CODE_DB), connection));
                flight.setPricePerSeat(resultSet.getFloat(Parameters.PRICE_PER_SEAT_DB));
                flightsFromDB.add(flight);
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeResultSet(resultSet);
            ConnectorDB.closeStatement(statement);
        }
        return flightsFromDB;
    }

    /**
     * ***NOT USED***
     * This method reads and returns information from all records (rows) of a database table.
     *
     * @param connection - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return - list of all entities from a database table.
     */
    @Override
    public List<Flight> getAll(Connection connection) throws DAOException {
        return null;
    }
}