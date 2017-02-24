package Servlet;
import DB.DBManager;
import DB.Utente;
import java.io.IOException;
import java.io.PrintWriter;
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
public class AdminSetting extends HttpServlet {
    int idgroup;
    int iduser;
    int idadmin;
    DBManager manager;
    List<Utente> utenti, utentiinviti;
    boolean pubblico;
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
                        HttpSession session = request.getSession();

                        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        idgroup = Integer.parseInt(request.getParameter("idgroup"));
        iduser = Integer.parseInt(request.getParameter("iduser"));
        idadmin = this.manager.idadmin(idgroup);
        utenti = this.manager.utentipartecipanti(idgroup, iduser);
        utentiinviti = this.manager.utentiinviti(idgroup);
        if (idadmin != iduser) {
            RequestDispatcher rd = request.getRequestDispatcher("ViewGroup?idgroup=" + idgroup + "&iduser=" + iduser);
            rd.forward(request, response);
        }
       request.setAttribute("utenti", utenti);
        request.setAttribute("utentiinviti", utentiinviti);
        pubblico=manager.trovasepubblico(idgroup);
        
                session.setAttribute("pagina", "AdminSetting?idgroup="+idgroup+"&iduser=" + session.getAttribute("idutente"));

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/AdminSetting.jsp?idgroup=" + idgroup + "&iduser=" + iduser+ "&pubblico=" + pubblico);
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
