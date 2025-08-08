package projetointegrador.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import projetointegrador.objects.Editoras;

public class EditorasDAO {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public EditorasDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Editoras> listarGerenciar(String busca) throws Exception {
        try {
            String statement = "SELECT id, nome FROM editoras";
            if (!busca.equals("")) {
                statement = statement + " WHERE (nome LIKE ? OR id = ?)";
            }
            statement = statement + " ORDER BY id";
            preparedStatement = connection.prepareStatement(statement);
            if (!busca.equals("")) {
                preparedStatement.setString(1, "%" + busca + "%");
                preparedStatement.setString(2, busca);
            }
            resultSet = preparedStatement.executeQuery();
            List<Editoras> lista = new ArrayList<>();
            while (resultSet.next()) {
                Editoras editora = new Editoras(resultSet.getInt("id"), resultSet.getString("nome"));
                lista.add(editora);
            }
            return lista;
        } catch (SQLException e) {
            System.out.println("Erro ao listar as editoras.");
            throw new Exception("Erro ao listar as editoras.");
        } finally {
            desconectar();
        }
    }

    public void excluir(int id) throws Exception {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM editoras WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao excluir editora.");
            throw new Exception("Erro ao excluir editora.");
        } finally {
            desconectar();
        }
    }

    public Editoras getEditora(int id) throws Exception {
        try {
            preparedStatement = connection.prepareStatement("SELECT nome FROM editoras WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            Editoras editora = new Editoras();
            if (resultSet.next()) {
                editora.setNome(resultSet.getString("nome"));
            }
            return editora;
        } catch (SQLException ex) {
            System.out.println("Erro ao exibir editora.");
            throw new Exception("Erro ao exibir editora.");
        } finally {
            desconectar();
        }
    }

    public void cadastrar(String nome) throws Exception {
        try {
            preparedStatement = connection.prepareStatement("SELECT id FROM editoras WHERE nome = ?");
            preparedStatement.setString(1, nome);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                throw new Exception("Editora já existe.");
            } else {
                preparedStatement = connection.prepareStatement("INSERT INTO editoras(nome) VALUES (?)");
                preparedStatement.setString(1, nome);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar editora.");
            throw new Exception("Erro ao cadastrar editora.");
        } finally {
            desconectar();
        }
    }

    public void alterar(int id, String nome) throws Exception {
        try {
            preparedStatement = connection.prepareStatement("SELECT id FROM editoras WHERE nome = ?");
            preparedStatement.setString(1, nome);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                throw new Exception("Editora já existe.");
            } else {
                preparedStatement = connection.prepareStatement("UPDATE editoras SET nome = ? WHERE id = ?");
                preparedStatement.setString(1, nome);
                preparedStatement.setInt(2, id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao alterar editora.");
            throw new Exception("Erro ao alterar editora.");
        } finally {
            desconectar();
        }
    }

    public List<String> listarCadastroLivro(int id) throws Exception {
        try {
            int idBanido;
            List<String> lista = new ArrayList();
            if (id != -1) {
                preparedStatement = connection.prepareStatement("SELECT e.id, e.nome FROM editoras e INNER JOIN livros l WHERE l.editoras_id = e.id AND l.id = ?");
                preparedStatement.setInt(1, id);
                resultSet = preparedStatement.executeQuery();
                resultSet.next();
                idBanido = resultSet.getInt("id");
                String editoraAtual = idBanido + "/" + resultSet.getString("nome");
                lista.add(editoraAtual);
            } else {
                idBanido = -1;
            }
            preparedStatement = connection.prepareStatement("SELECT id, nome FROM editoras WHERE id!= ? ORDER BY nome");
            preparedStatement.setInt(1, idBanido);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String nomeEditora = resultSet.getString("id") + "/" + resultSet.getString("nome");
                lista.add(nomeEditora);
            }
            return lista;
        } catch (SQLException e) {
            System.out.println("Erro ao listar as editoras.");
            throw new Exception("Erro ao listar as editoras.");
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
