package projetointegrador.objects;

import java.awt.Image;
import java.util.List;

public class Livros {

    private int id;
    private String titulo;
    private Autores autor;
    private int ano;
    private String descricao;
    private List<Categorias> categorias;
    private Editoras editora;
    private Image capa;

    public Livros() {
    }

    public Livros(int id, String titulo, Editoras editora) {
        this.id = id;
        this.titulo = titulo;
        this.editora = editora;
    }

    public Livros(int id, String titulo, Autores autor, Editoras editora) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.editora = editora;
    }

    public Livros(int id, String titulo, Autores autor, int ano, String descricao, List<Categorias> categorias, Editoras editora) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
        this.descricao = descricao;
        this.categorias = categorias;
        this.editora = editora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autores getAutor() {
        return autor;
    }

    public void setAutor(Autores autor) {
        this.autor = autor;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Categorias> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categorias> categorias) {
        this.categorias = categorias;
    }

    public Editoras getEditora() {
        return editora;
    }

    public void setEditora(Editoras editora) {
        this.editora = editora;
    }

    public Image getCapa() {
        return capa;
    }

    public void setCapa(Image capa) {
        this.capa = capa;
    }
}
