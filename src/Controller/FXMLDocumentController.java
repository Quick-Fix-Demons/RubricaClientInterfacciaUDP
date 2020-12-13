/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/**
 *
 * @author Alessio
 */
public class FXMLDocumentController implements Initializable {
    private static final int PORT = 1051;
    private InetAddress address;
    private DatagramSocket socket;
    private byte[] buffer;
    
    @FXML
    private void buttonAction0(ActionEvent event) {
        // Ricerca per nome
        //System.out.println("Opzione 0");
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Inserire il nome da cercare.");
        dialog.setHeaderText("Opzione 1");
        dialog.setContentText("Inserire il nome:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            String message = 0 + " - " + name;
            buffer = message.getBytes();
            DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, PORT);
            try {
                socket.send(request);
                byte[] ricevirisposta = new byte[1024];
                DatagramPacket response = new DatagramPacket(ricevirisposta, ricevirisposta.length);
                socket.receive(response); //ricevo il messaggio
                String risposta = new String(response.getData());
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Risultato");
                alert.setHeaderText("Ecco a te i risultati:");
                alert.setContentText(risposta);

                alert.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
    }
    
    @FXML
    private void buttonAction1(ActionEvent event) {
        // Ricerca per numero
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Inserire il numero da cercare.");
        dialog.setHeaderText("Opzione 2");
        dialog.setContentText("Inserire il numero:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(number -> {
            String message = 1 + " - " + number;
            buffer = message.getBytes();
            DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, PORT);
            try {
                socket.send(request);
                byte[] ricevirisposta = new byte[1024];
                DatagramPacket response = new DatagramPacket(ricevirisposta, ricevirisposta.length);
                socket.receive(response); //ricevo il messaggio
                String risposta = new String(response.getData());
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Risultato");
                alert.setHeaderText("Ecco a te i risultati:");
                alert.setContentText(risposta);

                alert.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });    
    }
    
    @FXML
    private void buttonAction2(ActionEvent event) {
        // Aggiungi un contatto
        // Crea il dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Inserimento nuovo contatto");
        dialog.setHeaderText("Opzione 3");

        // Imposta i bottoni.
        ButtonType sendButtonType = new ButtonType("Invia", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(sendButtonType, ButtonType.CANCEL);

        // Crea i fields e i labels.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Nome");
        TextField numero = new TextField();
        numero.setPromptText("Numero");

        grid.add(new Label("Nome:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Numero:"), 0, 1);
        grid.add(numero, 1, 1);

        // Abilita o disabilita il bottone nel caso uno dei due campi fosse vuoto.
        Node sendButton = dialog.getDialogPane().lookupButton(sendButtonType);
        sendButton.setDisable(true);

        username.textProperty().addListener((observable, oldValue, newValue) -> {
            if(username.getText().equals("") || numero.getText().equals("") ) {
                sendButton.setDisable(true);
            }
            else {
                sendButton.setDisable(false);
            }
        });
        numero.textProperty().addListener((observable, oldValue, newValue) -> {
            if(username.getText().equals("") || numero.getText().equals("") ) {
                sendButton.setDisable(true);
            }
            else {
                sendButton.setDisable(false);
            }
        });

        // Metto la griglia nel dialog
        dialog.getDialogPane().setContent(grid);

        // Richiedo il focus sul campo dell'username di default.
        Platform.runLater(() -> username.requestFocus());

        // Ritorno una coppia di valori.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == sendButtonType) {
                return new Pair<>(username.getText(), numero.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernameNumero -> {
            System.out.println("Username=" + usernameNumero.getKey() + ", Numero=" + usernameNumero.getValue());
            String message = 2 + " - " + usernameNumero.getKey() + " - " + usernameNumero.getValue();
            buffer = message.getBytes();
            DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, PORT);
            try {
                socket.send(request);
                byte[] ricevirisposta = new byte[1024];
                DatagramPacket response = new DatagramPacket(ricevirisposta, ricevirisposta.length);
                socket.receive(response); //ricevo il messaggio
                String risposta = new String(response.getData());
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Risultato");
                alert.setHeaderText("Ecco a te i risultati:");
                alert.setContentText(risposta);

                alert.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
    }
    
    @FXML
    private void buttonAction3(ActionEvent event) {
        // Modifica contatto
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText("Questa funzione è ancora \"Work in Progress\"");

        alert.showAndWait();
    }
    
    @FXML
    private void buttonAction4(MouseEvent event) {
        // Display tutto
        String message = "4";
        buffer = message.getBytes();
        DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, PORT);
        try {
            socket.send(request);
            byte[] ricevirisposta = new byte[1024];
            DatagramPacket response = new DatagramPacket(ricevirisposta, ricevirisposta.length);
            socket.receive(response); //ricevo il messaggio
            String risposta = new String(response.getData());
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Risultato");
            alert.setHeaderText("Ecco a te i risultati:");
            alert.setContentText(risposta);

            alert.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            buffer = new byte[1024];
            socket = new DatagramSocket();
            address = InetAddress.getByName("127.0.0.1");
        } catch (SocketException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
