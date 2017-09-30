package by.htp.hermanovich.airline.dao;

import by.htp.hermanovich.airline.entities.Flight;
import by.htp.hermanovich.airline.entities.Airport;
import by.htp.hermanovich.airline.exceptions.DAOException;
import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Description: Thi interface describes methods for working with <i>flights</i> database table.
 *
 * Created by Yauheni Hermanovich on 19.07.2017.
 */
public interface ImplFlightDAO extends AbstractDAO<Flight> {

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
    List<Flight> getFlightsByDepArrDate(Airport depAirportForSearch, Airport arrAirportForSearch,
                                        Date dateForSearch, Connection connection) throws DAOException;

    /**
     * This method describes actions to find the flights by the departure and arrival airports of the flight.
     *
     * @param depAirportForSearch   - departure airport for the search context;
     * @param arrAirportForSearch   - arrival airport for the search context;
     * @param connection            - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return                      - a list of flights that is fulfilled of the condition.
     * @throws DAOException
     */
    List<Flight> getFlightsByDepArr(Airport depAirportForSearch, Airport arrAirportForSearch, Connection connection) throws DAOException;

    /**
     * This method describes actions to find the flights by the departure airport and the date of the flight.
     *
     * @param depAirportForSearch   - departure airport for the search context;
     * @param dateForSearch         - date of flight for the search context;
     * @param connection            - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return                      - a list of flights that is fulfilled of the condition.
     * @throws DAOException
     */
    List<Flight> getFlightsByDepDate(Airport depAirportForSearch, Date dateForSearch, Connection connection) throws DAOException;

    /**
     * This method creates an information about flight represented in <i>map</i> view.
     *
     * @param id                - flight id;
     * @param connection        - the current connection to a database. Transmitted from the service module to provide transactions;
     * @return                  - a <i>map</i> of parameters for building the flight object.
     * @throws DAOException
     */
    HashMap<String, String> getFlightInfoById(int id, Connection connection) throws DAOException;

}
