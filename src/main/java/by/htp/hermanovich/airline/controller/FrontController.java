package by.htp.hermanovich.airline.controller;

import by.htp.hermanovich.airline.utils.controllerUtils.RequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: This class describes main servlet of the application called FrontController
 *
 * Created by Yauheni Hermanovich on 14.07.2017.
 */
@SuppressWarnings("serial")
public class FrontController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestHandler.processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestHandler.processRequest(request, response);
    }
}
