<%--Created by Yauheni Hermanovich 24.07.2017--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="spec"%>
<html>
    <head>
        <title>Client's Flights</title>
        <link rel="stylesheet" type="text/css" href="../css/flightsMain.css"/>
    </head>
    <body>
        <div class="wrapperWelcomeInfo">
            <div class="welcomeElement">
                <i>${user.getFirstName()} ${user.getSurname()}.</i>
            </div>
        </div>
        <div class="wrapperPageData">
            <div class="resultFlightForm">
                <form name="flightSearchResults">
                    <fieldset>
                        <legend align="left">Search Results</legend>
                        <input type="hidden" name="command" value="returnticket"/>
                        <div class="resultTableUsersFlights">
                            <table>
                                <th>Ticket No</th>
                                <th>Flight Route</th>
                                <th>Route</th>
                                <th>Date</th>
                                <th>Luggage Type</th>
                                <th>Total Price</th>
                                <th></th>
                                <spec:forEach var="ticket" items="${ticketsList}">
                                    <tr>
                                        <td>${ticket.getTicketNumber()}</td>
                                        <td>${ticket.getFlight().getFlightNumber()}</td>
                                        <td>
                                            from ${ticket.getFlight().getDepartureAirport()}
                                            to ${ticket.getFlight().getArrivalAirport()}
                                        </td>
                                        <td>${String.valueOf(ticket.getFlight().getSheduledDeparture())}</td>
                                        <td>${ticket.getLuggage().getLuggageType().toUpperCase()}</td>
                                        <td>${ticket.getTotalPrice()}&#8364;</td>
                                        <td><input type="radio" name="ticketNumber" value="${ticket.getTicketNumber()}"/></td>
                                    </tr>
                                </spec:forEach>
                            </table>
                        </div>
                        <div class="wrapperButtonsChooseFlight">
                            <input class="buttonElement" type="submit" value="Return Ticket"/>
                            ${pageContext.session.setAttribute("backpage", "client")}
                            <input class="buttonElement" type="reset" value="Reset"/>
                            <input class="buttonElement" type="button" value="Back To Menu" onclick='location.href="controller?command=back"'/>
                        </div>
                    </fieldset>
                </form>
            </div>
            <!--USER TYPE-->
            <div class="userTypeElement">
                </br>${String.valueOf(user.getUserType())}
            </div>
        </div>
        <!--LOGOUT-->
        <div class="logoutElement">
            <form name="logout" method="POST" action="controller">
                <input type="hidden" name="command" value="logout" />
                <input type="submit" value="Log Out" />
            </form>
        </div>
    </body>
</html>
