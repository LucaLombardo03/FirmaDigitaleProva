package com.lombardodumb.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class HelloController {
    
    handler handler = new handler();
    String fileToSign = null;

    @FXML
    private TextArea publicKey, privateKey, verificaFirma;

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
    protected void uploadFile(String fileDaPassare){
        /*
        try{
            //TODO try to upload the file from pc
        }catch(FileNotFoundException e){

            //TODO
        }
        */
    }

    @FXML
    protected void signFile(){
        if(fileToSign == null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You yet have to upload the file cancer", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.showAndWait();
            alert.getResult();//do stuff
        } else{
            //TODO firma file
        }

    }

    private void printKeys(KeyPair keyPair){
        privateKey.setText("chiave privata: \n" + Base64.getEncoder().encodeToString(keyPair.getPrivate().toString().getBytes(StandardCharsets.UTF_8)));
        publicKey.setText("chiave pubblica: \n" +  Base64.getEncoder().encodeToString(keyPair.getPublic().toString().getBytes(StandardCharsets.UTF_8)));
    }

    private void storeKeys(KeyPair keyPair){
        String formatCode = "'__'HH-mm-ss'__'dd-MM-yyyy'.txt'";
        SimpleDateFormat formatter = new SimpleDateFormat(formatCode);
        Date date = new Date(System.currentTimeMillis());
        String timeEndString = formatter.format(date);


        PrintWriter fileWriter = null;
        try {
            fileWriter = new PrintWriter("privateKey"+timeEndString, "UTF-8");
            fileWriter.println(Base64.getEncoder().encodeToString(keyPair.getPrivate().toString().getBytes(StandardCharsets.UTF_8)));
            fileWriter.close();

            fileWriter = new PrintWriter("publicKey"+timeEndString,"UTF-8");
            fileWriter.println(Base64.getEncoder().encodeToString(keyPair.getPublic().toString().getBytes(StandardCharsets.UTF_8)));
            fileWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}