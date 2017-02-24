
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href='bootstrap.css' rel='stylesheet'>
        <link href='impostadmin.css' rel='stylesheet'>
        <link href='jquery-2.0.3.min' rel='stylesheet'>
    </head>
    <body>
         <c:set var="idgroup" value="${param.idgroup}"/>
        <c:set var="iduser" value="${param.iduser}"/>
        <div class='container'>
           
            <c:if test="${param.pubblico==false}">
<h1 class='sottotitoli'>Utenti presenti nel gruppo</h1><br>
<table border='0' class='table table-condensed table-hover'>
             <c:forEach var="utenti" items="${utenti}">   
    <tr><td align='center'>
            <c:out value="${utenti.name}"/>
            </td><td align='center'>
                <form action='RemoveUser?idgroup=${idgroup}&ideliminato=${utenti.id}&iduser=${iduser}'method='POST'>
             <input class='btn btn-lg btn-block' type='submit' value='Elimina dal gruppo' name='BtnEliminadagruppo'></form>
            </td></tr>    
            </c:forEach>
    </table>

<h2 class='sottotitoli'>Invita un nuovo utente</h2><br>
<table border='0' class='table table-condensed table-hover'>

<c:forEach var="utentiinviti" items="${utentiinviti}">  
    <tr><td align='center'>
         <c:out value="${utentiinviti.name}"/> 
         </td><td align='center'>
             <form action='InviteUser?idgroup=${idgroup}&iduser=${utentiinviti.id}&idadmin=${iduser}'method='POST'>
                 <input class='btn btn-lg btn-block' type='submit' value='Invita nel gruppo' name='BtnInvita'></form>
         </td></tr>    
         </c:forEach>
</table>
            </c:if>
        <br><br>
        <table border='0'>
            <form action='Rename?idgroup=${idgroup}&iduser=${iduser}'method='POST' >
               <tr><td><h3 class='sottotitoli'>Rinomina il gruppo</h3></td></tr>
               <tr><td>
                       <input type='text' placeholder='Nome del gruppo' autocomplete='Off' required name='InputRename' class='form-control'>
                </td><td>
                   <input type='submit' value='Rinomina' class='btn btn-lg btn-block btn-primary'name='BtnRename'> 
                </td></tr></table></form>
               
                   </div>
                   
               <form action='ChangeGroup?idgroup=${idgroup}'method='POST' class='crea'>
                   <fieldset><legend>Cambia tipologia gruppo</legend>
                       Gruppo pubblico<input type='radio' name='RdGruppo' value='pubblico'/>
                       Gruppo privato<input type='radio' name='RdGruppo' checked='checked' value='privato'/></fieldset>
                 <input type='submit' value='Cambia' name='BtnLog' class='btn btn-lg btn-primary btn-block'></form>  
               
                 <form action='ViewGroup?idgroup=${idgroup}&iduser=${iduser}'method='POST'>
                   <input type='submit' class='btn btn-lg btn-primary' value='Torna al gruppo' name='BtnTOrnagruppo' class='spaziosotto'></form>
        <%/*
            List<Utente> utenti, utentiinviti;
            int idgroup = Integer.parseInt(request.getParameter("idgroup"));
            int iduser = Integer.parseInt(request.getParameter("iduser"));
            
            utenti = (List<Utente>) request.getAttribute("utenti");
            utentiinviti = (List<Utente>) request.getAttribute("utentiinviti");
            //out.println("<div class='container'>");
            //inserimento parte grafica, titolo, tabella (utenti del gruppo, bottone per eliminarlo)
             DBManager manager;
            manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        /*
            if(!manager.trovasepubblico(idgroup)){
            out.println("<h1 class='sottotitoli'>Utenti presenti nel gruppo</h1><br>");
            out.println("<table border=\"0\" class='table table-condensed table-hover'>");
            for (int i = 0; i < utenti.size(); i++) {
                out.println("<tr>");
                out.println("<td align='center'>");
                out.println(utenti.get(i).getName());
                out.println("</td>");
                out.println("<td align='center'>");
                out.println("<form action=\"RemoveUser?idgroup=" + idgroup + "&ideliminato=" + utenti.get(i).getid() + "&iduser=" + iduser + "\"method=\"POST\">");
                out.println("<input class='btn btn-lg btn-block' type=\"submit\" value=\"Elimina dal gruppo\" name=\"BtnEliminadagruppo\"></form>");
                out.println("</td>");
                out.println("</tr>");
            }
            out.println("</table>");
            //inserimento parte grafica, titolo, tabella (utenti del sistema ma non del gruppo, bottone per aggiungerli)
           
            out.println("<h2 class='sottotitoli'>Invita un nuovo utente</h2><br>");
            out.println("<table border=\"0\" class='table table-condensed table-hover'>");
            for (int i = 0; i < utentiinviti.size(); i++) {
                out.println("<tr>");
                out.println("<td align='center'>");
                out.println(utentiinviti.get(i).getName());
                out.println("</td>");
                out.println("<td align='center'>");
                out.println("<form action=\"InviteUser?idgroup=" + idgroup + "&iduser=" + utentiinviti.get(i).getid() + "&idadmin=" + iduser + "\"method=\"POST\">");
                out.println("<input class='btn btn-lg btn-block' type=\"submit\" value=\"Invita nel gruppo\" name=\"BtnInvita\"></form>");
                out.println("</td>");
                out.println("</tr>");
            }
            out.println("</table>");
            }
            out.println("<br>");
            out.println("<br>");
            //inserimento parte grafica, textbox e bottone per rinominare il gruppo
            out.println("<table border=\"0\">");
            out.println("<form action=\"Rename?idgroup=" + idgroup + "&iduser=" + iduser + "\"method=\"POST\" >");
            out.println("<tr><td><h3 class='sottotitoli'>Rinomina il gruppo</h3></td></tr>");
            out.println("<tr><td>");
            out.println("<input type=\"text\" placeholder='Nome del gruppo' autocomplete=\"Off\" required name=\"InputRename\" class=\"form-control\">");
            out.println("</td><td>");
            out.println("<input type=\"submit\" value=\"Rinomina\" class='btn btn-lg btn-block btn-primary'"
                    + " onClick=\"window.alert('Gruppo rinominato con successo.')\" name=\"BtnRename\">");
            out.println("</td></tr></table></form>");
            //inserimento parte grafica, bottone per la creazione del pdf
            out.println("<form action=\"/CreaPDF?idgroup=" + idgroup + "&iduser=" + iduser + "\"method=\"POST\" class='spaziosotto'>");
            out.println("<input type=\"submit\" class='btn btn-lg btn-primary' value=\"Crea Pdf\" onClick=\"window.alert('Pdf creato con successo.')\" name=\"BtnPdf\">");
            out.println("</form>");
            //inserimento parte grafica, bottone per tornare alla pagina del gruppo
            out.println("<form action=\"ViewGroup?idgroup=" + idgroup + "&iduser=" + iduser + "\"method=\"POST\">");
            out.println("<input type=\"submit\" class='btn btn-lg btn-primary' value=\"Torna al gruppo\" name=\"BtnOk\" class='spaziosotto'></form>");
            out.println("</div>");
            
            out.println("<form action='ChangeGroup?idgroup=" + idgroup + "'method='POST' class='crea'>");
            
             out.println("<fieldset><legend>Cambia tipologia gruppo</legend>");
             out.println( "Gruppo pubblico<input type='radio' name='RdGruppo' value='pubblico'/>");
             out.println( "Gruppo privato<input type='radio' name='RdGruppo' checked='checked' value='privato'/></fieldset>");
            out.println("<input type='submit' value='Cambia' name='BtnLog' class='btn btn-lg btn-primary btn-block'></form>");
        */%>
    </body>
</html>