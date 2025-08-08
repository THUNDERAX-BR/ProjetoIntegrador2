package projetointegrador.objects;

import java.awt.Image;

public class Autores {

    private int id;
    private String nome;
    private String dataNascimento;
    private String dataFalecimento;
    private Movimentos movimento;
    private String biografia;
    private Image foto;

    public Autores() {
    }

    public Autores(int id, String nome, Movimentos movimento) {
        this.id = id;
        this.nome = nome;
        this.movimento = movimento;
    }

    public Autores(int id, String nome, String dataNascimento, String dataFalecimento, Movimentos movimento, String biografia, Image foto) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.dataFalecimento = dataFalecimento;
        this.movimento = movimento;
        this.biografia = biografia;
        this.foto = foto;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getDataFalecimento() {
        return dataFalecimento;
    }

    public void setDataFalecimento(String dataFalecimento) {
        this.dataFalecimento = dataFalecimento;
    }

    public Movimentos getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimentos movimento) {
        this.movimento = movimento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Image getFoto() {
        return foto;
    }

    public void setFoto(Image foto) {
        this.foto = foto;
    }
}
