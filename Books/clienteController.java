import java.net.InetAddress;
import java.net.Socket;
import java.io.*;
import java.util.List;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.ArrayList;
import java.lang.InterruptedException;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.concurrent.Task;
//import what I need to use the sleep function in the addLibroButtonPressed method



public class clienteController implements Runnable{

    @FXML
    private ListView<libros> listViewLibros;

    @FXML
    private TextField isbn;

    @FXML
    private TextField titulo;

    @FXML
    private TextField autor;

    @FXML
    private TextField anioEdicion;
    
    private final ObservableList<libros> librosList = FXCollections.observableArrayList();
    
    private Socket connection; // connection to server
    private File selectedFile;
    private DataInputStream fromServer;
    private DataOutputStream toServer;

    
    public void initialize(){
        startClient();
        listViewLibros.setItems(librosList);
        String request = "GET_ALL";
        try{
            toServer.writeUTF(request);
            toServer.flush();
            ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
            List<libros> allLibros = (List<libros>) input.readObject();
            librosList.setAll(allLibros);
            listViewLibros.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    displayLibro(newValue);
                }
            );
        } catch (IOException | ClassNotFoundException e) {
             e.printStackTrace();
             displayAlert(AlertType.ERROR, "Error", "An error occurred.");
        }

      
    }
    
    private void displayLibro(libros libro) {
      if (libro != null) {
         isbn.setText(String.valueOf(libro.getIsbn()));
         titulo.setText(libro.getTitulo());
         autor.setText(libro.getAutor());
         anioEdicion.setText(String.valueOf(libro.getAnio()));
      }
      else {
         isbn.clear();
         titulo.clear();
         autor.clear();  
         anioEdicion.clear();   }
   }

       // start the client thread
    public void startClient()
    {
       // connect to server and get streams
       try 
       {
          // make connection to server
          connection = new Socket(
          InetAddress.getByName("127.0.0.1"), 8000);
 
          // get streams for input and output
          fromServer = new DataInputStream(connection.getInputStream());
          toServer = new DataOutputStream(connection.getOutputStream());
          ;
       } 
       catch (IOException ioException)
       {
          ioException.printStackTrace();         
       } 

       // create and start worker thread for this client
       ExecutorService worker = Executors.newFixedThreadPool(1);
       worker.execute(this); // execute client
    }
        
    public void run(){
       displayAlert(AlertType.INFORMATION, "Cliente", 
            "Cliente conectado.");
           
    }

    @FXML
    private void browseAllLibroButtonPressed(ActionEvent event) {
        String request = "GET_ALL";
        try{
            toServer.writeUTF(request);
            toServer.flush();
            ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
            List<libros> allLibros = (List<libros>) input.readObject();

            librosList.clear();
            librosList.setAll(allLibros);
            listViewLibros.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                displayLibro(newValue);
                }
            );

            isbn.clear();
            titulo.clear();
            autor.clear();
            anioEdicion.clear();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            displayAlert(AlertType.ERROR, "Error", "An error occurred.");
        }
    }
   
    @FXML
    private void buscarLibroByIsbnButtonPressed(ActionEvent event) {
        String isbnStr = isbn.getText();

        try {
            String request = "GET_ISBN";
            try{
            toServer.writeUTF(request + " " + isbnStr);
            toServer.flush();
            ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
            List<libros> resultado = (List<libros>) input.readObject();
            librosList.clear();
            librosList.addAll(resultado);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                displayAlert(AlertType.ERROR, "Error", "An error occurred.");
            }
        } catch (NumberFormatException e) {
            displayAlert(AlertType.ERROR, "Error", "Invalid ISBN format.");
        }
    }

    @FXML
    private void buscarLibroByTituloButtonPressed(ActionEvent event) {
        String tituloStr = titulo.getText();

        try{
            String request = "GET_TITULO";
            try{
            toServer.writeUTF(request + " " + tituloStr);
            toServer.flush();
            ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
            List<libros> resultado = (List<libros>) input.readObject();
            librosList.clear();
            librosList.addAll(resultado);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                displayAlert(AlertType.ERROR, "Error", "An error occurred.");
            }
        } catch (NumberFormatException e) {
            displayAlert(AlertType.ERROR, "Error", "Invalid titulo format.");
        }
    }

    @FXML
    private void buscarLibroByAutorButtonPressed(ActionEvent event) {
        String autorStr = autor.getText();

        // Create a libros object with the author
        try{
            String request = "GET_AUTOR";
            try{
            toServer.writeUTF(request + " " + autorStr);
            toServer.flush();
            ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
            List<libros> resultado = (List<libros>) input.readObject();
            librosList.clear();
            librosList.addAll(resultado);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                displayAlert(AlertType.ERROR, "Error", "An error occurred.");
            }
        } catch (NumberFormatException e) {
            displayAlert(AlertType.ERROR, "Error", "Invalid autor format.");
        }
    }

    @FXML
    private void buscarLibroByAnioButtonPressed(ActionEvent event) {
        String anioS = anioEdicion.getText();

        try {
            String request = "GET_ANIO";
            try{
            toServer.writeUTF(request + " " + anioS);
            toServer.flush();
            ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
            List<libros> resultado = (List<libros>) input.readObject();
            librosList.clear();
            librosList.addAll(resultado);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                displayAlert(AlertType.ERROR, "Error", "An error occurred.");
            }
        } catch (NumberFormatException e) {
            displayAlert(AlertType.ERROR, "Error", "Invalid Year of Edition format.");
        }
    }
    
        @FXML
        private void addLibroButtonPressed(ActionEvent event) {
            // Retrieve data from input fields
            String isbnStr = isbn.getText();
            String tituloStr = titulo.getText();
            String autorStr = autor.getText();
            String anioEdicionStr = anioEdicion.getText();

            // Validate input data
            if (isbnStr.isEmpty() || tituloStr.isEmpty() || autorStr.isEmpty() || anioEdicionStr.isEmpty()) {
                displayAlert(AlertType.ERROR, "Error", "All fields are required.");
                return;
            }

            // Construct the request to send to the server to add a book
            String request = "ADD_LIBRO" + " " + isbnStr + "," + tituloStr + "," + autorStr + "," + anioEdicionStr;

            Task<Void> addBookTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    toServer.writeUTF(request);
                    toServer.flush();

                    // Sleep briefly to allow the server to process the request
                    Thread.sleep(500);

                    // Refresh the book list by requesting all books from the server
                    String refreshRequest = "GET_ALL";
                    toServer.writeUTF(refreshRequest);
                    toServer.flush();

                    // Receive and update the book list
                    ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
                    List<libros> allLibros = (List<libros>) input.readObject();

                    Platform.runLater(() -> {
                        librosList.setAll(allLibros);
                        displayAlert(AlertType.INFORMATION, "Success", "Book added.");
                    });
                    return null;
                }
            };

            new Thread(addBookTask).start();
        }


    @FXML
    private void deleteLibroButtonPressed(ActionEvent event) {
        String isbnStr = isbn.getText();
        String request = "DELETE_LIBRO"+ " " + isbnStr;
        try {
            toServer.writeUTF(request); 
            toServer.flush(); 
            displayAlert(AlertType.INFORMATION, "Success", "Book deleted.");
        } catch (IOException | NumberFormatException e) {
            displayAlert(AlertType.ERROR, "Error", "Invalid ISBN format.");
        }
    }

    @FXML
    private void updateLibroButtonPressed(ActionEvent event) {
        // Retrieve data from input fields
        String isbnStr = isbn.getText();
        String tituloStr = titulo.getText();
        String autorStr = autor.getText();
        String anioEdicionStr = anioEdicion.getText();

        // Validate input data
        if (isbnStr.isEmpty() || tituloStr.isEmpty() || autorStr.isEmpty() || anioEdicionStr.isEmpty()) {
            displayAlert(AlertType.ERROR, "Error", "All fields are required for update.");
            return;
        }

        try {

            String request = "UPDATE_LIBRO" + " " + isbnStr + "," + tituloStr + "," + autorStr + "," + anioEdicionStr;
            try {
                toServer.writeUTF(request);
                toServer.flush();
                displayAlert(AlertType.INFORMATION, "Success", "Book updated.");
            } catch (IOException e) {
                e.printStackTrace();
                displayAlert(AlertType.ERROR, "Error", "An error occurred while communicating with the server.");
            }
        } catch (NumberFormatException e) {
            displayAlert(AlertType.ERROR, "Error", "Invalid ISBN or Year of Edition format.");
        }
    }

   @FXML
   private void XMLButtonPressed(ActionEvent event) {
       FileChooser fileChooser = new FileChooser();
       fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos XML", "*.xml"));
       selectedFile = fileChooser.showSaveDialog(null);
   
       if (selectedFile != null) {
           try (FileWriter writer = new FileWriter(selectedFile);
               BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
                String request = "GET_ALL";
                toServer.writeUTF(request);
                toServer.flush();
                ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
                List<libros> libros = (List<libros>) input.readObject();
   
               // Start the XML file
               bufferedWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
               bufferedWriter.newLine();
               bufferedWriter.write("<libros>");
               bufferedWriter.newLine();
   
               // Write data for each book to the XML file
               for (libros libro : libros) {
                   bufferedWriter.write("    <libro>");
                   bufferedWriter.newLine();
                   escribirCampoXML(bufferedWriter, "isbn", String.valueOf(libro.getIsbn()));
                   escribirCampoXML(bufferedWriter, "titulo", libro.getTitulo());
                   escribirCampoXML(bufferedWriter, "autor", libro.getAutor());
                   escribirCampoXML(bufferedWriter, "anioEdicion", String.valueOf(libro.getAnio())); // Updated line
                   bufferedWriter.write("    </libro>");
                   bufferedWriter.newLine();
               }
   
               // Close the XML file
               bufferedWriter.write("</libros>");
               bufferedWriter.newLine();
   
               displayAlert(AlertType.INFORMATION, "Creacion", "XML creado con exito.");
           } catch (IOException | ClassNotFoundException e) {
               e.printStackTrace();
           }
   
       }
   }
   
   @FXML
   private void JSONButtonPressed(ActionEvent event) {
       FileChooser fileChooser = new FileChooser();
       fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos JSON", "*.json"));
       selectedFile = fileChooser.showSaveDialog(null);
   
       if (selectedFile != null) {
           try (FileWriter writer = new FileWriter(selectedFile)) {
                String request = "GET_ALL";
                toServer.writeUTF(request);
                toServer.flush();
                ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
                List<libros> libros = (List<libros>) input.readObject();
       
               StringBuilder json = new StringBuilder();
               json.append("{");
               json.append("\"libros\": [");
       
               for (int i = 0; i < libros.size(); i++) {
                   libros libro = libros.get(i);
       
                   json.append("{");
                   json.append("\"isbn\": \"" + libro.getIsbn() + "\",");
                   json.append("\"titulo\": \"" + libro.getTitulo() + "\",");
                   json.append("\"autor\": \"" + libro.getAutor() + "\",");
                   json.append("\"anioEdicion\": \"" + libro.getAnio() + "\""); // Updated line
                   json.append("}");
       
                   if (i < libros.size() - 1) {
                       json.append(",");
                   }
               }
       
               json.append("]");
               json.append("}");
       
               writer.write(json.toString());
       
               displayAlert(AlertType.INFORMATION, "Creacion", "JSON creado con exito.");
           } catch (IOException | ClassNotFoundException e) {
               e.printStackTrace();
               displayAlert(AlertType.ERROR, "Error", "An error occurred while reading data from the server.");
           }
       }
   }
   
   @FXML
   private void CSVButtonPressed(ActionEvent event) {
       FileChooser fileChooser = new FileChooser();
       fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"));
       selectedFile = fileChooser.showSaveDialog(null);
   
       if (selectedFile != null) {
           try (FileWriter writer = new FileWriter(selectedFile);
                BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
                String request = "GET_ALL";
                toServer.writeUTF(request);
                toServer.flush();
                ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
                List<libros> libros = (List<libros>) input.readObject();
   
                // Write the CSV header
                StringBuilder header = new StringBuilder();
                header.append("ISBN,Titulo,Autor,AnioEdicion");
                bufferedWriter.write(header.toString());
                bufferedWriter.newLine();
   
               // Write data for each book to the CSV file
                for (libros libro : libros) {
                   StringBuilder line = new StringBuilder();
                   line.append(libro.getIsbn() + ",");
                   line.append(libro.getTitulo() + ",");
                   line.append(libro.getAutor() + ",");
                   line.append(libro.getAnio()); // Updated line
   
                   bufferedWriter.write(line.toString());
                   bufferedWriter.newLine();
               }
   
               displayAlert(AlertType.INFORMATION, "Creacion", "CSV creado con exito.");
           } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                displayAlert(AlertType.ERROR, "Error", "An error occurred while reading data from the server.");
           }
       }
   }

    private void escribirCampoXML(BufferedWriter writer, String etiqueta, String valor) throws IOException {
        writer.write("        <" + etiqueta + ">" + valor + "</" + etiqueta + ">");
        writer.newLine();
    }
   
    private void displayAlert(AlertType type, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}