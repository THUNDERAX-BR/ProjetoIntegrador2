package projetointegrador.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conector {

    private static final String url = "jdbc:mysql://localhost:3306/kumoridb";
    private static final String user = "kumori";
    private static final String senha = "a7X@pL#9zWq1!Km$Tf8&nB2^RsE0*VhY";

    public static Connection conectar() throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, senha);
            return connection;
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Erro ao tentar conectar ao banco de dados.");
            throw new Exception("Erro ao tentar conectar ao banco de dados.");
        }
    }
}
