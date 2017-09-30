package by.htp.hermanovich.airline.commands.implementations.user;

import by.htp.hermanovich.airline.commands.BasicCommand;
import by.htp.hermanovich.airline.constants.MessageConstants;
import by.htp.hermanovich.airline.managers.ConfigManagerPages;
import by.htp.hermanovich.airline.constants.PathPageConstants;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 * Description: This class describes actions of logout logic.
 * Created by Yauheni Hermanovich on 14.07.2017.
 */
public class LogoutCommand implements BasicCommand {
    private final static Logger logger = Logger.getLogger(LogoutCommand.class);

    /**
     * This method describes the logon logic.
     *
     * @param request - request which will be processed.
     * @return - a page which user will be directed to.
     */
    @Override
    public String execute(HttpServletRequest request) {
        request.getSession().invalidate();
        logger.info(MessageConstants.SUCCESS_LOGOUT);
        return ConfigManagerPages.getInstance().getProperty(PathPageConstants.INDEX_PAGE_PATH);
    }
}