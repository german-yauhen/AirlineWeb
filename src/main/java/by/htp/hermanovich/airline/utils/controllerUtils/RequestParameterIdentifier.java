package by.htp.hermanovich.airline.utils.controllerUtils;

import by.htp.hermanovich.airline.commands.factory.CommandType;
import by.htp.hermanovich.airline.constants.Parameters;
import by.htp.hermanovich.airline.entities.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Description: This class identifies request parameters.
 *
 * Created by Yauheni Hermanovich on 14.07.2017.
 */
public class RequestParameterIdentifier {

    /**
     * This method identifies a type command from request.
     *
     * @param request   - object of request.
     * @return          - type of command that will be executed.
     */
    public static CommandType getCommandFromRequest(HttpServletRequest request) {
        CommandType commandType = CommandType.DEFAULT;
        if (request.getParameter(Parameters.COMMAND) != null
                && !request.getParameter(Parameters.COMMAND).isEmpty()) {
            commandType = CommandType.valueOf(request.getParameter(Parameters.COMMAND).toUpperCase());
        }
        return commandType;
    }

    /**
     * This method defines user type from request.
     *
     * @param request   - object of request.
     * @return          - user type; it can be client or admin.
     */
    public static UserType getUserTypeFromRequest(HttpServletRequest request) {
        String userType = String.valueOf(request.getSession().getAttribute(Parameters.USER_TYPE));
        if (!userType.isEmpty() && userType.equals(Parameters.CLIENT)) {
            return UserType.CLIENT;
        } else {
            return UserType.ADMIN;
        }
    }

    /**
     * This method receives users login and password from an object of request
     * and constructs an user with the corresponding fields.
     *
     * @param request   - object of request.
     * @return          - an user with the following fields: login and password.
     */
    public static User getUserLoginPasswordFromRequest(HttpServletRequest request) {
        User user = new User();
        String login = request.getParameter(Parameters.LOGIN);
        String password = request.getParameter(Parameters.PASSWORD);
        if (login != null && !login.isEmpty()) {
            user.setLogin(login);
        }
        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }
        return user;
    }

    /**
     * This method updates user's fields according to the requests parameters.
     * This method receives first name, surname, document number from request
     * and sets this values to the corresponding fields of the user which is passed
     * to the method as parameter.
     *
     * @param user      - user, which fields will be updated.
     * @param request   - request object with necessaries parameters.
     * @return          - user object with updated fields.
     */
    public static User updateUserFromRequest(User user, HttpServletRequest request) {
        String firstName = request.getParameter(Parameters.FIRST_NAME);
        String surname = request.getParameter(Parameters.SURNAME);
        String documentNumber = request.getParameter(Parameters.DOCUMENT_NUMBER);
        String login = request.getParameter(Parameters.LOGIN);
        if ( (firstName != null && !firstName.isEmpty())
                & (surname != null && !surname.isEmpty())
                & (documentNumber != null && !documentNumber.isEmpty())
                & (login != null && !login.isEmpty())) {
            user.setFirstName(firstName);
            user.setSurname(surname);
            user.setDocumentNumber(documentNumber);
        }
        return user;
    }

    /**
     * This method creates a new user of application. This method is used during registration operation.
     * This method receives first name, surname, document number, login and password from request
     * and sets this values to the corresponding fields of the user.
     *
     * @param request   - an object of request with necessary parameters.
     * @return          - user object with updated fields.
     */
    public static User getUserFromRequest(HttpServletRequest request) {
        User user = new User();
        String firstName = request.getParameter(Parameters.FIRST_NAME);
        String surname = request.getParameter(Parameters.SURNAME);
        String documentNumber = request.getParameter(Parameters.DOCUMENT_NUMBER);
        String login = request.getParameter(Parameters.LOGIN);
        String password = request.getParameter(Parameters.PASSWORD);
        if ( (firstName != null && !firstName.isEmpty())
                & (surname != null && !surname.isEmpty())
                & (documentNumber != null && !documentNumber.isEmpty())
                & (login != null && !login.isEmpty())
                & (password != null && !password.isEmpty()) ) {
            user.setFirstName(firstName);
            user.setSurname(surname);
            user.setDocumentNumber(documentNumber);
            user.setLogin(login);
            user.setPassword(password);
        }
        return user;
    }

    /**
     * This method receives a name of the page from the request.
     *
     * @param request   - request object with necessaries parameters.
     * @return          - a name of the page from request.
     */
    public static String getPageFromRequest(HttpServletRequest request) {
        String pageFromRequest = request.getSession().getAttribute(Parameters.BACK_PAGE).toString();
        request.getSession().removeAttribute(Parameters.BACK_PAGE);
        if (pageFromRequest != null && !pageFromRequest.isEmpty()) {
            return pageFromRequest;
        } else {
            return null;
        }
    }

    /**
     * This method checks if the fields of the form are filled.
     *
     * @param request   - an object of request with necessary parameters.
     * @return          - boolean value of the condition.
     */
    public static boolean areFieldsFilled(HttpServletRequest request) {
        if ( !request.getParameter(Parameters.LOGIN).isEmpty()
                && !request.getParameter(Parameters.PASSWORD).isEmpty()
                && !request.getParameter(Parameters.FIRST_NAME).isEmpty()
                && !request.getParameter(Parameters.SURNAME).isEmpty()
                && !request.getParameter(Parameters.DOCUMENT_NUMBER).isEmpty() ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method creates a new type of luggage for clients of airline company.
     *
     * @param request   - an object of request with necessary parameters.
     * @return          - a new type of luggage.
     */
    public static Luggage getLuggageFromRequest(HttpServletRequest request) {
        Luggage luggage = new Luggage();
        String luggageType = request.getParameter(Parameters.LUGGAGE_TYPE);
        String luggagePrice = request.getParameter(Parameters.LUGGAGE_PRICE);
        if (luggageType != null && !luggageType.isEmpty()) {
            luggage.setLuggageType(luggageType);
        }
        if (luggagePrice != null && !luggagePrice.isEmpty()) {
            luggage.setPrice(Float.parseFloat(luggagePrice));
        }
        return luggage;
    }

    /**
     * This method identifies a kind of a luggage from request that will be deleted.
     *
     * @param request   - an object of request with necessary parameters.
     * @return          - a type of luggage from request that will be deleted.
     */
    public static Luggage getLuggaggeToUpdate(HttpServletRequest request) {
        Luggage luggage = new Luggage();
        String luggageType = request.getParameter(Parameters.LUGGAGE_TYPE_TO_UPDATE);
        if (luggageType != null && !luggageType.isEmpty()) {
            luggage.setLuggageType(luggageType);
        } else {
            luggageType = String.valueOf(request.getSession().getAttribute(Parameters.LUGGAGE_TYPE_TO_UPDATE));
            luggage.setLuggageType(luggageType);
            request.getSession().removeAttribute(Parameters.LUGGAGE_TYPE_TO_UPDATE);
        }
        return luggage;
    }

    /**
     * This method updates the fields of current luggage type according to the requests parameters.
     * This method receives a type of a luggage and price from request and sets this values
     * to the corresponding fields of the luggage which is passed to the method as parameter.
     *
     * @param request   - an object of request with necessary parameters.
     * @return          - a type of luggage from request that will be updated.
     */
    public static Luggage updateLuggageFromRequest(Luggage luggage, HttpServletRequest request) {
        String luggagePrice = request.getParameter(Parameters.LUGGAGE_PRICE);
        if (luggagePrice != null && !luggagePrice.isEmpty()) {
            luggage.setPrice(Float.parseFloat(luggagePrice));
        }
        return luggage;
    }

    /**
     * This method creates a new entity of airport from request parameters.
     *
     * @param request   - an object of request with necessary parameters.
     * @return          - a new entity of airport.
     */
    public static Airport getAirportFromRequest(HttpServletRequest request) {
        Airport airport = new Airport();
        String airportCode = request.getParameter(Parameters.AIRPORT_CODE);
        String airportName = request.getParameter(Parameters.AIRPORT_NAME);
        String airportCity = request.getParameter(Parameters.AIRPORT_CITY);
        if ( airportCode != null && !airportCode.isEmpty()
                && airportName != null && !airportName.isEmpty()
                && airportCity != null && !airportCity.isEmpty() ) {
            airport.setAirportCode(airportCode.toUpperCase());
            airport.setAirportName(airportName.toUpperCase());
            airport.setCity(airportCity.toUpperCase());
        }
        return airport;
    }

    /**
     * This method creates a new entity of aircraft from request parameters.
     *
     * @param request   - an object of request with necessary parameters.
     * @return          - a new entity of aircraft.
     */
    public static Aircraft getAircraftFromRequest(HttpServletRequest request) {
        Aircraft aircraft = new Aircraft();
        String aircraftCode = request.getParameter(Parameters.AIRCRAFT_CODE);
        String aircraftModel = request.getParameter(Parameters.AIRCRAFT_MODEL);
        if (aircraftCode != null && !aircraftCode.isEmpty()
                && aircraftModel != null && !aircraftModel.isEmpty()) {
            aircraft.setAircraftCode(aircraftCode);
            aircraft.setModel(aircraftModel);
        }
        return aircraft;
    }

    /**
     * This method fills a <i>map</i> of parameters of the flight with values from the request.
     *
     * @param request   - an object of request with necessary parameters.
     * @return          - a map with parameters.
     */
    public static HashMap<String,String> getFlightInfoFromRequest(HttpServletRequest request) {
        HashMap<String, String> flightInfoFromRequest = new HashMap<>();
        String aircraftForFlight = request.getParameter(Parameters.AIRCRAFT_FOR_FLIGHT);
        String flightNumber = request.getParameter(Parameters.FLIGHT_NUMBER_FOR_FLIGHT);
        String departureForFlight = request.getParameter(Parameters.DEPARTURE_FOR_FLIGHT);
        String arrivalForFlight = request.getParameter(Parameters.ARRIVAL_FOR_FLIGHT);
        String dateForFlight = request.getParameter(Parameters.DATE_OF_FLIGHT);
        String pricePerSeat = request.getParameter(Parameters.PRICE_PER_SEAT);
        if (    aircraftForFlight != null && !aircraftForFlight.isEmpty()
                && flightNumber != null && !flightNumber.isEmpty()
                && departureForFlight != null && !departureForFlight.isEmpty()
                && arrivalForFlight != null && !arrivalForFlight.isEmpty()
                && dateForFlight != null && !dateForFlight.isEmpty()
                && pricePerSeat != null && !pricePerSeat.isEmpty()      ) {
            flightInfoFromRequest.put(Parameters.AIRCRAFT_FOR_FLIGHT, aircraftForFlight);
            flightInfoFromRequest.put(Parameters.FLIGHT_NUMBER_FOR_FLIGHT, flightNumber);
            flightInfoFromRequest.put(Parameters.DEPARTURE_FOR_FLIGHT, departureForFlight);
            flightInfoFromRequest.put(Parameters.ARRIVAL_FOR_FLIGHT, arrivalForFlight);
            flightInfoFromRequest.put(Parameters.DATE_OF_FLIGHT, dateForFlight);
            flightInfoFromRequest.put(Parameters.PRICE_PER_SEAT, pricePerSeat);
        }
        if (departureForFlight != null && !departureForFlight.isEmpty()) {
            flightInfoFromRequest.put(Parameters.DEPARTURE_FOR_FLIGHT, departureForFlight);
        }
        if (arrivalForFlight != null && !arrivalForFlight.isEmpty()) {
            flightInfoFromRequest.put(Parameters.ARRIVAL_FOR_FLIGHT, arrivalForFlight);
        }
        if (dateForFlight != null && !dateForFlight.isEmpty()) {
            flightInfoFromRequest.put(Parameters.DATE_OF_FLIGHT, dateForFlight);
        }
        return flightInfoFromRequest;
    }

    /**
     * This method fills a <i>map</i> of parameters of the flight with values from the request.
     *
     * @param request       - an object of request with necessary parameters.
     * @return              - a map with parameters.
     */
    public static HashMap<String,String> getTicketInfoFromRequest(HttpServletRequest request) {
        HashMap<String, String> ticketInfoMap = new HashMap<>();
        String userLogin = request.getParameter(Parameters.LOGIN);
        String flightId = request.getParameter(Parameters.FLIGHT_ID);
        String luggageId = request.getParameter(Parameters.LUGGAGE_ID);
        if (userLogin != null && !userLogin.isEmpty()
                && flightId != null && !flightId.isEmpty()
                && luggageId != null && !luggageId.isEmpty()) {
            ticketInfoMap.put(Parameters.LOGIN, userLogin);
            ticketInfoMap.put(Parameters.FLIGHT_ID, flightId);
            ticketInfoMap.put(Parameters.LUGGAGE_ID, luggageId);
        }
        return ticketInfoMap;
    }

    /**
     * This method receives an user object from the session object retrieved from the request instance.
     * An user object was passed into the session object while the login operation has been executing.
     *
     * @param request   - an object of request.
     * @return          - an user object.
     */
    public static User getUserFromSession(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Parameters.USER);
        return user;
    }

    /**
     * This method received a ticket number from request.
     *
     * @param request   - an object of request.
     * @return          - a ticket number.
     */
    public static String getTicketNumberFromRequest(HttpServletRequest request) {
        String ticketNumber = request.getParameter(Parameters.TICKET_NUMBER);
        if (!ticketNumber.isEmpty()) {
            return ticketNumber;
        } else {
            return null;
        }
    }
}