package net.sehales.ts3sm.util;

import java.util.Optional;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;

import org.controlsfx.dialog.ExceptionDialog;

import net.sehales.ts3_japi.wrapper.QueryError;
import net.sehales.ts3sm.ServerManager;

public class DialogHelper {
    public static void showCommandError(QueryError error, ServerManager srv) {
        DialogHelper.showErrorDialog(
                        srv.lang("global.error.title"),
                        srv.lang("global.error.message",
                                        error.getId(), error.getMessage()));
    }

    public static void showErrorDialog(String title, String text) {
        Alert dlg = new Alert(AlertType.ERROR);
        dlg.setHeaderText(null);
        dlg.setTitle(title);
        dlg.setContentText(text);
        dlg.show();
    }

    public static Optional<String> showInputDialogAndWait(String title, String text) {
        TextInputDialog dlg = new TextInputDialog(text);
        dlg.setTitle(title);
        dlg.setHeaderText(null);
        dlg.setWidth(100);
        return dlg.showAndWait();
    }

    public static void showLastCommandError(ServerManager srv) {
        QueryError error = srv.api().getLastCommand().getError();
        showCommandError(error, srv);
    }

    public static void showModalExceptionDialog(Throwable t) {
        ExceptionDialog dlg = new ExceptionDialog(t);
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.show();
    }

    public static Optional<Integer> showNumberInputDialogAndWait(String title, int maxNumber, int maxLength) {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setHeaderText(null);
        dlg.setWidth(100);
        dlg.setTitle(title);
        dlg.getEditor().textProperty().addListener((value, oldValue, newValue) -> {
            StringProperty sp = (StringProperty) value;
            if ((!newValue.matches("\\d+") && !newValue.isEmpty()) || (newValue.length() > maxLength)) {
                sp.setValue(oldValue);
            } else if (!newValue.isEmpty()) {
                try {
                    int n = Integer.parseInt(newValue);
                    if (n > maxNumber) {
                        sp.setValue(Integer.toString(maxNumber));
                    }
                } catch (NumberFormatException e) {
                    sp.setValue(Integer.toString(maxNumber));
                }
            }
        });
        Optional<String> rawResult = dlg.showAndWait();
        Optional<Integer> result = rawResult.map(Util::mapStringToInt);
        return result;
    }
}
