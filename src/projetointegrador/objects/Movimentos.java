package projetointegrador.objects;

public class Movimentos {

    private int id;
    private String nome;

    public Movimentos() {
    }

    public Movimentos(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
