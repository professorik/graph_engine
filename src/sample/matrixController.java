/*
 * Copyright (c) professorik  2019.
 * All rights reserved :)
 */

package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.util.ResourceBundle;

public class matrixController implements Initializable {

    @FXML
    private Button copyWW;
    @FXML
    private Button copyNW;

    @FXML
    private TextArea textAreaNW;
    @FXML
    private TextArea textAreaWW;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textAreaWW.setText(Controller.getMatrix());
        textAreaNW.setText(Controller.getMatrixWithoutWeights());

        copyWW.setOnAction(event -> {
            StringSelection stringSelection = new StringSelection(textAreaWW.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            ((Stage) copyWW.getScene().getWindow()).close();
        });

        copyNW.setOnAction(event -> {
            StringSelection stringSelection = new StringSelection(textAreaNW.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            ((Stage) copyNW.getScene().getWindow()).close();
        });
    }
}
