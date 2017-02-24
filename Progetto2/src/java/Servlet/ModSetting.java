/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Servlet;

import DB.DBManager;
import DB.GruppoMod;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Luca Carli
 */
public class ModSetting extends HttpServlet {

    DBManager manager;
    int iduser;
    ArrayList<GruppoMod> gruppi;
    ArrayList<Integer> nutenti, npost;
    

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        HttpSession session = request.getSession();
        
        iduser = Integer.parseInt(request.getParameter("iduser"));
        gruppi = this.manager.groups();
        nutenti=this.manager.allgroupsusers();
        npost=this.manager.allgroupspost();
        
        
        request.setAttribute("gruppi", gruppi);
        request.setAttribute("nutenti", nutenti);
        request.setAttribute("npost", npost);
        
        session.setAttribute("pagina", "ModSetting?iduser=" + session.getAttribute("idutente"));

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/ModSetting.jsp?iduser=" + iduser);
        dispatcher.forward(request, response);

    
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
