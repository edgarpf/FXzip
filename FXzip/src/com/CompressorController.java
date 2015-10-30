package com;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javax.swing.JFileChooser;

public class CompressorController implements Initializable {

    @FXML
    private ProgressBar pbar;

    @FXML
    private TextField txtInput;
    
    @FXML
    private TextField txtOutput;
    
    @FXML
    private TextField txtPass;

    @FXML
    private Button btnStart;

    @FXML
    private ComboBox<String> cbAction;

    private File input;
    
    private File outputDirectory;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    public void browseInput(ActionEvent event) {
        showFileSelectionDialog(input, txtInput, JFileChooser.FILES_AND_DIRECTORIES, 
                "Choose a file/diretory to compress or a file to decompress");                              
    }

    @FXML
    public void browseOutput(ActionEvent event) {
        showFileSelectionDialog(outputDirectory, txtOutput, JFileChooser.DIRECTORIES_ONLY, 
                "Choose a diretory");                 
    }
    
    private void showFileSelectionDialog(File file, TextField txtPath ,int selectionMode, String dialogTitile)
    {
        JFileChooser chooser = new JFileChooser(".");       
        chooser.setDialogTitle(dialogTitile);
        chooser.setFileSelectionMode(selectionMode);
        int ret = chooser.showOpenDialog(null);        
        if (ret == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();  
            txtPath.setText(file.getPath());
        }  
        
        else
            file = null;
    }
}
