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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.security.PrivateKey;

public class Program {

    List<File> selectedFiles = null;
    Signature dsa = null;
    KeyPair keyPair = null;


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
        keyPair = keyGen.generateKeyPair();

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

        if(new File("privateKey.txt").exists() && selectedFiles != null) {


            for (File selectedFile:selectedFiles
            ) {
                try {
                    dsa = Signature.getInstance("SHA256withRSA");
                    dsa.initSign(loadPrivateKFromFile());
                    dsa.update(Files.readAllBytes(Path.of(selectedFile.getAbsolutePath())));
                    byte[] signature = dsa.sign();

                    return Base64.getEncoder().encodeToString(signature);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SignatureException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
            }



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




    private static PrivateKey loadPrivateKFromFile() {
        PrivateKey privateKey = null;

        byte[] keyBytes = new byte[0];
        try {
            keyBytes = Files.readAllBytes(Paths.get("privateKey.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = null;
        try {
            kf = KeyFactory.getInstance("RSA");

            privateKey = kf.generatePrivate(spec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return privateKey;
    }


}