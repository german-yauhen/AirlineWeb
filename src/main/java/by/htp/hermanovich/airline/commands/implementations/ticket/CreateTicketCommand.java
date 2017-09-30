package by.htp.hermanovich.airline.commands.implementations.ticket;

import by.htp.hermanovich.airline.commands.BasicCommand;
import by.htp.hermanovich.airline.constants.Parameters;
import by.htp.hermanovich.airline.dao.services.TicketService;
import by.htp.hermanovich.airline.entities.Ticket;
import by.htp.hermanovich.airline.managers.ConfigManagerPages;
import by.htp.hermanovich.airline.utils.controllerUtils.RequestParameterIdentifier;
import by.htp.hermanovich.airline.constants.MessageConstants;
import by.htp.hermanovich.airline.constants.PathPageConstants;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Description: This class describes actions to create ticket for client of the airline company.
 *
 * Created by Yauheni Hermanovich on 21.07.2017.
 */
public class CreateTicketCommand implements BasicCommand {
    private static final Logger logger = Logger.getLogger(CreateTicketCommand.class);

    /**
     * The method describes workflow of the actions to create the ticket to the flight.
     *
     * @param request - request which will be processed.
     * @return - a page which user will be directed to.
     */
    @Override
    public String execute(HttpServletRequest request) {
        String page = null;
        HashMap<String, String> ticketInfoMap = RequestParameterIdentifier.getTicketInfoFromRequest(request);
        try {
            Ticket ticket = TicketService.getInstance().createTicket(ticketInfoMap);
            TicketService.getInstance().addTicketToDB(ticket);
            request.getSession().setAttribute(Parameters.TICKET_BOOKING_SUCCESS, Parameters.TRUE);
            page = ConfigManagerPages.getInstance().getProperty(PathPageConstants.FLIGHTS_PAGE_PATH);
        } catch (SQLException e) {
            page = ConfigManagerPages.getInstance().getProperty(PathPageConstants.ERROR_PAGE_PATH);
            request.setAttribute(Parameters.ERROR_DATABASE, MessageConstants.DATABASE_ACCESS_ERROR);
            logger.error(MessageConstants.DATABASE_ACCESS_ERROR);
        }
        return page;
    }
}