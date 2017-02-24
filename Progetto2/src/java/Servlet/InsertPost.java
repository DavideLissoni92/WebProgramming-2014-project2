package Servlet;

import DB.DBManager;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;


public class InsertPost extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    int idgroup, m=0;
    int iduser, idadmin, idpost;
    String testo,testoqr, input, sottostringa, prova, prova2;
    DBManager manager;
    List<String> nomefile;
    private String dirName;
    int n;
    String sottostringaqr;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        PrintWriter out = response.getWriter();
        idgroup = Integer.parseInt(request.getParameter("idgroup"));
        idadmin = Integer.parseInt(request.getParameter("idadmin"));
        iduser = Integer.parseInt(request.getParameter("iduser"));
        input = request.getParameter("InputPost");
        idpost = Integer.parseInt(request.getParameter("idpost"));
        sottostringa = "";
        prova = "";
        prova2 = "";
        testoqr="";
        String stringamod ="";
        String inputst="";
        try {

            inputst= input;
            Pattern patternqr = Pattern.compile("(.*?)\\$(QR)\\$(.*?)\\$\\$(.*?)");
            Pattern pattern = Pattern.compile("(.*?)\\$\\$(.+?)\\$\\$(.*?)");
            Pattern patternweb = Pattern.compile("\\(?\\b((ht|f)tp(s?)://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]");
             int s = 0;
            int j = 0;
            
            
            
            
            Matcher matcherweb = patternweb.matcher(inputst);
            
            while (matcherweb.find()) {
                prova2 = prova2 + input.substring(j, matcherweb.start()) + "<a href=\"" + matcherweb.group() + "\">" + matcherweb.group() + "</a>";

                s = matcherweb.end();
                j = s;
            }

            stringamod = prova2 + inputst.substring(s, inputst.length());
Matcher matcherqr = patternqr.matcher(stringamod);
           
                        
            while (matcherqr.find()){
                  ByteArrayOutputStream outQR = QRCode.from(matcherqr.group(3)).to(ImageType.PNG).stream();
         

         
 try {
     dirName = request.getServletContext().getRealPath(""); 
     dirName = dirName + "/qr";  
     File folder = new File(dirName); 
     if (!folder.isDirectory()) { folder.mkdir(); }
            FileOutputStream fout = new FileOutputStream(new File(
                   dirName+"/" + matcherqr.group(3)+".jpg"));
 
            fout.write(outQR.toByteArray());
 
            fout.flush();
            fout.close();
 
        } catch (FileNotFoundException e) {
            // Do Logging
        } catch (IOException e) {
            // Do Logging
        }
      
                 testoqr = testoqr + matcherqr.group(1) +"<a href=\"http://www."+ matcherqr.group(3)+"\">"+matcherqr.group(3)+"</a> <img src='qr/"+ matcherqr.group(3)+".jpg' width='150' height='150'></img>";
                    m=matcherqr.end(3);
                    
                                       
            }
            
            if (m != 0) {
                m = m + 2;
            }
          
                 
           
            
            if (testoqr.equals("")) {
                stringamod = stringamod;
            } else {
                 sottostringaqr = inputst.substring(m, stringamod.length());
                stringamod= testoqr + sottostringaqr;
            }
            Matcher matcher = pattern.matcher(stringamod);

            int n = 0;
            while (matcher.find()) {

                if (manager.cercanomefile(matcher.group(2))) {
                    prova = prova + matcher.group(1) + "<a href='DownloadFile?nomefile=" + matcher.group(2) + "'>" + matcher.group(2) + "</a>";
                    n = matcher.end(2);
                } else {
                    prova = prova + matcher.group(1) + "<a href='http://www." + matcher.group(2) + "'>" + matcher.group(2) + "</a>";
                    n = matcher.end(2);
                }

            }
            if (n != 0) {
                n = n + 2;
            }
            sottostringa = stringamod.substring(n, stringamod.length());
            if (prova.equals("")) {
                testo = stringamod;
            } else {
                testo = prova + sottostringa;
            }
            prova = "";

            nomefile = this.manager.prendinomefile(idpost);
            if (!nomefile.isEmpty()) {
                testo = testo + "<br>File: ";
                for (int i = 0; i < nomefile.size(); i++) {
                    testo = testo + "<a href=\"DownloadFile?nomefile=" + nomefile.get(i) + "\">" + nomefile.get(i) + "</a>   ";
                }
            }
            
            
            // gestione dell'inserimento di un post all'interno di un gruppo

    manager.insertpost(iduser, idgroup, testo);

            response.sendRedirect("ViewGroup?idgroup=" + idgroup + "&iduser=" + iduser);
        } catch (IOException e) {
            e.printStackTrace();

        }
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
