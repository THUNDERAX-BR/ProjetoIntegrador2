package projetointegrador.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Conector {

    private static final String url = "jdbc:mysql://localhost:3306/kumoridb";
    private static final String user = "kumori";
    private static final String senha = "a7X@pL#9zWq1!Km$Tf8&nB2^RsE0*VhY";

    public static Connection conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, senha);
            return connection;
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao tentar conectar ao banco de dados.");
            return null;
        }
    }
}
