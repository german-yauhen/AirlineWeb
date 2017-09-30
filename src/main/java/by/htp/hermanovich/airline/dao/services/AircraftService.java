package by.htp.hermanovich.airline.dao.services;

import by.htp.hermanovich.airline.entities.Aircraft;
import by.htp.hermanovich.airline.utils.ConnectorDB;
import by.htp.hermanovich.airline.constants.MessageConstants;
import by.htp.hermanovich.airline.dao.implementations.AircraftDAO;
import by.htp.hermanovich.airline.exceptions.DAOException;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Description: This class describes actions on the aircraft object.
 *
 * This class contains methods that implement work with transaction support.
 *
 * Created by Yauheni Hermanovich on 18.07.2017.
 */
public class AircraftService {
    private final static Logger logger = Logger.getLogger(AircraftService.class);

    private volatile static AircraftService instance;

    public AircraftService() {
    }

    /**
     * Singleton realization with "Double Checked Locking & Volatile" principle for high performance and thread safety.
     *
     * @return      - an instance of the class.
     */
    public static AircraftService getInstance() {
        if (instance == null) {
            synchronized (AircraftService.class) {
                if (instance == null) {
                    return instance = new AircraftService();
                }
            }
        }
        return instance;
    }

    /**
     * This method checks the uniqueness of the aircraft. This method implements work with transaction support.
     *
     * @param aircraft      - an airport object with fields will be checked.
     * @return              - boolean value of the condition.
     * @throws SQLException
     */
    public boolean isUniqueAircraft(Aircraft aircraft) throws SQLException {
        boolean isUnique = false;
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            if (AircraftDAO.getInstance().checkUniqueAircraft(aircraft.getAircraftCode(), connection)) {
                isUnique = true;
            }
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
        return isUnique;
    }

    /**
     * This method registers new aircraft of the application. This method implements work with transaction support.
     *
     * @param aircraft   - a new airport object will be registered.
     * @throws SQLException
     */
    public void createAircraft(Aircraft aircraft) throws SQLException {
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            AircraftDAO.getInstance().add(aircraft, connection);
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
     * This method receives all aircrafts from database. This method implements work with transaction support.
     *
     * @return          - a list of aircrafts from the database.
     * @throws SQLException
     */
    public List<Aircraft> showAllAircrafts() throws SQLException {
        List<Aircraft> aircraftList;
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            aircraftList = AircraftDAO.getInstance().getAll(connection);
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
        return aircraftList;
    }

    /**
     * This method receive an aircraft from database by code. This method implements work with transaction support.
     *
     * @param aircraftCode      - an aircraftCode for query to database to find an aircraft in the database.
     * @return                  - an entity of aircraft from database.
     */
    public Aircraft getAircraftFromDB(String aircraftCode) throws SQLException {
        Aircraft aircraftFromDB;
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            aircraftFromDB = AircraftDAO.getInstance().getByCode(aircraftCode, connection);
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
        return aircraftFromDB;
    }
}