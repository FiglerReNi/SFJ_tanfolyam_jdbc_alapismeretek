package firstconnection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


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

    public DB() {
        //Ez a híd a kód és az adatbázis között
        Connection conn = null;
        try {
            //Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            conn = DriverManager.getConnection(URL);
            System.out.println("A híd létrejött.");
        } catch (SQLException ex) {
            System.out.println("Valami baj van a connection létrehozásakor.");
            System.out.println("" + ex);
        }

        /*Statement: ezzel mondjuk meg, hogy adatot küldünk vagy kérünk
       Kétféle adatot nyerhetünk ki:
            - metaadatok: az adatbázisról adnak információt pl hány adattábla van
            - konkrét adatok*/
        //Ez a teherautó a hídon
        Statement createStatement = null;
        if (conn != null) {
            try {
                createStatement = conn.createStatement();
            } catch (SQLException ex) {
                System.out.println("Valami baj van a createStatement létrehozásakor.");
                System.out.println("" + ex);
            }
        }
        //Üres e az adatbázis? (Első futtatáskor)
        //Lekérjük az adatbázis metaadatait
        DatabaseMetaData dbmd = null;
        try {
            dbmd = conn.getMetaData();
        } catch (SQLException ex) {
            System.out.println("Valami baj van az adatbázis leírás létrehozásakor.");
            System.out.println("" + ex);
        }
        //Az adatbázis információk ebben a resultsetben jönnek vissza, táblázatszerűen tárolja
        //az adatokat.
        /*Shema: APP
          Adattábla neve: USERS
         */
        //megnézzük, hogy adott tábla létezik-e az adatbázisban, ha nem létrehozzuk
        try {
            ResultSet rs1 = dbmd.getTables(null, "APP", "USERS", null);
            //ha rs1-nek van következő értéke: rs1.next()
            if (!rs1.next()) {
                createStatement.execute("create table users(name varchar(20), age varchar(20))");
            }
        } catch (SQLException ex) {
            System.out.println("Valami baj van az adattábla létrehozásakor.");
            System.out.println("" + ex);
        }
    }
}
