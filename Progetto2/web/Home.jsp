<%-- 
    Document   : Home
    Created on : 31-mar-2014, 14.58.51
    Author     : davide
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Servlet Home</title>


        <link href='bootstrap.css' rel='stylesheet'>
        <link href='home.css' rel='stylesheet'>
        <link href='jquery-2.0.3.min' rel='stylesheet'>

    </head>
    <body>
        <c:set var="avatar" value="${param.avatar}"/>
        <c:set var="iduser" value="${param.iduser}"/>
        <c:set var="aggiornamenti" value="${aggiornamenti}"/>
        <c:set var="gruppiinviti" value="${gruppiinviti}"/>
        <c:set var="moderatore" value="${moderatore}"/>
        Ultimo accesso: <c:out value="${param.cookie}"/>
        <br>
        <img src='${avatar}' width='150' height='150' class='img-thumbnail'>




        <form action='CreaGruppo?iduser=${iduser}' method='POST' class='creagruppo'>
            <input type='submit' class='btn btn-lg btn-primary btn-block' value='Crea Gruppo' name='BtnCreaGruppo'></form>

        <form action='MyGroups?iduser=${iduser}'method='POST' class='imieigruppi'>
            <input type='submit' class='btn btn-lg btn-primary btn-block' value='I miei gruppi' name='BtnHomeGroups'></form>


        <c:choose>
            <c:when test="${fn:length(aggiornamenti)==0}">

                <h4 class='sottotitoli'>Nessun aggiornamento dall'ultimo login</h4>
            </c:when>
            <c:otherwise>
                <h3 class='sottotitoli'>Aggiornamenti dall'ultimo login</h3>
                <br>
                <ul>
                    <c:forEach var="a" items="${aggiornamenti}">
                        <li>
                            E' stato aggiornato il gruppo <c:out value="${a}"/>  
                        </li>
                    </ul>

                </c:forEach>
            </c:otherwise>
        </c:choose>
        <br>

        <h3 class='sottotitoli'>I miei inviti</h3>
        
 <c:choose>
            <c:when test="${fn:length(gruppiinviti)==0}">

               <h4 class='sottotitoli'>Nessun invito</h4>
            </c:when>
            <c:otherwise>
                <table border='0' class='table'>
                    <c:forEach  items="${gruppiinviti}" var="gruppo">
                        <tr><td>
                        sei stato invitato nel gruppo <c:out value="${gruppo.name}"/> 
                         </td><td>
                             <form action='InviteAccepted?iduser=${iduser}&idgroup=${gruppo.id} 'method='POST'>
                                 <input type='submit' value='Accetta' class='btn btn-lg btn-block' name='BtnAccetta'>
                                 </td>
                                 <td>
                                     <form action='InviteRefused?iduser=${iduser}&idgroup=${gruppo.id} 'method='POST'>
                                         <input type='submit' value='Rifiuta' class='btn btn-lg btn-block' name='BtnRifiuta'></form>
                                     
                                     </td></tr>
                                 
                    </c:forEach>
                                 </table>
            </c:otherwise>
 </c:choose>
               
               
               <% 
               
               
               
               
               %>
              
               
                <c:if test="${moderatore==true}">
                    
                     <form action='ModSetting?iduser=${iduser}' method='POST'>
    <input type='submit' value='Impostazioni Moderatore' class='btn btn-lg btn-block' name='BtnImp'></form>
        
                  
                 </c:if>
              
               
               
               
<form action='UserSettings?iduser=${iduser}' method='POST'>
    <input type='submit' value='Impostazioni' class='btn btn-lg btn-block' name='BtnImpostazioni'></form>
        

        <form action='LogOut' method='POST'>
    <input type='submit' value='Log out' class='btn btn-lg btn-block' name='BtnLogOut'></form>
    </body>
</html>
