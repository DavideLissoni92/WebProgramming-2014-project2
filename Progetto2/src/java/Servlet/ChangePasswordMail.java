/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;
import DB.DBManager;
import java.io.IOException;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author Daniele
 */
public class ChangePasswordMail extends HttpServlet {
    String nuovapassword;
    int iduser;
    DBManager manager;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        iduser = Integer.parseInt(request.getParameter("iduser"));
        nuovapassword = request.getParameter("nuovapassword");
        Timestamp datainviomail = manager.tornarecuperopassword(iduser);
        long millisecinvio = datainviomail.getTime();
        long tempoattuale = System.currentTimeMillis();
      //  System.out.println("tempoattuale " + tempoattuale);
      //  System.out.println("millisecinvio " + millisecinvio);
        if (tempoattuale - millisecinvio < 90000) {
            manager.aggiornapassword(iduser, nuovapassword);
        }
        response.sendRedirect("Login.jsp");
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
