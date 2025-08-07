package projetointegrador.dao;

import java.sql.Statement;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import projetointegrador.objects.Autores;
import projetointegrador.objects.Categorias;
import projetointegrador.objects.Editoras;
import projetointegrador.objects.Livros;

public class LivrosDAO {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public LivrosDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Livros> listarBuscarLivros(String busca, String anoInicial, String anoFinal, String categoria) {
        try {
            String statement = "SELECT l.id, l.titulo, a.nome AS autor, e.nome AS editora FROM livros l INNER JOIN autores a INNER JOIN editoras e LEFT JOIN categorias_livros cl ON l.id = cl.livros_id LEFT JOIN categorias c ON c.id = cl.categorias_id WHERE l.autores_id = a.id AND l.editoras_id = e.id";
            if (!busca.equals("")) {
                statement = statement + " AND l.titulo LIKE ?";
            }
            if (!anoInicial.equals("")) {
                statement = statement + " AND l.ano >= ?";
            }
            if (!anoFinal.equals("")) {
                statement = statement + " AND l.ano <= ?";
            }
            if (!categoria.equals("")) {
                statement = statement + " AND c.nome = ?";
            }

            statement = statement + " GROUP BY id ORDER BY titulo";
            preparedStatement = connection.prepareStatement(statement);

            int i = 1;
            if (!busca.equals("")) {
                preparedStatement.setString(i, "%" + busca + "%");
                i++;
            }
            if (!anoInicial.equals("")) {
                preparedStatement.setString(i, anoInicial);
                i++;
            }
            if (!anoFinal.equals("")) {
                preparedStatement.setString(i, anoFinal);
                i++;
            }
            if (!categoria.equals("")) {
                preparedStatement.setString(i, categoria);
                i++;
            }

            resultSet = preparedStatement.executeQuery();
            List<Livros> lista = new ArrayList<>();
            while (resultSet.next()) {
                Autores autor = new Autores();
                autor.setNome(resultSet.getString("autor"));
                Editoras editora = new Editoras();
                editora.setNome(resultSet.getString("editora"));
                Livros livro = new Livros(resultSet.getInt("id"), resultSet.getString("titulo"), autor, editora);
                lista.add(livro);
            }
            return lista;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar os livros.");
            return null;
        } finally {
            desconectar();
        }
    }

    public List<Livros> listarGerenciar(String busca) {
        try {
            String statement = "SELECT l.id, l.titulo, a.nome AS autor, e.nome AS editora FROM livros l INNER JOIN autores a INNER JOIN editoras e WHERE l.autores_id = a.id AND l.editoras_id = e.id";
            if (!busca.equals("")) {
                statement = statement + " AND (l.titulo LIKE ? OR l.id = ?)";
            }
            statement = statement + " ORDER BY id";
            preparedStatement = connection.prepareStatement(statement);
            if (!busca.equals("")) {
                preparedStatement.setString(1, "%" + busca + "%");
                preparedStatement.setString(2, busca);
            }
            resultSet = preparedStatement.executeQuery();
            List<Livros> lista = new ArrayList<>();
            while (resultSet.next()) {
                Autores autor = new Autores();
                autor.setNome(resultSet.getString("autor"));
                Editoras editora = new Editoras();
                editora.setNome(resultSet.getString("editora"));
                Livros livro = new Livros(resultSet.getInt("id"), resultSet.getString("titulo"), autor, editora);
                lista.add(livro);
            }
            return lista;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar os livros.");
            return null;
        } finally {
            desconectar();
        }
    }

    public List<Livros> exbirLivrosAutor(int id) {
        try {
            preparedStatement = connection.prepareStatement("SELECT l.id, l.titulo, e.nome FROM livros l INNER JOIN editoras e INNER JOIN autores a WHERE l.editoras_id = e.id AND l.autores_id = a.id AND a.id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            List<Livros> lista = new ArrayList<>();
            while (resultSet.next()) {
                Editoras editora = new Editoras();
                editora.setNome(resultSet.getString("nome"));
                Livros livro = new Livros(resultSet.getInt("id"), resultSet.getString("titulo"), editora);
                lista.add(livro);
            }
            return lista;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar os livros.");
            return null;
        } finally {
            desconectar();
        }
    }

    public Livros exibirLivro(int id) {
        Livros livro = new Livros();
        Autores autor = new Autores();
        Editoras editora = new Editoras();
        InputStream input;
        BufferedImage imagem;
        ImageIcon capa;
        try {
            preparedStatement = connection.prepareStatement("SELECT l.titulo, l.ano, l.descricao, a.id, a.nome AS autor, e.nome AS editora, l.capa FROM livros l INNER JOIN autores a INNER JOIN editoras e WHERE l.id = ? AND a.id = l.autores_id AND e.id = l.editoras_id");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                editora.setNome(resultSet.getString("editora"));
                livro.setEditora(editora);
                autor.setId(resultSet.getInt("id"));
                autor.setNome(resultSet.getString("autor"));
                livro.setAutor(autor);
                livro.setId(id);
                livro.setTitulo(resultSet.getString("titulo"));
                livro.setAno(resultSet.getInt("ano"));
                livro.setDescricao(resultSet.getString("descricao"));
                if (resultSet.getBinaryStream("capa") != null) {
                    input = resultSet.getBinaryStream("capa");
                    imagem = ImageIO.read(input);
                    Image ajuste = imagem.getScaledInstance(225, 300, Image.SCALE_SMOOTH);
                    capa = new ImageIcon(ajuste);
                    livro.setCapa(capa);
                }

                return livro;
            } else {
                return null;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao exibir livro.");
            return null;
        } finally {
            desconectar();
        }
    }

    public void cadastrar(String titulo, int ano, int autor, int editora, String sinopse, File capa, List<Categorias> categorias) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO livros(titulo, ano, autores_id, editoras_id, descricao, capa) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, titulo);
            preparedStatement.setInt(2, ano);
            preparedStatement.setInt(3, autor);
            preparedStatement.setInt(4, editora);
            preparedStatement.setString(5, sinopse);
            FileInputStream input = null;
            if (capa != null) {
                input = new FileInputStream(capa);
            }
            preparedStatement.setBinaryStream(6, input);
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            int id = resultSet.getInt(1);
            for (Categorias c : categorias) {
                preparedStatement = connection.prepareStatement("INSERT INTO categorias_livros(livros_id, categorias_id) VALUES (?,?)");
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, c.getId());
                preparedStatement.executeUpdate();
            }
            JOptionPane.showMessageDialog(null, "Livro cadastrado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar livro.");
        } finally {
            desconectar();
        }
    }

    public void alterar(int id, String titulo, int ano, int autor, int editora, String sinopse, File capa, List<Categorias> categorias) {
        try {
            if (capa != null) {
                preparedStatement = connection.prepareStatement("UPDATE livros SET titulo = ?, ano = ?, autores_id = ?, editoras_id = ?, descricao = ?, capa = ? WHERE id = ?");
            } else {
                preparedStatement = connection.prepareStatement("UPDATE livros SET titulo = ?, ano = ?, autores_id = ?, editoras_id = ?, descricao = ? WHERE id = ?");
            }
            preparedStatement.setString(1, titulo);
            preparedStatement.setInt(2, ano);
            preparedStatement.setInt(3, autor);
            preparedStatement.setInt(4, editora);
            preparedStatement.setString(5, sinopse);
            if (capa != null) {
                FileInputStream input = new FileInputStream(capa);
                preparedStatement.setBinaryStream(6, input);
                preparedStatement.setInt(7, id);
            } else {
                preparedStatement.setInt(6, id);
            }
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("DELETE FROM categorias_livros WHERE livros_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            for (Categorias c : categorias) {
                preparedStatement = connection.prepareStatement("INSERT INTO categorias_livros(livros_id, categorias_id) VALUES (?,?)");
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, c.getId());
                preparedStatement.executeUpdate();
            }
            JOptionPane.showMessageDialog(null, "Livro alterado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao alterar livro.");
        } finally {
            desconectar();
        }
    }

    public void excluir(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM livros WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Livro excluído.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir livro.");
        } finally {
            desconectar();
        }
    }

    public List<String> listarGerenciarExemplares(int id) {
        try {
            int idBanido;
            List<String> lista = new ArrayList();
            if (id != -1) {
                preparedStatement = connection.prepareStatement("SELECT l.id, l.titulo, e.nome AS editora FROM exemplares ed INNER JOIN livros l INNER JOIN editoras e WHERE l.editoras_id = e.id AND l.id = ed.livros_id AND ed.id = ? ORDER BY titulo");
                preparedStatement.setInt(1, id);
                resultSet = preparedStatement.executeQuery();
                resultSet.next();
                idBanido = resultSet.getInt("id");
                String livroAtual = idBanido + "/" + resultSet.getString("titulo") + "/" + resultSet.getString("editora");
                lista.add(livroAtual);
            } else {
                idBanido = -1;
            }
            preparedStatement = connection.prepareStatement("SELECT l.id, l.titulo, e.nome AS editora FROM livros l INNER JOIN editoras e WHERE l.editoras_id = e.id AND l.id != ? ORDER BY titulo");
            preparedStatement.setInt(1, idBanido);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String nomeLivro = resultSet.getString("id") + "/" + resultSet.getString("titulo") + "/" + resultSet.getString("editora");
                lista.add(nomeLivro);
            }
            return lista;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar livros.");
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
