<%-- 
    Document   : MyGroups
    Created on : 1-apr-2014, 9.23.39
    Author     : davide
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MyGroups</title>
        <link href='bootstrap.css' rel='stylesheet'>
           <link href='jquery-2.0.3.min' rel='stylesheet'>
    </head>
    <body>
        <div class='container'>
        <br>
            <br>
            <h2 class='sottotitoli'>Gruppi a cui sono iscritto</h2>
            <br>
            <ul>
                
                 <c:forEach var="gruppi" items="${gruppi}">
                        <li>
                         <a href='ViewGroup?idgroup=${gruppi.id}&iduser=${param.idutente}'>
                             <c:out value="${gruppi.name}"/></a>
                        </li>
                  

                </c:forEach> 
                 </ul>
            </body>
</html>
