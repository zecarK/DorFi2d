import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyConnection {
    
    public static Connection getConnection(){
    Connection con = null;
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/dorfi_2d",
            "root",
            ""
        );
        System.out.println("Database connected!");
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return con;
}
}
