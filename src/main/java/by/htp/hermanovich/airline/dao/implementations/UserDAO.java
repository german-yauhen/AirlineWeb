package by.htp.hermanovich.airline.dao.implementations;

import by.htp.hermanovich.airline.constants.MessageConstants;
import by.htp.hermanovich.airline.constants.Parameters;
import by.htp.hermanovich.airline.entities.User;
import by.htp.hermanovich.airline.entities.UserType;
import by.htp.hermanovich.airline.utils.ConnectorDB;
import by.htp.hermanovich.airline.constants.QueriesDB;
import by.htp.hermanovich.airline.dao.ImplUserDAO;
import by.htp.hermanovich.airline.exceptions.DAOException;
import org.apache.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: This class contains implementation of interface methods which works with <i>users</i> database table.
 *
 * Created by Yauheni Hermanovich on 11.07.2017.
 */
public class UserDAO implements ImplUserDAO {
    private static final Logger logger = Logger.getLogger(UserDAO.class);

    private volatile static UserDAO instance;

    private UserDAO() {
    }

    /**
     * Singleton realization with "Double Checked Locking & Volatile" principle for high performance and thread safety.
     *
     * @return      - an instance of the class.
     */
    public static UserDAO getInstance() {
        if (instance == null) {
            synchronized (UserDAO.class) {
                if (instance == null) {
                    instance = new UserDAO();
                }
            }
        }
        return instance;
    }

    /**
     * This method creates and inserts an entity in a database table.
     *
     * @param user          - the current user which has been created.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     */
    @Override
    public void add(User user, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(QueriesDB.ADD_USER_WITHOUT_USERTYPE);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getDocumentNumber());
            statement.setString(4, user.getLogin());
            statement.setString(5, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeStatement(statement);
        }
    }

    /**
     * This method checks the availability of the <i>login</i> and <i>password</i> in the <i>users</i> database table.
     *
     * @param login         - entered <i>login</i> filed of the user.
     * @param password      - entered <i>password</i> field of the user.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return              - boolean value as a result:
     *                          returns "true" if the incoming data correspond to the record of the database table;
     *                          returns "false" if the incoming data do not correspond to the record of the database table.
     */
    @Override
    public boolean isAuthorized(String login, String password, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean isLogined = false;
        try {
            statement = connection.prepareStatement(QueriesDB.CHECK_AUTHORIZATION);
            statement.setString(1, login);
            statement.setString(2, password);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                isLogined = true;
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeResultSet(resultSet);
            ConnectorDB.closeStatement(statement);
        }
        return isLogined;
    }

    /**
     * This method updates an existing record (row) in a database table.
     *
     * @param user          - the current entity of user which will be updated.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     */
    @Override
    public void update(User user, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(QueriesDB.UPDATE_USER_BY_ID);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getDocumentNumber());
            statement.setString(4, user.getLogin());
            statement.setString(5, user.getPassword());
            statement.setString(6, String.valueOf(user.getUserType()));
            statement.setInt(7, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeStatement(statement);
        }
    }

    /**
     * This method deletes an existing record (row) in a database table.
     *
     * @param id            - id number of the current entity which will be deleted.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     */
    @Override
    public void deleteById(int id, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(QueriesDB.DELETE_USER_BY_ID);
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
     * This method reads data from <i>users</i> database table, creates and returns User object according to the entered login.
     *
     * @param login         - entered <i>login</i>.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return              - User object.
     */
    @Override
    public User getByLogin(String login, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = new User();
        try {
            statement = connection.prepareStatement(QueriesDB.GET_USER_BY_LOGIN);
            statement.setString(1, login);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                createUser(resultSet, user);
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeResultSet(resultSet);
            ConnectorDB.closeStatement(statement);
        }
        return user;
    }

    /**
     * This method reads and returns information from a record (row) of a database table.
     *
     * @param id            - id number of the record (row) in the database table..
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return              - an entity from a database table according to the incoming id number.
     */
    @Override
    public User getById(int id, Connection connection) throws DAOException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = new User();
        try {
            statement = connection.prepareStatement(QueriesDB.GET_USER_BY_ID);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                createUser(resultSet, user);
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeResultSet(resultSet);
            ConnectorDB.closeStatement(statement);
        }
        return user;
    }

    /**
     * This method reads and returns information from all records (rows) of a database table.
     *
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return              - list of all entities from a database table.
     */
    @Override
    public List<User> getAll(Connection connection) throws DAOException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<User> users = new ArrayList<User>();
        try {
            statement = connection.prepareStatement(QueriesDB.GET_ALL_USERS);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(createUser(resultSet, new User()));
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeResultSet(resultSet);
            ConnectorDB.closeStatement(statement);
        }
        return users;
    }

    /**
     * An additional method.
     * This method creates entity of User class from data received from ResultSet.
     *
     * @param resultSet     - a database result "row" with required values.
     * @param user          - the entity of User with "null" value for setting corresponding values.
     * @return              - created user with fields.
     * @throws SQLException
     */
    private User createUser(ResultSet resultSet, User user) throws SQLException {
        user.setId(resultSet.getInt(Parameters.ID));
        user.setFirstName(resultSet.getString(Parameters.FIRST_NAME));
        user.setSurname(resultSet.getString(Parameters.SURNAME));
        user.setDocumentNumber(resultSet.getString(Parameters.DOCUMENT_NUMBER_DB));
        user.setLogin(resultSet.getString(Parameters.LOGIN));
        user.setPassword(resultSet.getString(Parameters.PASSWORD));
        switch (resultSet.getString(Parameters.USER_TYPE_DB)) {
            case Parameters.CLIENT:
                user.setUserType(UserType.CLIENT);
                break;
            case Parameters.ADMIN:
                user.setUserType(UserType.ADMIN);
                break;
        }
        return user;
    }

    /**
     * This method check the uniqueness of the user.
     *
     * @param login         - entered <i>login</i>.
     * @param connection    - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return              - boolean value of the condition.
     * @throws DAOException
     */
    @Override
    public boolean checkUniqueUser(String login, Connection connection) throws DAOException {
        boolean isUniqueUser = true;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(QueriesDB.GET_USER_BY_LOGIN);
            statement.setString(1, login);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                isUniqueUser = false;
            }
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectorDB.closeResultSet(resultSet);
            ConnectorDB.closeStatement(statement);
        }
        return isUniqueUser;
    }
}