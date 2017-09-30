package by.htp.hermanovich.airline.dao.services;

import by.htp.hermanovich.airline.constants.MessageConstants;
import by.htp.hermanovich.airline.dao.implementations.AirportDAO;
import by.htp.hermanovich.airline.exceptions.DAOException;
import by.htp.hermanovich.airline.utils.ConnectorDB;
import by.htp.hermanovich.airline.entities.Airport;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Description: This class describes actions on the airport object.
 * This class contains methods that implement work with transaction support.
 *
 * Created by Yauheni Hermanovich on 18.07.2017.
 */
public class AirportService {
    private final static Logger logger = Logger.getLogger(AirportService.class);

    private volatile static AirportService instance;

    public AirportService() {
    }

    /**
     * Singleton realization with "Double Checked Locking & Volatile" principle for high performance and thread safety.
     *
     * @return      - an instance of the class.
     */
    public static AirportService getInstance() {
        if (instance == null) {
            synchronized (AirportService.class) {
                if (instance == null) {
                    return instance = new AirportService();
                }
            }
        }
        return instance;
    }

    /**
     * This method checks the uniqueness of the airport. This method implements work with transaction support.
     *
     * @param airport   - an airport object with fields will be checked.
     * @return          - boolean value of the condition.
     * @throws SQLException
     */
    public boolean isUniqueAirport(Airport airport) throws SQLException {
        boolean isUnique = false;
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            if (AirportDAO.getInstance().checkUniqueAirport(airport.getAirportCode(), connection)) {
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
     * This method registers new airport of application. This method implements work with transaction support.
     *
     * @param airport   - a new airport object will be registered.
     * @throws SQLException
     */
    public void createAirport(Airport airport) throws SQLException {
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            AirportDAO.getInstance().add(airport, connection);
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
     * This method receives all airports from database. This method implements work with transaction support.
     *
     * @return          - a list of airports received from database.
     * @throws SQLException
     */
    public List<Airport> showAllAirports() throws SQLException {
        List<Airport> airportList;
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            airportList = AirportDAO.getInstance().getAll(connection);
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
        return airportList;
    }

    /**
     * This method receives an entity of an airport from the database. This method implements work with transaction support.
     *
     * @param airportCode               - an airportCode for query to database to find an aircraft in the database.
     * @return                          - an entity of airport from database.
     * @throws SQLException
     */
    public Airport getAirportFromDB(String airportCode) throws SQLException {
        Airport airport;
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            airport = AirportDAO.getInstance().getByCode(airportCode, connection);
            connection.commit();
        } catch (SQLException | DAOException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.error(MessageConstants.TRANSACTION_FAILED);
            throw new SQLException(e);
        } finally {
            ConnectorDB.closeConnection(connection);
        }
        return airport;
    }
}