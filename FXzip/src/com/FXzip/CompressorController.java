package com.FXzip;

import com.FXzip.util.Util;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javax.swing.JFileChooser;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;
import org.apache.commons.io.FilenameUtils;

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

    private File showFileSelectionDialog(TextField txtPath, int selectionMode, String dialogTitile) {
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
    public void start() throws IOException {
        if (input == null || outputDirectory == null) {
            Util.showErrorDialog("Error", "File Error", "You need to insert a valid path in input and output fields.");
            return;
        }

        String action = cbAction.getSelectionModel().getSelectedItem();

        if (action.equalsIgnoreCase("Decompress") && !FilenameUtils.getExtension(input.getName()).equals("zip")) {
            Util.showErrorDialog("Error", "File Error", "You need to insert a zip file in input.");
            return;
        }

        pbar.setProgress(0);

        if (action.equals("Decompress")) {
            decompress();
        } else {
            compress();
        }
    }

    private void compress() {

    }

    private void decompress() throws IOException {
        try {

            ZipFile zipFile = new ZipFile(input.getPath());
            zipFile.setRunInThread(true);

            if (zipFile.isEncrypted()) {
                if (txtPass.getText().equals("")) {
                    zipFile.setPassword("1");
                } else {
                    zipFile.setPassword(txtPass.getText());
                }

            }

            zipFile.extractAll(outputDirectory.getPath());
            ProgressMonitor progressMonitor = zipFile.getProgressMonitor();

            new Thread() {
                @Override
                public void run() {
                    while (progressMonitor.getState() == ProgressMonitor.STATE_BUSY) {
                        double d = ((double) progressMonitor.getPercentDone()) / 100;
                        pbar.setProgress(d);
                    }
                }
            }.start();

        } catch (ZipException ex) {
            Util.showErrorDialog("Error", "File Error", ex.getMessage());
        }
    }
}
