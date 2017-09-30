package by.htp.hermanovich.airline.commands.implementations.aircraft;

import by.htp.hermanovich.airline.commands.BasicCommand;
import by.htp.hermanovich.airline.constants.Parameters;
import by.htp.hermanovich.airline.dao.services.AircraftService;
import by.htp.hermanovich.airline.entities.Aircraft;
import by.htp.hermanovich.airline.managers.ConfigManagerPages;
import by.htp.hermanovich.airline.utils.controllerUtils.RequestParameterIdentifier;
import by.htp.hermanovich.airline.constants.MessageConstants;
import by.htp.hermanovich.airline.constants.PathPageConstants;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * Description: This class describes actions to add information about new aircraft to the database.
 *
 * Created by Yauheni Hermanovich on 18.07.2017.
 */
public class CreateAircraftCommand implements BasicCommand {
    private static final Logger logger = Logger.getLogger(CreateAircraftCommand.class);

    /**
     * This method creates new aircraft in system and adds information about to database.
     *
     * @param request   - request which will be processed.
     * @return          - a page which user will be directed to.
     */
    @Override
    public String execute(HttpServletRequest request) {
        String page = null;
        Aircraft aircraft = RequestParameterIdentifier.getAircraftFromRequest(request);
        try {
            if (AircraftService.getInstance().isUniqueAircraft(aircraft)) {
                AircraftService.getInstance().createAircraft(aircraft);
                request.getSession().setAttribute(Parameters.AIRCRAFT_ADD_SUCCESS, Parameters.TRUE);
                page = ConfigManagerPages.getInstance().getProperty(PathPageConstants.ADMIN_PAGE_PATH);
            } else {
                request.getSession().setAttribute(Parameters.AIRCRAFT_UNIQUE_ERROR, Parameters.TRUE);
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