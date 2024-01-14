import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class servidor extends Application {

    private ServerSocket server;
    private TextArea textArea;
    
    private final librosQuerie libroQuerie = new librosQuerie();

    @Override
    public void start(Stage primaryStage) {
        textArea = new TextArea();
        textArea.setEditable(false);

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        Scene scene = new Scene(scrollPane, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Servidor");
        primaryStage.show();

        new Thread(() -> {
            try {
                server = new ServerSocket(8000); // Establecer el ServerSocket
                textArea.appendText("Server Started \n");

                while (true) {
                    // Esperar y aceptar una conexión de cliente
                    textArea.appendText("Esperando una conexión de cliente...\n");
                    Socket clientSocket = server.accept();
                    textArea.appendText("Cliente conectado desde " + clientSocket.getInetAddress() + "\n");
                    Handler handler = new Handler(clientSocket);
                    new Thread(handler).start();
                        
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }).start();
    }

    private class Handler implements Runnable {
        private Socket socket; // conexión con el cliente
        private DataInputStream input; // entrada desde el cliente
        private DataOutputStream output; // salida hacia el cliente

        // configurar la comunicación
        public Handler(Socket socket) {
            this.socket = socket; // establecer la conexión
            try{
                output = new DataOutputStream(socket.getOutputStream());
                input = new DataInputStream(socket.getInputStream());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        // enviar mensajes al cliente
        public void run() {
            try{
                while (true) {
                    String command = input.readUTF(); 
                    String[] parts = command.split(" ", 2);
                    if (parts[0].equals("GET_ALL")) {
                        textArea.appendText("Busco todos los libros...\n");
                        // Simula una lista de libros (cámbialo según tus datos)
                        List<libros> resultados = getAll();
                        ObjectOutputStream objectOutputToClient = new ObjectOutputStream(output);
                        objectOutputToClient.writeObject(resultados);
                        objectOutputToClient.flush(); 
                    }
                    else if (parts[0].equals("GET_ISBN")) {
                        textArea.appendText("Busco por isbn...\n");
                        // La solicitud es para buscar libros por ISBN
                        int isbn = Integer.parseInt(parts[1]); // 8 es la longitud de "GET_ISBN "
                        
                        // Lógica para buscar libros por ISBN y obtener la lista de resultados
                        List<libros> resultados = getByIsbn(isbn);
                        ObjectOutputStream objectOutputToClient = new ObjectOutputStream(output);
                        objectOutputToClient.writeObject(resultados);
                        objectOutputToClient.flush();
                    }
                    else if(parts[0].equals("GET_TITULO")) {
                        textArea.appendText("Busco por titulo...\n");
                        // La solicitud es para buscar libros por ISBN
                        String titulo = parts[1]; // 11 es la longitud
                        
                        // Lógica para buscar libros por ISBN y obtener la lista de resultados
                        List<libros> resultados = getByTitulo(titulo);
                        ObjectOutputStream objectOutputToClient = new ObjectOutputStream(output);
                        objectOutputToClient.writeObject(resultados);
                        objectOutputToClient.flush();

                    }
                    else if(parts[0].equals("GET_AUTOR")) {
                        textArea.appendText("Busco por autor...\n");
                        // La solicitud es para buscar libros por autor
                        String autor = parts[1]; // 11 es la longitud
                        
                        // Lógica para buscar libros por ISBN y obtener la lista de resultados
                        List<libros> resultados = getByAutor(autor);
                        ObjectOutputStream objectOutputToClient = new ObjectOutputStream(output);
                        objectOutputToClient.writeObject(resultados);
                        objectOutputToClient.flush();
                    }
                    else if (parts[0].equals("GET_ANIO")) {
                        textArea.appendText("Busco por anio...\n");
                        // La solicitud es para buscar libros por anio
                        int anio = Integer.parseInt(parts[1]); // 8 es la longitud de "GET_ISBN "
                        
                        // Lógica para buscar libros por ISBN y obtener la lista de resultados
                        List<libros> resultados = getByAnio(anio);
                        ObjectOutputStream objectOutputToClient = new ObjectOutputStream(output);
                        objectOutputToClient.writeObject(resultados);
                        objectOutputToClient.flush();
                    }
                    else if (parts[0].equals("ADD_LIBRO")){
                        textArea.appendText("Agrego un libro...\n");
                        // La solicitud es para agregar un libro
                        String libro = parts[1]; // 10 es la longitud de "ADD_LIBRO "
                        
                        // Lógica para agregar un libro
                        String[] libroArray = libro.split(",");
                        int isbn = Integer.parseInt(libroArray[0]);
                        String titulo = libroArray[1];
                        String autor = libroArray[2];
                        int anio = Integer.parseInt(libroArray[3]);
                        
                        libroQuerie.addLibros(isbn, titulo, autor, anio);
                        
                    }
                    else if (parts[0].equals("DELETE_LIBRO")){
                        textArea.appendText("Elimino un libro...\n");
                        // La solicitud es para agregar un libro
                        int isbn = Integer.parseInt(parts[1]); // 10 es la longitud de "DEL_LIBRO "
                        
                        // Lógica para borrar un libro
                        libroQuerie.deleteLibro(isbn);

                    }
                    else if (parts[0].equals("UPDATE_LIBRO")){
                        textArea.appendText("Actualizo un libro...\n");
                        // La solicitud es para agregar un libro
                        String libro = parts[1]; // 10 es la longitud de "DEL_LIBRO "
                        
                        // Lógica para borrar un libro
                        String[] libroArray = libro.split(",");
                        int isbn = Integer.parseInt(libroArray[0]);
                        String titulo = libroArray[1];
                        String autor = libroArray[2];
                        int anio = Integer.parseInt(libroArray[3]);
                        
                        int a = libroQuerie.updateLibro(isbn, titulo, autor, anio);
                    }
                    else if (parts[0].equals("CLOSE")) {
                        textArea.appendText("Cerrando la conexión con el cliente...\n");
                        break; // Salir del bucle while
                    }
                }
        
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    private List<libros> getAll() {        
        List<libros> listaLibros = libroQuerie.getAllLibros();
        return listaLibros;
    }
    
    private List<libros> getByIsbn(int isbn) {        
        List<libros> listaLibros = libroQuerie.getLibrosByIsbn(isbn);
        return listaLibros;
    }
    
    private List<libros> getByAutor(String autor) {        
        List<libros> listaLibros = libroQuerie.getLibrosByAutor(autor);
        return listaLibros;
    }
    
    private List<libros> getByTitulo(String titulo) {        
        List<libros> listaLibros = libroQuerie.getLibrosByTitulo(titulo);
        return listaLibros;
    }
    
    private List<libros> getByAnio(int anio) {        
        List<libros> listaLibros = libroQuerie.getLibrosByAnio(anio);
        return listaLibros;
    }

    public static void main(String[] args) {
        launch(args);
    }
}