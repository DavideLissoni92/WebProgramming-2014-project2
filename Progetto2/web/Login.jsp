<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html> <html> <head> <meta charset='utf-8'> <meta name='viewport' content='width=device-width, initial-scale=1.0'> <title>Progetto web 2</title>

<link href='bootstrap.css' rel='stylesheet'> <link href='signin.css' rel='stylesheet'> <link href='jquery-2.0.3.min' rel='stylesheet'> </head>

<body> <div class='container'> <form class='form-signin' action='Login'> <h2 class='form-signin-heading'>Log In</h2> <input name='Inputname' type='text' class='form-control' placeholder='Username' required autofocus> <input name='InputPassword' type='password' class='form-control' placeholder='Password' required> <button name='BtnLog' class='btn btn-lg btn-primary btn-block' type='submit'>Entra</button> </form> </div> <br><br> <% session.setAttribute("login", "false");

 out.println("<a href='NoLoggedEntry?iduser="+0+"'class='btn btn-primary btn-lg' class='spaziosotto'>Entra senza Login</a>"); %> <br><br> <a href='RecuperoPassword.jsp' class='btn btn-primary btn-lg' class='spaziosotto'> Recupero Password </a> <br><br> <a href='Registration.jsp' class='btn btn-primary btn-lg' class='spaziosotto'>Registrazione</a> </body> </html>