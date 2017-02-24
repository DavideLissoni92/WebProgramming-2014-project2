/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;
import DB.DBManager;
import java.io.IOException;
import java.security.Security;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.*;
/**
 *
 * @author davide
 */
public class InviteUser extends HttpServlet {
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
    int idutente, idgroup, admin;
    String email, percorso, nomegruppo, nomeamministratore;
    DBManager manager;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, AddressException, MessagingException {
        response.setContentType("text/html;charset=UTF-8");
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        idutente = Integer.parseInt(request.getParameter("iduser"));
        idgroup = Integer.parseInt(request.getParameter("idgroup"));
        admin = Integer.parseInt(request.getParameter("idadmin"));
        email = manager.emailutente(idutente);
        nomegruppo=manager.prendinome(idgroup);
        nomeamministratore=manager.trovanomepost(admin);
        //trovare percorso
percorso = request.getScheme()
      + "://"
      + request.getServerName()
      + ":"
      + request.getServerPort()
      
      ;
        inviamail(email, idutente,idgroup, nomegruppo,percorso,nomeamministratore);
        manager.invitautente(idgroup, idutente);
        // la successiva pagina visualizzata è quella delle impostazioni dell'admin
        response.sendRedirect("AdminSetting?idgroup=" + idgroup + "&iduser=" + admin);
    }
    public void inviamail(String email, int idutente, int idgroup, String nomegruppo,String percorso, String nomeamministratore) throws MessagingException {
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
       
        msg.setSubject("Sei stato invitato nel gruppo "+nomegruppo);
        
        //aggiungere testo
        msg.setContent("<b>"+nomeamministratore+"</b> ti ha invitato a entrare nel gruppo <b>"+nomegruppo+"</b>. <br><br>Se vuoi accettare l'invito <a href='"+percorso+"/Progetto2k/InviteAccepted?idgroup="+idgroup+"&iduser="+idutente+"'>clicca qui</a>.","text/html; charset=utf-8");
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
        } catch (AddressException ex) {
            Logger.getLogger(InviteUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(InviteUser.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (AddressException ex) {
            Logger.getLogger(InviteUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(InviteUser.class.getName()).log(Level.SEVERE, null, ex);
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
