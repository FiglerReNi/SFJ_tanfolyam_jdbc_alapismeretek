package firstconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {
    /*Library-hez plusz önyvtár adása - jelen esetben szükségünk lesz a driver-re
      Libraries -> jobb egérgomb -> Add Library -> Java DB Driver
    */
    final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver"; //ez a derbyhez szükséges driver
    final String URL = "";
    final String USERNAME = "";
    final String PASSWORD = "";
    
    public DB(){
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException ex) {
            System.out.println("" + ex);
        }
    }
}
