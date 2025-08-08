package projetointegrador.dao;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import projetointegrador.objects.Autores;
import projetointegrador.objects.Movimentos;

public class AutoresDAO {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public AutoresDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Autores> listarBuscarAutores(String busca, String movimento) throws Exception {
        try {
            String statement = "SELECT a.id, a.nome, m.nome AS movimento FROM autores a INNER JOIN movimentos m WHERE m.id = a.movimentos_id";
            if (!busca.equals("")) {
                statement = statement + " AND a.nome LIKE ?";
            }
            if (!movimento.equals("")) {
                statement = statement + " AND m.nome = ?";
            }
            statement = statement + " ORDER BY a.nome";

            preparedStatement = connection.prepareStatement(statement);

            int i = 1;
            if (!busca.equals("")) {
                preparedStatement.setString(i, "%" + busca + "%");
                i++;
            }
            if (!movimento.equals("")) {
                preparedStatement.setString(i, movimento);
            }

            resultSet = preparedStatement.executeQuery();
            List<Autores> lista = new ArrayList();
            while (resultSet.next()) {
                Movimentos movimentoResultado = new Movimentos();
                movimentoResultado.setNome(resultSet.getString("movimento"));
                Autores autor = new Autores(resultSet.getInt("id"), resultSet.getString("nome"), movimentoResultado);
                lista.add(autor);
            }
            return lista;
        } catch (SQLException e) {
            System.out.println("Erro ao listar os autores.");
            throw new Exception("Erro ao listar os autores.");
        } finally {
            desconectar();
        }
    }

    public List<Autores> listarGerenciar(String busca) throws Exception {
        try {
            String statement = "SELECT a.id, a.nome, m.nome AS movimento FROM autores a INNER JOIN movimentos m WHERE m.id = a.movimentos_id";
            if (!busca.equals("")) {
                statement = statement + " AND (a.nome LIKE ? OR a.id = ?)";
            }
            statement = statement + " ORDER BY id";
            preparedStatement = connection.prepareStatement(statement);
            if (!busca.equals("")) {
                preparedStatement.setString(1, "%" + busca + "%");
                preparedStatement.setString(2, busca);
            }
            resultSet = preparedStatement.executeQuery();
            List<Autores> lista = new ArrayList();
            while (resultSet.next()) {
                Movimentos movimento = new Movimentos();
                movimento.setNome(resultSet.getString("movimento"));
                Autores autor = new Autores(resultSet.getInt("id"), resultSet.getString("nome"), movimento);
                lista.add(autor);
            }
            return lista;
        } catch (SQLException e) {
            System.out.println("Erro ao listar os autores.");
            throw new Exception("Erro ao listar os autores.");
        } finally {
            desconectar();
        }
    }

    public Autores exibirAutor(int id) throws Exception {
        Autores autor = new Autores();
        Movimentos movimento = new Movimentos();
        InputStream input;
        BufferedImage imagem;
        Date dataSql;
        LocalDate data;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");;

        try {
            preparedStatement = connection.prepareStatement("SELECT a.nome, a.data_nascimento, a.data_falecimento, m.nome AS movimento, a.biografia, a.foto FROM autores a INNER JOIN movimentos m WHERE m.id = a.movimentos_id AND a.id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                movimento.setNome(resultSet.getString("movimento"));
                autor.setMovimento(movimento);

                autor.setNome(resultSet.getString("nome"));

                dataSql = resultSet.getDate("data_nascimento");
                data = dataSql.toLocalDate();
                autor.setDataNascimento(data.format(formatter));

                if (resultSet.getDate("data_falecimento") != null) {
                    dataSql = resultSet.getDate("data_falecimento");
                    data = dataSql.toLocalDate();
                    autor.setDataFalecimento(data.format(formatter));
                }
                autor.setBiografia(resultSet.getString("biografia"));

                if (resultSet.getBinaryStream("foto") != null) {
                    input = resultSet.getBinaryStream("foto");
                    imagem = ImageIO.read(input);
                    autor.setFoto(imagem);
                }

                return autor;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao exibir o autor.");
            throw new Exception("Erro ao exibir o autor.");
        } finally {
            desconectar();
        }
    }

    public void excluir(int id) throws Exception {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM autores WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao excluir autor.");
            throw new Exception("Erro ao excluir autor.");
        } finally {
            desconectar();
        }
    }

    public void cadastrar(String nome, int idmovimento, String nascimento, String falecimento, String biografia, File imagem) throws Exception {
        try {
            SimpleDateFormat entrada = new SimpleDateFormat("dd/MM/yyyy");
            entrada.setLenient(false);
            preparedStatement = connection.prepareStatement("INSERT INTO autores(nome, data_nascimento, data_falecimento, movimentos_id, biografia, foto) VALUES (?, ?, ?, ?, ?, ?)");
            java.util.Date data = entrada.parse(nascimento);
            Date dataNascimento = new Date(data.getTime());
            Date dataFalecimento = null;
            if (falecimento != null) {
                data = entrada.parse(falecimento);
                dataFalecimento = new Date(data.getTime());
            }
            preparedStatement.setString(1, nome);
            preparedStatement.setDate(2, dataNascimento);
            preparedStatement.setDate(3, dataFalecimento);
            preparedStatement.setInt(4, idmovimento);
            preparedStatement.setString(5, biografia);
            FileInputStream input = null;
            if (imagem != null) {
                input = new FileInputStream(imagem);
            }
            preparedStatement.setBinaryStream(6, input);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar autor.");
            throw new Exception("Erro ao cadastrar autor.");
        } finally {
            desconectar();
        }
    }

    public void alterar(int id, String nome, int idMovimento, String nascimento, String falecimento, String biografia, File imagem) throws Exception {
        try {
            SimpleDateFormat entrada = new SimpleDateFormat("dd/MM/yyyy");
            entrada.setLenient(false);
            if (imagem != null) {
                preparedStatement = connection.prepareStatement("UPDATE autores SET nome = ?, data_nascimento = ?, data_falecimento = ?, movimentos_id = ?, biografia = ?, foto = ? WHERE id = ?");
            } else {
                preparedStatement = connection.prepareStatement("UPDATE autores SET nome = ?, data_nascimento = ?, data_falecimento = ?, movimentos_id = ?, biografia = ? WHERE id = ?");
            }
            java.util.Date data = entrada.parse(nascimento);
            Date dataNascimento = new Date(data.getTime());
            Date dataFalecimento = null;
            if (falecimento != null) {
                data = entrada.parse(falecimento);
                dataFalecimento = new Date(data.getTime());
            }
            preparedStatement.setString(1, nome);
            preparedStatement.setDate(2, dataNascimento);
            preparedStatement.setDate(3, dataFalecimento);
            preparedStatement.setInt(4, idMovimento);
            preparedStatement.setString(5, biografia);
            if (imagem != null) {
                FileInputStream input = new FileInputStream(imagem);
                preparedStatement.setBinaryStream(6, input);
                preparedStatement.setInt(7, id);
            } else {
                preparedStatement.setInt(6, id);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao alterar autor.");
            throw new Exception("Erro ao alterar autor.");
        } finally {
            desconectar();
        }
    }

    public List<String> listarCadastroLivro(int id) throws Exception {
        try {
            int idBanido;
            List<String> lista = new ArrayList();
            if (id != -1) {
                preparedStatement = connection.prepareStatement("SELECT a.id, a.nome FROM autores a INNER JOIN livros l WHERE l.autores_id = a.id AND l.id = ?");
                preparedStatement.setInt(1, id);
                resultSet = preparedStatement.executeQuery();
                resultSet.next();
                idBanido = resultSet.getInt("id");
                String autorAtual = idBanido + "/" + resultSet.getString("nome");
                lista.add(autorAtual);
            } else {
                idBanido = -1;
            }
            preparedStatement = connection.prepareStatement("SELECT id, nome FROM autores WHERE id!= ? ORDER BY nome");
            preparedStatement.setInt(1, idBanido);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String nomeAutor = resultSet.getString("id") + "/" + resultSet.getString("nome");
                lista.add(nomeAutor);
            }
            return lista;
        } catch (SQLException e) {
            System.out.println("Erro ao listar os autores.");
            throw new Exception("Erro ao listar os autores.");
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
