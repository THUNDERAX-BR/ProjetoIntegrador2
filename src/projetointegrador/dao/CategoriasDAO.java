package projetointegrador.dao;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import projetointegrador.objects.Categorias;

public class CategoriasDAO {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public CategoriasDAO(Connection connection) {
        this.connection = connection;
    }

    public List<String> listarBuscarLivros() throws Exception {
        List<String> lista = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT DISTINCT(nome) FROM categorias ORDER BY nome");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lista.add(resultSet.getString("nome"));
            }
            return lista;
        } catch (SQLException ex) {
            System.out.println("Erro ao listar as categorias.");
            throw new Exception("Erro ao listar as categorias.");
        } finally {
            desconectar();
        }
    }

    public List<String> listarCadastroLivro() throws Exception {
        try {
            List<String> lista = new ArrayList();
            preparedStatement = connection.prepareStatement("SELECT id, nome FROM categorias ORDER BY nome");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String nomeCategoria = resultSet.getString("id") + "/" + resultSet.getString("nome");
                lista.add(nomeCategoria);
            }
            return lista;
        } catch (SQLException e) {
            System.out.println("Erro ao listar as categorias.");
            throw new Exception("Erro ao listar as categorias.");
        } finally {
            desconectar();
        }
    }

    public List<Categorias> getCategoriasLivro(int id) throws Exception {
        List<Categorias> lista = new ArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT c.id, c.nome FROM categorias c INNER JOIN categorias_livros cl INNER JOIN livros l WHERE cl.categorias_id = c.id AND cl.livros_id = l.id AND l.id = ? ORDER BY c.nome");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Categorias categoria = new Categorias();
                categoria.setId(resultSet.getInt("id"));
                categoria.setNome(resultSet.getString("nome"));
                lista.add(categoria);
            }
            return lista;
        } catch (SQLException ex) {
            System.out.println("Erro ao listar as categorias.");
            throw new Exception("Erro ao listar as categorias.");
        } finally {
            desconectar();
        }
    }

    public Categorias getCategoria(int id) throws Exception {
        try {
            preparedStatement = connection.prepareStatement("SELECT nome FROM categorias WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            Categorias categoria = new Categorias();
            if (resultSet.next()) {
                categoria.setNome(resultSet.getString("nome"));
            }
            return categoria;
        } catch (SQLException ex) {
            System.out.println("Erro ao listar as categorias.");
            throw new Exception("Erro ao listar as categorias.");
        } finally {
            desconectar();
        }
    }

    public List<Categorias> listarGerenciar(String busca) throws Exception {
        try {
            String statement = "SELECT id, nome FROM categorias";
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
            List<Categorias> lista = new ArrayList<>();
            while (resultSet.next()) {
                Categorias categoria = new Categorias(resultSet.getInt("id"), resultSet.getString("nome"));
                lista.add(categoria);
            }
            return lista;
        } catch (SQLException e) {
            System.out.println("Erro ao listar as categorias.");
            throw new Exception("Erro ao listar as categorias.");
        } finally {
            desconectar();
        }
    }

    public void excluir(int id) throws Exception {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM categorias WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao listar as categorias.");
            throw new Exception("Erro ao listar as categorias.");
        } finally {
            desconectar();
        }
    }

    public void cadastrar(String nome) throws Exception {
        try {
            preparedStatement = connection.prepareStatement("SELECT id FROM categorias WHERE nome = ?");
            preparedStatement.setString(1, nome);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                throw new Exception("Categoria já existe.");
            } else {
                preparedStatement = connection.prepareStatement("INSERT INTO categorias(nome) VALUES (?)");
                preparedStatement.setString(1, nome);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar categoria.");
            throw new Exception("Erro ao cadastrar categoria.");
        } finally {
            desconectar();
        }
    }

    public void alterar(int id, String nome) throws Exception {
        try {
            preparedStatement = connection.prepareStatement("SELECT id FROM categorias WHERE nome = ?");
            preparedStatement.setString(1, nome);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                throw new Exception("Categoria já existe.");
            } else {
                preparedStatement = connection.prepareStatement("UPDATE categorias SET nome = ? WHERE id = ?");
                preparedStatement.setString(1, nome);
                preparedStatement.setInt(2, id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao alterar categoria.");
            throw new Exception("Erro ao alterar categoria.");
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

    public void reconectar(Connection connection) {
        this.connection = connection;
    }
}
