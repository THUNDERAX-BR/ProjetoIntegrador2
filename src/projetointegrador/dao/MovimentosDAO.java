package projetointegrador.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import projetointegrador.objects.Movimentos;

public class MovimentosDAO {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public MovimentosDAO(Connection connection) {
        this.connection = connection;
    }

    public List<String> listarFiltroBuscarAutores() {
        List<String> lista = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT DISTINCT(nome) FROM movimentos ORDER BY nome");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lista.add(resultSet.getString("nome"));
            }
            return lista;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao listar movimentos");
            return null;
        } finally {
            desconectar();
        }
    }

    public List<Movimentos> listarGerenciar(String busca) {
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar os movimentos.");
            return null;
        } finally {
            desconectar();
        }
    }

    public Movimentos getMovimento(int id) {
        try {
            preparedStatement = connection.prepareStatement("SELECT nome FROM movimentos WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            Movimentos movimento = new Movimentos();
            if (resultSet.next()) {
                movimento.setNome(resultSet.getString("nome"));
            }
            return movimento;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao exibir o movimento.");
            return null;
        } finally {
            desconectar();
        }
    }

    public void cadastrar(String nome) {
        try {
            preparedStatement = connection.prepareStatement("SELECT id FROM movimentos WHERE nome = ?");
            preparedStatement.setString(1, nome);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Movimento já existe.");
            } else {
                preparedStatement = connection.prepareStatement("INSERT INTO movimentos(nome) VALUES (?)");
                preparedStatement.setString(1, nome);
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Movimento cadastrado.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar movimento.");
        } finally {
            desconectar();
        }
    }

    public void alterar(int id, String nome) {
        try {
            preparedStatement = connection.prepareStatement("SELECT id FROM movimentos WHERE nome = ?");
            preparedStatement.setString(1, nome);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Movimento já existe.");
            } else {
                preparedStatement = connection.prepareStatement("UPDATE movimentos SET nome = ? WHERE id = ?");
                preparedStatement.setString(1, nome);
                preparedStatement.setInt(2, id);
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Movimento alterado.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao alterar movimento.");
        } finally {
            desconectar();
        }
    }

    public void excluir(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM movimentos WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Movimento excluído.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir movimento.");
        } finally {
            desconectar();
        }
    }

    public List<String> listarCadastroAutor(int id) {
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar os movimentos.");
            return null;
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
