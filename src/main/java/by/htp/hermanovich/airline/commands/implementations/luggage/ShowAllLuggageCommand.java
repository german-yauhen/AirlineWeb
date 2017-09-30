package by.htp.hermanovich.airline.commands.implementations.luggage;

import by.htp.hermanovich.airline.dao.services.LuggageService;
import by.htp.hermanovich.airline.entities.Luggage;
import by.htp.hermanovich.airline.constants.MessageConstants;
import org.apache.log4j.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: This class describes operation to print all luggage types.
 *
 * Created by Yauheni Hermanovich on 18.07.2017.
 */
public class ShowAllLuggageCommand {
    private static final Logger logger = Logger.getLogger(ShowAllLuggageCommand.class);

    /**
     * This method is used as additional command to print all types of luggage on jsp page.
     *
     * @return  - a list of luggage types received from database.
     */
    public static List<Luggage> getAllLuggage() {
        List<Luggage> luggageTypes = new ArrayList<>();
        try {
            luggageTypes = LuggageService.getInstance().showAllLuggageTypes();
        } catch (SQLException e) {
            logger.error(MessageConstants.DATABASE_ACCESS_ERROR);
        }
        return luggageTypes;
    }
}
