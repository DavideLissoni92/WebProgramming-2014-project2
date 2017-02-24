<%@page import="DB.Post"%>
<%@page import="java.util.List"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="java.util.regex.Pattern"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <!DOCTYPE html>
    <html>
        <head>
            <title>Servlet ViewGroup</title>
            <link href='bootstrap.css' rel='stylesheet'>
            <link href='viewgroup.css' rel='stylesheet'>
            <link href='jquery-2.0.3.min' rel='stylesheet'>
        </head>
        <body>
            <c:set var="nomeGruppo" value="${param.nomeGruppo}"/>
            <c:set var="idgroup" value="${param.idgroup}"/>
            <c:set var="iduser" value="${param.iduser}"/>
            <c:set var="idadmin" value="${param.idadmin}"/>
            <c:set var="chiuso" value="${param.chiuso}"/>
            <c:set var="inter" value="${0}"/>




            <c:choose>
                <c:when test="${param.pubblico==false}">
                    <h1 class='sottotitoli'>

                        <c:out value="${nomeGruppo}"/>(gruppo privato)</h1>
                    </c:when>
                    <c:otherwise>
                    <h1 class='sottotitoli'><c:out value="${nomeGruppo}"/>(gruppo pubblico)</h1>
                </c:otherwise>
            </c:choose>
            <form action='InsertPost?idgroup=${idgroup}&iduser=${iduser}&idadmin=${idadmin}&idpost=${param.maxidpost}'method='POST'>
                <table border='0' class='table table-condensed table-hover'>

                    <%int i = 0;
                        List<Post> posts;
                        posts = (List<Post>) request.getAttribute("posts");
                    %>

                    <c:forEach var="posts" items="${posts}">
                        <tr><td>
                                <FONT SIZE='1'><c:out value="${posts.data}"/></font></td>
                            <td>
                                <img src='${posts.avatar}' width='50' height='50' />
                            </td>
                            <td><b><c:out value="${posts.nomeUtente}"/></b></td>
                            <td>
                                <c:choose>
                                    <c:when test="${idutente==0}">
                                        <%String testo = "";
                                            String prova, prova2;
                                            int end = 0;
                                            int j = 0;
                                            boolean vero = false;
                                            Pattern pattern = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
                                            Matcher matcher = pattern.matcher(posts.get(i).getTesto());
                                            while (matcher.find()) {
                                                while (matcher.group().charAt(j) != '@') {
                                                    j++;
                                                }
                                                j = j + 1;

                                                testo = testo + posts.get(i).getTesto().substring(end, matcher.start()) + matcher.group().substring(0, j) + "xxxxxxxxx";

                                                end = matcher.end();
                                                j = 0;
                                            }

                                            out.println(testo + posts.get(i).getTesto().substring(end, posts.get(i).getTesto().length()));

                                        %>
                                    </c:when>
                                    <c:otherwise>
                                        ${posts.testo}

                                    </c:otherwise>
                                </c:choose>



                            </td>
                        </tr>
                        <%i = i + 1;
                        %>
                    </c:forEach>

                </table>


                <c:choose>

                    <c:when test="${idutente==0}">
                    </c:when>
                    <c:when test="${(param.autentic==-1)&&(param.pubblico==false)}">

                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${chiuso==false}">

                                <table border='0' class ='table table-condensed' >
                                    <tr><td>     
                                            <input placeholder='Inserisci il testo del tuo post' class='form-control' type='text' autocomplete='Off' name='InputPost' >
                                            <br>
                                            <input type='submit' value='Inserisci post' name='BtnInserisciPost' class='btn btn-lg btn-primary btn-block'>
                                            </form>
                                        </td>
                                        <td>
                                            <form action='UploadFile?iduser=${iduser}&idgroup=${idgroup}&idpost=${param.maxidpost}&idadmin=${idadmin}'method='post' enctype='multipart/form-data'>
                                                <input class='btn btn-lg btn-block' type='file' name='file'/>
                                        </td><td>
                                            <input class='btn btn-lg btn-block' type='submit' value='Upload File'>
                                            </form>
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        Gruppo chiuso dal moderatore, non è possibile inserire nuovi post.
                                    </c:otherwise>
                                </c:choose>


                            </c:otherwise>




                        </c:choose>
                    </tr>
                </table>
                <br>
                <h4 class='sottotitoli'>Utenti partecipanti alla discussione</h4>
                <ul> 
                    <c:forEach var="utenti" items="${utenti}">
                        <li><c:out value="${utenti.name}"/></li>

                    </c:forEach>       
                </ul>




                <c:if test="${param.admin==true}">
                    <form action='AdminSetting?idgroup=${idgroup}&iduser=${iduser}'method='POST' class='spaziosotto'>  
                        <input type='submit' class='btn btn-lg  btn-primary' value='Impostazioni Amministratore' name='BtnImpostazioniAdmin'>
                    </form>
                </c:if>


                <c:choose>
                    <c:when test="${iduser!=0}">
                        <a href='Home2?iduser=${iduser}' class='btn btn-primary btn-lg' class='spaziosotto'>Home</a>
                    </c:when>
                    <c:otherwise>
                        <a href='MyGroups?iduser=${iduser}' class='btn btn-primary btn-lg' class='spaziosotto'>Home</a>
                    </c:otherwise>
                </c:choose>




            </div>



        </tr>
    </table>
    <br>


    </body>
    </html>