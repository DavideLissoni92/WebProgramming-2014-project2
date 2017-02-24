package Servlet;
import DB.DBManager;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
/**
 *
 * @author Daniele
 */
public class UploadFile extends HttpServlet {
     int idgruppo;
    int idutente, autenticazionegruppo;
    int idpost;
    int idadmin;
    String file;
    private String dirName;
    DBManager manager;
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
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
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
        dirName = request.getServletContext().getRealPath("");
        dirName = dirName + "\\File";
// Creiamo l'oggetto File
        File folder = new File(dirName);
        if (!folder.isDirectory()) {
            folder.mkdir();
        }
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        PrintWriter out = response.getWriter();
        idutente = Integer.parseInt(request.getParameter("iduser"));
        idadmin = Integer.parseInt(request.getParameter("idadmin"));
        idgruppo = Integer.parseInt(request.getParameter("idgroup"));
        idpost = Integer.parseInt(request.getParameter("idpost"));
        int idfile = this.manager.trovaidfile();
        out.println(dirName);
        response.setContentType("text/plain");
        out.println("Demo Upload Servlet using MultipartRequest");
        out.println();
        try {
            MultipartRequest multi
                    = new MultipartRequest(request, dirName, 10 * 1024 * 1024,
                            "ISO-8859-1", new DefaultFileRenamePolicy());
            out.println("PARAMS:");
            Enumeration params = multi.getParameterNames();
            while (params.hasMoreElements()) {
                String name = (String) params.nextElement();
                String value = multi.getParameter(name);
                out.println(name + "=" + value);
            }
            out.println();
            out.println("FILES:");
            Enumeration files = multi.getFileNames();
            int rs;
            while (files.hasMoreElements()) {
                String name = (String) files.nextElement();
                String filename = multi.getFilesystemName(name);
                String type = multi.getContentType(name);
                File f = multi.getFile(name);
                f.renameTo(new File(dirName + "\\" + idfile + filename));
                filename = idfile + filename;
                String query = "INSERT INTO POST_FILE(IDPOST,NOME) VALUES(?,?)";
                PreparedStatement st = manager.db.prepareStatement(query);
                st.setInt(1, idpost);
                st.setString(2, filename);
                rs = st.executeUpdate();
                autenticazionegruppo = this.manager.controllogruppo(idgruppo, idutente);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("ViewGroup?idgroup=" + idgruppo + "&iduser=" + idutente + "&idadmin=" + idadmin + "&autenticazionegruppo=" + autenticazionegruppo);
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