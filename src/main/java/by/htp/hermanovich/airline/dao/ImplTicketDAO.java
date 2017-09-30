package by.htp.hermanovich.airline.dao;

import by.htp.hermanovich.airline.entities.Ticket;
import by.htp.hermanovich.airline.entities.User;
import by.htp.hermanovich.airline.exceptions.DAOException;
import java.sql.Connection;
import java.util.List;

/**
 * Description: This interface describes methods for working with <i>tickets</i> database table,
 * extending the capabilities of basic DAO interface.
 *
 * Created by Yauheni Hermanovich on 20.07.2017.
 */
public interface ImplTicketDAO extends AbstractDAO<Ticket> {

    /**
     * This method creates and inserts an entity in a database table.
     *
     * @param ticket     - the current ticket which has been created.
     * @param connection - the current connection to a database. Transmitted from the service module to provide transactions.
     */
    @Override
    void add(Ticket ticket, Connection connection) throws DAOException;

    /**
     * This method reads and returns information about user's tickets from a database table.
     *
     * @param user          - an user object with necessary fields.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return              - list of all tickets from a database table.
     */
    List<Ticket> getAllUsersTickets(User user, Connection connection) throws DAOException;

    /**
     * This method reads and returns information from all records (rows) of a database table.
     *
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return              - list of all tickets from a database table.
     */
    @Override
    List<Ticket> getAll(Connection connection) throws DAOException;

    /**
     * This method removes the ticket from database table according to the ticket number.
     *
     * @param ticketNumber  - a ticket number.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     */
    void deleteByNumber(String ticketNumber, Connection connection) throws DAOException;
}