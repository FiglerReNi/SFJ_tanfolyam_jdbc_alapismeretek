package firstconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {
    /*Library-hez plusz önyvtár adása - jelen esetben szükségünk lesz a driver-re
      Libraries -> jobb egérgomb -> Add Library -> Java DB Driver (kellhet a GlassFish Srever:
      Tools->Servers)
    */
    final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver"; //ez a derbyhez szükséges driver
    //nincsen server a gépen, és sehol ebben a példában, így belső útvonalat adunk meg
    //sampleDB -> adatbázis neve
    //create true -> ha nincs még ilyen létrehozza
    final String URL = "jdbc:derby:sampleDB;create=true";
//  Belső adatbázisunk lesz, így ez nem feltétlenül kell
//    final String USERNAME = "";
//    final String PASSWORD = "";
    
    public DB(){
        try {
 //           Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Connection conn = DriverManager.getConnection(URL);
            System.out.println("A híd létrejött.");
        } catch (SQLException ex) {
            System.out.println("Valami baj van.");
            System.out.println("" + ex);
        }
    }
}
