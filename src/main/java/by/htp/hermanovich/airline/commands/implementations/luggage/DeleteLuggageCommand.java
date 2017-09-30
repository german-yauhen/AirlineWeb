package by.htp.hermanovich.airline.commands.implementations.luggage;

import by.htp.hermanovich.airline.commands.BasicCommand;
import by.htp.hermanovich.airline.constants.Parameters;
import by.htp.hermanovich.airline.dao.services.LuggageService;
import by.htp.hermanovich.airline.managers.ConfigManagerPages;
import by.htp.hermanovich.airline.utils.controllerUtils.RequestParameterIdentifier;
import by.htp.hermanovich.airline.constants.MessageConstants;
import by.htp.hermanovich.airline.constants.PathPageConstants;
import by.htp.hermanovich.airline.entities.Luggage;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * Description: This class describes operation of deleting a luggage.
 *
 * Created by Yauheni Hermanovich on 17.07.2017.
 */
public class DeleteLuggageCommand implements BasicCommand {
    private static final Logger logger = Logger.getLogger(DeleteLuggageCommand.class);

    /**
     * This method describes delete luggage logic.
     *
     * @param request - request which will be processed.
     * @return - a page which user will be directed to.
     */
    @Override
    public String execute(HttpServletRequest request) {
        String page = null;
        Luggage luggage = RequestParameterIdentifier.getLuggaggeToUpdate(request);
        try {
            luggage = LuggageService.getInstance().getLuggageFromDB(luggage);
            LuggageService.getInstance().deleteLuggage(luggage);
            request.getSession().setAttribute(Parameters.LUGGAGE_DELETE_SUCCESS, Parameters.TRUE);
            page = ConfigManagerPages.getInstance().getProperty(PathPageConstants.ADMIN_PAGE_PATH);
        } catch (SQLException e) {
            request.setAttribute(Parameters.ERROR_DATABASE, MessageConstants.DATABASE_ACCESS_ERROR);
            page = ConfigManagerPages.getInstance().getProperty(PathPageConstants.ERROR_PAGE_PATH);
            logger.error(MessageConstants.DATABASE_ACCESS_ERROR);
        }
        return page;
    }
}
