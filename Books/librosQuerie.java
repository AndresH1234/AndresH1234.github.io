import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import javafx.event.ActionEvent; 

public class librosQuerie {
    private static final String URL = "jdbc:derby:books";

    private Connection connection;

    private PreparedStatement selectTodosLosLibros;
    private PreparedStatement selectLibrosPorIsbn;
    private PreparedStatement selectLibrosPorAutor;
    private PreparedStatement selectLibrosPorTitulo;
    private PreparedStatement selectLibrosPorAnio;

    private PreparedStatement insertNuevoLibro;
    private PreparedStatement updateLibro;
    private PreparedStatement deleteLibro;

    public librosQuerie(){
        try{
            connection = DriverManager.getConnection(URL);

            selectTodosLosLibros = connection.prepareStatement(
                    "SELECT * FROM books"
                    );
            selectLibrosPorIsbn = connection.prepareStatement(
                    "SELECT * FROM books WHERE isbn = ?"
                    );
            selectLibrosPorAutor = connection.prepareStatement(
                    "SELECT * FROM books WHERE autor = ?"
                    );
            selectLibrosPorTitulo = connection.prepareStatement(
                    "SELECT * FROM books WHERE titulo = ?"
                    );
            selectLibrosPorAnio = connection.prepareStatement(
                    "SELECT * FROM books WHERE anioedicion = ?"
                    );

            insertNuevoLibro = connection.prepareStatement(
                    "INSERT INTO books (isbn, titulo, autor, anioedicion) VALUES (?, ?, ?, ?)"
                    );
            updateLibro = connection.prepareStatement(
                    "UPDATE books SET " +
                    "titulo = ?, autor = ?, anioedicion = ? " +
                    "WHERE isbn = ?"
                    );
            deleteLibro = connection.prepareStatement(
                    "DELETE FROM books WHERE isbn = ?"
                    );
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            System.exit(1);
        }
    }

    public List<libros> getAllLibros(){
        List<libros> results = null;
        ResultSet resultSet = null;

        try{
            resultSet = selectTodosLosLibros.executeQuery();
            results = new ArrayList<libros>();

            while(resultSet.next()){
                results.add(new libros(
                            resultSet.getInt("isbn"),
                            resultSet.getString("titulo"),
                            resultSet.getString("autor"),
                            resultSet.getInt("anioedicion")
                            ));
            }
            return results;
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            return null;
        } 
    }

    public List<libros> getLibrosByIsbn(int isbn){
        List<libros> results = null;
        ResultSet resultSet = null;

        try{
            selectLibrosPorIsbn.setInt(1, isbn);
            resultSet = selectLibrosPorIsbn.executeQuery();
            results = new ArrayList<libros>();

            while(resultSet.next()){
                results.add(new libros(
                            resultSet.getInt("isbn"),
                            resultSet.getString("titulo"),
                            resultSet.getString("autor"),
                            resultSet.getInt("anioedicion")
                            ));
            }
            return results;
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            return null;
        } 
    }

    public List<libros> getLibrosByAutor(String autor){
        List<libros> results = null;
        ResultSet resultSet = null;

        try{
            selectLibrosPorAutor.setString(1, autor);
            resultSet = selectLibrosPorAutor.executeQuery();
            results = new ArrayList<libros>();

            while(resultSet.next()){
                results.add(new libros(
                            resultSet.getInt("isbn"),
                            resultSet.getString("titulo"),
                            resultSet.getString("autor"),
                            resultSet.getInt("anioedicion")
                            ));
            }
            return results;
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            return null;
        } 
    }

    public List<libros> getLibrosByTitulo(String titulo){
        List<libros> results = null;
        ResultSet resultSet = null;

        try{
            selectLibrosPorTitulo.setString(1, titulo);
            resultSet = selectLibrosPorTitulo.executeQuery();
            results = new ArrayList<libros>();

            while(resultSet.next()){
                results.add(new libros(
                            resultSet.getInt("isbn"),
                            resultSet.getString("titulo"),
                            resultSet.getString("autor"),
                            resultSet.getInt("anioedicion")
                            ));
            }
            return results;
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            return null;
        } 
    }
    
    public List<libros> getLibrosByAnio(int anio){
        List<libros> results = null;
        ResultSet resultSet = null;

        try{
            selectLibrosPorAnio.setInt(1, anio);
            resultSet = selectLibrosPorAnio.executeQuery();
            results = new ArrayList<libros>();

            while(resultSet.next()){
                results.add(new libros(
                            resultSet.getInt("isbn"),
                            resultSet.getString("titulo"),
                            resultSet.getString("autor"),
                            resultSet.getInt("anioedicion")
                            ));
            }
            return results;
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            return null;
        } 
    }

    public int addLibros(int isbn, String titulo, String autor, int anio){

        try{
            insertNuevoLibro.setInt(1, isbn);
            insertNuevoLibro.setString(2, titulo);
            insertNuevoLibro.setString(3, autor);
            insertNuevoLibro.setInt(4, anio);
            

            return insertNuevoLibro.executeUpdate();
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            return 0;
        }
    }

    public int deleteLibro(int isbn){
        try{
            deleteLibro.setInt(1, isbn);
            return deleteLibro.executeUpdate();
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            return 0;
        }
    }

    public int updateLibro(int isbn, String titulo, String autor, int anio) {
        try {
            updateLibro.setString(1, titulo);   // Set new ISBN
            updateLibro.setString(2, autor);     // Set new title
            updateLibro.setInt(3, anio);       // Set new author
            updateLibro.setInt(4, isbn);    // Set new year of edition                // Identify the book by ISBN
    
            return updateLibro.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return 0;
        }
    }
}
