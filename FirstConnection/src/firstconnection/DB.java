package firstconnection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DB {

  
    final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver"; 
   
    final String URL = "jdbc:derby:sampleDB;create=true";
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
        //Ez a teherautó a hídon
        if (conn != null) {
            try {
                createStatement = conn.createStatement();
            } catch (SQLException ex) {
                System.out.println("Valami baj van a createStatement létrehozásakor.");
                System.out.println("" + ex);
            }
        }
        //Lekérjük az adatbázis metaadatait        
        try {
            dbmd = conn.getMetaData();
        } catch (SQLException ex) {
            System.out.println("Valami baj van az adatbázis leírás létrehozásakor.");
            System.out.println("" + ex);
        }
        
        /*Shema: APP
          Adattábla neve: USERS
         */
        //megnézzük, hogy adott tábla létezik-e az adatbázisban, ha nem létrehozzuk
        try {
            ResultSet rs = dbmd.getTables(null, "APP", "USERS", null);
            if (!rs.next()) {
                createStatement.execute("create table users(name varchar(20), address varchar(20))");
            }
        } catch (SQLException ex) {
            System.out.println("Valami baj van az adattábla létrehozásakor.");
            System.out.println("" + ex);
        }      
    }
    
    public void addUser(String name, String address){
        try {
            //createStatement.execute("insert into  users values('test1' , 'Budapest'), ('test2', 'Budapest')");
            //A változat
            //String sql = "insert into  users values('" + name + "','" + address + "')";
            //createStatement.execute(sql);
            //B változat
            String sql = "insert into  users values(?,?)";
            PreparedStatement pstm = conn.prepareStatement(sql);
            //pstm.setInt() -> ha szám stb.
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
    
    public ArrayList<User> getAllUsers(){
        String sql = "select * from users";
        ArrayList<User> users = null;
        try {
            ResultSet rs = createStatement.executeQuery(sql);
            users = new ArrayList<User>();
             while (rs.next()) {
                 User user = new User(rs.getString("name"), rs.getString("address"));
                 users.add(user);
             }
        } catch (SQLException ex) {
              System.out.println("Valami baj van a user kiolvasásakor");
              System.out.println("" + ex);
        }
        return users;
    }
    
    public void addUser(User user){
         try {
            String sql = "insert into  users values(?,?)";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, user.getName());
            pstm.setString(2, user.getAddress());
            pstm.execute();
        } catch (SQLException ex) {
            System.out.println("Valami baj van a user hozzáadásakor");
            System.out.println("" + ex);
        }
    }
}
