package by.htp.hermanovich.airline.dao.implementations;

import by.htp.hermanovich.airline.entities.Aircraft;
import by.htp.hermanovich.airline.utils.ConnectorDB;
import by.htp.hermanovich.airline.constants.MessageConstants;
import by.htp.hermanovich.airline.constants.QueriesDB;
import by.htp.hermanovich.airline.dao.ImplAircraftDAO;
import by.htp.hermanovich.airline.exceptions.DAOException;
import org.apache.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: This class contains implementation of interface methods which works with <i>aircrafts</i> database table.
 * Created by Yauheni Hermanovich on 13.07.2017.
 */
public class AircraftDAO implements ImplAircraftDAO {
    private static final Logger logger = Logger.getLogger(Aircraft.class);

    private volatile static AircraftDAO instance;

    public AircraftDAO() {
    }

    /**
     * Singleton realization with "Double Checked Locking & Volatile" principle for high performance and thread safety.
     *
     * @return      - an instance of the class.
     */
    public static AircraftDAO getInstance() {
        if (instance == null) {
            synchronized (AircraftDAO.class) {
                if (instance == null) {
                    instance = new AircraftDAO();
                }
            }
        }
        return instance;
    }

    /**
     * This method creates and inserts an entity in a database table.
     *
     * @param aircraft      - the current entity which has been created.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     */
    @Override
    public void add(Aircraft aircraft, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(QueriesDB.ADD_AIRCRAFT);
            statement.setString(1, aircraft.getAircraftCode());
            statement.setString(2, aircraft.getModel());
            statement.executeUpdate();
        } catch (SQLException e) {
            String message = "An error was occurred while executing the request to add the aircraft.";
            logger.error(message, e);
            throw new DAOException(message, e);
        } finally {
            ConnectorDB.closeStatement(statement);
        }
    }

    /**
     * This method updates an existing record (row) in a database table.
     *
     * @param aircraft      - the current entity which will be updated.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     */
    @Override
    public void update(Aircraft aircraft, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(QueriesDB.UPDATE_AIRCRAFT);
            statement.setString(1, aircraft.getModel());
            statement.setString(2, aircraft.getAircraftCode());
            statement.executeUpdate();
        } catch (SQLException e) {
            String message = "An error was occurred while executing the query to update the aircraft.";
            logger.error(message, e);
            throw new DAOException(message, e);
        } finally {
            ConnectorDB.closeStatement(statement);
        }
    }

    /**
     * This method reads and returns information from all records (rows) of a database table.
     *
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return              - list of all entities from a database table.
     */
    @Override
    public List<Aircraft> getAll(Connection connection) throws DAOException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Aircraft> aircrafts = new ArrayList<Aircraft>();
        try {
            statement = connection.prepareStatement(QueriesDB.GET_ALL_AIRCRAFTS);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                aircrafts.add(createAircraft(resultSet, new Aircraft()));
                logger.info(aircrafts.get((aircrafts.size()-1)));
            }
        } catch (SQLException e) {
            String message = "There are no records in the aircrafts database table or one particular record in the database was not found.";
            logger.error(message, e);
            throw new DAOException(message, e);
        } finally {
            ConnectorDB.closeResultSet(resultSet);
            ConnectorDB.closeStatement(statement);
        }
        return aircrafts;
    }

    /**
     * This method reads data from <i>aircrafts</i> database table, creates and returns Aircraft object
     * according to the entered aircraft code, called hull No.
     *
     * @param aicraftCode - an entered aircraft code, called hull No.
     * @param connection  - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return - Aircraft object.
     */
    @Override
    public Aircraft getByCode(String aicraftCode, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Aircraft aircraft = new Aircraft();
        try {
            statement = connection.prepareStatement(QueriesDB.GET_AIRCRAFT_BY_CODE);
            statement.setString(1, aicraftCode);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                createAircraft(resultSet, aircraft);
            }
        } catch (SQLException e) {
            String message = "The record in the database was not found.";
            logger.error(message, e);
            throw new DAOException(message, e);
        } finally {
            ConnectorDB.closeResultSet(resultSet);
            ConnectorDB.closeStatement(statement);
        }
        return aircraft;
    }

    /**
     * This method deletes an existing record (row) in a database table.
     *
     * @param aircraftCode      - aircraft code which will be used for deleting aircraft.
     * @param connection        - the current connection to a database. Transmitted from the service module to provide transactions.
     * @throws DAOException
     */
    @Override
    public void deleteAircraftByCode(String aircraftCode, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(QueriesDB.DELETE_AIRCRAFT_BY_CODE);
            statement.setString(1, aircraftCode);
            statement.executeUpdate();
        } catch (SQLException e) {
            String message = "An error was occurred while executing the query to delete the aircraft from database.";
            logger.error(message, e);
            throw new DAOException(message, e);
        } finally {
            ConnectorDB.closeStatement(statement);
        }
    }

    /**
     * This method check the uniqueness of the airport.
     *
     * @param aircraftCode  - unique value of the aircraft code.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return              - boolean value of the condition.
     */
    @Override
    public boolean checkUniqueAircraft(String aircraftCode, Connection connection) throws DAOException {
        boolean isUniqueAircraft = true;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(QueriesDB.GET_AIRCRAFT_BY_CODE);
            statement.setString(1, aircraftCode);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                isUniqueAircraft = false;
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeResultSet(resultSet);
            ConnectorDB.closeStatement(statement);
        }
        return isUniqueAircraft;
    }

    /**
     * An additional method.
     * This method creates entity of User class from data received from ResultSet.
     *
     * @param resultSet         - a database result "row" with required values.
     * @param aircraft          - the entity of Aircraft with "null" value for setting corresponding values.
     * @return                  - created aircraft with fields.
     * @throws SQLException
     */
    private Aircraft createAircraft(ResultSet resultSet, Aircraft aircraft) throws SQLException {
        aircraft.setAircraftCode(resultSet.getString("aircraft_code"));
        aircraft.setModel(resultSet.getString("model"));
        return aircraft;
    }
}
