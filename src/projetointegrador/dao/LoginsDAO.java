package projetointegrador.dao;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import projetointegrador.objects.LoginFactory;
import projetointegrador.objects.Logins;
import tools.Criptografador;

public class LoginsDAO {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public LoginsDAO(Connection connection) {
        this.connection = connection;
    }

    public Logins validarLogin(String login, String senha) throws Exception {
        try {
            if (!login.isEmpty() && !senha.isEmpty()) {
                String senhaCriptografada = Criptografador.md5(senha);
                preparedStatement = connection.prepareStatement("SELECT * FROM Logins WHERE login = ? AND senha = ?");
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, senhaCriptografada);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    LoginFactory fabrica = new LoginFactory();
                    Logins loginRetorno = fabrica.getLogin(resultSet.getString("acesso").charAt(0));
                    loginRetorno.setLogin(resultSet.getString("login"));
                    return loginRetorno;
                } else {
                    throw new Exception("Login inválido ou senha incorreta.");
                }
            } else {
                throw new Exception("Os campos Login e Senha devem ser prenchidos.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao validar login");
            throw new Exception("Erro ao validar login");
        } finally {
            desconectar();
        }
    }

    public List<Logins> listarGerenciar(String busca) throws Exception {
        try {
            String statement = "SELECT id, login, acesso FROM logins";
            if (!busca.equals("")) {
                statement = statement + " WHERE (login LIKE ? OR id = ?)";
            }
            statement = statement + " ORDER BY id";
            preparedStatement = connection.prepareStatement(statement);
            if (!busca.equals("")) {
                preparedStatement.setString(1, "%" + busca + "%");
                preparedStatement.setString(2, busca);
            }
            resultSet = preparedStatement.executeQuery();
            ArrayList<Logins> lista = new ArrayList();
            while (resultSet.next()) {
                LoginFactory fabrica = new LoginFactory();
                Logins loginGerenciar = fabrica.getLogin(resultSet.getString("acesso").charAt(0));
                loginGerenciar.setLogin(resultSet.getString("login"));
                loginGerenciar.setId(resultSet.getInt("id"));
                lista.add(loginGerenciar);
            }
            return lista;
        } catch (SQLException e) {
            System.out.println("Erro ao listar os logins.");
            throw new Exception("Erro ao listar os logins.");
        } finally {
            desconectar();
        }
    }

    public void excluir(int id) throws Exception {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM logins WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao excluir login.");
            throw new Exception("Erro ao excluir login.");
        } finally {
            desconectar();
        }
    }

    public void cadastrar(String login, String senha, String acesso) throws Exception {
        try {
            if (!login.isEmpty() && !senha.isEmpty() && acesso != null) {
                if (senha.length() >= 8) {
                    preparedStatement = connection.prepareStatement("SELECT id FROM logins WHERE login = ?");
                    preparedStatement.setString(1, login);
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        throw new Exception("Login já existe");
                    } else {
                        String senhaCriptografada = Criptografador.md5(senha);
                        preparedStatement = connection.prepareStatement("INSERT INTO logins(login, senha, acesso) VALUES (?, ?, ?)");
                        preparedStatement.setString(1, login);
                        preparedStatement.setString(2, senhaCriptografada);
                        preparedStatement.setString(3, acesso);
                        preparedStatement.executeUpdate();
                    }
                } else {
                    throw new Exception("A senha deve ter 8 ou mais caracteres.");
                }
            } else {
                throw new Exception("Preencha todos os campos e selecione o tipo de acesso.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar login.");
            throw new Exception("Erro ao cadastrar login.");
        } finally {
            desconectar();
        }
    }

    private void desconectar() {
        try {
            connection.close();
        } catch (SQLException ex) {
        }
    }
}
