package by.htp.hermanovich.airline.commands.implementations;

import by.htp.hermanovich.airline.commands.BasicCommand;
import by.htp.hermanovich.airline.managers.ConfigManagerPages;
import by.htp.hermanovich.airline.constants.PathPageConstants;

import javax.servlet.http.HttpServletRequest;

/**
 * Description: This class defines action in the case if servlet receives a message without a command.
 *
 * Created by Yauheni Hermanovich on 14.07.2017.
 */
public class DefaultCommand implements BasicCommand {

    /**
     * The method describes action in the case if servlet receives a message without a command.
     * In this case user will be redirected to the main page of the application index.jsp,
     * which redirects user to the login page login.jsp.
     *
     * @param request   - request which will be processed.
     * @return          - a page which user will be directed to, in this case it will be the index.jsp.
     */
    @Override
    public String execute(HttpServletRequest request) {
        return ConfigManagerPages.getInstance().getProperty(PathPageConstants.INDEX_PAGE_PATH);
    }
}