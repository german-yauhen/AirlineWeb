package by.htp.hermanovich.airline.commands.implementations.airport;

import by.htp.hermanovich.airline.constants.MessageConstants;
import by.htp.hermanovich.airline.dao.services.AirportService;
import by.htp.hermanovich.airline.entities.Airport;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: This class describes operation to print all airports.
 *
 * Created by Yauheni Hermanovich on 18.07.2017.
 */
public class ShowAllAirportsCommand {
    private static final Logger logger = Logger.getLogger(ShowAllAirportsCommand.class);

    /**
     * This method is used as additional command to print all airports on jsp page.
     *
     * @return  - a list of airports received from database.
     */
    public static List<Airport> getAllAirports() {
        List<Airport> airportList = new ArrayList<>();
        try {
            airportList = AirportService.getInstance().showAllAirports();
        } catch (SQLException e) {
            logger.error(MessageConstants.DATABASE_ACCESS_ERROR);
        }
        return airportList;
    }
}
