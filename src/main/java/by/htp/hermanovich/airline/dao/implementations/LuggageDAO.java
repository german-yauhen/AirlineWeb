package by.htp.hermanovich.airline.dao.implementations;

import by.htp.hermanovich.airline.constants.MessageConstants;
import by.htp.hermanovich.airline.constants.Parameters;
import by.htp.hermanovich.airline.constants.QueriesDB;
import by.htp.hermanovich.airline.dao.ImplLuggageDAO;
import by.htp.hermanovich.airline.entities.Luggage;
import by.htp.hermanovich.airline.exceptions.DAOException;
import by.htp.hermanovich.airline.utils.ConnectorDB;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: This class contains implementation of interface methods which works with <i>luggage</i> database table.
 *
 * Created by Yauheni Hermanovich on 13.07.2017.
 */
public class LuggageDAO implements ImplLuggageDAO {
    private static final Logger logger = Logger.getLogger(LuggageDAO.class);

    private static LuggageDAO instance;

    public LuggageDAO() {
    }

    /**
     * Singleton realization with "Double Checked Locking & Volatile" principle for high performance and thread safety.
     *
     * @return      - an instance of the class.
     */
    public static LuggageDAO getInstance() {
        if (instance == null) {
            synchronized (LuggageDAO.class) {
                if (instance == null) {
                    instance = new LuggageDAO();
                }
            }
        }
        return instance;
    }

    /**
     * This method creates and inserts an entity in a database table.
     *
     * @param luggage       - the current entity which has been created.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     */
    @Override
    public void add(Luggage luggage, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(QueriesDB.ADD_LUGGAGE);
            statement.setString(1, luggage.getLuggageType());
            statement.setFloat(2, luggage.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeStatement(statement);
        }
    }

    /**
     * This method updates an existing record (row) in a database table.
     *
     * @param luggage       - the current entity which will be updated.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     */
    @Override
    public void update(Luggage luggage, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(QueriesDB.UPDATE_LUGGAGE);
            statement.setString(1, luggage.getLuggageType());
            statement.setFloat(2, luggage.getPrice());
            statement.setInt(3, luggage.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeStatement(statement);
        }
    }

    /**
     * This method reads data from <i>luggage</i> database table, creates and returns Luggage object according to the entered id.
     *
     * @param id                - entered <i>id</i> of luggage type.
     * @param connection        - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return                  - Luggage object.
     * @throws DAOException
     */
    public Luggage getById(int id, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Luggage luggage = new Luggage();
        try {
            statement = connection.prepareStatement(QueriesDB.GET_LUGGAGE_BY_ID);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                createLuggage(resultSet, luggage);
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeResultSet(resultSet);
            ConnectorDB.closeStatement(statement);
        }
        return luggage;
    }

    /**
     * This method reads data from <i>luggage</i> database table, creates and returns Luggage object according to the entered luggage type.
     *
     * @param luggageType   - entered <i>luggage type</i>.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return              - Luggage object.
     */
    @Override
    public Luggage getByType(String luggageType, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        ResultSet resultSet =  null;
        Luggage luggage = new Luggage();
        try {
            statement = connection.prepareStatement(QueriesDB.GET_LUGGAGE_BY_TYPE);
            statement.setString(1, luggageType);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                createLuggage(resultSet, luggage);
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeResultSet(resultSet);
            ConnectorDB.closeStatement(statement);
        }
        return luggage;
    }

    /**
     * This method reads and returns information from all records (rows) of a database table.
     *
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return              - list of all entities from a database table.
     */
    @Override
    public List<Luggage> getAll(Connection connection) throws DAOException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Luggage> luggageTypes = new ArrayList<Luggage>();
        try {
            statement = connection.prepareStatement(QueriesDB.GET_ALL_LUGGAGE_TYPES);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                luggageTypes.add(createLuggage(resultSet, new Luggage()));
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeResultSet(resultSet);
            ConnectorDB.closeStatement(statement);
        }
        return luggageTypes;
    }

    /**
     * This method deletes an existing record (row) in a database table.
     *
     * @param id         - entered <i>id</i>.
     * @param connection - the current connection to a database. Transmitted from the service module to provide transactions.
     */
    @Override
    public void deleteById(int id, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(QueriesDB.DELETE_LUGGAGE_BY_ID);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeStatement(statement);
        }
    }

    /**
     * This method check the uniqueness of the user.
     *
     * @param luggageType   - entered <i>type of the luggage</i>.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return              - boolean value of the condition:
     *                          "false" if the incoming data correspond to the record of the database table;
     *                          "true" if the incoming data do not correspond to the record of the database table.
     * @throws DAOException
     */
    @Override
    public boolean checkUniqueLuggage(String luggageType, Connection connection) throws DAOException {
        boolean isUniqueLuggage = true;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(QueriesDB.GET_LUGGAGE_BY_TYPE);
            statement.setString(1, luggageType);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                isUniqueLuggage = false;
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeResultSet(resultSet);
            ConnectorDB.closeStatement(statement);
        }
        return isUniqueLuggage;
    }

    /**
     * An additional method.
     * This method creates entity of Luggage class from data received from ResultSet.
     *
     * @param resultSet     - a database result "row" with required values.
     * @param luggage       - the entity of Luggage class with "null" value for setting corresponding values.
     * @return              - created luggage with fields.
     * @throws SQLException
     */
    private Luggage createLuggage(ResultSet resultSet, Luggage luggage) throws SQLException {
        luggage.setId(resultSet.getInt(Parameters.ID));
        luggage.setLuggageType(resultSet.getString(Parameters.LUGGAGE_TYPE_DB));
        luggage.setPrice(resultSet.getFloat(Parameters.PRICE_DB));
        return luggage;
    }
}
