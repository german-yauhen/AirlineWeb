package by.htp.hermanovich.airline.commands.implementations.user;

import by.htp.hermanovich.airline.commands.BasicCommand;
import by.htp.hermanovich.airline.constants.Parameters;
import by.htp.hermanovich.airline.managers.ConfigManagerPages;
import by.htp.hermanovich.airline.utils.controllerUtils.RequestParameterIdentifier;
import by.htp.hermanovich.airline.constants.PathPageConstants;
import javax.servlet.http.HttpServletRequest;

/**
 * Description: This class describe action to "back" command.
 *
 * Created by Yauheni Hermanovich on 17.07.2017.
 */
public class BackCommand implements BasicCommand {

    /**
     * This method describes action to "back" command.
     *
     * @param request   - request which will be processed.
     * @return          - a page which user will be directed to.
     */
    @Override
    public String execute(HttpServletRequest request) {
        String pageFromRequest = RequestParameterIdentifier.getPageFromRequest(request);
        String page = null;
        if (pageFromRequest != null && pageFromRequest.equals(Parameters.LOGIN)) {
            page = ConfigManagerPages.getInstance().getProperty(PathPageConstants.LOGIN_PAGE_PATH);
        } else if (pageFromRequest != null && pageFromRequest.equals(Parameters.CLIENT)){
            page = ConfigManagerPages.getInstance().getProperty(PathPageConstants.CLIENT_PAGE_PATH);
        } else if (pageFromRequest != null && pageFromRequest.equals(Parameters.ADMIN)) {
            page = ConfigManagerPages.getInstance().getProperty(PathPageConstants.ADMIN_PAGE_PATH);
        }
        return page;
    }
}