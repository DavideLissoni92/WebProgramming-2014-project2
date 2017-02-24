/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import DB.DBManager;
import DB.Utente;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author davide
 */
public class Login extends HttpServlet {
    DBManager manager;
    Utente utente;
    String inputuser;
    String inputpswd;

    Cookie datalog = new Cookie("datalog", "");
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
    this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        HttpSession session = request.getSession(true);
         
        inputuser = request.getParameter("Inputname");
        inputpswd = request.getParameter("InputPassword");
          
       
         try {
            utente = this.manager.autenticazione(inputuser, inputpswd);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (utente == null) {

                response.sendRedirect("Login.jsp");

            } else {
                   
    session.setAttribute("login", "true");
    session.setAttribute("moderatore", manager.moderatore(utente.getid()));
       
                session.setAttribute("idutente", utente.getid());
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String time = formatter.format(new Date(session.getLastAccessedTime()).getTime());
                datalog.setValue(time);
                datalog.setMaxAge(-1);
                response.addCookie(datalog);
                response.sendRedirect("Home2?iduser="+utente.getid());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
