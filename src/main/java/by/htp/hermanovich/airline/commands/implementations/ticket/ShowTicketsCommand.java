package by.htp.hermanovich.airline.commands.implementations.ticket;

import by.htp.hermanovich.airline.commands.BasicCommand;
import by.htp.hermanovich.airline.entities.User;
import by.htp.hermanovich.airline.managers.ConfigManagerPages;
import by.htp.hermanovich.airline.utils.controllerUtils.RequestParameterIdentifier;
import by.htp.hermanovich.airline.constants.MessageConstants;
import by.htp.hermanovich.airline.constants.Parameters;
import by.htp.hermanovich.airline.constants.PathPageConstants;
import by.htp.hermanovich.airline.dao.services.TicketService;
import by.htp.hermanovich.airline.entities.Ticket;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

/**
 * Description: This class describes actions to show all user's tickets to the flights.
 *
 * Created by Yauheni Hermanovich on 24.07.2017.
 */
public class ShowTicketsCommand implements BasicCommand {
    private static final Logger logger = Logger.getLogger(ShowTicketsCommand.class);

    /**
     * This method to prints all user's tickets to the different flights on the user's page.
     *
     * @param request       - request which will be processed.
     * @return              - a page which user will be directed to.
     */
    @Override
    public String execute(HttpServletRequest request) {
        String page = null;
        User user = RequestParameterIdentifier.getUserFromSession(request);
        try {
            List<Ticket> ticketsList = TicketService.getInstance().getTicketsFromDB(user, request);
            request.getSession().setAttribute(Parameters.TICKETS_LIST, ticketsList);
            page = ConfigManagerPages.getInstance().getProperty(PathPageConstants.CLIENT_FLIGHTS_PAGE_PATH);
        } catch (SQLException e) {
            page = ConfigManagerPages.getInstance().getProperty(PathPageConstants.ERROR_PAGE_PATH);
            request.setAttribute(Parameters.ERROR_DATABASE, MessageConstants.DATABASE_ACCESS_ERROR);
            logger.error(MessageConstants.DATABASE_ACCESS_ERROR);
        }
        return page;
    }
}