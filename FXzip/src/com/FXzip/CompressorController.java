package com.FXzip;

import com.FXzip.util.Util;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.*;
import javafx.fxml.*;
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
        input = showFileSelectionDialog(txtInput, JFileChooser.FILES_AND_DIRECTORIES, 
                "Choose a file/diretory to compress or a file to decompress");                              
    }

    @FXML
    public void browseOutput(ActionEvent event) {
        outputDirectory = showFileSelectionDialog(txtOutput, JFileChooser.DIRECTORIES_ONLY, 
                "Choose a diretory");                 
    }
    
    private File showFileSelectionDialog(TextField txtPath ,int selectionMode, String dialogTitile)
    {
        File file = null;
        JFileChooser chooser = new JFileChooser(".");       
        chooser.setDialogTitle(dialogTitile);
        chooser.setFileSelectionMode(selectionMode);
        int ret = chooser.showOpenDialog(null);        
        if (ret == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();  
            txtPath.setText(file.getPath());            
        }  
        
        return file;
    }
    
    @FXML
    public void start()
    {
        if(input == null || outputDirectory == null)
        {
            Util.showErrorDialog("Error", "File Error", "You need to insert a valid path to input and output fields.");            
            return;
        }
        
        if(cbAction.getSelectionModel().getSelectedItem().equals("Compress"))
            compress();
        else
            decompress();
    }
    
    private void compress()
    {
        
    }
    
    private void decompress()
    {
        
    }
}
