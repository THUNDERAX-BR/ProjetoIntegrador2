package projetointegrador.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import projetointegrador.objects.Movimentos;

public class MovimentosDAO {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public MovimentosDAO(Connection connection) {
        this.connection = connection;
    }

    public List<String> listarFiltroBuscarAutores() throws Exception {
        List<String> lista = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT DISTINCT(nome) FROM movimentos ORDER BY nome");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lista.add(resultSet.getString("nome"));
            }
            return lista;
        } catch (SQLException ex) {
            System.out.println("Erro ao listar movimentos.");
            throw new Exception("Erro ao listar movimentos.");
        } finally {
            desconectar();
        }
    }

    public List<Movimentos> listarGerenciar(String busca) throws Exception {
        try {
            String statement = "SELECT id, nome FROM movimentos";
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
            List<Movimentos> lista = new ArrayList<>();
            while (resultSet.next()) {
                Movimentos movimento = new Movimentos(resultSet.getInt("id"), resultSet.getString("nome"));
                lista.add(movimento);
            }
            return lista;
        } catch (SQLException e) {
            System.out.println("Erro ao listar movimentos.");
            throw new Exception("Erro ao listar movimentos.");
        } finally {
            desconectar();
        }
    }

    public Movimentos getMovimento(int id) throws Exception {
        try {
            preparedStatement = connection.prepareStatement("SELECT nome FROM movimentos WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            Movimentos movimento = new Movimentos();
            if (resultSet.next()) {
                movimento.setNome(resultSet.getString("nome"));
            }
            return movimento;
        } catch (SQLException e) {
            System.out.println("Erro ao exibir o movimento.");
            throw new Exception("Erro ao exibir o movimento.");
        } finally {
            desconectar();
        }
    }

    public void cadastrar(String nome) throws Exception {
        try {
            preparedStatement = connection.prepareStatement("SELECT id FROM movimentos WHERE nome = ?");
            preparedStatement.setString(1, nome);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                throw new Exception("Movimento já existe.");
            } else {
                preparedStatement = connection.prepareStatement("INSERT INTO movimentos(nome) VALUES (?)");
                preparedStatement.setString(1, nome);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar movimento.");
            throw new Exception("Erro ao cadastrar movimento.");
        } finally {
            desconectar();
        }
    }

    public void alterar(int id, String nome) throws Exception {
        try {
            preparedStatement = connection.prepareStatement("SELECT id FROM movimentos WHERE nome = ?");
            preparedStatement.setString(1, nome);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                throw new Exception("Movimento já existe.");
            } else {
                preparedStatement = connection.prepareStatement("UPDATE movimentos SET nome = ? WHERE id = ?");
                preparedStatement.setString(1, nome);
                preparedStatement.setInt(2, id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao alterar movimento.");
            throw new Exception("Erro ao alterar movimento.");
        } finally {
            desconectar();
        }
    }

    public void excluir(int id) throws Exception {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM movimentos WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao excluir movimento.");
            throw new Exception("Erro ao excluir movimento.");
        } finally {
            desconectar();
        }
    }

    public List<String> listarCadastroAutor(int id) throws Exception {
        try {
            int idBanido;
            List<String> lista = new ArrayList();
            if (id != -1) {
                preparedStatement = connection.prepareStatement("SELECT m.id, m.nome FROM movimentos m INNER JOIN autores a WHERE a.movimentos_id = m.id AND a.id = ?");
                preparedStatement.setInt(1, id);
                resultSet = preparedStatement.executeQuery();
                resultSet.next();
                idBanido = resultSet.getInt("id");
                String movimentoAtual = idBanido + "/" + resultSet.getString("nome");
                lista.add(movimentoAtual);
            } else {
                idBanido = -1;
            }
            preparedStatement = connection.prepareStatement("SELECT id, nome FROM movimentos WHERE id!= ? ORDER BY nome");
            preparedStatement.setInt(1, idBanido);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String nomeMovimento = resultSet.getString("id") + "/" + resultSet.getString("nome");
                lista.add(nomeMovimento);
            }
            return lista;
        } catch (SQLException e) {
            System.out.println("Erro ao listar os movimentos.");
            throw new Exception("Erro ao listar os movimentos.");
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
