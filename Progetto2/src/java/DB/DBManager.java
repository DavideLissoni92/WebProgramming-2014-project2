/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import static DB.DBManager.db;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DBManager implements Serializable {

    //La connessione col Database
    public static Connection db;

    public DBManager(String dburl) throws SQLException {
        try {

            Class.forName("org.apache.derby.jdbc.ClientDriver", true, getClass().getClassLoader());

            db = DriverManager.getConnection(dburl);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Utente autenticazione(String username, String password) throws SQLException {

        ResultSet rs;

        try {

            String query = "SELECT ID,USERNAME,PASSWORD FROM UTENTE WHERE USERNAME=? AND PASSWORD=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setString(1, username);
            st.setString(2, password);

            rs = st.executeQuery();

            try {

                if (rs.next()) {
                    Utente utente = new Utente();
                    utente.setName(rs.getString(2));
                    utente.setPswd(rs.getString(3));
                    utente.setid(rs.getInt(1));
                    return utente;

                } else {
                    return null;
                }
            } finally {
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void shutdown() throws SQLException {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String trovaavatar(int id) {
        ResultSet rs;
        try {
            String query = "SELECT AVATAR FROM UTENTE WHERE ID=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {

                return rs.getString(1);
            } else {
                return "errore";

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "errore";
        }
    }

    public List<Gruppo> invitigruppo(int id) {
        List<Gruppo> mygroups = new ArrayList<Gruppo>();
        ResultSet rs;
        try {
            String query = "SELECT GRUPPO.ID,GRUPPO.NOME,GRUPPO.IDPROPR,GRUPPO.DATACREAZ "
                    + "FROM GRUPPO JOIN INVITO ON GRUPPO.ID=IDGRUPPO "
                    + "WHERE IDUTENTE=? AND ACCETTATO=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, id);
            st.setBoolean(2, false);
            rs = st.executeQuery();
            while (rs.next()) {
                Gruppo g = new Gruppo();
                g.setName(rs.getString(2));
                g.setdatacreazione(rs.getString(4));
                g.setid(rs.getInt(1));
                g.setidadmin(rs.getInt(3));
                mygroups.add(g);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return mygroups;
    }

    public ArrayList<String> aggiornamenti(int id) {
        ArrayList<String> aggiornamenti = new ArrayList<String>();
        try {
            ResultSet rs;
            String query = "SELECT DISTINCT NOME FROM GRUPPO, POST, UTENTE WHERE POST.DATA>=UTENTE.ULTIMOLOGIN AND UTENTE.ID=POST.IDUTENTE AND GRUPPO.ID=POST.IDGRUPPO AND UTENTE.ID=?";
            PreparedStatement st = DBManager.db.prepareStatement(query);
            st.setInt(1, id);

            rs = st.executeQuery();
            while (rs.next()) {
                aggiornamenti.add(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aggiornamenti;
    }

    public void aggiornamentoultimologin(Timestamp ts, int id) {
        try {

            String query = "UPDATE UTENTE SET ULTIMOLOGIN=? WHERE ID=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setTimestamp(1, ts);
            st.setInt(2, id);

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public List<Gruppo> mygroups(int id) {
        List<Gruppo> mygroups = new ArrayList<Gruppo>();
        ResultSet rs;
        try {
            String query = "SELECT DISTINCT GRUPPO.ID,GRUPPO.NOME,GRUPPO.IDPROPR,GRUPPO.DATACREAZ "
                    + "FROM GRUPPO_UTENTE,GRUPPO "
                    + "WHERE (IDGRUPPO=GRUPPO.ID AND GRUPPO_UTENTE.IDUTENTE=? AND GRUPPO_UTENTE.ELIMINATO=?) OR PUBBLICO=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, id);
            st.setBoolean(2, false);
            st.setBoolean(3, true);

            rs = st.executeQuery();
            while (rs.next()) {
                Gruppo g = new Gruppo();
                g.setName(rs.getString(2));
                g.setdatacreazione(rs.getString(4));
                g.setid(rs.getInt(1));
                g.setidadmin(rs.getInt(3));
                mygroups.add(g);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return mygroups;
    }

    public void inserisciGruppo(String nomeGruppo, int id) {

        try {

            String query = "INSERT INTO GRUPPO(NOME,IDPROPR,DATACREAZ) VALUES(?,?,CURRENT_DATE)";
            PreparedStatement st = db.prepareStatement(query);
            st.setString(1, nomeGruppo);
            st.setInt(2, id);
            st.executeUpdate();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int tornaidgroup(String nomeGruppo) {
        ResultSet rs;
        try {
            String query = "SELECT ID FROM GRUPPO WHERE NOME=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setString(1, nomeGruppo);

            rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    // funzione che inserisce il creatore del gruppo come amministratore, e quindi partecipante, al gruppo stesso
    public void inserisciGruppoUtente(int idgruppo, int id) {
        try {

            String query = "INSERT INTO GRUPPO_UTENTE(IDGRUPPO,IDUTENTE) VALUES(?,?)";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idgruppo);
            st.setInt(2, id);
            st.executeUpdate();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void setaccettato(int idgroup, int iduser) {

        try {

            String query = "UPDATE INVITO SET ACCETTATO=? WHERE IDGRUPPO=? AND IDUTENTE=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setBoolean(1, true);
            st.setInt(2, idgroup);
            st.setInt(3, iduser);
            st.executeUpdate();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rifiutainvito(int idgroup, int iduser) {
        int rs;
        try {

            String query = "DELETE FROM INVITO WHERE IDGRUPPO=? AND IDUTENTE=?";
            PreparedStatement st = DBManager.db.prepareStatement(query);
            st.setInt(1, idgroup);
            st.setInt(2, iduser);
            rs = st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int idadmin(int idgroup) {
        try {
            ResultSet rs2;
            String query2 = "SELECT IDPROPR FROM GRUPPO WHERE ID=?";
            PreparedStatement st2 = db.prepareStatement(query2);
            st2.setInt(1, idgroup);
            rs2 = st2.executeQuery();
            if (rs2.next()) {
                return Integer.parseInt(rs2.getString(1));
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Utente> utentiinviti(int idgruppo) {
        List<Utente> utenti = new ArrayList<Utente>();
        ResultSet rs;
        try {
            String query = "SELECT ID,USERNAME,PASSWORD FROM UTENTE WHERE ID NOT IN"
                    + "(SELECT IDUTENTE FROM GRUPPO_UTENTE WHERE IDGRUPPO=? AND ELIMINATO=?) AND ID NOT IN"
                    + "(SELECT IDUTENTE FROM INVITO WHERE IDGRUPPO=? )";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idgruppo);
            st.setBoolean(2, false);
            st.setInt(3, idgruppo);
            rs = st.executeQuery();
            while (rs.next()) {
                Utente u = new Utente();
                u.setName(rs.getString(2));
                u.setid(rs.getInt(1));
                u.setPswd(rs.getString(3));
                utenti.add(u);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utenti;
    }

    public boolean cercanomefile(String stringa) {
        try {
            ResultSet rs2;
            String query2 = "SELECT ID FROM POST_FILE WHERE NOME=?";
            PreparedStatement st2 = db.prepareStatement(query2);
            st2.setString(1, stringa);
            rs2 = st2.executeQuery();
            if (rs2.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String prendinome(int idgruppo) throws IOException {
        ResultSet rs;
        try {
            String query = "SELECT NOME FROM GRUPPO WHERE ID=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idgruppo);
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            } else {

                return null;

            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String trovanomepost(int idutentepost) {
        ResultSet rs;
        try {
            String query = "SELECT USERNAME FROM UTENTE WHERE ID=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idutentepost);
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            } else {
                return "errore";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "errore";
        }
    }

    public int prendiidpost() {
        try {
            ResultSet rs2;
            String query2 = "SELECT MAX(ID) FROM POST";
            PreparedStatement st2 = db.prepareStatement(query2);
            rs2 = st2.executeQuery();
            if (rs2.next()) {
                return Integer.parseInt(rs2.getString(1)) + 1;
            } else {
                return 0;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void insertpost(int iduser, int idgroup, String testo) {
        int rs;
        try {
            String query = "INSERT INTO POST(DATA,IDUTENTE,IDGRUPPO,TESTO) VALUES(CURRENT_TIMESTAMP,?,?,?)";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, iduser);
            st.setInt(2, idgroup);
            st.setString(3, testo);
            rs = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Rename(String newname, int id) {
        try {
            String query = "UPDATE GRUPPO SET NOME=? WHERE ID=?";
            PreparedStatement st = DBManager.db.prepareStatement(query);
            st.setString(1, newname);
            st.setInt(2, id);
            st.executeUpdate();
            st.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> prendinomefile(int idpost) {
        List<String> prendinomefile = new ArrayList<String>();
        ResultSet rs;
        try {
            String query = "SELECT NOME FROM POST_FILE WHERE IDPOST=? ";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idpost);
            rs = st.executeQuery();
            while (rs.next()) {
                String s;
                s = rs.getString(1);
                prendinomefile.add(s);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prendinomefile;
    }

    public List<Utente> utentipartecipanti(int idgruppo, int iduser) {
        List<Utente> utentipartecipanti = new ArrayList<Utente>();
        ResultSet rs;
        try {
            String query = "SELECT USERNAME,UTENTE.ID,PASSWORD FROM UTENTE,GRUPPO_UTENTE WHERE IDUTENTE=UTENTE.ID AND IDGRUPPO=? AND ELIMINATO=? AND IDUTENTE!=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idgruppo);
            st.setBoolean(2, false);
            st.setInt(3, iduser);
            rs = st.executeQuery();
            while (rs.next()) {
                Utente u = new Utente();
                u.setName(rs.getString(1));
                u.setid(rs.getInt(2));
                u.setPswd(rs.getString(3));
                utentipartecipanti.add(u);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utentipartecipanti;
    }

    public List<Post> mypost(int idgruppo) {
        List<Post> mypost = new ArrayList<Post>();
        ResultSet rs;
        try {
            String query = "SELECT POST.ID,DATA,IDUTENTE,TESTO, AVATAR, UTENTE.USERNAME FROM POST, UTENTE WHERE POST.IDUTENTE=UTENTE.ID AND IDGRUPPO=? ORDER BY DATA";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idgruppo);
            rs = st.executeQuery();
            while (rs.next()) {
                Post p = new Post();
                p.setId(rs.getInt(1));
                p.setData(rs.getString(2));
                p.setIdGruppo(idgruppo);
                p.setIdUtente(rs.getInt(3));
                p.setTesto(rs.getString(4));
                p.setAvatar(rs.getString(5));
                p.setNomeUtente(rs.getString(6));

                mypost.add(p);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mypost;
    }

    public int trovaidfile() {
        try {
            ResultSet rs2;
            String query2 = "SELECT MAX(ID) FROM POST_FILE ";
            PreparedStatement st2 = db.prepareStatement(query2);
            rs2 = st2.executeQuery();
            if (rs2.next()) {
                return Integer.parseInt(rs2.getString(1)) + 1;
            } else {
                return 1;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static int controllogruppo(int idgruppo, int idutente) {
        ResultSet rs;
        try {
            String query = "(SELECT ID FROM GRUPPO_UTENTE WHERE IDGRUPPO=? AND IDUTENTE=?)UNION(SELECT ID FROM GRUPPO WHERE PUBBLICO=? AND ID=? )";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idgruppo);
            st.setInt(2, idutente);
            st.setBoolean(3, true);
            st.setInt(4, idgruppo);
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return -1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

    }

    public static int controllogruppononloggato(int idgruppo, int idutente) {
        ResultSet rs;
        try {
            String query = "SELECT ID FROM GRUPPO WHERE PUBBLICO=? AND ID=?";
            PreparedStatement st = db.prepareStatement(query);

            st.setBoolean(1, true);
            st.setInt(2, idgruppo);
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return -1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void invitautente(int idgroup, int idutente) {
        try {
            String query = "INSERT INTO INVITO(IDGRUPPO,IDUTENTE) VALUES(?,?)";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idgroup);
            st.setInt(2, idutente);
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rimuoviutente(int idgroup, int ideliminato) {
        try {
            String query = "UPDATE GRUPPO_UTENTE SET ELIMINATO=TRUE WHERE IDGRUPPO=? AND IDUTENTE=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idgroup);
            st.setInt(2, ideliminato);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registrautente(String username, String password, String email) {
        try {
            String query = "INSERT INTO UTENTE(USERNAME,PASSWORD,EMAIL,ULTIMOLOGIN) VALUES(?,?,?,CURRENT_TIMESTAMP)";
            PreparedStatement st = db.prepareStatement(query);
            st.setString(1, username);
            st.setString(2, password);
            st.setString(3, email);
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String nomeutente(int idgroup) {
        try {

            ResultSet rs;
            String query = "SELECT NOME FROM GRUPPO WHERE ID=?";
            PreparedStatement st = db.prepareStatement(query);

            st.setInt(1, idgroup);
            rs = st.executeQuery();
            while (rs.next()) {
                return rs.getString(1);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public List<Utente> listautenti(int idgroup) {
        List<Utente> utenti = new ArrayList<Utente>();
        try {
            ResultSet rs;
            String query = "SELECT USERNAME, UTENTE.ID FROM UTENTE, GRUPPO_UTENTE WHERE IDGRUPPO=? AND UTENTE.ID=GRUPPO_UTENTE.IDUTENTE";

            PreparedStatement st = db.prepareStatement(query);

            st.setInt(1, idgroup);
            rs = st.executeQuery();

            while (rs.next()) {
                Utente u = new Utente();
                u.setName(rs.getString(1));
                u.setid(rs.getInt(2));
                utenti.add(u);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utenti;
    }

    public void gruppopubblico(int idgroup) {
        try {
            String query = "UPDATE GRUPPO SET PUBBLICO=TRUE WHERE ID=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idgroup);

            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cambiainpubblicoprivato(int idgroup, boolean pubblico) {
        try {

            String query = "UPDATE GRUPPO SET PUBBLICO=? WHERE ID=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setBoolean(1, pubblico);
            st.setInt(2, idgroup);

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void eliminautentigruppo(int idgroup) {
        try {
            String query = "UPDATE GRUPPO_UTENTE SET ELIMINATO=TRUE WHERE IDGRUPPO=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idgroup);

            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean trovasepubblico(int idgroup) {
        try {

            ResultSet rs;
            String query = "SELECT ID FROM GRUPPO WHERE ID=? AND PUBBLICO=?";
            PreparedStatement st = db.prepareStatement(query);

            st.setInt(1, idgroup);
            st.setBoolean(2, true);
            rs = st.executeQuery();
            if (rs.next()) {
                return true;
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public void cambioavatar(String avatar, int idutente) {
        int rs;
        try {
            String query = "UPDATE UTENTE SET AVATAR=? WHERE ID=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setString(1, avatar);
            st.setInt(2, idutente);

            rs = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean controllapswd(int iduser, String password) {
        ResultSet rs;

        try {

            String query = "SELECT USERNAME FROM UTENTE WHERE ID=? AND PASSWORD=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, iduser);
            st.setString(2, password);

            rs = st.executeQuery();
            if (rs.next()) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void inseriscinuovapassword(int iduser, String password) {
        try {

            String query = "UPDATE UTENTE SET PASSWORD=? WHERE ID=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setString(1, password);
            st.setInt(2, iduser);

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public boolean moderatore(int iduser) {
        try {

            ResultSet rs;
            String query = "SELECT MODERATORE FROM UTENTE WHERE ID=?";
            PreparedStatement st = db.prepareStatement(query);

            st.setInt(1, iduser);
            rs = st.executeQuery();
            while (rs.next()) {
                return rs.getBoolean(1);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public List<Gruppo> gruppimoderatore() {
        List<Gruppo> mygroups = new ArrayList<Gruppo>();
        ResultSet rs;
        try {
            String query = "SELECT GRUPPO.ID,GRUPPO.NOME,GRUPPO.IDPROPR,GRUPPO.DATACREAZ "
                    + "FROM GRUPPO ";
            PreparedStatement st = db.prepareStatement(query);

            rs = st.executeQuery();
            while (rs.next()) {
                Gruppo g = new Gruppo();
                g.setName(rs.getString(2));
                g.setdatacreazione(rs.getString(4));
                g.setid(rs.getInt(1));
                g.setidadmin(rs.getInt(3));
                mygroups.add(g);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return mygroups;
    }

    public static int controllogruppomod(int idgruppo, int idutente) {
        ResultSet rs;
        try {
            String query = "SELECT ID FROM GRUPPO_UTENTE WHERE IDGRUPPO=? AND IDUTENTE=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idgruppo);
            st.setInt(2, idutente);

            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public String emailutente(int idutente) {
        try {
            ResultSet rs;
            String query = "SELECT EMAIL FROM UTENTE WHERE ID=?";
            PreparedStatement st = DBManager.db.prepareStatement(query);
            st.setInt(1, idutente);
            rs = st.executeQuery();
            while (rs.next()) {
                return rs.getString(1);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public int trovaidutente(String nomeutente) {
        try {
            ResultSet rs;
            String query = "SELECT ID FROM UTENTE WHERE USERNAME=?";
            PreparedStatement st = DBManager.db.prepareStatement(query);
            st.setString(1, nomeutente);
            rs = st.executeQuery();
            while (rs.next()) {
                return rs.getInt(1);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    public void aggiornarecuperopassword(int idutente) {
        try {
            String query = "UPDATE UTENTE SET RECUPEROPASSWORD=CURRENT_TIMESTAMP WHERE ID=?";
            PreparedStatement st = DBManager.db.prepareStatement(query);
            st.setInt(1, idutente);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Timestamp tornarecuperopassword(int idutente) {
        try {
            ResultSet rs;
            String query = "SELECT RECUPEROPASSWORD FROM UTENTE WHERE ID=?";
            PreparedStatement st = DBManager.db.prepareStatement(query);
            st.setInt(1, idutente);
            rs = st.executeQuery();
            while (rs.next()) {
                return rs.getTimestamp(1);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public void aggiornapassword(int idutente, String nuovapassword) {
        try {
            String query = "UPDATE UTENTE SET PASSWORD=? WHERE ID=?";
            PreparedStatement st = DBManager.db.prepareStatement(query);
            st.setString(1, nuovapassword);
            st.setInt(2, idutente);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chiudigruppo(int id) {
        try {

            String query = "UPDATE GRUPPO SET CHIUSO=TRUE WHERE ID=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, id);

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public ArrayList<Boolean> gruppochiuso() {
        ArrayList<Boolean> chiuso = new ArrayList<Boolean>();
        ResultSet rs;
        try {
            String query = "SELECT GRUPPO.CHIUSO "
                    + "FROM GRUPPO ";
            PreparedStatement st = db.prepareStatement(query);

            rs = st.executeQuery();
            while (rs.next()) {
                chiuso.add(rs.getBoolean(1));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return chiuso;
    }

    public ArrayList<Integer> allgroupspost() {
        ArrayList<Integer> post = new ArrayList<Integer>();
        ResultSet rs;
        try {

            String query = "SELECT COUNT(POST.ID) "
                    + "FROM POST RIGHT JOIN GRUPPO ON POST.IDGRUPPO=GRUPPO.ID "
                    + "GROUP BY GRUPPO.ID ";
            PreparedStatement st = db.prepareStatement(query);

            rs = st.executeQuery();
            while (rs.next()) {
                post.add(rs.getInt(1));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return post;
    }

    public ArrayList<Integer> allgroupsusers() {
        ArrayList<Integer> allgroupsnome = new ArrayList<Integer>();
        ResultSet rs;
        try {
            String query = "SELECT COUNT(IDUTENTE) "
                    + "FROM GRUPPO LEFT JOIN GRUPPO_UTENTE "
                    + "ON GRUPPO_UTENTE.IDGRUPPO=GRUPPO.ID "
                    + "GROUP BY GRUPPO.ID";
            PreparedStatement st = db.prepareStatement(query);

            rs = st.executeQuery();
            while (rs.next()) {
                allgroupsnome.add(rs.getInt(1));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return allgroupsnome;
    }

    public ArrayList<GruppoMod> groups() {
        ArrayList<GruppoMod> groups = new ArrayList<GruppoMod>();

        ResultSet rs3;
        try {
            String query3 = "select gruppo.NOME,avatar, gruppo.PUBBLICO, gruppo.CHIUSO, gruppo.id from gruppo, utente where gruppo.IDPROPR=utente.ID";

            PreparedStatement st3 = db.prepareStatement(query3);

            rs3 = st3.executeQuery();
            while (rs3.next()) {

                GruppoMod g = new GruppoMod();

                g.setAvatar(rs3.getString(2));
                g.setName(rs3.getString(1));
                g.setPubblico(rs3.getBoolean(3));
                g.setChiuso(rs3.getBoolean(4));
                g.setid(rs3.getInt(5));
                groups.add(g);
            }

            rs3.close();
            st3.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    public boolean controllochiudi(int idgroup) {
        boolean chiuso=true;
        ResultSet rs;

        try {
            
            String query = "SELECT GRUPPO.CHIUSO "
                    + "FROM GRUPPO "
                    + "WHERE ID=?";

            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idgroup);
            rs = st.executeQuery();
            if (rs.next()){
            chiuso = rs.getBoolean(1);}
            rs.close();
            st.close();
            

        } catch (SQLException e) {
            e.printStackTrace();
        }

        
        return chiuso;
    }



}