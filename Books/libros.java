import java.io.Serializable;

public class libros implements Serializable{
    int isbn;
    String titulo;
    String autor;
    int anio;

    public libros(int isbn, String titulo, String autor, int anio) {
        setIsbn(isbn);
        setTitulo(titulo);
        setAutor(autor);
        setAnio(anio);
    }
    
    public libros(){
        setIsbn(0);
        setTitulo("null");
        setAutor("null");
        setAnio(0);
    }

    public int getIsbn() {
        return isbn;
    }
    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public int getAnio() {
        return anio;
    }
    public void setAnio(int anio) {
        this.anio = anio;
    }

    @Override
    public String toString() {
        return isbn+ ", "+ titulo + ", "+ autor + ", " + anio;
    }
}