package com.sistemiereti.firmadigitale;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.List;

public class Program {

    List<File> selectedFiles = null;

    @FXML
    private TextArea publicKey, privateKey, verificaFirma;
    @FXML
    private ListView uploadedFiles;

    @FXML
    protected void createKeys(){

        //Genero le chiavi
        KeyPairGenerator keyGen = null;
        SecureRandom random = null;

        try {
            keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        keyGen.initialize(1024, random);
        KeyPair keyPair = keyGen.generateKeyPair();

        //Mostro a schermo le chiavi
        printKeys(keyPair);

        //Metto le chiavi su file
        storeKeys(keyPair);
        
    }

    @FXML
    protected void uploadFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        selectedFiles = fileChooser.showOpenMultipleDialog(null);
        if (selectedFiles != null) {
            for (File selectedfile: selectedFiles
                 ) {
                uploadedFiles.getItems().add(selectedfile.getAbsolutePath());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Easter Egg", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    protected void signFile(){
        if(selectedFiles != null) {

        } else{
            Alert alert = new Alert(Alert.AlertType.WARNING, "You yet have to upload the file cancer", ButtonType.OK);
            alert.showAndWait();
        }

    }

    private void printKeys(KeyPair keyPair){
        privateKey.setText("chiave privata: \n" + Base64.getEncoder().encodeToString(keyPair.getPrivate().toString().getBytes(StandardCharsets.UTF_8)));
        publicKey.setText("chiave pubblica: \n" +  Base64.getEncoder().encodeToString(keyPair.getPublic().toString().getBytes(StandardCharsets.UTF_8)));
    }

    private void storeKeys(KeyPair keyPair){

        PrintWriter fileWriter = null;
        try {
            fileWriter = new PrintWriter("privateKey.txt", "UTF-8");
            fileWriter.println(Base64.getEncoder().encodeToString(keyPair.getPrivate().toString().getBytes(StandardCharsets.UTF_8)));
            fileWriter.close();

            fileWriter = new PrintWriter("publicKey.txt","UTF-8");
            fileWriter.println(Base64.getEncoder().encodeToString(keyPair.getPublic().toString().getBytes(StandardCharsets.UTF_8)));
            fileWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void signDocument(){

    }


}