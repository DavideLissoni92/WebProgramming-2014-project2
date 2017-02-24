package Servlet;
import DB.DBManager;
import java.io.IOException;
import java.security.Security;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author Daniele
 */
public class RecuperoPassword extends HttpServlet {
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
    int idutente;
    DBManager manager;
    String email, nomeutente, nuovapassword,percorso;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, MessagingException {
        response.setContentType("text/html;charset=UTF-8");
        nomeutente = request.getParameter("Username");
nuovapassword=request.getParameter("NuovaPassword");
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        idutente = manager.trovaidutente(nomeutente);
        email = manager.emailutente(idutente);
        manager.aggiornarecuperopassword(idutente);
        
        percorso = request.getScheme()
      + "://"
      + request.getServerName()
      + ":"
      + request.getServerPort();
        
        inviamail(email, idutente, nuovapassword,percorso);
        
        response.sendRedirect("Login.jsp");
    }
    public void inviamail(String email, int idutente, String nuovapassword,String percorso) throws MessagingException {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("daniele.dellagiacoma@gmail.com", "google92");
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("daniele.dellagiacoma@gmail.com"));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
        msg.setSubject("Questa è la tua nuova password");
       
        
        //aggiungere testo 
        msg.setContent("La tua nuova password è <b>"+nuovapassword+"</b>. <br><br>Per procedere al cambio della password <a href='"+percorso+"/Progetto2k/ChangePasswordMail?iduser="+idutente+"&nuovapassword="+nuovapassword+"'>clicca qui</a> entro 90 secondi dalla richiesta.","text/html; charset=utf-8");
        msg.setSentDate(Calendar.getInstance().getTime());
        Transport.send(msg);
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
        try {
            processRequest(request, response);
        } catch (MessagingException ex) {
            Logger.getLogger(RecuperoPassword.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (MessagingException ex) {
            Logger.getLogger(RecuperoPassword.class.getName()).log(Level.SEVERE, null, ex);
        }
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
