package by.htp.hermanovich.airline.dao.services;

import by.htp.hermanovich.airline.dao.implementations.LuggageDAO;
import by.htp.hermanovich.airline.utils.ConnectorDB;
import by.htp.hermanovich.airline.constants.MessageConstants;
import by.htp.hermanovich.airline.entities.Luggage;
import by.htp.hermanovich.airline.exceptions.DAOException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Description: This class describes actions on the luggage object.
 * This class contains methods that implement work with transaction support.
 *
 * Created by Yauheni Hermanovich on 17.07.2017.
 */
public class LuggageService {
    private static final Logger logger = Logger.getLogger(LuggageService.class);
    private volatile static LuggageService instance;

    public LuggageService() {
    }

    /**
     * Singleton realization with "Double Checked Locking & Volatile" principle for high performance and thread safety.
     *
     * @return      - an instance of the class.
     */
    public static LuggageService getInstance() {
        if (instance == null) {
            synchronized (LuggageService.class) {
                instance = new LuggageService();
            }
        }
        return instance;
    }

    /**
     * This method checks the uniqueness of the luggage. This method implements work with transaction support.
     *
     * @param luggage   - an luggage object with fields will be checked.
     * @return          - boolean value of the condition.
     */
    public boolean isUniqueLuggage(Luggage luggage) throws SQLException {
        boolean isUnique = false;
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            if (LuggageDAO.getInstance().checkUniqueLuggage(luggage.getLuggageType(), connection)) {
                isUnique = true;
            }
            connection.commit();
            logger.info(MessageConstants.TRANSACTION_SUCCEEDED);
        } catch (SQLException | DAOException e) {
            if (connection != null) {
                connection.rollback();
            }
            logger.info(MessageConstants.TRANSACTION_SUCCEEDED);
            throw new SQLException(e);
        }
        return isUnique;
    }

    /**
     * This method registers new luggage for users of application. This method implements work with transaction support.
     *
     * @param luggage
     * @throws SQLException
     */
    public void addLuggage(Luggage luggage) throws SQLException {
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            LuggageDAO.getInstance().add(luggage, connection);
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
     * This method deletes luggage type from database.
     *
     * @param luggage   - a luggage type which will be deleted from database.
     */
    public void deleteLuggage(Luggage luggage) throws SQLException {
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            LuggageDAO.getInstance().deleteById(luggage.getId(), connection);
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
     * This method receives a luggage from database.
     *
     * @param id        - luggage id;
     * @return          - luggage entity from database.
     * @throws SQLException
     */
    public Luggage getLuggageById(int id) throws SQLException {
        Luggage luggage = null;
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            luggage = LuggageDAO.getInstance().getById(id, connection);
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
        return luggage;
    }

    /**
     * This method receives a luggage from database.
     *
     * @param luggage   - luggage type from request.
     * @return          - luggage type from database.
     * @throws SQLException
     */
    public Luggage getLuggageFromDB(Luggage luggage) throws SQLException {
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            luggage = LuggageDAO.getInstance().getByType(luggage.getLuggageType(), connection);
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
        return luggage;
    }

    /**
     * This method receives all luggage types from database.
     *
     * @return      - a list of luggage types received from database.
     * @throws SQLException
     */
    public List<Luggage> showAllLuggageTypes() throws SQLException {
        List<Luggage> luggageTypesFromDB;
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            luggageTypesFromDB = LuggageDAO.getInstance().getAll(connection);
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
        return luggageTypesFromDB;
    }

    /**
     * This method updates luggage object. This method implements work with transaction support.
     *
     * @param luggage          - an luggage which fields will be updated.
     * @throws SQLException
     */
    public void updateLuggage(Luggage luggage) throws SQLException {
        Connection connection = null;
        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            LuggageDAO.getInstance().update(luggage, connection);
            connection.commit();
            logger.info(MessageConstants.TRANSACTION_SUCCEEDED);
        }  catch (SQLException | DAOException e) {
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
