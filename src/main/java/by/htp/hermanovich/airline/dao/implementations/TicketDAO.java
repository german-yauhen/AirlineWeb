package by.htp.hermanovich.airline.dao.implementations;

import by.htp.hermanovich.airline.constants.MessageConstants;
import by.htp.hermanovich.airline.constants.Parameters;
import by.htp.hermanovich.airline.dao.ImplTicketDAO;
import by.htp.hermanovich.airline.entities.*;
import by.htp.hermanovich.airline.utils.ConnectorDB;
import by.htp.hermanovich.airline.constants.QueriesDB;
import by.htp.hermanovich.airline.exceptions.DAOException;
import org.apache.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: This class contains implementation of interface methods which works with <i>tickets</i> database table.
 *
 * Created by Yauheni Hermanovich on 20.07.2017.
 */
public class TicketDAO implements ImplTicketDAO {
    private static final Logger logger = Logger.getLogger(TicketDAO.class);

    private volatile static TicketDAO instance;

    private TicketDAO() {
    }

    /**
     * Singleton realization with "Double Checked Locking & Volatile" principle for high performance and thread safety.
     *
     * @return      - an instance of the class.
     */
    public static TicketDAO getInstance() {
        if (instance == null) {
            synchronized (TicketDAO.class) {
                if (instance == null) {
                    instance = new TicketDAO();
                }
            }
        }
        return instance;
    }

    /**
     * This method creates and inserts an entity in a database table.
     *
     * @param ticket     - the current ticket which has been created.
     * @param connection - the current connection to a database. Transmitted from the service module to provide transactions.
     */
    @Override
    public void add(Ticket ticket, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(QueriesDB.ADD_TICKET);
            statement.setString(1, ticket.getTicketNumber().toUpperCase());
            statement.setInt(2, ticket.getUser().getId());
            statement.setInt(3, ticket.getFlight().getId());
            statement.setInt(4, ticket.getLuggage().getId());
            statement.setFloat(5, ticket.getTotalPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeStatement(statement);
        }
    }

    /**
     * This method removes the ticket from database table according to the ticket number.
     *
     * @param ticketNumber  - a ticket number.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     */
    @Override
    public void deleteByNumber(String ticketNumber, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(QueriesDB.DELETE_TICKET_BY_NUMBER);
            statement.setString(1, ticketNumber);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeStatement(statement);
        }
    }

    /**
     * This method reads and returns information about user's tickets from a database table.
     *
     * @param user          - an user object with necessary fields.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return              - list of all tickets from a database table.
     */
    @Override
    public List<Ticket> getAllUsersTickets(User user, Connection connection) throws DAOException {
        List<Ticket> ticketsList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(QueriesDB.GET_ALL_TICKETS_BY_USER);
            statement.setInt(1, user.getId());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ticketsList.add(createTicketFromDB(user, resultSet, new Ticket()));
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeResultSet(resultSet);
            ConnectorDB.closeStatement(statement);
        }
        return ticketsList;
    }

    /**
     * An additional method. Not from interface.
     * This method creates an entity of ticket from resultSet.
     *
     * @param user              - the entity of the current user.
     * @param resultSet         - a table of data representing a database result set.
     * @return                  - an entity of ticket.
     * @return
     * @throws SQLException
     */
    public Ticket createTicketFromDB(User user, ResultSet resultSet, Ticket ticket) throws SQLException {
        try {
            ticket.setTicketNumber(resultSet.getString(Parameters.TICKET_NUMBER_DB));
            ticket.setUser(user);
            ticket.setFlight(buildFlightForTicket(resultSet));
            ticket.setLuggage(buildLuggageForTicket(resultSet));
            ticket.setTotalPrice(resultSet.getFloat(Parameters.TOTAL_PRICE_DB));
        } catch (SQLException e) {
            logger.error(MessageConstants.COLUMN_IS_NOT_VALID, e);
            throw new SQLException(MessageConstants.COLUMN_IS_NOT_VALID, e);
        }
        return ticket;
    }

    /**
     * An additional method. Not from interface.
     * This method creates an entity of luggage from resultSet.
     *
     * @param resultSet         - a table of data representing a database result set.
     * @return                  - an entity of flight.
     * @throws SQLException
     */
    private Luggage buildLuggageForTicket(ResultSet resultSet) throws SQLException {
        Luggage luggage = new Luggage();
        try {
            luggage.setLuggageType(resultSet.getString(Parameters.LUGGAGE_TYPE_DB));
            luggage.setPrice(resultSet.getFloat(Parameters.PRICE_DB));
        } catch (SQLException e) {
            logger.error(MessageConstants.COLUMN_IS_NOT_VALID, e);
            throw new SQLException(MessageConstants.COLUMN_IS_NOT_VALID, e);
        }
        return luggage;
    }

    /**
     * An additional method. Not from interface.
     * This method creates an entity of flight from resultSet.
     *
     * @param resultSet         - a table of data representing a database result set.
     * @return                  - an entity of flight.
     * @throws SQLException
     */
    private Flight buildFlightForTicket(ResultSet resultSet) throws SQLException {
        Flight flight = new Flight();
        try {
            flight.setAircraft(buildAircraftForFlight(resultSet));
            flight.setFlightNumber(resultSet.getString(Parameters.FLIGHT_NUMBER_DB));
            flight.setDepartureAirport(buildDepAirportForFlight(resultSet));
            flight.setArrivalAirport(buildArrAirportForFlight(resultSet));
            flight.setSheduledDeparture(resultSet.getDate(Parameters.SHEDULED_DEPARTURE_DB));
            flight.setSheduledArrival(resultSet.getDate(Parameters.SHEDULED_ARRIVAL_DB));
            flight.setPricePerSeat(resultSet.getFloat(Parameters.PRICE_PER_SEAT_DB));
        } catch (SQLException e) {
            logger.error(MessageConstants.COLUMN_IS_NOT_VALID, e);
            throw new SQLException(MessageConstants.COLUMN_IS_NOT_VALID, e);
        }
        return flight;
    }

    /**
     * An additional method. Not from interface.
     * This method creates an entity of arrival airport from resultSet.
     *
     * @param resultSet         - a table of data representing a database result set.
     * @return                  - an entity of departure airport.
     * @throws SQLException
     */
    private Airport buildArrAirportForFlight(ResultSet resultSet) throws SQLException {
        Airport airport = new Airport();
        try {
            airport.setAirportCode(resultSet.getString(Parameters.ARRIVAL_AIRPORT_DB_AS));
            airport.setAirportName(resultSet.getString(Parameters.ARRIVAL_AIRPORT_NAME_DB_AS));
            airport.setCity(resultSet.getString(Parameters.ARRIVAL_AIRPORT_CITY_DB_AS));
        } catch (SQLException e) {
            logger.error(MessageConstants.COLUMN_IS_NOT_VALID, e);
            throw new SQLException(MessageConstants.COLUMN_IS_NOT_VALID, e);
        }
        return airport;
    }

    /**
     * An additional method. Not from interface.
     * This method creates an entity of departure airport from resultSet.
     *
     * @param resultSet         - a table of data representing a database result set.
     * @return                  - an entity of departure airport.
     * @throws SQLException
     */
    private Airport buildDepAirportForFlight(ResultSet resultSet) throws SQLException {
        Airport airport = new Airport();
        try {
            airport.setAirportCode(resultSet.getString(Parameters.DEPARTURE_AIRPORT_DB_AS));
            airport.setAirportName(resultSet.getString(Parameters.DEPARTURE_AIRPORT_NAME_DB_AS));
            airport.setCity(resultSet.getString(Parameters.DEPARTURE_AIRPORT_CITY_DB_AS));
        } catch (SQLException e) {
            logger.error(MessageConstants.COLUMN_IS_NOT_VALID, e);
            throw new SQLException(MessageConstants.COLUMN_IS_NOT_VALID, e);
        }
        return airport;
    }

    /**
     * An additional method. Not from interface.
     * This method creates an entity of aircraft from resultSet.
     *
     * @param resultSet         - a table of data representing a database result set.
     * @return                  - an entity of aircraft.
     * @throws SQLException
     */
    private Aircraft buildAircraftForFlight(ResultSet resultSet) throws SQLException {
        Aircraft aircraft = new Aircraft();
        try {
            aircraft.setAircraftCode(resultSet.getString(Parameters.AIRCRAFT_CODE_DB));
            aircraft.setModel(resultSet.getString(Parameters.AIRCRAFT_MODEL_DB));
        } catch (SQLException e) {
            logger.error(MessageConstants.COLUMN_IS_NOT_VALID, e);
            throw new SQLException(MessageConstants.COLUMN_IS_NOT_VALID, e);
        }
        return aircraft;
    }

    /**
     * An additional method. Not from interface.
     * This method checks the uniqueness of the ticket number.
     *
     * @param number        - a ticket number that will be processed.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return              - a boolean value of the condition.
     * @throws DAOException
     */
    public boolean checkUniqueNumber(String number, Connection connection) throws DAOException {
        boolean isUnique = true;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(QueriesDB.GET_TICKET_BY_NUMBER);
            statement.setString(1, number);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                isUnique = false;
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeResultSet(resultSet);
            ConnectorDB.closeStatement(statement);
        }
        return isUnique;
    }


    /**
     * ***NOT USED***
     * This method reads and returns information from all records (rows) of a database table.
     *
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return              - list of all tickets from a database table.
     */
    @Override
    public List<Ticket> getAll(Connection connection) throws DAOException {
        return null;
    }
}