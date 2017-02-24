
package listener;

import DB.DBManager;
import java.sql.SQLException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;

public class WebappContextListener implements ServletContextListener {

    // funzione che inizializza il database e lo collega al sito
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String dburl = "jdbc:derby://localhost:1527/Dbsecondo;user=progetto2;password=progetto2;";

        try {
            DBManager manager = new DBManager(dburl);
            sce.getServletContext().setAttribute("dbmanager", manager);
        } catch (SQLException e) {
            e.printStackTrace();
        }
      
    }

    // funzione che chiude il database in caso di exception
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            DBManager.shutdown();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
