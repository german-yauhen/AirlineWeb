package by.htp.hermanovich.airline.commands.factory;

import by.htp.hermanovich.airline.commands.BasicCommand;
import by.htp.hermanovich.airline.commands.implementations.DefaultCommand;
import by.htp.hermanovich.airline.commands.implementations.user.*;
import by.htp.hermanovich.airline.commands.implementations.aircraft.CreateAircraftCommand;
import by.htp.hermanovich.airline.commands.implementations.airport.CreateAirportCommand;
import by.htp.hermanovich.airline.commands.implementations.flight.CreateFlightCommand;
import by.htp.hermanovich.airline.commands.implementations.flight.ShowFlightsCommand;
import by.htp.hermanovich.airline.commands.implementations.luggage.CreateLuggageCommand;
import by.htp.hermanovich.airline.commands.implementations.luggage.DeleteLuggageCommand;
import by.htp.hermanovich.airline.commands.implementations.luggage.UpdateLuggageCommand;
import by.htp.hermanovich.airline.commands.implementations.ticket.CreateTicketCommand;
import by.htp.hermanovich.airline.commands.implementations.ticket.ReturnTicketCommand;
import by.htp.hermanovich.airline.commands.implementations.ticket.ShowTicketsCommand;

/**
 * Description: This class describes all type of using commands.
 *
 * Created by Yauheni Hermanovich on 14.07.2017.
 */
public enum CommandType {

    /*user commands*/
    LOGIN, LOGOUT, REGISTRATION, GOTOREGISTRATION, UPDATECLIENT, BACK, DEFAULT,

    /*luggage commands*/
    CREATELUGGAGE, UPDATELUGGAGE, DELETELUGGAGE,

    /*airport commands*/
    CREATEAIRPORT,

    /*aircraft command*/
    CREATEAIRCRAFT,

    /*flight commands*/
    CREATEFLIGHT, SHOWFLIGHTS,

    /*ticket commands*/
    CREATETICKET, SHOWTICKETS, RETURNTICKET;

    /**
     * This method directs the control to the corresponding class. The transfer of the control to the corresponding class
     * is carried out by determining the value of the parameter "command" from request. The current request is generated
     * from the "form" placed on the jsp page.
     *
     * @return      - the current class will be processed.
     */
    public BasicCommand getCurrentCommand() {
        switch (this) {
            case LOGIN:
                return new LoginCommand();
            case LOGOUT:
                return new LogoutCommand();
            case REGISTRATION:
                return new RegistrationCommand();
            case GOTOREGISTRATION:
                return new GotoRegistrationCommand();
            case UPDATECLIENT:
                return new UpdateClientCommand();
            case BACK:
                return new BackCommand();
            case CREATELUGGAGE:
                return new CreateLuggageCommand();
            case DELETELUGGAGE:
                return new DeleteLuggageCommand();
            case UPDATELUGGAGE:
                return new UpdateLuggageCommand();
            case CREATEAIRPORT:
                return new CreateAirportCommand();
            case CREATEAIRCRAFT:
                return new CreateAircraftCommand();
            case CREATEFLIGHT:
                return new CreateFlightCommand();
            case SHOWFLIGHTS:
                return new ShowFlightsCommand();
            case CREATETICKET:
                return new CreateTicketCommand();
            case SHOWTICKETS:
                return new ShowTicketsCommand();
            case RETURNTICKET:
                return new ReturnTicketCommand();
            case DEFAULT:
                return new DefaultCommand();
            default:
                return new DefaultCommand();
        }
    }
}