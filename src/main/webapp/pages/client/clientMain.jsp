<%--Created by Yauheni Hermanovich 14.07.2017--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="spec"%>
<html>
    <head>
        <title>Clients account page</title>
        <link rel="stylesheet" type="text/css" href="../css/clientMain.css"/>
        <link rel="stylesheet" type="text/css" href="../css/tcal.css"/>
        <script type="text/javascript" src="../js/tcal_en.js"></script>
    </head>
    <body>
        <div class="wrapperWelcomeInfo">
            <div class="welcomeElement">
                Welcome to Airline Company, <i>${user.getFirstName()} ${user.getSurname()}</i>!
            </div>
        </div>
        <div class="wrapperPageData">
            <!--UPDATE-->
            <div class="updateUserElement">
                <form name="clientData" method="POST" action="controller">
                    <fieldset>
                        <legend align="center">Update Form</legend>
                        <input type="hidden" name="command" value="updateclient"/>
                        <table>
                            <tr>
                                <td>Login:</td>
                                <td><input class="inputElement" type="text" name="login" value="${user.getLogin()}" readonly="readonly" /></td>
                            </tr>
                            <tr>
                                <td>Name:</td>
                                <td><input class="inputElement" type="text" name="firstName" value=""/></td>
                            </tr>
                            <tr>
                                <td>Surname:</td>
                                <td><input class="inputElement" type="text" name="surname" value=""/></td>
                            </tr>
                            <tr>
                                <td>Document:</td>
                                <td><input class="inputElement" type="text" name="documentNumber" value=""/></td>
                            </tr>
                        </table>
                        <div class="wrapperButtons">
                            <input class="buttonElement" type="submit" value="Update"/>
                            <input class="buttonElement" type="reset" value="Reset"/>
                        </div>
                    </fieldset>
                    <div class="messageElement">
                        <spec:if test="${sessionScope.successUpdate eq 'true'}">
                            <i>You have successfully updated you personal data.</i>
                        </spec:if>
                    </div>
                </form>
            </div>
            <div class="flightsFindForm">
                <form name="flightsFindForm" method="POST" action="controller">
                    <fieldset>
                        <legend align="left">Find Flights</legend>
                        <input type="hidden" name="command" value="showflights"/>
                        <div class="flightInfoForm">
                            <table>
                                <tr>
                                    <th>Departure</th>
                                    <th>Arrival</th>
                                    <th>Date</th>
                                </tr>
                                <tr>
                                    <td>
                                        <select name="departureForFlight">
                                            <option selected="selected" disabled>Choose airport</option>
                                            <spec:forEach var="airport" items="${allAirports}">
                                                <option value="${airport.getAirportCode()}">[${airport.getAirportCode().toUpperCase()}]-[${airport.getCity().toUpperCase()}]</option>
                                            </spec:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <select name="arrivalForFlight">
                                            <option selected="selected" disabled>Choose airport</option>
                                            <spec:forEach var="airport" items="${allAirports}">
                                                <option value="${airport.getAirportCode()}">[${airport.getAirportCode().toUpperCase()}]-[${airport.getCity().toUpperCase()}]</option>
                                            </spec:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <!--CALENDAR-->
                                        <form action="#">
                                            <div><input class="tcal" type="text" name="dateForFlight" value=""/></div>
                                        </form>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="wrapperButtonsSearchFlight">
                            <input class="buttonElement" type="submit" value="Search"/>
                            <input class="buttonElement" type="reset" value="Reset"/>
                        </div>
                    </fieldset>
                </form>
            </div>
            <div class="showTicketsForm">
                <button class="buttonShow" onclick="location.href='controller?command=showtickets'">Show My Tickets</button>
            </div>
            <!--USER TYPE-->
            <div class="userTypeElement">
                </br>${String.valueOf(user.getUserType())}
                <spec:if test="${sessionScope.ticketReturnSuccess eq 'true'}">
                    <i> : MESSAGE : You have successfully return the ticket.</i>
                </spec:if>
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