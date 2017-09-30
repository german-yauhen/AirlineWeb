package by.htp.hermanovich.airline.dao.implementations;

import by.htp.hermanovich.airline.constants.Parameters;
import by.htp.hermanovich.airline.dao.ImplAirportDAO;
import by.htp.hermanovich.airline.utils.ConnectorDB;
import by.htp.hermanovich.airline.constants.MessageConstants;
import by.htp.hermanovich.airline.constants.QueriesDB;
import by.htp.hermanovich.airline.entities.Airport;
import by.htp.hermanovich.airline.exceptions.DAOException;
import org.apache.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: This class contains implementation of interface methods which works with <i>airports</i> database table.
 * Created by Yauheni Hermanovich on 13.07.2017.
 */
public class AirportDAO implements ImplAirportDAO {
    private static final Logger logger = Logger.getLogger(AirportDAO.class);

    private volatile static AirportDAO instance;

    private AirportDAO() {
    }

    /**
     * Singleton realization with "Double Checked Locking & Volatile" principle for high performance and thread safety.
     *
     * @return  - an instance of the class.
     */
    public static AirportDAO getInstance() {
        if (instance == null) {
            synchronized (AirportDAO.class) {
                if (instance == null) {
                    instance = new AirportDAO();
                }
            }
        }
        return instance;
    }

    /**
     * This method creates and inserts an entity in a database table.
     *
     * @param airport       - the current entity which has been created.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     */
    @Override
    public void add(Airport airport, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(QueriesDB.ADD_AIRPORT);
            statement.setString(1, String.valueOf(airport.getAirportCode()).toUpperCase());
            statement.setString(2, airport.getAirportName().toUpperCase());
            statement.setString(3, airport.getCity().toUpperCase());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeStatement(statement);
        }
    }

    /**
     * This method reads data from <i>airportss</i> database table, creates and returns Airport object
     * according to the entered airport code.
     *
     * @param airportCode   - an entered airport code.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return              - Airport object.
     */
    @Override
    public Airport getByCode(String airportCode, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Airport airport = new Airport();
        try {
            statement = connection.prepareStatement(QueriesDB.GET_AIRPORT_BY_CODE);
            statement.setString(1, airportCode);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                createAirport(resultSet, airport);
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        }
        return airport;
    }

    /**
     * This method reads and returns information from all records (rows) of a database table.
     *
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return              - list of all entities from a database table.
     */
    @Override
    public List<Airport> getAll(Connection connection) throws DAOException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Airport> airports = new ArrayList<Airport>();
        try {
            statement = connection.prepareStatement(QueriesDB.GET_ALL_AIRPORTS);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                airports.add(createAirport(resultSet, new Airport()));
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeResultSet(resultSet);
            ConnectorDB.closeStatement(statement);
        }
        return airports;
    }

    /**
     * An additional method.
     * This method creates entity of Airport class from data received from ResultSet.
     *
     * @param resultSet     - a database result "row" with required values.
     * @param airport       - the entity of Airport with "null" value for setting corresponding values.
     * @return              - created user with fields.
     * @throws SQLException
     */
    private Airport createAirport(ResultSet resultSet, Airport airport) throws SQLException {
        airport.setAirportCode(resultSet.getString(Parameters.AIRPORT_CODE_DB));
        airport.setAirportName(resultSet.getString(Parameters.AIRPORT_NAME_DB));
        airport.setCity(resultSet.getString(Parameters.CITY_DB));
        return airport;
    }

    /**
     * This method updates an existing record (row) in a database table.
     *
     * @param airport       - the current entity which will be updated.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     */
    @Override
    public void update(Airport airport, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(QueriesDB.UPDATE_AIRPORT);
            statement.setString(1, airport.getAirportName());
            statement.setString(2, airport.getAirportName());
            statement.setString(3, airport.getAirportCode());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeStatement(statement);
        }
    }

    /**
     * This method deletes an existing airport in a database table according to the airport code.
     *
     * @param airportCode   - an entered airport code.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     */
    @Override
    public void deleteByCode(String airportCode, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(QueriesDB.DELETE_AIRPORT_BY_CODE);
            statement.setString(1, airportCode);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeStatement(statement);
        }
    }

    /**
     * This method check the uniqueness of the airport.
     *
     * @param airportCode   - unique value of the airport code.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return              - boolean value of the condition.
     */
    public boolean checkUniqueAirport(String airportCode, Connection connection) throws DAOException {
        boolean isUniqueAirport = true;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(QueriesDB.GET_AIRPORT_BY_CODE);
            statement.setString(1, airportCode);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                isUniqueAirport = false;
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeResultSet(resultSet);
            ConnectorDB.closeStatement(statement);
        }
        return isUniqueAirport;
    }
}
