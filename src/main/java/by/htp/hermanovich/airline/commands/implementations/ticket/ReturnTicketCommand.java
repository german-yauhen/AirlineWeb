package by.htp.hermanovich.airline.commands.implementations.ticket;

import by.htp.hermanovich.airline.commands.BasicCommand;
import by.htp.hermanovich.airline.constants.Parameters;
import by.htp.hermanovich.airline.dao.services.TicketService;
import by.htp.hermanovich.airline.managers.ConfigManagerPages;
import by.htp.hermanovich.airline.utils.controllerUtils.RequestParameterIdentifier;
import by.htp.hermanovich.airline.constants.MessageConstants;
import by.htp.hermanovich.airline.constants.PathPageConstants;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * Description: This class describes actions to return (remove) an user's ticket.
 *
 * Created by Yauheni Hermanovich on 24.07.2017.
 */
public class ReturnTicketCommand implements BasicCommand {
    private static final Logger logger = Logger.getLogger(ReturnTicketCommand.class);

    /**
     * This method removes an user's ticket from the database table.
     *
     * @param request       - request which will be processed.
     * @return              - a page which user will be directed to.
     */
    @Override
    public String execute(HttpServletRequest request) {
        String page = null;
        String ticketNumber = RequestParameterIdentifier.getTicketNumberFromRequest(request);
        try {
            TicketService.getInstance().deleteTicket(ticketNumber);
            request.getSession().setAttribute(Parameters.TICKET_RETURN_SUCCESS, Parameters.TRUE);
            page = ConfigManagerPages.getInstance().getProperty(PathPageConstants.CLIENT_PAGE_PATH);
        } catch (SQLException e) {
            request.setAttribute(Parameters.ERROR_DATABASE, MessageConstants.DATABASE_ACCESS_ERROR);
            page = ConfigManagerPages.getInstance().getProperty(PathPageConstants.ERROR_PAGE_PATH);
            logger.error(MessageConstants.DATABASE_ACCESS_ERROR);
        }
        return page;
    }
}