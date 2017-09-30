package by.htp.hermanovich.airline.commands.implementations.aircraft;

import by.htp.hermanovich.airline.constants.MessageConstants;
import by.htp.hermanovich.airline.dao.services.AircraftService;
import by.htp.hermanovich.airline.entities.Aircraft;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: This class describes operation to print all aircrafts.
 *
 * Created by Yauheni Hermanovich on 18.07.2017.
 */
public class ShowAllAircraftsCommand {
    private static final Logger logger = Logger.getLogger(ShowAllAircraftsCommand.class);

    /**
     * This method is used as additional command to print all aircrafts on jsp page.
     *
     * @return  - a list of aircrafts received from database.
     */
    public static List<Aircraft> getAllAircrafts() {
        List<Aircraft> aircraftList = new ArrayList<>();
        try {
            aircraftList = AircraftService.getInstance().showAllAircrafts();
        } catch (SQLException e) {
            logger.error(MessageConstants.DATABASE_ACCESS_ERROR);
        }
        return aircraftList;
    }
}
