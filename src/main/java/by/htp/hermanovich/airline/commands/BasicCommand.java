package by.htp.hermanovich.airline.commands;

import javax.servlet.http.HttpServletRequest;

/**
 * Description: This interface defines one method <i>execute()</i>. All commands will be executed using a simple implementation
 * of the Command pattern. A class which implements this interface describes implementation of this method and provides a business logic
 * for executing of the corresponding command.
 *
 * Created by Yauheni Hermanovich on 14.07.2017.
 */
public interface BasicCommand {

    /**
     * The basic method for all implementations of the interface.
     *
     * @param request       - request which will be processed.
     * @return              - a page which user will be directed to.
     */
    String execute(HttpServletRequest request);
}