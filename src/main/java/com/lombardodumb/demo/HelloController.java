package com.lombardodumb.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

import java.io.FileNotFoundException;
import java.security.*;

public class HelloController {
    
    handler handler = new handler();
    String fileToSign = null;

    @FXML
    private TextArea chiavePubblica, chiavePrivata, verificaFirma;

    @FXML
    protected void createKeys(){
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
        chiavePrivata.setText("chiave privata: \n" + keyPair.getPrivate());
        chiavePubblica.setText("chiave pubblica: \n" + keyPair.getPublic());
        handler.storeKeys(keyPair);
        
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
    
    
    
}