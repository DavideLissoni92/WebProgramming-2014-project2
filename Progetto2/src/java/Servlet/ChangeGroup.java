/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import DB.DBManager;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author davide
 */

public class ChangeGroup extends HttpServlet {
String newtipologia;
 DBManager manager;
 int idgroup;
 int idadmin;
 boolean pubblico;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        idgroup=Integer.parseInt(request.getParameter("idgroup"));
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        newtipologia=request.getParameter("RdGruppo");
        idadmin=manager.idadmin(idgroup);
        if(newtipologia.equals("privato")){
            pubblico=false;
            manager.cambiainpubblicoprivato(idgroup,pubblico);
            
            manager.inserisciGruppoUtente(idgroup, idadmin);
            
        }else{
            pubblico=true;
             manager.cambiainpubblicoprivato(idgroup,pubblico);
             manager.eliminautentigruppo(idgroup);
           
        }
        response.sendRedirect("AdminSetting?idgroup="+idgroup+"&iduser="+idadmin);
        
        
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
