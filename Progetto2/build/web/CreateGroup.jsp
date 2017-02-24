<%-- 
    Document   : CreateGroup
    Created on : 1-apr-2014, 9.45.15
    Author     : davide
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Group</title>
        <link href='bootstrap.css' rel='stylesheet'>
            <link href='creagruppo.css' rel='stylesheet'>
            <link href='jquery-2.0.3.min' rel='stylesheet'>
    </head>
    <body>
        <div class='container'>
        <h2 class='sottotitoli'>Crea gruppo</h2>
        <form action='CreateGroup?iduser=${param.iduser}'method='POST' class='crea'>
            <input type='text' placeholder='Nome del gruppo' class='form-control' autocomplete='Off' name='InputNomeGruppo' required><br><br>
        <fieldset><legend>Tipologia gruppo</legend>
          Gruppo pubblico<input type='radio' name='RdGruppo' value='pubblico'/>  
          Gruppo privato<input type='radio' name='RdGruppo' checked='checked' value='privato'/></fieldset>
           <input type='submit' value='Submit' name='BtnLog' class='btn btn-lg btn-primary btn-block'></form>
            <a href='Home2?iduser=${param.iduser}' class='btn btn-primary btn-lg' class='spaziosotto'>Home</a>

            </div>
    </body>
</html>
