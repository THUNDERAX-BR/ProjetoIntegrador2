package projetointegrador.objects;

public class Exemplares {

    private int id;
    private Livros livro;
    private String localizacao;

    public Exemplares() {
    }

    public Exemplares(int id, String localizacao) {
        this.id = id;
        this.localizacao = localizacao;
    }

    public Exemplares(int id, Livros livro, String localizacao) {
        this.id = id;
        this.livro = livro;
        this.localizacao = localizacao;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Livros getLivro() {
        return livro;
    }

    public void setLivro(Livros livro) {
        this.livro = livro;
    }
}
