package by.htp.hermanovich.airline.commands.implementations.airport;

import by.htp.hermanovich.airline.commands.BasicCommand;
import by.htp.hermanovich.airline.constants.Parameters;
import by.htp.hermanovich.airline.managers.ConfigManagerPages;
import by.htp.hermanovich.airline.utils.controllerUtils.RequestParameterIdentifier;
import by.htp.hermanovich.airline.constants.MessageConstants;
import by.htp.hermanovich.airline.constants.PathPageConstants;
import by.htp.hermanovich.airline.dao.services.AirportService;
import by.htp.hermanovich.airline.entities.Airport;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * Description: This class describes actions to add information about new airport to the database.
 *
 * Created by Yauheni Hermanovich on 18.07.2017.
 */
public class CreateAirportCommand implements BasicCommand {
    private static final Logger logger = Logger.getLogger(CreateAirportCommand.class);

    /**
     * This method creates new airport in system and adds information about to database.
     *
     * @param request - request which will be processed.
     * @return - a page which user will be directed to.
     */
    @Override
    public String execute(HttpServletRequest request) {
        String page = null;
        Airport airport = RequestParameterIdentifier.getAirportFromRequest(request);
        try {
            if (AirportService.getInstance().isUniqueAirport(airport)) {
                AirportService.getInstance().createAirport(airport);
                request.getSession().setAttribute(Parameters.AIRPORT_ADD_SUCCESS, Parameters.TRUE);
                page = ConfigManagerPages.getInstance().getProperty(PathPageConstants.ADMIN_PAGE_PATH);
            } else {
                request.getSession().setAttribute(Parameters.AIRPORT_UNIQUE_ERROR, Parameters.TRUE);
                page = ConfigManagerPages.getInstance().getProperty(PathPageConstants.ADMIN_PAGE_PATH);
            }
        } catch (SQLException e) {
            request.setAttribute(Parameters.ERROR_DATABASE, MessageConstants.DATABASE_ACCESS_ERROR);
            page = ConfigManagerPages.getInstance().getProperty(PathPageConstants.ERROR_PAGE_PATH);
            logger.error(MessageConstants.DATABASE_ACCESS_ERROR);
        }
        return page;
    }
}
