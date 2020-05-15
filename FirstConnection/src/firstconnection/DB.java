package firstconnection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
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
    //Belső adatbázisunk lesz, így ez nem feltétlenül kell
    //final String USERNAME = "";
    //final String PASSWORD = "";
    private Connection conn = null;
    private Statement createStatement = null;
    private DatabaseMetaData dbmd = null;
    
    public DB() {
        //Ez a híd a kód és az adatbázis között       
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
            ResultSet rs = dbmd.getTables(null, "APP", "USERS", null);
            //ha rs1-nek van következő értéke: rs.next()
            if (!rs.next()) {
                createStatement.execute("create table users(name varchar(20), address varchar(20))");
            }
        } catch (SQLException ex) {
            System.out.println("Valami baj van az adattábla létrehozásakor.");
            System.out.println("" + ex);
        }      
    }
    
    
    /*típusok: 
        - alap/sima: Statement (A változathoz): meghívható sql string nélkül
        - PreparedStatement (B változathoz): egyből kell sql string a meghívásánál 
            Ez ellenőrzi a paramétereket, hogy tényleg az van benne amilyen típus kell
            Gyorsabb lekérdezések
        - speciális: adatbázis függvények meghívásáért felelős (ezek a függvények akkor jók, ha pl többféle platformra írunk egy
            programot, többféle programnyelven, ami ugyanazt az adatbázist használja, akkor nem kell minden nyelven megírni a leérdezést, hanem 
            megcsinálhatjuk adatbázis függvényként és mindegyikben csak meghívjuk).
    */
    public void addUser(String name, String address){
        try {
            //createStatement.execute("insert into  users values('test1' , 'Budapest'), ('test2', 'Budapest')");
            //A változat
            //String sql = "insert into  users values('" + name + "','" + address + "')";
            //createStatement.execute(sql);
            //B változat
            //Itt az sql Stringbe nem írhatok özvetlenül paramétert
            String sql = "insert into  users values(?,?)";
            PreparedStatement pstm = conn.prepareStatement(sql);
            //pstm.setInt() -> ha szám stb.
            //itt 1-től indul a számozás
            pstm.setString(1, name);
            pstm.setString(2, address);
            pstm.execute();
        } catch (SQLException ex) {
            System.out.println("Valami baj van a user hozzáadásakor");
            System.out.println("" + ex);
        }
    }
    
    public void showAllUsers(){
        //sima adatot kérünk vissza
        String sql = "select * from users";
        try {
            ResultSet rs = createStatement.executeQuery(sql);
             while (rs.next()) {
                 String name = rs.getString("name");
                 String address = rs.getString("address");
                 System.out.println(name + " | " + address);
                 
             }
        } catch (SQLException ex) {
              System.out.println("Valami baj van a user kiolvasásakor");
              System.out.println("" + ex);
        }
    }
    
    public void showUsersMeta(){
        //pl users tábla column nevek kiiratása --> metaadatok kérünk most vissza
        String sql = "select * from users";
        //sima eredménytábla
        ResultSet rs = null;
        //metaadatok eredménytáblája
        ResultSetMetaData rsmd = null;
        try {
             rs = createStatement.executeQuery(sql);
             //lekérdezés eredményéből veszi ki a metaadatokat amik hozzá tartoznak
             rsmd = rs.getMetaData();
             //hány oszlopa van a táblának
             //az sql mindig 1-től indul
             int columnCount = rsmd.getColumnCount();
             for (int i = 1; i <= columnCount; i++) {
                 System.out.print(rsmd.getColumnName(i) + " | ");
            }
        } catch (SQLException ex) {
              System.out.println("Valami baj van a user kiolvasásakor");
              System.out.println("" + ex);
        }
        
    }
}
