package tradealpha.LISTENER;

import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.tomcat.jdbc.pool.DataSource;


public class DBConnectionListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
       //final String DB_URL = "jdbc:mysql://localhost:3306/alpha-trade?serverTimezone=Africa/Lagos";
       final String DB_URL = "jdbc:mysql://mysql3000.mochahost.com/tradesal_alpha-trade?serverTimezone=Africa/Lagos";
       //final String DB_USERNAME = "root";
       final String DB_USERNAME = "tradesal_alpha";
       //final String DB_PASSWORD = "Drizzydrakeovo1";
       final String DB_PASSWORD = "q0?EKOwD?K=e";
        
       final DataSource ds = new DataSource();
       ds.setUrl(DB_URL);
       ds.setUsername(DB_USERNAME);
       ds.setPassword(DB_PASSWORD);
       ds.setInitialSize(10);
       ds.setMaxActive(100);
       ds.setMinEvictableIdleTimeMillis(30000);
       ds.setTestOnBorrow(true);
       ds.setRemoveAbandoned(true);
       ds.setRemoveAbandonedTimeout(60);
       ds.setValidationInterval(30000);
       ds.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        
        sce.getServletContext().setAttribute("DATA_SOURCE", ds);
        
        try(Connection con = ds.getConnection()){
            System.out.println("Database Connected");
        }
        catch(SQLException e){
            System.err.println(e);
        } //To change body of generated methods, choose Tools | Templat
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DataSource ds = (DataSource)sce.getServletContext().getAttribute("DATA_SOURCE");
        ds.close();
    }
}

