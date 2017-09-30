package by.htp.hermanovich.airline.dao.services;

import by.htp.hermanovich.airline.constants.Parameters;
import by.htp.hermanovich.airline.entities.User;
import by.htp.hermanovich.airline.utils.ConnectorDB;
import by.htp.hermanovich.airline.constants.MessageConstants;
import by.htp.hermanovich.airline.dao.implementations.TicketDAO;
import by.htp.hermanovich.airline.entities.Flight;
import by.htp.hermanovich.airline.entities.Luggage;
import by.htp.hermanovich.airline.entities.Ticket;
import by.htp.hermanovich.airline.exceptions.DAOException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Description: This class describes actions on the ticket object.
 *
 * Created by Yauheni Hermanovich on 23.07.2017.
 */
public class TicketService {
    private final static Logger logger = Logger.getLogger(FlightService.class);

    private volatile static TicketService instance;

    public TicketService() {
    }

    /**
     * Singleton realization with "Double Checked Locking & Volatile" principle for high performance and thread safety.
     *
     * @return      - an instance of the class.
     */
    public static TicketService getInstance() {
        if (instance == null) {
            synchronized (TicketService.class) {
                if (instance == null) {
                    return instance = new TicketService();
                }
            }
        }
        return instance;
    }

    /**
     * This method create a new entity of ticket from a <i>map</i> of parameters.
     * The map is passed the the method as a parameter. The method invokes and directs
     * the control to other service classes of this module:
     *      - UserService
     *      - FlightService
     *      - LuggageService
     *
     * @param ticketInfoMap     - a map with parameters.
     * @return                  - an entity of ticket.
     * @throws SQLException
     */
    public Ticket createTicket(HashMap<String, String> ticketInfoMap) throws SQLException {
        Ticket ticket = new Ticket();
        try {
            User user = UserService.getInstance().getUserByLogin(ticketInfoMap.get(Parameters.LOGIN));
            Flight flight = FlightService.getInstance().getFlightById(Integer.parseInt(ticketInfoMap.get(Parameters.FLIGHT_ID)));
            Luggage luggage = LuggageService.getInstance().getLuggageById(Integer.parseInt(ticketInfoMap.get(Parameters.LUGGAGE_ID)));
            String ticketNumber = createTicketNumber();
            float totalPrice = (flight.getPricePerSeat() + luggage.getPrice());
            if (user != null && flight != null && luggage != null
                    && ticketNumber != null && !ticketNumber.isEmpty() ) {
                ticket.setTicketNumber(ticketNumber);
                ticket.setUser(user);
                ticket.setFlight(flight);
                ticket.setLuggage(luggage);
                ticket.setTotalPrice(totalPrice);
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.DATABASE_ACCESS_ERROR);
            throw new SQLException(MessageConstants.DATABASE_ACCESS_ERROR, e);
        }
        return ticket;
    }

    /**
     * This method create an unique ticket number for the client and checks this number for the uniqueness.
     *
     * @return  - an unique string that means an unique ticket number.
     */
    private String createTicketNumber() throws SQLException {
        String number = "";
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            while (true) {
                number = TicketNumberGenerator.getInstance().generateTicketNumber();
                if (TicketDAO.getInstance().checkUniqueNumber(number, connection)) {
                    break;
                }
            }
            connection.commit();
            logger.error(MessageConstants.TRANSACTION_SUCCEEDED);
        } catch (SQLException | DAOException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error(MessageConstants.TRANSACTION_FAILED);
            throw new SQLException(e);
        } finally {
            ConnectorDB.closeConnection(connection);
        }
        return number;
    }

    /**
     * This method add the current entity of the ticket to database table.
     *
     * @param ticket            - the current entity of the ticket.
     * @throws SQLException
     */
    public void addTicketToDB(Ticket ticket) throws SQLException {
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            TicketDAO.getInstance().add(ticket, connection);
            connection.commit();
            logger.error(MessageConstants.TRANSACTION_SUCCEEDED);
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
     * This method describes actions to print all tickets to the flight on the user's jsp page.
     *
     *
     * @param user
     * @param request   - an object of current request with necessary parameters.
     * @return
     */
    public List<Ticket> getTicketsFromDB(User user, HttpServletRequest request) throws SQLException {
        List<Ticket> ticketsList = null;
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            ticketsList = TicketDAO.getInstance().getAllUsersTickets(user, connection);
            connection.commit();
            logger.error(MessageConstants.TRANSACTION_SUCCEEDED);
        } catch (SQLException | DAOException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error(MessageConstants.TRANSACTION_FAILED);
            throw new SQLException(e);
        } finally {
            ConnectorDB.closeConnection(connection);
        }
        return ticketsList;
    }

    /**
     * This method removes the shosen ticket from database table.
     *
     * @param ticketNumber   - a ticket number.
     */
    public void deleteTicket(String ticketNumber) throws SQLException {
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            TicketDAO.getInstance().deleteByNumber(ticketNumber, connection);
            connection.commit();
            logger.error(MessageConstants.TRANSACTION_SUCCEEDED);
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
}