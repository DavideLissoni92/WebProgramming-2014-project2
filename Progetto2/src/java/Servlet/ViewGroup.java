package Servlet;

import DB.DBManager;
import DB.Post;
import DB.Utente;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Daniele
 */
public class ViewGroup extends HttpServlet {

    int idgroup;
    int iduser;
    int idadmin;
    boolean admin, chiuso;
    DBManager manager;
    String nomeGruppo;
    List<Post> posts;
    List<Utente> utenti;
    int maxidpost;
    int autentic;
    Boolean pubblico;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        idgroup = Integer.parseInt(request.getParameter("idgroup"));
        iduser = Integer.parseInt(request.getParameter("iduser"));
        idadmin = this.manager.idadmin(idgroup);
        HttpSession session = request.getSession();
        admin = idadmin == iduser;
        chiuso = this.manager.controllochiudi(idgroup);
        nomeGruppo = manager.prendinome(idgroup);
        maxidpost = manager.prendiidpost();
        posts = this.manager.mypost(idgroup);
        utenti = this.manager.utentipartecipanti(idgroup, iduser);
        autentic = 0;
        pubblico = manager.trovasepubblico(idgroup);
        request.setAttribute("posts", posts);
        request.setAttribute("utenti", utenti);

        if (nomeGruppo == null) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Home2?iduser=" + iduser);
            dispatcher.forward(request, response);

        }
        if ((Boolean) session.getAttribute("moderatore") == true) {
            autentic = manager.controllogruppomod(idgroup, iduser);
        }

        session.setAttribute("pagina", "ViewGroup?idgroup=" + idgroup + 
                "&iduser=" + session.getAttribute("idutente"));

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/ViewGroup.jsp?nomeGruppo=" 
                + nomeGruppo + "&maxidpost=" + maxidpost + "&idgroup=" + idgroup +
                "&iduser=" + iduser + "&idadmin=" + idadmin + "&admin=" + admin +
                "&pubblico=" + pubblico + "&autentic=" + autentic + "&chiuso=" + chiuso);
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
