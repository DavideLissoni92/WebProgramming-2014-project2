package Filter;

import DB.DBManager;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class FilterGroup implements Filter {

    private FilterConfig filterConfig = null;

    public FilterGroup() {
    }
    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    int iduser, idgroup, autenticazionegruppo, gruppononloggato;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();

        iduser = Integer.parseInt(req.getParameter("iduser"));
        idgroup = Integer.parseInt(req.getParameter("idgroup"));
        autenticazionegruppo = DBManager.controllogruppo(idgroup, iduser);
        gruppononloggato = DBManager.controllogruppononloggato(idgroup, iduser);


        if (iduser > 0) {

            if ((Boolean) session.getAttribute("moderatore") == true) {
                chain.doFilter(request, response);
            } else {
                if (autenticazionegruppo != -1) {
                    chain.doFilter(request, response);
                } else {
                    RequestDispatcher rd = req.getRequestDispatcher("Home2?iduser=" + iduser);
                    rd.forward(request, response);

                    res.sendRedirect("Home2?iduser=" + iduser);
                }
            }
        } else {
            if ((Boolean) session.getAttribute("moderatore") == true) {
                chain.doFilter(request, response);
            } else {
                if (gruppononloggato != -1) {
                    chain.doFilter(request, response);
                } else {
                    RequestDispatcher rd = req.getRequestDispatcher("MyGroups?iduser="+iduser);
                    rd.forward(request, response);

//res.sendRedirect("MyGroups?iduser="+iduser);
                       }
            }
        }


        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }
}
