package com.FXzip;

import com.FXzip.util.Util;
import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javax.swing.JFileChooser;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.*;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.FilenameUtils;

public class CompressorController implements Initializable {

    @FXML
    private ProgressBar pbar;

    @FXML
    private TextField txtInput;

    @FXML
    private TextField txtOutput;

    @FXML
    private PasswordField txtPass;

    @FXML
    private ComboBox<String> cbAction;

    private File input;

    private File outputDirectory;

    private String lastInputPath;
    private String lastOutputPath;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    public void browseInput(ActionEvent event) {
        input = showFileSelectionDialog(txtInput, JFileChooser.FILES_AND_DIRECTORIES,
                "Choose a file/diretory to compress or a file to decompress", true);
    }

    @FXML
    public void browseOutput(ActionEvent event) {
        outputDirectory = showFileSelectionDialog(txtOutput, JFileChooser.DIRECTORIES_ONLY,
                "Choose a diretory", false);
    }

    private File showFileSelectionDialog(TextField txtPath, int selectionMode, String dialogTitile, boolean isInput) {
        File file = null;
        JFileChooser chooser = new JFileChooser(".");

        if (isInput && lastInputPath != null) {
            chooser = new JFileChooser(lastInputPath);
        }

        if (!isInput && lastOutputPath != null) {
            chooser = new JFileChooser(lastOutputPath);
        }

        chooser.setDialogTitle(dialogTitile);
        chooser.setFileSelectionMode(selectionMode);
        int ret = chooser.showOpenDialog(null);
        if (ret == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            txtPath.setText(file.getPath());
        } else {
            return null;
        }

        if (isInput) {
            lastInputPath = file.getPath();
        } else {
            lastOutputPath = file.getPath();
        }

        return file;
    }

    @FXML
    public void start() {
        if (input == null || outputDirectory == null) {
            Util.showErrorDialog("Error", "File Error", "You need to insert a valid path in input and output fields.", Alert.AlertType.ERROR);
            return;
        }

        String action = cbAction.getSelectionModel().getSelectedItem();

        if (action.equalsIgnoreCase("Decompress") && !FilenameUtils.getExtension(input.getName()).equals("zip")) {
            Util.showErrorDialog("Error", "File Error", "You need to insert a zip file in input.", Alert.AlertType.ERROR);
            return;
        }

        if (action.equals("Compress") && FilenameUtils.getExtension(input.getName()).equals("zip")) {
            Util.showErrorDialog("Error", "File Error", "File is already compressed", Alert.AlertType.ERROR);
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
        try {
            ZipFile zipFile = new ZipFile(outputDirectory.getPath() + "\\" + FilenameUtils.removeExtension(input.getName()) + ".zip");
            zipFile.setRunInThread(true);
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            if (!txtPass.getText().equals("")) {
                parameters.setEncryptFiles(true);
                parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
                parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_128);
                parameters.setPassword(txtPass.getText());

            }

            zipFile.createZipFile(input, parameters);

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
            Util.showErrorDialog("Error", "File Error", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void decompress() {
        try {

            ZipFile zipFile = new ZipFile(input.getPath());
            zipFile.setRunInThread(true);

            if (zipFile.isEncrypted()) {
                if (txtPass.getText().equals("")) {
                    zipFile.setPassword("1");
                } else {
                    zipFile.setPassword(txtPass.getText());
                }
                checkIfPassIsCorrect(zipFile);
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

        } catch (Exception ex) {
            Util.showErrorDialog("Error", "File Error", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void checkIfPassIsCorrect(ZipFile zipFile) throws ZipException, IOException {
        List<FileHeader> fileHeaders = zipFile.getFileHeaders();

        for (FileHeader fileHeader : fileHeaders) {
            InputStream is = zipFile.getInputStream(fileHeader);
            byte[] b = new byte[4 * 4096];
            while (is.read(b) != -1) {
            }
            is.close();
        }
    }
}
