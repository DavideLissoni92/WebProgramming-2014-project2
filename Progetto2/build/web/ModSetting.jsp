
<%@page import="java.util.ArrayList"%>
<%@page import="DB.GruppoMod"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@page import="java.util.List"%>


<%@page import="DB.DBManager"%>


<!DOCTYPE html>
<html>
    <head>  
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Impostazioni moderatore</title>
        <link rel="stylesheet" href="<c:url value="/css/jquery.dataTables.css"/>"/>
        <script type="text/javascript" src='<c:url value="/js/jquery.js"/>'></script>
        <script type="text/javascript" src='<c:url value="/js/jquery.dataTables.min.js"/>'></script>
<link href='bootstrap.css' rel='stylesheet'>
    </head>
    <body>


        <h1>Impostazioni moderatore</h1>

        <script>
            $(document).ready(function() {
                $('#tabella').dataTable({
                });
            });
        </script> 

        
        <c:set var="iduser" value="${param.iduser}"/>
        <%
            int i = 0;
           
            List<Integer> npost, nutenti;
            List<GruppoMod> gruppi;
            
            gruppi = (List<GruppoMod>) request.getAttribute("gruppi");
            npost = (List<Integer>) request.getAttribute("npost");
            nutenti = (List<Integer>) request.getAttribute("nutenti");


        %>



        <table id="tabella" border='1'>
            <thead>
                <tr>
                    <th>
                        Nome gruppo
                    </th>
                    <th>
                        Pubblico/Privato
                    </th>
                    <th>
                        Numero partecipanti
                    </th>
                    <th>
                        Numero post
                    </th>
                    <th>
                        Avatar amministratore
                    </th>
                    <th>
                        Chiudi gruppo
                    </th>
                </tr>
            </thead>




            <tbody>
                <c:forEach var="gruppi" varStatus="loop" items="${gruppi}">

                    <tr>
                        <td>
                            <c:out value="${gruppi.name}"/> 
                        </td>
                        <td>

                            <c:choose>
                                <c:when test="${gruppi.pubblico==true}">
                                    Pubblico
                                </c:when>
                                <c:otherwise>
                                    Privato
                                </c:otherwise>
                            </c:choose>                            
                        </td>
                        <td>
                             <c:choose>
                                <c:when test="${gruppi.pubblico==true}">
                                    -
                                </c:when>
                                <c:otherwise>
                                    <% out.println(nutenti.get(i));%>
                                </c:otherwise>
                            </c:choose> 
                            
                        </td>
                        <td>
                            <% out.println(npost.get(i));%>
                        </td>
                        <td>

                            <img src='${gruppi.avatar}'  width='50' height='50'/>

                        </td>
                        <td>



                            <c:choose>
                                <c:when test="${gruppi.chiuso==true}">
                                    Gruppo chiuso
                                </c:when>

                                <c:when test="${gruppi.chiuso==false}">
                                    <a href='CloseGroup?id=${gruppi.id}&iduser=${iduser}'>Chiudi</a>
                                </c:when>


                            </c:choose>


                        </td>
                    </tr>
                    <% i++;%>
                </c:forEach>

            </tbody>
        </table>
        <br><br>
        <a href='Home2?iduser=${iduser}' class='btn btn-primary btn-lg' class='spaziosotto'>Home</a>

    </body>
</html>
