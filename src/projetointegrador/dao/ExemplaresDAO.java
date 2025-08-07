package projetointegrador.dao;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import projetointegrador.objects.Editoras;
import projetointegrador.objects.Exemplares;
import projetointegrador.objects.Livros;

public class ExemplaresDAO {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public ExemplaresDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Exemplares> listarExemplaresLivro(int id) {
        List<Exemplares> lista = new ArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT id, localizacao FROM exemplares WHERE livros_id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Exemplares exemplar = new Exemplares(resultSet.getInt("id"), resultSet.getString("localizacao"));
                lista.add(exemplar);
            }
            return lista;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao listar exemplares");
            return null;
        } finally {
            desconectar();
        }
    }

    public List<Exemplares> listarGerenciar(String busca) {
        try {
            String statement = "SELECT e.id, l.titulo, ed.nome AS editora, e.localizacao FROM exemplares e INNER JOIN livros l INNER JOIN editoras ed WHERE e.livros_id = l.id AND l.editoras_id = ed.id";
            if (!busca.equals("")) {
                statement = statement + " AND (l.titulo LIKE ? OR e.id = ?)";
            }
            statement = statement + " ORDER BY id";
            preparedStatement = connection.prepareStatement(statement);
            if (!busca.equals("")) {
                preparedStatement.setString(1, "%" + busca + "%");
                preparedStatement.setString(2, busca);
            }
            resultSet = preparedStatement.executeQuery();
            List<Exemplares> lista = new ArrayList();
            while (resultSet.next()) {
                Editoras editora = new Editoras();
                editora.setNome(resultSet.getString("editora"));
                Livros livro = new Livros();
                livro.setTitulo(resultSet.getString("titulo"));
                livro.setEditora(editora);
                Exemplares exemplar = new Exemplares(resultSet.getInt("id"), livro, resultSet.getString("localizacao"));
                lista.add(exemplar);
            }
            return lista;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar os exemplares.");
            return null;
        } finally {
            desconectar();
        }
    }

    public Exemplares getExemplar(int id) {
        try {
            preparedStatement = connection.prepareStatement("SELECT localizacao FROM exemplares WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            Exemplares exemplar = new Exemplares();
            if (resultSet.next()) {
                exemplar.setLocalizacao(resultSet.getString("localizacao"));
            }
            return exemplar;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao exibir o exemplar.");
            return null;
        } finally {
            desconectar();
        }
    }

    public int getIdLivro(int id) {
        try {
            preparedStatement = connection.prepareStatement("SELECT livros_id FROM exemplares WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("livros_id");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao verificar o livro do exemplar.");
            return -1;
        } finally {
            desconectar();
        }
    }

    public void cadastrar(int idLivro, String local) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO exemplares(livros_id, localizacao) VALUES (?,?)");
            preparedStatement.setInt(1, idLivro);
            preparedStatement.setString(2, local);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Exemplar cadastrado.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar o exemplar.");
        } finally {
            desconectar();
        }
    }

    public void alterar(int id, int idLivro, String local) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE exemplares SET livros_id = ?, localizacao = ? WHERE id = ?");
            preparedStatement.setInt(1, idLivro);
            preparedStatement.setString(2, local);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Exemplar alterado.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao alterar o exemplar.");
        } finally {
            desconectar();
        }
    }

    public void excluir(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM exemplares WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Exemplar excluído.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir exemplar.");
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
