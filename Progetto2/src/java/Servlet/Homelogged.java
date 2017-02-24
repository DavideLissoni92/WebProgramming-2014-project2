/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import DB.DBManager;
import DB.Gruppo;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
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
public class Homelogged extends HttpServlet {

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
    String avatar;
    DBManager manager;
    String cookievalue;
    Boolean mod;
    int id;
    List<Gruppo> gruppiinviti;
    ArrayList<String> aggiornamenti = new ArrayList<String>();
    Timestamp ts;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        Cookie[] cookies = request.getCookies();
        Cookie cookie;
        cookie = cookies[1];
        cookievalue = cookie.getValue();
        HttpSession session = request.getSession();


        if (session.getAttribute("utenteloggato") == "no") {
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        }

        try {
            final String OLD_FORMAT = "dd/MM/yyyy HH:mm:ss";
            final String NEW_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
            String oldDateString = cookie.getValue().toString();
            String newDateString;

            DateFormat formatter = new SimpleDateFormat(OLD_FORMAT);
            java.util.Date d = formatter.parse(oldDateString);
            ((SimpleDateFormat) formatter).applyPattern(NEW_FORMAT);
            newDateString = formatter.format(d);

            ts = Timestamp.valueOf(newDateString);

        } catch (Exception e) {
            ts = null;
            e.printStackTrace();
        }



        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        id = Integer.parseInt(request.getParameter("iduser"));
        PrintWriter out = response.getWriter();
        avatar = manager.trovaavatar(id);
        mod = manager.moderatore(id);

        gruppiinviti = manager.invitigruppo(id);

        aggiornamenti = manager.aggiornamenti(id);

        request.setAttribute("mod", mod);
        request.setAttribute("gruppiinviti", gruppiinviti);
        request.setAttribute("aggiornamenti", aggiornamenti);

        manager.aggiornamentoultimologin(ts, id);
        
        session.setAttribute("pagina", "Home2?iduser=" + session.getAttribute("idutente"));

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Home.jsp?avatar=" + avatar + "&iduser=" + id + "&cookie=" + cookievalue);
        dispatcher.forward(request, response);


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
