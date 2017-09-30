package by.htp.hermanovich.airline.dao;

import by.htp.hermanovich.airline.entities.Aircraft;
import by.htp.hermanovich.airline.exceptions.DAOException;

import java.sql.Connection;

/**
 * Description: This interface describes methods for working with <i>aircrafts</i> database table.
 * extending the capabilities of basic DAO interface.
 *
 * Created by Yauheni Hermanovich on 13.07.2017.
 */
public interface ImplAircraftDAO extends AbstractDAO<Aircraft> {

    /**
     * This method reads data from <i>aircrafts</i> database table, creates and returns Aircraft object
     * according to the entered aircraft code, called hull No.
     *
     * @param aicraftCode   - an entered aircraft code, called hull No.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return              - Aircraft object.
     */
    Aircraft getByCode(String aicraftCode, Connection connection) throws DAOException;

    /**
     * This method deletes an existing record (row) in a database table.
     *
     * @param aircraftCode      - aircraft code which will be used for deleting aircraft.
     * @param connection        - the current connection to a database. Transmitted from the service module to provide transactions.
     * @throws DAOException
     */
    void deleteAircraftByCode(String aircraftCode, Connection connection) throws DAOException;

    /**
     * This method check the uniqueness of the airport.
     *
     * @param aircraftCode  - unique value of the aircraft code.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return              - boolean value of the condition.
     */
    boolean checkUniqueAircraft(String aircraftCode, Connection connection) throws DAOException;

    /**
     * This method updates an existing record (row) in a database table.
     *
     * @param aircraft      - the current entity which will be updated.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     */
    void update(Aircraft aircraft, Connection connection) throws DAOException;
}
